package com.redxun.common.ribbon.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * feign拦截器，只包含http相关数据
 *
 */
public class FeignHttpInterceptorConfig {

    /**
     * 使用feign client访问别的微服务时，将上游传过来的access_token、username、roles等信息放入header传递给下一个服务
     */
    @Bean
    public RequestInterceptor httpFeignInterceptor() {
        return new TokenRelayRequestIntecepor();
    }


}
