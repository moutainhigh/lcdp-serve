package com.redxun.user.org.service.impl;


import com.redxun.user.org.service.ISyncService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 同步服务配置
 */
@Configuration
public class SyncServiceConfig {
    //这里在nacos上加上一句   redxun.syncorg=weixin或者redxun.syncorg=dd
    @Bean
    @ConditionalOnProperty(prefix = "redxun",name = "syncorg",havingValue = "weixin")
    public ISyncService weixinImpl()  {
        ISyncService wxService=new WeixinSyncServiceImpl();
        return wxService;
    }

    @Bean
    @ConditionalOnProperty(prefix = "redxun",name = "syncorg",havingValue = "dd")
    public ISyncService ddImpl()  {
        ISyncService ddService=new DdSyncServiceImpl();
        return ddService;
    }


    @Bean
    @ConditionalOnProperty(prefix = "redxun",name = "syncorg",havingValue = "dummy")
    public ISyncService dummyImpl()  {
        ISyncService ddService=new DummySyncServiceImpl();
        return ddService;
    }
}
