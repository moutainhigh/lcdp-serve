package com.redxun.common.utils;


import com.redxun.common.base.entity.IUser;
import com.redxun.common.constant.SecurityConstants;
import com.redxun.common.model.Company;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.io.IOException;
import java.util.List;


/**
 * @Author: yjy
 * @Date: 2019/12/20 16:40
 */
public class SysUserUtil {

    /**
     * 获取登陆的 LoginAppUser
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static JPaasUser getLoginUser() throws IOException {
        // 当内部服务，不带token时，内部服务
        String accessToken = TokenUtil.getToken();
        if(StringUtils.isNotEmpty(accessToken)){
            return getLoginUser(accessToken);
        }else {
            IUser user = TokenUtil.getUser();
            return (JPaasUser) user;
        }
    }

    /**
     * 获取登陆的 JPaasUser
     * @param accessToken
     * @return
     */
    public static JPaasUser getLoginUser(String accessToken) {
        RedisTemplate<String, Object> redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
        //获取用户信息
        Object authObj = redisTemplate.opsForValue().get(SecurityConstants.REDIS_TOKEN_AUTH + accessToken);
        if(authObj instanceof OAuth2Authentication){
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)authObj;
            Object principal = oAuth2Authentication.getPrincipal();
            if(principal instanceof JPaasUser){
                JPaasUser jPaasUser=(JPaasUser)principal;
                return jPaasUser;
            }
        }

        return null;
    }

    /**
     * 验证token
     * @param accessToken
     * @return
     */
    public static Boolean validToken(String accessToken) {
        RedisTemplate<String, Object> redisTemplate = SpringUtil.getBean("redisTemplate", RedisTemplate.class);
        //获取用户信息
        Object authObj = redisTemplate.opsForValue().get(SecurityConstants.REDIS_TOKEN_AUTH + accessToken);
        if(authObj instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authObj;
            JPaasUser jPaasUser=(JPaasUser) oAuth2Authentication.getPrincipal();
            if(jPaasUser!=null){
                return true;
            }
        }
        return false;
    }
}


