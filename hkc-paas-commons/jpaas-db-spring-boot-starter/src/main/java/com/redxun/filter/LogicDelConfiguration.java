package com.redxun.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicDelConfiguration {
    /**
     * 注册逻辑删除过滤器。
     * @return
     */
    @Bean
    public FilterRegistrationBean registerLogicFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LogicFilter());
        registration.addUrlPatterns("*");

        registration.setName("logicFilter");
        registration.setOrder(1006);
        return registration;
    }




}
