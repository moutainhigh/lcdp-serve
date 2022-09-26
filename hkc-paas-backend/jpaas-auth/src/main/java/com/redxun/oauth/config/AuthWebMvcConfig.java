package com.redxun.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

/**
 * 认证WebMVC的配置
 */
@Configuration
public class AuthWebMvcConfig  {

    @Bean
    public RequestContextListener requestContextListenerBean() {
        return new RequestContextListener();
    }
}
