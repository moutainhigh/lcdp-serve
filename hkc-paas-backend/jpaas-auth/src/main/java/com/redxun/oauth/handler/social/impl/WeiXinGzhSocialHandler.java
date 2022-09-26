package com.redxun.oauth.handler.social.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IUserService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.OsUserPlatformDto;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SysPropertiesUtil;
import com.redxun.feign.org.OrgClient;
import com.redxun.oauth.handler.social.ISocialHandler;
import com.redxun.oauth.service.IOAuth2Service;
import com.redxun.util.wechat.WeixinUtil;
import com.redxun.util.wechat.WxEntUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信公众号社会化登陆处理器
 *
 * @author ycs
 */
@Service
@Slf4j
public class WeiXinGzhSocialHandler implements ISocialHandler {

    @Autowired
    WxEntUtil wxEntUtil;
    @Autowired
    private OrgClient orgClient;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOAuth2Service oAuth2Service;

    @Override
    public String getType() {
        return "1";
    }

    @Override
    public JsonResult handAuthUrl(String tenantId, String redirectUrl, String state) {
        //
        String appid = SysPropertiesUtil.getString("wxgzh_appid");
        //String secret=SysPropertiesUtil.getString("wxgzh_secret");
        String authUrl = wxEntUtil.getAuthorizeUrl(appid, redirectUrl);
        return JsonResult.getSuccessResult("成功").setData(authUrl);
    }

    @Override
    public JsonResult handGetUser(String tenantId, String code) {
        //获取用户信息
        IUser user = null;
        try {
            //暂从配置中读取
            String appid = SysPropertiesUtil.getString("wxgzh_appid");
            String secret = SysPropertiesUtil.getString("wxgzh_secret");
            String webAccessToken = WeixinUtil.getGzhWebAccessToken(appid, secret, code);
            JSONObject webAccessTokenJson = (JSONObject) JSONObject.parse(webAccessToken);
            String openId = String.valueOf(webAccessTokenJson.get("openid"));
            if (StringUtils.isEmpty(openId)) {
                return JsonResult.getFailResult("获取授权信息失败");
            }
            //获取用户信息
            user = userService.findByOpenId(tenantId, openId, OsUserPlatformDto.TYPE_WECHAT);
            if (user == null) {
                return JsonResult.getFailResult(openId, 10001, "未绑定用户");
            }
            return JsonResult.getSuccessResult(user);
        } catch (Exception e) {
            log.error("获取企业微信授权信息失败tenantId:{},code:{}，message:{}", tenantId, code, ExceptionUtil.getExceptionMessage(e));
        }
        return JsonResult.getFailResult("获取授权信息失败");
    }

    @Override
    public JsonResult handBindUserByOpenId(String tenantId, String openId) {
        try {
            if (StringUtils.isEmpty(openId)) {
                return JsonResult.getFailResult("openId不能为空");
            }
            JsonResult jsonResult = userService.platformBind(openId, 1, tenantId);
            if (!jsonResult.getSuccess()) {
                return jsonResult;
            }
            //绑定后修改当前登陆信息
            oAuth2Service.updateCurrentUser(user -> {
                user.setOpenId(OsUserPlatformDto.TYPE_WECHAT,openId);
            });
            return JsonResult.getSuccessResult("绑定成功").setData(openId);
        } catch (Exception e) {
            log.error("绑定失败", e);
        }
        return JsonResult.getFailResult("绑定失败");
    }

    @Override
    public JsonResult handBindUserByCode(String tenantId, String code) {
        try {
            //暂从配置中读取
            String appid = SysPropertiesUtil.getString("wxgzh_appid");
            String secret = SysPropertiesUtil.getString("wxgzh_secret");
            String webAccessToken = WeixinUtil.getGzhWebAccessToken(appid, secret, code);
            JSONObject webAccessTokenJson = (JSONObject) JSONObject.parse(webAccessToken);
            String openId = String.valueOf(webAccessTokenJson.get("openid"));
            if (StringUtils.isEmpty(openId)) {
                return JsonResult.getFailResult("获取授权信息失败");
            }
            return this.handBindUserByOpenId(tenantId, openId);
        } catch (Exception e) {
            log.error("绑定失败", e);
        }
        return JsonResult.getFailResult("绑定失败");
    }

}
