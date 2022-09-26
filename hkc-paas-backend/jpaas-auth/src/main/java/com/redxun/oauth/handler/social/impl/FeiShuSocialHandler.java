package com.redxun.oauth.handler.social.impl;

import com.redxun.api.org.IUserService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.OsUserPlatformDto;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsFsAgentDto;
import com.redxun.feign.org.OrgClient;
import com.redxun.oauth.handler.social.ISocialHandler;
import com.redxun.oauth.service.IOAuth2Service;
import com.redxun.util.feishu.FeiShuClient;
import com.redxun.util.feishu.entity.FeiShuUserAccessTokenInfo;
import com.redxun.util.feishu.entity.FeiShuUserAccessTokenInfoReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 飞书社会化登陆处理器
 *
 * @author ycs
 */
@Service
@Slf4j
public class FeiShuSocialHandler implements ISocialHandler {

    @Autowired
    private FeiShuClient feiShuClient;
    @Autowired
    private OrgClient orgClient;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOAuth2Service oAuth2Service;
    @Override
    public String getType() {
        return "4";
    }

    @Override
    public JsonResult handAuthUrl(String tenantId, String redirectUrl, String state) {
        OsFsAgentDto fsDefaultAgent = orgClient.getFsDefaultAgent(tenantId);
        if (fsDefaultAgent == null) {
            return JsonResult.getFailResult("未配置默认飞书信息");
        }
        String feiShuAuthUrl = feiShuClient.getFeishuAuthUrl(fsDefaultAgent.getAppId(), redirectUrl, state);
        return JsonResult.getSuccessResult("成功").setData(feiShuAuthUrl);
    }

    @Override
    public JsonResult handGetUser(String tenantId, String code) {
        //获取用户信息
        IUser user = null;

        try {
            OsFsAgentDto fsDefaultAgent = orgClient.getFsDefaultAgent(tenantId);
            if (fsDefaultAgent == null) {
                return JsonResult.getFailResult("未配置默认飞书配置");
            }
            //获取飞书用户信息
            String accessToken = feiShuClient.getAppAccessToken(fsDefaultAgent.getAppId(), fsDefaultAgent.getSecret());
            FeiShuUserAccessTokenInfoReq req = new FeiShuUserAccessTokenInfoReq();
            req.setCode(code);
            FeiShuUserAccessTokenInfo feiShuUserInfo = feiShuClient.getFeiShuUserAccessTokenInfo(accessToken, req);
            if (feiShuUserInfo == null) {
                return JsonResult.getFailResult("获取授权信息失败");
            }
            //获取用户信息
            user = userService.findByOpenId(tenantId, feiShuUserInfo.getOpenId(), OsUserPlatformDto.TYPE_FEISHU);
            if (user == null) {
                return JsonResult.getFailResult(feiShuUserInfo.getOpenId(), 10001, "未绑定用户");
            }
            return JsonResult.getSuccessResult(user);
        } catch (Exception e) {
            log.error("获取飞书授权信息失败tenantId:{},code:{}，message:{}", tenantId, code, ExceptionUtil.getExceptionMessage(e));
        }
        return JsonResult.getFailResult("获取授权信息失败");
    }

    @Override
    public JsonResult handBindUserByOpenId(String tenantId, String openId) {
        try {
            JsonResult jsonResult = userService.platformBind(openId, OsUserPlatformDto.TYPE_FEISHU, tenantId);
            if (!jsonResult.getSuccess()) {
                return jsonResult;
            }
            //绑定后修改当前登陆信息
            oAuth2Service.updateCurrentUser(user -> {
                user.setOpenId(OsUserPlatformDto.TYPE_FEISHU,openId);
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
            OsFsAgentDto fsDefaultAgent = orgClient.getFsDefaultAgent(tenantId);
            if (fsDefaultAgent == null) {
                return JsonResult.getFailResult("未配置默认飞书配置");
            }
            //获取飞书用户信息
            String accessToken = feiShuClient.getAppAccessToken(fsDefaultAgent.getAppId(), fsDefaultAgent.getSecret());
            FeiShuUserAccessTokenInfoReq req = new FeiShuUserAccessTokenInfoReq();
            req.setCode(code);
            FeiShuUserAccessTokenInfo feiShuUserInfo = feiShuClient.getFeiShuUserAccessTokenInfo(accessToken, req);
            if (feiShuUserInfo == null) {
                return JsonResult.getFailResult("获取授权信息失败");
            }
            return this.handBindUserByOpenId(tenantId, feiShuUserInfo.getOpenId());
        } catch (Exception e) {
            log.error("绑定失败", e);
        }
        return JsonResult.getFailResult("绑定失败");
    }

}
