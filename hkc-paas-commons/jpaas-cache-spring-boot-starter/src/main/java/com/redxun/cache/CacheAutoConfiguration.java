package com.redxun.cache;

import com.alibaba.nacos.api.config.ConfigService;
import com.redxun.cache.impl.J2CacheImpl;
import com.redxun.cache.impl.MemoryImpl;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.J2CacheBuilder;
import net.oschina.j2cache.J2CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;

import java.io.StringReader;
import java.util.Properties;

public class CacheAutoConfiguration {

    @Value(value="${spring.profiles.active}")
    private String active="";
    @Autowired
    private ConfigService configService;

    @Bean
    @ConditionalOnProperty(prefix = "redxun",name = "cache",havingValue = "j2cache")
    public J2CacheConfig cacheConfig() throws Exception {
        String config = configService.getConfig("j2cache-"+active+".properties", "DEFAULT_GROUP", 0L);
        Properties properties = new Properties();
        properties.load(new StringReader(config));
        J2CacheConfig j2CacheConfig = J2CacheConfig.initFromConfig(properties);

        return j2CacheConfig;
    }

    @Bean
    @ConditionalOnProperty(prefix = "redxun",name = "cache",havingValue = "j2cache")
    @DependsOn("cacheConfig")
    public CacheChannel cacheChannel(J2CacheConfig j2CacheConfig) throws Exception {
        J2CacheBuilder builder = J2CacheBuilder.init(j2CacheConfig);
        CacheChannel channel = builder.getChannel();
        return channel;
    }

    @Bean
    @ConditionalOnProperty(prefix = "redxun",name = "cache",havingValue = "j2cache")
    @DependsOn("cacheChannel")
    public ICache j2Cache(CacheChannel cacheChannel) throws Exception {
        J2CacheImpl j2Cache=new J2CacheImpl();
        j2Cache.setCacheChannel(cacheChannel);
        return (ICache) j2Cache;
    }

    @Bean
    @ConditionalOnProperty(prefix = "redxun",name = "cache",havingValue = "memory")
    public ICache memoryCache() throws Exception {
        MemoryImpl memory=new MemoryImpl();
        return (ICache) memory;
    }


}
