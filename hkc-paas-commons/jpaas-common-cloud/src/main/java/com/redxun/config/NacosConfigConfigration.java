package com.redxun.config;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 *  构造ConfigService工具类。
 */
@Configuration
public class NacosConfigConfigration implements EnvironmentAware {

    private Environment env;

    private static final String NACOS_ADDRESS="nacos.address";
    private static final String NACOS_NAMESPACE="nacos.namespace";

    @Override
    public void setEnvironment(Environment environment) {
        this.env=environment;
    }

    @Bean
    public ConfigService configService() throws NacosException {
        NacosConfigService configService=new NacosConfigService();
        String address=this.env.getProperty(NACOS_ADDRESS);
        String namespace=this.env.getProperty(NACOS_NAMESPACE);

        ConfigService service= configService.getConfigService(address,namespace);
        return  service;
    }
}
