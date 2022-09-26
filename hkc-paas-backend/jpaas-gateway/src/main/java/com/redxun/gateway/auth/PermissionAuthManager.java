package com.redxun.gateway.auth;

import com.redxun.cache.CacheUtil;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.SpringUtil;
import com.redxun.gateway.feign.RemoteApiService;
import com.redxun.oauth2.common.service.impl.DefaultPermissionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

//import com.redxun.constant.CacheConstant;

/**
 * url拦截权限认证
 *
 * @author yjy
 * @author  csx
 * @date 2019/10/6
 * <p>
 *
 *
 */
@Slf4j
@Component
public class PermissionAuthManager extends DefaultPermissionServiceImpl implements ReactiveAuthorizationManager<AuthorizationContext> {





    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        return authentication.map(auth -> {
            ServerWebExchange exchange = authorizationContext.getExchange();
            ServerHttpRequest request = exchange.getRequest();
            boolean isPermission = super.hasPermission(auth, request.getMethodValue(), request.getURI().getPath());
            return new AuthorizationDecision(isPermission);
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    public Map<String, Set<String>> selectApisByGroupIdsAndRedis(){
        RemoteApiService remoteApiService= SpringUtil.getBean(RemoteApiService.class);
        Map<String, Set<String>> menuMap=(Map<String, Set<String>>) CacheUtil.get(CommonConstant.API_REGION, CommonConstant.API_KEY);
        if(menuMap==null){
            //将接口授权合并到菜单授权
            menuMap = remoteApiService.getUrlGroupIdMap();
            CacheUtil.set(CommonConstant.API_REGION,CommonConstant.API_KEY, menuMap);
        }
        return menuMap;
    }
}
