package com.redxun.common.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {
    /**
     * 注册LoginUserFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean registerLoginFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new WebContextFilter());
        registration.addUrlPatterns("*");

        registration.setName("loginUserFilter");
        registration.setOrder(1001);
        return registration;
    }

    /**
     * 注册MessageFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean registerMessageFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new MessageFilter());
        registration.addUrlPatterns("*");

        registration.setName("messageFilter");
        registration.setOrder(1004);
        return registration;
    }

    /**
     * 注册TraceFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean registerTraceFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new TraceFilter());
        registration.addUrlPatterns("*");

        registration.setName("traceFilter");
        registration.setOrder(1002);
        return registration;
    }


}
