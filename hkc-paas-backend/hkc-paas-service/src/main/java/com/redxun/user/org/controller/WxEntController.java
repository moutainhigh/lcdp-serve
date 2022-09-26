package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.entity.OsWxEntAgent;
import com.redxun.user.org.service.OsUserServiceImpl;
import com.redxun.user.org.service.OsWxEntAgentServiceImpl;
import com.redxun.util.wechat.WxEntUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * WxEntController
 * @Description: 企业微信相关控制
 * @Author: Elwin ZHANG
 * @Date: 2021/4/13 18:35
 */
@Api(tags = "企业微信相关操作")
@ClassDefine(title = "企业微信用户认证",packageName = "认证中心",alias = "QyWexinAuth",path = "")
@Slf4j
@RestController
@RequestMapping("/wxent")
public class WxEntController extends BaseController<OsWxEntAgent> {

    @Autowired
    OsWxEntAgentServiceImpl osWxEntAgentService;

    @Autowired
    OsUserServiceImpl osUserServiceImpl;

    @Autowired
    WxEntUtil wxEntUtil;

    @Override
    public BaseService getBaseService() {
        return osWxEntAgentService;
    }

    @Override
    public String getComment() {
        return "企业微信应用";
    }

    @ApiOperation(value = "根据企业微信用户ID，获取用户信息")
    @AuditLog(operation = "根据企业微信用户ID，获取用户信息")
    @GetMapping("/getuser")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JPaasUser getUserByOpenIdAndTenantId(@ApiParam(required = true, name = "tenantId", value = "租户ID")
                                                @RequestParam("tenantId") String tenantId
            , @ApiParam(required = true, name = "openId", value = "企业微信用户ID") @RequestParam("openId")String openId){
        if (openId==null ||openId.isEmpty()){
            return null;
        }
        return osUserServiceImpl.selectByOpenIdAndTenantId(openId,tenantId);
    }

    /**
     * @Description: 获取企业微信网页授权访问地址
     * @Author: Elwin ZHANG
     * @Date: 2021/4/13 18:13
     * @param tenantId  : 租户ID
     * @param redirectUrl 回调地址
     * @return : com.redxun.common.base.entity.JsonResult
     **/
    @ApiOperation(value = "获取企业微信网页授权访问地址")
    @AuditLog(operation = "获取企业微信网页授权访问地址")
    @PostMapping("/authorizeUrl")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JsonResult authorizeUrl(@ApiParam(required = true, name = "tenantId", value = "租户ID")
                                           String tenantId,@ApiParam(required = true, name = "redirectUrl", value = "回调地址") String redirectUrl){
        JsonResult result=new JsonResult(false);
        if (tenantId==null ||tenantId.isEmpty()){
            result.setMessage("参数不正确，未取到租户ID!");
            return result;
        }
        //根据租户ID获取企业微信的配置信息
        OsWxEntAgent osWxEntAgent= osWxEntAgentService.getDefaultAgent(tenantId);
        if(osWxEntAgent==null ){
            result.setMessage("没有默认的企业微信应用配置，请先后台配置!");
            return result;
        }
        //构造前端要调用的URL
        String url=wxEntUtil.getAuthorizeUrl(osWxEntAgent.getCorpId(),redirectUrl);
        result.setSuccess(true);
        result.setData(url);
        result.setMessage("成功，请在浏览器中访问Data中的地址");
        return result;
    }

    @ApiOperation(value = "根据企业微信授权获取到的code，尝试获取OpenId")
    @AuditLog(operation = "根据企业微信授权获取到的code，尝试获取OpenId")
    @PostMapping("/getOpenIdByCode")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JsonResult getOpenIdByCode(@ApiParam(required = true, name = "tenantId", value = "租户ID") String tenantId
            ,@ApiParam(required = true, name = "code", value = "微信成员授权获取到的code")String code,
            @ApiParam(required = true, name = "autoBind", value = "是否自动绑定")Integer autoBind){
        JsonResult result=new JsonResult(false);
        if (tenantId==null ||tenantId.isEmpty()){
            result.setMessage("参数不正确，未取到租户ID!");
            return result;
        }
        if (code==null ||code.isEmpty()){
            result.setMessage("参数不正确，Code无效!");
            return result;
        }
        //根据租户ID获取企业微信的配置信息
        OsWxEntAgent osWxEntAgent= osWxEntAgentService.getDefaultAgent(tenantId);
        if(osWxEntAgent==null ){
            result.setMessage("没有默认的企业微信应用配置，请先在后台配置!");
            return result;
        }
        //获取Token
        String token=wxEntUtil.getToken(osWxEntAgent.getCorpId(),osWxEntAgent.getSecret(),tenantId);
        if(StringUtils.isEmpty(token)){
            result.setMessage("获取Token失败，请检查网格连接或安全设置!");
            return result;
        }

        //获取OpenID
        String openId=wxEntUtil.getUserInfo(token,code);
        if(StringUtils.isEmpty(openId)){
            result.setMessage("获取OpenId失败，请检查网格连接或安全设置!!");
            return result;
        }
        //自动绑定
        if(autoBind!=null && autoBind==1 ){
            LogContext.put(Audit.DETAIL, "并绑定到系统登录账号");
            return bind2CurUser(openId);
        }
        result.setData(openId);
        result.setSuccess(true);
        return result;
    }

    /**
     * @Description: 绑定企业微信到系统登录账号
     * @param openId 企业微信用户用ID或OpenID
     * @return com.redxun.common.base.entity.JsonResult
     * @Author: Elwin ZHANG  @Date: 2021/4/14 18:02
     **/
    @ApiOperation(value = "绑定企业微信到系统登录账号")
    @AuditLog(operation = "绑定企业微信到系统登录账号")
    @PostMapping("/bind")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JsonResult bind2CurUser(@ApiParam(required = true, name = "openId", value = "企业微信用户用ID或OpenID") String openId){
        JsonResult result=new JsonResult(false);
        if (openId==null ||openId.isEmpty()){
            result.setMessage("参数不正确，请检查OpenId!");
            return result;
        }
        IUser  user =ContextUtil.getCurrentUser();
        if (user==null){
            result.setMessage("请先登录再绑定!");
            return result;
        }
        //更新用户信息
        OsUser osUser=osUserServiceImpl.getById(user.getUserId());
        osUser.setWxOpenId(openId);
        osUserServiceImpl.updateUser(osUser);
        //成功返回
        result.setSuccess(true);
        result.setData(openId);
        result.setMessage("绑定成功！");
        return result;
    }

    /**
     * @Description: 解绑企业微信到系统登录账号
     * @return com.redxun.common.base.entity.JsonResult
     * @Author: Elwin ZHANG  @Date: 2021/4/14 18:02
     **/
    @ApiOperation(value = "解绑企业微信到系统登录账号")
    @AuditLog(operation = "解绑企业微信到系统登录账号")
    @PostMapping("/unbind")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JsonResult unBind (@ApiParam(required = false, name = "userId", value = "用户的ID")String userId){
        JsonResult result=JsonResult.Fail();
        IUser  user =ContextUtil.getCurrentUser();
        if (user==null){
            result.setMessage("请先登录再解绑!");
            return result;
        }
        //更新用户信息
        OsUser osUser=osUserServiceImpl.getById(user.getUserId());
        String openId=osUser.getWxOpenId();
        if(StringUtils.isEmpty(openId)){
            result.setMessage("当前还没有绑定企业微信!");
            //没有绑定的情况下也算解绑成功，可能在别处删除了
            result.setSuccess(true);
            return result;
        }
        osUser.setWxOpenId(null);
        osUserServiceImpl.updateById(osUser);
        //成功返回
        result.setSuccess(true);
        result.setMessage("解绑成功！");
        return result;
    }

    @ApiOperation(value = "获取企业微信的扫码登录URL")
    @AuditLog(operation = "获取企业微信的扫码登录URL")
    @PostMapping("/getQRCodeUrl")
    @Deprecated //迁移至jpaas-auth统一第三方平台登陆
    public JsonResult getQRCodeUrl (@ApiParam(required = true, name = "tenantId", value = "租户ID") String tenantId,
                 @ApiParam(required = true, name = "redirectUrl", value = "回调地址") String redirectUrl) {
        JsonResult result=new JsonResult(false);
        if (tenantId==null ||tenantId.isEmpty()){
            result.setMessage("参数不正确，未取到租户ID!");
            return result;
        }
        String serverAddress= SysPropertiesUtil.getString("serverAddress");
        String ctxPath= SysPropertiesUtil.getString("ctxPath");
        String newUrl=serverAddress +  ctxPath  + redirectUrl;
        //根据租户ID获取企业微信的配置信息
        OsWxEntAgent osWxEntAgent= osWxEntAgentService.getDefaultAgent(tenantId);
        if(osWxEntAgent==null ){
            result.setMessage("没有默认的企业微信应用配置，请先后台配置!");
            return result;
        }
        //构造前端要调用的URL
        String url=wxEntUtil.getQRConnectUrl(osWxEntAgent.getCorpId(),osWxEntAgent.getAgentId(),newUrl);
        result.setSuccess(true);
        result.setData(url);
        return result;
    }

}
