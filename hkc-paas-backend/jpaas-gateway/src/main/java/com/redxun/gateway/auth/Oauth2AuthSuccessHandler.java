package com.redxun.gateway.auth;

import cn.hutool.core.collection.CollectionUtil;
import com.redxun.common.constant.SecurityConstants;
import com.redxun.common.model.JPaasUser;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证成功处理类
 *
 * @author yjy
 * @date 2019/10/7
 * <p>
 */
public class Oauth2AuthSuccessHandler implements ServerAuthenticationSuccessHandler {
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        JPaasUser user = (JPaasUser)authentication.getPrincipal();
        //获取用户ID跟用户名
        String userId = user.getUserId();
        String username = user.getUsername();
        //登录成功后，获取当前用户信息
        OAuth2Authentication oauth2Authentication = (OAuth2Authentication)authentication;
        //获取ClientId
        String clientId = oauth2Authentication.getOAuth2Request().getClientId();

        ServerWebExchange exchange = webFilterExchange.getExchange();
        //认证成功后，在请求头增加以下配置
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                .headers(h -> {
                    h.add(SecurityConstants.USER_ID_HEADER, String.valueOf(userId));
                    h.add(SecurityConstants.USER_HEADER, username);
                    h.add(SecurityConstants.TENANT_HEADER, clientId);
                    h.add(SecurityConstants.ROLE_HEADER, CollectionUtil.join(authentication.getAuthorities(), ","));
                })
                .build();

        ServerWebExchange build = exchange.mutate().request(serverHttpRequest).build();
        return webFilterExchange.getChain().filter(build);
    }
}
