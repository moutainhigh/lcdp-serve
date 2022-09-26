package com.redxun.oauth.handler.social.impl;

import com.redxun.api.org.IUserService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.OsUserPlatformDto;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsWxEntAgentDto;
import com.redxun.feign.org.OrgClient;
import com.redxun.oauth.handler.social.ISocialHandler;
import com.redxun.oauth.service.IOAuth2Service;
import com.redxun.util.wechat.WxEntUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企业微信社会化登陆处理器
 *
 * @author ycs
 */
@Service
@Slf4j
public class WeiXinEntSocialHandler implements ISocialHandler {

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
        return "2";
    }

    @Override
    public JsonResult handAuthUrl(String tenantId, String redirectUrl, String state) {
        //
        OsWxEntAgentDto osWxEntAgentDto = orgClient.getDefaultAgent(tenantId);
        if (osWxEntAgentDto == null) {
            return JsonResult.getFailResult("未配置默认企业微信信息");
        }
        String authUrl = wxEntUtil.getAuthorizeUrl(osWxEntAgentDto.getCorpId(), redirectUrl);
        return JsonResult.getSuccessResult("成功").setData(authUrl);
    }

    @Override
    public JsonResult handGetUser(String tenantId, String code) {
        //获取用户信息
        IUser user = null;
        try {
            OsWxEntAgentDto osWxEntAgentDto = orgClient.getDefaultAgent(tenantId);
            if (osWxEntAgentDto == null) {
                return JsonResult.getFailResult("未配置默认企业微信信息");
            }
            //获取企业微信用户信息
            String token = wxEntUtil.getToken(osWxEntAgentDto.getCorpId(), osWxEntAgentDto.getSecret(), tenantId);
            String openId = wxEntUtil.getUserInfo(token, code);
            if (StringUtils.isEmpty(openId)) {
                return JsonResult.getFailResult("获取授权信息失败");
            }
            //获取用户信息
            user = userService.findByOpenId(tenantId, openId, OsUserPlatformDto.TYPE_WEIXIN);
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
            JsonResult jsonResult = userService.platformBind(openId, OsUserPlatformDto.TYPE_WEIXIN, tenantId);
            if (!jsonResult.getSuccess()) {
                return jsonResult;
            }
            //绑定后修改当前登陆信息
            oAuth2Service.updateCurrentUser(user -> {
                user.setOpenId(OsUserPlatformDto.TYPE_WEIXIN, openId);
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
            OsWxEntAgentDto osWxEntAgentDto = orgClient.getDefaultAgent(tenantId);
            if (osWxEntAgentDto == null) {
                return JsonResult.getFailResult("未配置默认企业微信信息");
            }
            //获取企业微信用户信息
            String token = wxEntUtil.getToken(osWxEntAgentDto.getCorpId(), osWxEntAgentDto.getSecret(), tenantId);
            String openId = wxEntUtil.getUserInfo(token, code);
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
