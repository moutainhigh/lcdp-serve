package com.redxun.config;


import com.alibaba.cloud.seata.web.SeataHandlerInterceptor;
import com.redxun.web.config.DefaultWebMvcConfig;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Spring MVC 配置入口
 * @author yjy
 * @date 2019/8/5
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends DefaultWebMvcConfig {
    /**
     * 增加Seata的拦截
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SeataHandlerInterceptor()).addPathPatterns(new String[]{"/**"});
    }
}
