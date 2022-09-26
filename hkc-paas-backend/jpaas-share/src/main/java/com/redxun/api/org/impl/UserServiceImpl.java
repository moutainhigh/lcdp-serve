package com.redxun.api.org.impl;

import com.redxun.api.org.IUserService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.model.JPaasUser;
import com.redxun.feign.org.UserClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserServiceImpl implements IUserService {

    @Resource
    @Lazy
    UserClient userClient;

    @Override
    public JPaasUser findByUsername(String username) {
        return userClient.findByUsername(username);
    }


    @Override
    public JPaasUser findByUsernameAndTenantId(String username, String tenantId) {
        return userClient.findByUsernameAndTenantId(username, tenantId);
    }

    @Override
    public JPaasUser findByMobile(String mobile) {
        return userClient.findByMobile(mobile);
    }


    @Override
    public JsonResult resetPassword(String username, String password) {
        return userClient.resetPassword(username,password);
    }

    @Override
    public String findTenanIdByDomain(String domain) {
        return userClient.findTenanIdByDomain(domain);
    }

    @Override
    public String findTenantIdByInstNo(String instNo) {
        return userClient.findTenanIdByInstNo(instNo);
    }

    @Override
    public Boolean isLockedByPasswordInputError(String username, String tenantId) {
        return userClient.isLockedByPasswordInputError(username, tenantId);
    }

    @Override
    public void saveInputErrorIncrement(String username, String tenantId) {
        userClient.saveInputErrorIncrement(username, tenantId);
    }

    @Override
    public Boolean isLockedByPasswordExpire(String username, String tenantId) {
        return userClient.isLockedByPasswordExpire(username, tenantId);
    }

    @Override
    public String getIsFirstLogin(String isFirstLogin, String tenantId) {
        return userClient.getIsFirstLogin(isFirstLogin, tenantId);
    }

    @Override
    public JsonResult changePassword(String userId, String password) {
        return userClient.changePassword(userId, password);
    }



    @Override
    public JPaasUser findByOpenId(String tenantId,String openId,Integer platformType) {
        return userClient.findByOpenId(tenantId,openId,platformType);
    }
    @Override
    public JsonResult platformBind(String openId, Integer platformType, String tenantId) {
        return userClient.bind(openId,platformType,tenantId);
    }
}
