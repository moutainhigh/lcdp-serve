package com.redxun.oauth.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redxun.api.org.IOrgService;
import com.redxun.api.org.IUserService;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.constant.SecurityConstants;
import com.redxun.common.context.TenantContextHolder;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.model.OsUserPlatformDto;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.*;
import com.redxun.dto.user.OsUserDto;
import com.redxun.dto.user.OsWxEntAgentDto;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.oauth.config.SecurityUtil;
import com.redxun.oauth.handler.social.ISocialHandler;
import com.redxun.oauth.handler.social.SocialHandlerContext;
import com.redxun.oauth.service.IValidateCodeService;
import com.redxun.oauth.service.impl.OAuth2ServiceImpl;
import com.redxun.oauth2.common.properties.AppProperties;
import com.redxun.oauth2.common.token.MobileAuthenticationToken;
import com.redxun.oauth2.common.token.OpenIdAuthenticationToken;
import com.redxun.util.wechat.WeixinUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * OAuth2相关操作，包括各端的登录认证处理
 * @author yjy
 */
@Api(tags = "OAuth2相关操作")
@ClassDefine(title = "用户认证",packageName = "认证中心",alias = "aAuth2Controller",path = "")
@Slf4j
@RestController
public class OAuth2Controller {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private RandomValueAuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private IValidateCodeService validateCodeService;

    @Autowired
    private AppProperties appProperties;

    @Resource
    private IUserService userService;

    @Resource
    private IOrgService orgService;
    @Resource
    TokenStore tokenStore;
    @Resource
    OAuth2ServiceImpl oAuth2Service;

    //钉钉
    final String dd_ok_code="0";

    private static final String TYPE_INST_INFO="instInfo";
    private static final String TYPE_TASK_ID="taskId";
    private static final String TYPE_INST_ID="instId";

    //获取微信公众号--认证url
    private static final String WXGZH_OPENADDR="https://open.weixin.qq.com/connect/oauth2/authorize";
    private static final String WXGZH_REDIRECT="/api/api-uaa/oauth/user/wxGzhRedirect";

    @MethodDefine(title = "用户cas登录", path = "/oauth/user/loginByTicket", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "casTicket", varName = "ticket")})
    @AuditLog(operation = "用户cas登录")
    @ApiOperation(value = "cas票根验证", notes = "验证通过时转发到前端主页")
    @PostMapping("/oauth/user/loginByTicket")
    public JsonResult index(@ApiParam(required = true, name = "ticket", value = "ticket")
                      @RequestParam(value = "ticket",required = true) String ticket,
                      HttpServletRequest request,HttpServletResponse response) throws IOException {
        String casServiceUrl= SysPropertiesUtil.getString("casServiceUrl");
        String casJpaasUrl= SysPropertiesUtil.getString("casJpaasUrl");
        Map<String, String> params=new HashMap<>();
        params.put("service", casJpaasUrl);
        params.put("ticket", ticket);
        try {
            String account="";
            String res=HttpClientUtil.getFromUrl(casServiceUrl, params);
            account= getContext(res);
            if(StringUtils.isEmpty(account)){
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(account, "");
                return writerToken(request, response, token, "用户名或密码错误", false);
            }
            OsUserDto user = orgService.getByAccount(account);
            if(BeanUtil.isEmpty(user)){
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(account, "");
                return writerToken(request, response, token, "用户名或密码错误", false);
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(account, "");
            return writerToken(request, response, token, "用户名或密码错误", true);
        }catch (Exception e){
            log.error("****OAuth2Controller.loginByTicket is errro: message={}",ExceptionUtil.getExceptionMessage(e));
        }
        return JsonResult.getFailResult("登陆失败");
    }

    private String getContext(String xml) {
        String resultString =null;
        Pattern regex = Pattern.compile("<cas:user>(.*?)</cas:user>", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.DOTALL | Pattern.MULTILINE);
        Matcher regexMatcher = regex.matcher(xml);
        if (regexMatcher.find()) {
            resultString = regexMatcher.group(1);
        }
        return resultString;
    }

    /**
     * 微信公共号认证
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "微信公共号认证")
    @AuditLog(operation = "微信公共号认证")
    @GetMapping("/oauth/user/wxGzhOauth")
    @Deprecated
    public void wxGzhOauth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String type = request.getParameter("type");
        String wxCode = request.getParameter("key");
        //跳转路径
        String pro_url = SysPropertiesUtil.getString("pro_url");
        String redirect_uri = pro_url + WXGZH_REDIRECT;
        redirect_uri = URLEncoder.encode(redirect_uri);
        String appid= SysPropertiesUtil.getString("wxgzh_appid");
        String encoderContent = WXGZH_OPENADDR + "?appid=" + appid + "&redirect_uri=" +
                redirect_uri + "&response_type=code&scope=snsapi_base&state=" + type + "#wechat_redirect";
        response.sendRedirect(encoderContent);
    }

    /**
     * 微信公共号跳转
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "微信公共号跳转")
    @AuditLog(operation = "微信公共号跳转")
    @GetMapping("/oauth/user/wxGzhRedirect")
    public void wxGzhRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String type = request.getParameter("state");
        String wxCode = request.getParameter("code");


        String appid=SysPropertiesUtil.getString("wxgzh_appid");
        String secret=SysPropertiesUtil.getString("wxgzh_secret");

        String openId ="";
        try{
            //获取token
            //第二步：通过code换取网页授权access_token
            String webAccessToken = WeixinUtil.getGzhWebAccessToken(appid, secret,wxCode);
            JSONObject webAccessTokenJson = (JSONObject) JSONObject.parse(webAccessToken);
            openId = String.valueOf(webAccessTokenJson.get("openid"));
        }catch (Exception e){
            log.error("---OAuth2Controller.wxGzhRedirect-- is error:"+e.getMessage());
        }
        //跳转路径
        String pro_url = SysPropertiesUtil.getString("pro_url");
        String url = pro_url + "/mobile/#/pages/";
        response.sendRedirect(url + "Login?wxOpenId="+openId);
    }

    /**
     * 移动端根据code进行登录跳转
     * @param wxCode
     * @param tenantId
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "移动端根据code进行登录跳转")
    @AuditLog(operation = "移动端根据code进行登录跳转")
    @PostMapping("/oauth/user/wxLoginCode")
    public JsonResult wxLoginCode( @ApiParam(required = true, name = "wxCode", value = "密码") String wxCode,
                                   @ApiParam(required = true, name = "tenantId", value = "账号") String tenantId,
                                   HttpServletRequest request, HttpServletResponse response) {
        log.debug("---OAuth2Controller.wxLoginCode--- wxCode=" + wxCode);
        //TODO 微信访问还需要修改。
        OsWxEntAgentDto agent = orgService.getDefaultAgent(tenantId);
        JSONObject userJson=new JSONObject();
        userJson.put("loginSuccess",false);
        try {

            //第二步：通过code换取网页授权access_token
            String webAccessToken = WeixinUtil.getWebAccessToken(agent.getCorpId(), agent.getSecret());
            JSONObject webAccessTokenJson = (JSONObject) JSONObject.parse(webAccessToken);
            String access_token = String.valueOf(webAccessTokenJson.get("access_token"));//获取到access_token

            //第三步：通过code和授权access_token获取用户信息
            String webUserInfo = WeixinUtil.getUserInfoByWebAccessTokenAndCode(access_token, wxCode);
            JSONObject userInfo = (JSONObject) JSONObject.parse(webUserInfo);
            String wxOpenId = String.valueOf(userInfo.get("UserId"));
            userJson.put("wxOpenId",wxOpenId);
            //如果微信id找不到对应用户，跳转到绑定页面
            IUser os = orgService.getByWxOpenId(wxOpenId);
            if (os == null) {
                return JsonResult.Success().setData(userJson);
            }
            String username=os.getAccount();
            //登录用户，获取token
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, "");
            OAuth2AccessToken oAuth2AccessToken = SecurityUtil.login(request, token, true);
            //登录成功，绑定更新企业微信账号
            orgService.updateByWxOpenId(username, wxOpenId);
            userJson.put("access_token",oAuth2AccessToken.getValue());
            userJson.put("loginSuccess",true);
        } catch (Exception e) {
            MessageUtil.triggerException("根据CODE进行登录跳转失败", ExceptionUtil.getExceptionMessage(e));
        }
        return JsonResult.Success().setData(userJson);
    }

    /**
     * 钉钉绑定用户
     * @param username 用户名
     * @param password 密码
     * @param validCode 验证码
     * @param deviceId 设备ID
     * @param ddCode 钉钉Code
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "钉钉绑定用户")
    @AuditLog(operation = "钉钉绑定用户")
    @PostMapping(SecurityConstants.DD_REGISTER_PRO_URL)
    public void ddResigter(
            @ApiParam(required = true, name = "username", value = "账号") @RequestParam(value = "username") String username,
            @ApiParam(required = true, name = "password", value = "密码") @RequestParam(value = "password") String password,
            @ApiParam(required = true, name = "validCode", value = "验证码") @RequestParam(value = "validCode") String validCode,
            @ApiParam(required = true, name = "deviceId", value = "验证码ID") @RequestParam(value = "deviceId")  String deviceId,
            @ApiParam(required = true, name = "ddCode", value = "钉钉code") @RequestParam(value = "ddCode",required = false)  String ddCode,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = validateCodeService.getCode(deviceId);
        Boolean ignoreValidCode = SysPropertiesUtil.getBoolean("ignoreValidCode");
        if (!ignoreValidCode && !validCode.equalsIgnoreCase(code)) {
            exceptionHandler(response, "验证码错误!");
            return;
        }
        /* ddUser:
         * code:返回码 0：正常，否则错误
         * errmsg:code不为0时的错误信息
         * userno：钉钉绑定的系统账号
         */
        JSONObject ddUser = orgService.getAccountByDdcode(ddCode);
        if (dd_ok_code.equals(ddUser.getString("code"))){
            if(StringUtils.isNotEmpty(ddUser.getString("userno"))){
                exceptionHandler(response, "当前用户已经绑定其他钉钉账号!");
                return;
            }
        }else{
            exceptionHandler(response, ddUser.getString("errmsg"));
            return;
        }
        String ddId=ddUser.getString("ddId");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        writerTokenFromDD(request, response, token, "用户名或密码错误", username, ddId, false);
    }

    /**
     * 从数据
     * @param request
     * @param response
     * @param token
     * @param badCredenbtialsMsg
     * @param username
     * @param ddId
     * @param ignorePwd
     * @throws IOException
     */
    private void writerTokenFromDD(HttpServletRequest request, HttpServletResponse response, AbstractAuthenticationToken token
            , String badCredenbtialsMsg, String username, String ddId, Boolean ignorePwd) throws IOException {
        try {
            OAuth2AccessToken oAuth2AccessToken = SecurityUtil.login(request, token, ignorePwd);
            //登录成功，绑定更新企业微信账号
            orgService.updateByDdId(username, ddId);
            ResponseUtil.responseSucceed(objectMapper, response, oAuth2AccessToken);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            exceptionHandler(response, badCredenbtialsMsg);
        } catch (Exception e) {
            exceptionHandler(response, e);
        }
    }

    @ApiOperation(value = "移动端根据钉钉code获取")
    @AuditLog(operation = "移动端根据钉钉code获取")
    @PostMapping("/oauth/user/tokenByDdcode")
    public JsonResult tokenByDdcode(@ApiParam(required = true, name = "ddCode", value = "密码") String ddCode,
                                   HttpServletRequest request, HttpServletResponse response) {

        JSONObject ddUser = orgService.getAccountByDdcode(ddCode);
        JSONObject userJson=new JSONObject();
        if(!dd_ok_code.equals(ddUser.getString("code"))){
            userJson.put("loginSuccess",false);
            return JsonResult.Success().setData(ddUser);
        }
        try {
            String username= ddUser.getString("userno");
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, "");
            OAuth2AccessToken oAuth2AccessToken = SecurityUtil.login(request, token, true);
            userJson.put("access_token",oAuth2AccessToken.getValue());
            userJson.put("loginSuccess",true);
        } catch (Exception e) {
            MessageUtil.triggerException("根据钉钉登录失败!",ExceptionUtil.getExceptionMessage(e));
        }
        return JsonResult.Success().setData(userJson);
    }

    @ApiOperation(value = "手机号获取token")
    @AuditLog(operation = "手机号获取token")
    @PostMapping(SecurityConstants.PASSWORD_LOGIN_MOBILE_PRO_URL)
    public JsonResult getMobileTokenInfo(
            @ApiParam(required = true, name = "mobile", value = "手机号") String mobile,
            @ApiParam(required = true, name = "captcha", value = "手机验证码") String captcha,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = validateCodeService.getCode(mobile);
        //手机号登录
        if (!captcha.equals(code)) {
            return JsonResult.getFailResult("验证码错误！");
        }
        MobileAuthenticationToken token = new MobileAuthenticationToken(mobile, null);
        writerToken(request, response, token, "手机号或密码错误", false);
        return writerToken(request, response, token, "手机号或密码错误", false);
    }

    @MethodDefine(title = "用户登录", path = "/oauth/user/token", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "机构编号", varName = "instNo"),
                    @ParamDefine(title = "账号", varName = "username"),
                    @ParamDefine(title = "密码", varName = "password"),
                    @ParamDefine(title = "验证码", varName = "validCode"),
                    @ParamDefine(title = "验证码ID", varName = "deviceId"),
                    @ParamDefine(title = "企业微信账号", varName = "wxOpenId")})
    @ApiOperation(value = "用户登录")
    @AuditLog(operation = "用户登录")
    @PostMapping(SecurityConstants.PASSWORD_LOGIN_PRO_URL)
    public JsonResult login(@ApiParam(required = true, name = "username", value = "账号")
            @RequestParam(value = "username") String username,
            @ApiParam(required = true, name = "password", value = "密码")
            @RequestParam(value = "password") String password,
            @ApiParam(required = true, name = "validCode", value = "验证码")
            @RequestParam(value = "validCode",required = false) String validCode,
            @ApiParam(required = true, name = "deviceId", value = "验证码ID")
            @RequestParam(value = "deviceId",required = false)   String deviceId,
            @ApiParam(required = false, name = "openId", value = "openId")
            @RequestParam(value = "openId",required = false)  String openId,
            @ApiParam(required = false, name = "loginType", value = "登陆类型：1微信公众号，2企业微信，3钉钉-内置，4飞书,5钉钉-扫码")
            @RequestParam(value = "loginType",required = false)  String loginType,
            @ApiParam(required = false, name = "tenantId", value = "租户id")
            @RequestParam(value = "tenantId",required = false)  String tenantId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        String vCode ="";

        Boolean ignoreValidCode = SysPropertiesUtil.getBoolean("ignoreValidCode");

        if(!ignoreValidCode){
            vCode = validateCodeService.getCode(deviceId);
        }
        String detail="";
        if (!ignoreValidCode && !validCode.equalsIgnoreCase(vCode)) {
            return JsonResult.getFailResult("验证码错误!");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        JsonResult jsonResult = writerToken(request, response, token, "用户名或密码错误", false);

        //登陆成功后，且userAccessToken、loginType、tenantId不为空时绑定第三方平台账号；
        if(jsonResult.getSuccess()
                && StringUtils.isNotEmpty(openId)
                && StringUtils.isNotEmpty(loginType)
                && StringUtils.isNotEmpty(tenantId)){
            request.setAttribute(SecurityConstants.Authorization,((OAuth2AccessToken)jsonResult.getData()).getValue());
            //如平台账号绑定失败不对外强提示！！！
            ISocialHandler socialHandler = SocialHandlerContext.getSocialHandler(loginType);
            if(socialHandler != null){
                socialHandler.handBindUserByOpenId(tenantId,openId);
            }
        }
        return jsonResult;
    }

    private void writerTokenFromWeChat(HttpServletRequest request, HttpServletResponse response, AbstractAuthenticationToken token
            , String badCredenbtialsMsg, String username, String wxOpenId, Boolean ignorePwd) throws IOException {
        String detail="";
        try {
            detail="用户:" + token.getName() +"登录成功!";
            OAuth2AccessToken oAuth2AccessToken = SecurityUtil.login(request, token, ignorePwd);
            //登录成功，绑定更新企业微信账号
            orgService.updateByWxOpenId(username, wxOpenId);
            ResponseUtil.responseSucceed(objectMapper, response, oAuth2AccessToken);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            detail="用户:" + token.getName() +"登录密码错误!";
            exceptionHandler(response, badCredenbtialsMsg);
        } catch (Exception e) {
            exceptionHandler(response, e);
            detail="用户:" + token.getName() +"登录失败!";
        }
        LogContext.put(Audit.DETAIL,detail);
    }


    @AuditLog(operation = "根据授权码获取token")
    @ApiOperation(value = "根据授权码获取token")
    @PostMapping(SecurityConstants.CODE_TOKEN_URL)
    public void getTokenByCode(
            @ApiParam(required = false, name = "client_id", value = "授权码") String clientId,
            @ApiParam(required = false, name = "client_secret", value = "授权码") String clientSecret,
            @ApiParam(required = false, name = "redirect_uri", value = "授权码") String redirectUri,
            @ApiParam(required = true, name = "code", value = "授权码") String code,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        writerTokenByCode(clientId, clientSecret, code, request, response, "用户名或密码错误");
    }

    @AuditLog(operation = "根据授权码获取token")
    @ApiOperation(value = "根据openId获取token")
    @PostMapping(SecurityConstants.OPENID_TOKEN_URL)
    public void getTokenByOpenId(
            @ApiParam(required = true, name = "openId", value = "openId") String openId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        OpenIdAuthenticationToken token = new OpenIdAuthenticationToken(openId);
        writerToken(request, response, token, "openId错误", false);
    }

    @AuditLog(operation = "根据手机号获取token")
    @ApiOperation(value = "根据手机号获取token")
    @PostMapping(SecurityConstants.MOBILE_TOKEN_URL)
    public JsonResult getTokenByMobile(
            @ApiParam(required = true, name = "mobile", value = "mobile") @RequestParam(value="mobile") String mobile,
            @ApiParam(required = true, name = "password", value = "密码") @RequestParam(value="password") String password,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        MobileAuthenticationToken token = new MobileAuthenticationToken(mobile, password);
        writerToken(request, response, token, "手机号或密码错误", false);
        return writerToken(request, response, token, "手机号或密码错误", false);
    }

    /**
     * 向客户端生成Token
     * @param request
     * @param response
     * @param token
     * @param badCredenbtialsMsg
     * @param ignorePwd
     * @throws IOException
     */
    private JsonResult writerToken(HttpServletRequest request, HttpServletResponse response, AbstractAuthenticationToken token
            , String badCredenbtialsMsg, Boolean ignorePwd) throws IOException {
        JsonResult jsonResult = null;
        String detail="";
        try {
            OAuth2AccessToken oAuth2AccessToken = SecurityUtil.login(request, token, ignorePwd);
            //ResponseUtil.responseSucceed(objectMapper, response, oAuth2AccessToken);
            detail="用户:" + token.getName() +"登录成功!";
            jsonResult = JsonResult.getSuccessResult(oAuth2AccessToken);
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            e.printStackTrace();
            if(StringUtils.isNotEmpty(e.getMessage()) && e.getMessage().indexOf("锁定") > -1 ){
                //exceptionHandler(response, e.getMessage());
                detail="用户:" + token.getName() + e.getMessage();
                jsonResult = JsonResult.getFailResult(badCredenbtialsMsg);
            }else{
                userService.saveInputErrorIncrement((String)token.getPrincipal(), SecurityUtil.getTenantIdByUsername((String)token.getPrincipal()));
                //exceptionHandler(response, badCredenbtialsMsg);
                detail="用户:" + token.getName() +"登录密码错误!";
                jsonResult = JsonResult.getFailResult(badCredenbtialsMsg);
            }

        }catch (Exception e) {
            e.printStackTrace();
            detail="用户:" + token.getName() +"登录失败!";
            //exceptionHandler(response, e);
            jsonResult = JsonResult.getFailResult("登录失败!");
        }
        LogContext.put(Audit.ACTION,"login");
        LogContext.put(Audit.DETAIL,detail);
        return jsonResult;
    }


    /**
     * 根据编码生成Token
     * @param clientId 客户端ID
     * @param clientSecret 客户端密码
     * @param code 授权Code
     * @param request
     * @param response
     * @param badCredenbtialsMsg
     * @throws IOException
     */
    private void writerTokenByCode(String clientId, String clientSecret, String code, HttpServletRequest request, HttpServletResponse response, String badCredenbtialsMsg) throws IOException {
        try {

            if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientSecret)) {
                clientId = appProperties.getClientId();
                clientSecret = appProperties.getClientSecret();
            }
            //保存租户id
            TenantContextHolder.setTenant(clientId);

            OAuth2Authentication storedAuth = authorizationCodeServices.consumeAuthorizationCode(code);

            OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(storedAuth);

            ResponseUtil.responseWrite(objectMapper, response, oAuth2AccessToken);

        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            exceptionHandler(response, badCredenbtialsMsg);
        } catch (Exception e) {
            exceptionHandler(response, e);
        }
    }

    private void exceptionHandler(HttpServletResponse response, Exception e) throws IOException {
        log.error("exceptionHandler-error:", e);
        exceptionHandler(response, e.getMessage());
    }

    private void exceptionHandler(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        ResponseUtil.responseFailed(objectMapper, response, msg);
    }



    /**
    * @Description:企业微信用户ID或OpenId尝试登录
    * @Author: Elwin ZHANG  @Date: 2021/4/15 16:35
    **/
    @ApiOperation(value = "企业微信用户ID或OpenId尝试登录")
    @PostMapping("/oauth/loginwx")
    public  JsonResult loginByOpenId(@ApiParam(required = true, name = "tenantId", value = "租户ID") String tenantId
            ,@ApiParam(required = true, name = "openId", value = "企业微信用户ID或OpenId")String openId,
              HttpServletRequest request) {
        JsonResult result= JsonResult.Fail();
        result.setShow(false);
        if (tenantId==null ||tenantId.isEmpty()){
            result.setMessage("参数不正确，未取到租户ID!");
            return result;
        }
        if (openId==null ||openId.isEmpty()){
            result.setMessage("参数不正确，未取到openId!");
            return result;
        }
        JPaasUser user=userService.findByOpenId(tenantId,openId, OsUserPlatformDto.TYPE_WEIXIN);
        //如果微信id找不到对应用户
        if (user == null) {
            result.setMessage("openId不存在当前企业中!");
            return result;
        }
        String token=autoLoginByAccount(user.getAccount(),request);
        if(StringUtils.isEmpty(token)){
            result.setMessage("企业微信用户尝试自动登录失败!");
            return result;
        }
        result.setSuccess(true);
        result.setData(token);
        return result;
    }

    /**
     * @Description:钉钉用户ID尝试登录
     * @Author: Elwin ZHANG  @Date: 2021/4/15 16:35
     **/
    @ApiOperation(value = "钉钉用户ID尝试登录")
    @PostMapping("/oauth/logindd")
    public  JsonResult loginByDdId(@ApiParam(required = true, name = "tenantId", value = "租户ID") String tenantId
            ,@ApiParam(required = true, name = "ddId", value = "钉钉用户ID")String ddId,
                                     HttpServletRequest request) {
        JsonResult result= JsonResult.Fail();
        result.setShow(false);
        if (tenantId==null ||tenantId.isEmpty()){
            result.setMessage("参数不正确，未取到租户ID!");
            return result;
        }
        if (ddId==null ||ddId.isEmpty()){
            result.setMessage("参数不正确，未取到ddId!");
            return result;
        }
        JPaasUser user=userService.findByOpenId(tenantId,ddId,OsUserPlatformDto.TYPE_DD);
        //如果找不到对应用户
        if (user == null) {
            result.setMessage("ddId不存在当前企业中!");
            return result;
        }
        String token=autoLoginByAccount(user.getAccount(),request);
        if(StringUtils.isEmpty(token)){
            result.setMessage("钉钉用户尝试自动登录失败!");
            return result;
        }
        result.setSuccess(true);
        result.setData(token);
        return result;
    }
    /**
    * @Description: 根据账号实现自动登录
    * @param account 用户账号, request
    * @return String 登录成功则返回token
    * @Author: Elwin ZHANG  @Date: 2021/4/15 17:13
    **/
    private String autoLoginByAccount(String account,HttpServletRequest request){
        try {
            //自动登录
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(account,"");
            OAuth2AccessToken oAuth2AccessToken = SecurityUtil.login(request, token, true);        //登录成功
            return  oAuth2AccessToken.getValue();
        }catch (Exception e){
            log.info("微信或钉钉尝试自动登录失败",e);
            return  "";
        }
    }

    /**
     * 根据token 获取当前登录人的信息。
     * @param token
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "根据token获取当前人登录信息")
    @PostMapping(SecurityConstants.OAUTH_CURRENT_USER_URL)
    public JsonResult<JPaasUser> getCurrentUser(@ApiParam(required = true, name = "token", value = "token")
                                                @RequestParam(value = "token")  String token) throws IOException {
        JsonResult result= JsonResult.Fail();
        if(StringUtils.isEmpty(token)){
            result.setCode(1);
            return result;
        }

        JPaasUser jPaasUser= SysUserUtil.getLoginUser(token);
        if(jPaasUser==null){
            result.setCode(2);
            return result;
        }
        result.setSuccess(true);
        result.setData(jPaasUser);
        return result;
    }

    /**
     * 重置密码
     * @param username 用户名
     * @param password 密码
     * @param mobile 手机号
     * @param captcha 验证码
     * @return
     * @throws IOException
     */
    @MethodDefine(title = "重置密码", path = "/oauth/user/resetPassword", method = HttpMethodConstants.POST,
            params = {
                    @ParamDefine(title = "账号", varName = "username"),
                    @ParamDefine(title = "密码", varName = "password"),
                    @ParamDefine(title = "手机号码", varName = "mobile"),
                    @ParamDefine(title = "验证码", varName = "captcha")})
    @ApiOperation(value = "重置密码")
    @AuditLog(operation = "重置密码")
    @PostMapping(SecurityConstants.PASSWORD_RESET)
    public JsonResult resetPassword(
            @ApiParam(required = true, name = "username", value = "账号")
            @RequestParam(value = "username") String username,
            @ApiParam(required = true, name = "password", value = "密码")
            @RequestParam(value = "password") String password,
            @ApiParam(required = true, name = "mobile", value = "手机号码")
            @RequestParam(value = "mobile") String mobile,
            @ApiParam(required = true, name = "captcha", value = "验证码")
            @RequestParam(value = "captcha")   String captcha) throws IOException {

        JsonResult jsonResult = validateCodeService.validateSms(mobile, captcha, username);
        if(jsonResult.getCode() == JsonResult.FAIL_CODE){
            return jsonResult;
        }else{
            return this.userService.resetPassword(username, password);
        }

    }


    /**
     * 用户首次登录修改密码
     * @param token    token
     * @param password 密码
     * @return
     * @throws IOException
     */
    @MethodDefine(title = "用户首次登录修改密码", path = "/oauth/user/changePassword", method = HttpMethodConstants.POST,
            params = {
                    @ParamDefine(title = "token", varName = "token"),
                    @ParamDefine(title = "密码", varName = "password")})
    @ApiOperation(value = "用户首次登录修改密码")
    @PostMapping("/oauth/user/changePassword")
    public JsonResult changePassword(
            @ApiParam(required = true, name = "token", value = "token")
            @RequestParam(value = "token") String token,
            @ApiParam(required = true, name = "password", value = "密码")
            @RequestParam(value = "password") String password) throws IOException {

        JPaasUser jPaasUser= SysUserUtil.getLoginUser(token);
        if(jPaasUser==null){
            return JsonResult.getFailResult("请先登录！");
        }

        return this.userService.changePassword(jPaasUser.getUserId(), password);

    }

    @MethodDefine(title = "切换租户", path = "/oauth/user/updTenant", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "租户ID", varName = "tenantId")})
    @PostMapping("/oauth/user/updTenant")
    public JsonResult updTenant(@RequestParam(name = "tenantId")String tenantId){
        JsonResult result=oAuth2Service.updateCurrentUser(user -> {
            user.setTenantId(tenantId);
        });
        return result;
    }





    @MethodDefine(title = "切换公司", path = "/oauth/user/switchCompany", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "公司ID", varName = "companyId")})
    @AuditLog(operation = "切换公司")
    @PostMapping("/oauth/user/switchCompany")
    public JsonResult switchCompany(@RequestParam(name = "companyId")String companyId){
        JsonResult result=oAuth2Service.updateCurrentUser(user -> {
            user.setCompanyId(companyId);
        });
        return JsonResult.Success("切换公司成功!");
    }

    @MethodDefine(title = "退出切换公司", path = "/oauth/user/exitSwitchCompany",
            method = HttpMethodConstants.POST)
    @AuditLog(operation = "退出切换公司")
    @PostMapping("/oauth/user/exitSwitchCompany")
    public JsonResult exitSwitchCompany(){
        JsonResult result=oAuth2Service.updateCurrentUser(user -> {
            user.setCompanyId(user.getOriginCompanyId());
        });
        return JsonResult.Success("退出切换公司成功!");
    }


    /**
     * 第三方平台登陆获取授权地址
     * @throws Exception
     */
    @MethodDefine(title = "第三方平台登陆获取授权地址", path = "/oauth/user/authUrl", method = HttpMethodConstants.GET,
            params = {
                    @ParamDefine(title = "租户id", varName = "tenantId"),
                    @ParamDefine(title = "第三方平台类型：1微信公众号，2企业微信，3钉钉，4飞书，5钉钉-扫码，6企业微信-PC登陆", varName = "loginType"),
                    @ParamDefine(title = "授权后重定向地址", varName = "redirectUrl"),
                    @ParamDefine(title = "自定义参数", varName = "state")})
    @ApiOperation(value = "第三方平台登陆获取授权地址")
    @AuditLog(operation = "第三方平台登陆获取授权地址")
    @GetMapping("/oauth/user/authUrl")
    public JsonResult authUrl(String tenantId,String loginType,String redirectUrl,String state) throws Exception {
        ISocialHandler socialHandler = SocialHandlerContext.getSocialHandler(loginType);
        if(socialHandler == null){
            return JsonResult.Fail("第三方平台类型不存在").setShow(false);
        }
        return socialHandler.handAuthUrl(tenantId, redirectUrl, state).setShow(false);
    }

    /**
     * 根据code进行登录
     * @param code
     * @param tenantId
     * @param loginType
     * @param request
     * @return
     */
    @ApiOperation(value = "第三方平台自动登陆")
    @AuditLog(operation = "第三方平台自动登陆")
    @PostMapping("/oauth/user/autoLogin")
    public JsonResult autoLogin(HttpServletRequest request, @ApiParam(required = true, name = "code", value = "第三方授权code") String code,
                                @ApiParam(required = true, name = "tenantId", value = "租户id") String tenantId,
                                @ApiParam(required = true, name = "loginType", value = "登陆类型") String loginType) {
        log.debug("第三方平台登陆，tenantId：{}，loginType：{}，code：{}", tenantId, loginType, code);
        try {
            ISocialHandler socialHandler = SocialHandlerContext.getSocialHandler(loginType);
            if(socialHandler == null){
                return JsonResult.Fail("第三方平台类型不存在");
            }
            JsonResult jsonResult = socialHandler.handGetUser(tenantId, code);
            //获取绑定用户信息失败，返回前端处理
            if(jsonResult.getCode() != JsonResult.SUCESS_CODE){
                return jsonResult.setShow(false);
            }
            //获取绑定用户信息成功，进行登录
            IUser os = (IUser)jsonResult.getData();
            String username=os.getAccount();
            //登录用户，获取token
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, "");
            OAuth2AccessToken oAuth2AccessToken = SecurityUtil.login(request, token, true);
            IUser user = ContextUtil.getCurrentUser();
            //登录成功，绑定更新企业微信账号
            return JsonResult.getSuccessResult(oAuth2AccessToken);
        } catch (Exception e) {
            log.error("第三方平台登陆失败，tenantId：{}，loginType：{}，code：{}", tenantId, loginType, code, e);
        }
        return JsonResult.Fail("登陆失败，请重试");
    }

    /**
     * 已登陆用户绑定第三方平台账号
     * @param code
     * @param tenantId
     * @param loginType
     * @param request
     * @return
     */
    @ApiOperation(value = "已登陆用户绑定第三方平台账号")
    @AuditLog(operation = "已登陆用户绑定第三方平台账号")
    @PostMapping("/oauth/user/bind")
    public JsonResult bind(HttpServletRequest request, @ApiParam(required = true, name = "code", value = "第三方授权code") String code,
                           @ApiParam(required = true, name = "tenantId", value = "租户id") String tenantId,
                           @ApiParam(required = true, name = "loginType", value = "登陆类型") String loginType) {
        log.debug("已登陆用户绑定第三方平台账号，tenantId：{}，loginType：{}，code：{}", tenantId, loginType, code);
        try {
            ISocialHandler socialHandler = SocialHandlerContext.getSocialHandler(loginType);
            if(socialHandler == null){
                return JsonResult.Fail("第三方平台类型不存在");
            }
            //绑定第三方平台账号账号
            return  socialHandler.handBindUserByCode(tenantId,code);
        } catch (Exception e) {
            log.error("已登陆用户绑定第三方平台账号，tenantId：{}，loginType：{}，code：{}", tenantId, loginType, code, e);
        }
        return JsonResult.Fail("绑定失败，请重试");
    }

}
