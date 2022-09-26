package com.redxun.common.ribbon.annotation;

import com.redxun.common.ribbon.config.FeignHttpInterceptorConfig;
import com.redxun.common.ribbon.config.FeignInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启feign拦截器传递数据给下游服务，包含基础数据和http的相关数据
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FeignHttpInterceptorConfig.class})
public @interface EnableFeignInterceptor {

}
