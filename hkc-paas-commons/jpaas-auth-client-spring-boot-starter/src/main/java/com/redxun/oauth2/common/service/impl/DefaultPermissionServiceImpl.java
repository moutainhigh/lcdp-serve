package com.redxun.oauth2.common.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.oauth2.common.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 请求权限判断service
 *
 * @author yjy
 * @date 2018/10/28
 */
@Slf4j
public abstract class DefaultPermissionServiceImpl {

    @Autowired
    private SecurityProperties securityProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    public abstract Map<String,Set<String>> selectApisByGroupIdsAndRedis();

    public static JPaasUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username = null;
        if (principal instanceof JPaasUser) {
            return  ((JPaasUser) principal);
        }
        return null;
    }

    public boolean hasPermission(Authentication authentication, String requestMethod, String requestURI) {
        requestURI = requestURI.substring(requestURI.indexOf("/",requestURI.indexOf("/")+1 ));
        // 前端跨域OPTIONS请求预检放行 也可通过前端配置代理实现
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(requestMethod)) {
            return true;
        }
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            //判断是否开启url权限验证
            if (!securityProperties.getAuth().getUrlPermission().getEnable()) {
                return true;
            }
            //超级管理员admin不需认证
            JPaasUser user = getUser(authentication);
            if (user !=null && user.isAdmin()) {
                return true;
            }

            OAuth2Authentication auth2Authentication = (OAuth2Authentication)authentication;
            //判断应用黑白名单
            if (!isNeedAuth(auth2Authentication.getOAuth2Request().getClientId())) {
                return true;
            }

            //判断不进行url权限认证的api，所有已登录用户都能访问的url
            for (String path : securityProperties.getAuth().getUrlPermission().getIgnoreUrls()) {
                if (antPathMatcher.match(path, requestURI)) {
                    return true;
                }
            }

            List<String> roles = user.getRoles();
            if (CollectionUtil.isEmpty(roles)) {
                log.warn("角色列表为空：{}", authentication.getPrincipal());
                return false;
            }


            //redis获取
            Map<String, Set<String>> apiDtoMap = selectApisByGroupIdsAndRedis();

            Map<String, Object> map = getGrantUrlGroupId(apiDtoMap,requestURI,requestMethod);
            boolean inManager = (boolean)map.get("inManager");
            //url未纳入授权管理
            if(!inManager){
                return true;
            }

            Set<String> groupIdSet = (Set<String>)map.get("data");

            boolean isIncludeGroupId = false;
            for (String groupId : roles) {
                if (groupIdSet!=null&&groupIdSet.contains(groupId)) {
                    isIncludeGroupId = true;
                    break;
                }
            }
            if (!isIncludeGroupId) {
                log.warn("Access is denied! Url:" + requestURI + " User:"
                        + user.getUsername());
                return false;
            }
            return true;
        }
        return false;
    }

    private Map<String, Object> getGrantUrlGroupId(Map<String,Set<String>> apiDtoMap,String requestURI,String requestMethod) {
        Map<String, Object> map = new HashMap<>();

        String key=requestURI + ":" + requestMethod;
        if(!apiDtoMap.containsKey(key)){
            map.put("inManager", false);
            return map;
        }

        // /user/org/getUser/1
        Set<String> groupIdSet = apiDtoMap.get(requestURI);
        if (BeanUtil.isNotEmpty(groupIdSet)) {
            map.put("data", groupIdSet);
            map.put("inManager", true);
            return map;
        }

        return map;

    }

    /**
     * 判断应用是否满足白名单和黑名单的过滤逻辑
     * @param clientId 应用id
     * @return true(需要认证)，false(不需要认证)
     */
    private boolean isNeedAuth(String clientId) {
        boolean result = true;
        //白名单
        List<String> includeClientIds = securityProperties.getAuth().getUrlPermission().getIncludeClientIds();
        //黑名单
        List<String> exclusiveClientIds = securityProperties.getAuth().getUrlPermission().getExclusiveClientIds();
        if (includeClientIds.size() > 0) {
            result = includeClientIds.contains(clientId);
        } else if(exclusiveClientIds.size() > 0) {
            result = !exclusiveClientIds.contains(clientId);
        }
        return result;
    }
}
