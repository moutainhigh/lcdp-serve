package com.redxun.oauth.service.impl;

import com.redxun.api.org.IUserService;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.feign.org.OrgClient;
import com.redxun.oauth.service.IUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 通过用户名与密码提供用户身份认证服务类
 *
 * @author yjy
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements IUserDetailsService, SocialUserDetailsService {
    @Resource
    private IUserService userService;

    @Autowired
    OrgClient orgClient;


    @Override
    public UserDetails loadUserByUsername(String username) {
        String tenantId = null;
        JPaasUser paasUser = null;
        // admin@redxun.cn
        int index = username.indexOf("@");
        //带有@，表示登录的用户名称是以 用户名@机构域名 组成的值。
        if(index > -1){
            //机构域名
            String domain = username.substring(index + 1);
            username=username.substring(0, index);
            //机构ID
            tenantId = userService.findTenanIdByDomain(domain);
            //获取登录用户
            paasUser = userService.findByUsernameAndTenantId(username, tenantId);
        } else{
            paasUser = userService.findByUsernameAndTenantId(username, CommonConstant.PLATFORM_TENANT_ID_VAL);
            tenantId = CommonConstant.PLATFORM_TENANT_ID_VAL;
        }

        if (paasUser == null || StringUtils.isEmpty(paasUser.getUserId())) {
            throw new InternalAuthenticationServiceException("用户名或密码错误");
        }

        //密码多次错误输入导致账号被锁定
        if(userService.isLockedByPasswordInputError(username, tenantId)){
            throw new InternalAuthenticationServiceException("密码多次错误输入导致账号被锁定，请联系管理员进行解锁。");
        }

        String isFirstLogin = StringUtils.isEmpty(paasUser.getIsFirstLogin()) ? "YES" : paasUser.getIsFirstLogin();
        paasUser.setIsFirstLogin(userService.getIsFirstLogin(isFirstLogin, tenantId));

        return checkUser(paasUser);
    }



    /**
     * 通过OpenID加载用户身份
     * @param openId
     * @return
     */
    @Override
    public SocialUserDetails loadUserByUserId(String openId) {
        throw new RuntimeException("not implements");
    }

    /**
     * 通过手机号加载用户身份
     * @param mobile
     * @return
     */
    @Override
    public UserDetails loadUserByMobile(String mobile) {
        JPaasUser loginAppUser = userService.findByMobile(mobile);
        //设置上下文用户。
        ContextUtil.setCurrentUser(loginAppUser);
        if (loginAppUser == null) {
            throw new InternalAuthenticationServiceException("手机号或密码错误");
        }
        return checkUser(loginAppUser);
    }

    /**
     * 检查用户身份是否合法
     * @param loginAppUser
     * @return
     */
    private JPaasUser checkUser(JPaasUser loginAppUser) {
        if (loginAppUser != null && !loginAppUser.isEnabled()) {
            throw new DisabledException("用户已作废");
        }
        return loginAppUser;
    }
}
