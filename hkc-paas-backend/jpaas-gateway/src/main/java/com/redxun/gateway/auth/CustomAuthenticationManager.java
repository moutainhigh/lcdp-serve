package com.redxun.gateway.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import reactor.core.publisher.Mono;

/**
 * 自定义认证处理器
 * @author yjy
 * @date 2019/10/6
 * <p>
 */
@Slf4j
public class CustomAuthenticationManager implements ReactiveAuthenticationManager {
    /**
     * OAuth2 认证Token存储器
     */
    private TokenStore tokenStore;

    public CustomAuthenticationManager(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * 认证处理
     * @param authentication 认证实体对象
     * @return
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(a -> a instanceof BearerTokenAuthenticationToken)
                .cast(BearerTokenAuthenticationToken.class)
                .map(BearerTokenAuthenticationToken::getToken)
                .flatMap((accessTokenValue -> {
                    //从缓存中获取token
                    OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
                    //tokenStore.readAuthentication(accessToken);
                    //不存在，则表示Token不合法
                    if (accessToken == null) {
                        log.error(" token is null");
                        return Mono.error(new InvalidTokenException("tokenInvalid"));
                    }

                    //获取认证对象
                    OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
                    if (result == null || !result.isAuthenticated()) { //认证不通过，则抛出令牌不合法
                        log.error(" result is null");
                        return Mono.error(new InvalidTokenException("tokenInvalid"));
                    }

                    if (accessToken.isExpired()) {//Token过期了
                        log.error(" token is expired");
                        tokenStore.removeAccessToken(accessToken);
                        return Mono.error(new InvalidTokenException("tokenExpried"));
                    }

                    return Mono.just(result);
                }))
                .cast(Authentication.class);
    }
}
