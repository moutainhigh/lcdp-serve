package com.redxun.oauth.config;

import com.redxun.oauth.service.impl.RedisAuthorizationCodeServices;
import com.redxun.oauth.service.impl.RedisClientDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import javax.annotation.Resource;

/**
 * 客户端用户身份认证配置
 * @author yjy
 * @date 2018/12/12
 */
@Configuration
public class ClientDetailsConfig {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 声明 ClientDetails实现
     */
    @Bean
    public RedisClientDetailsService clientDetailsService() {
        RedisClientDetailsService clientDetailsService = new RedisClientDetailsService();
        clientDetailsService.setRedisTemplate(redisTemplate);
        return clientDetailsService;
    }

    /**
     * 随机验证码服务类实例声明
     * @return
     */
    @Bean
    public RandomValueAuthorizationCodeServices authorizationCodeServices() {
        RedisAuthorizationCodeServices redisAuthorizationCodeServices = new RedisAuthorizationCodeServices();
        redisAuthorizationCodeServices.setRedisTemplate(redisTemplate);
        return redisAuthorizationCodeServices;
    }
}
