package com.redxun.oauth.handler.social.impl;

import com.redxun.api.org.IUserService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.OsUserPlatformDto;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.user.OsDdAgentDto;
import com.redxun.feign.org.OrgClient;
import com.redxun.oauth.handler.social.ISocialHandler;
import com.redxun.oauth.service.IOAuth2Service;
import com.redxun.util.dd.DingDingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 钉钉社会化登陆处理器
 * @author ycs
 */
@Service
@Slf4j
public class DingTalkSocialHandler implements ISocialHandler {

    @Autowired
    DingDingUtil dingDingUtil;
    @Autowired
    private OrgClient orgClient;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOAuth2Service oAuth2Service;

    @Override
    public String getType() {
        return "3";
    }

    @Override
    public JsonResult handAuthUrl(String tenantId, String redirectUrl, String state) {
        //钉钉无需授权地址，js可直接获取code
        return JsonResult.getSuccessResult("成功").setData("");
    }

    @Override
    public JsonResult handGetUser(String tenantId, String code) {
        //获取用户信息
        try {
            OsDdAgentDto osDdAgentDto = orgClient.getDdAgent(tenantId);
            if(osDdAgentDto==null){
                return JsonResult.getFailResult("未配置默认飞书配置");
            }
            //获取钉钉用户信息
            String token = dingDingUtil.getToken(osDdAgentDto.getAppKey(), osDdAgentDto.getSecret(), tenantId);
            String dingDingUserId = dingDingUtil.getUserId(token, code);
            if(StringUtils.isEmpty(dingDingUserId)){
                return JsonResult.getFailResult("获取授权信息失败");
            }
            //获取用户信息
            IUser user = userService.findByOpenId(tenantId,dingDingUserId, OsUserPlatformDto.TYPE_DD);
            if(user==null){
                return JsonResult.getFailResult(dingDingUserId,10001,"未绑定用户");
            }
            return JsonResult.getSuccessResult(user);
        } catch (Exception e) {
            log.error("获取钉钉授权信息失败tenantId:{},code:{}，message:{}",tenantId,code, ExceptionUtil.getExceptionMessage(e));
        }
        return JsonResult.getFailResult("获取授权信息失败");
    }

    @Override
    public JsonResult handBindUserByOpenId(String tenantId, String openId) {
        try {
            JsonResult jsonResult = userService.platformBind(openId,OsUserPlatformDto.TYPE_DD,tenantId);
            if(!jsonResult.getSuccess()){
                return jsonResult;
            }
            oAuth2Service.updateCurrentUser(user -> {
                user.setOpenId(OsUserPlatformDto.TYPE_DD,openId);
            });
            return JsonResult.getSuccessResult("绑定成功").setData(openId);
        } catch (Exception e) {
            log.error("绑定失败",e);
        }
        return JsonResult.getFailResult("绑定失败");
    }

    @Override
    public JsonResult handBindUserByCode(String tenantId, String code) {
        try {
            OsDdAgentDto osDdAgentDto = orgClient.getDdAgent(tenantId);
            if(osDdAgentDto==null){
                return JsonResult.getFailResult("未配置默认飞书配置");
            }

            //获取钉钉用户信息
            String token = dingDingUtil.getToken(osDdAgentDto.getAppKey(), osDdAgentDto.getSecret(), tenantId);
            String dingDingUserId = dingDingUtil.getUserId(token, code);
            if(StringUtils.isEmpty(dingDingUserId)){
                return JsonResult.getFailResult("获取授权信息失败");
            }
            return this.handBindUserByOpenId(tenantId,dingDingUserId);
        } catch (Exception e) {
            log.error("绑定失败",e);
        }
        return JsonResult.getFailResult("绑定失败");
    }
}
