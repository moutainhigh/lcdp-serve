package com.redxun.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * 构造NacosConfig配置。
 */
public class NacosConfigService {



    public ConfigService getConfigService(String address,String namespace) throws NacosException {

        if(StringUtils.isEmpty(address)){
            address="localhost:8848";
        }
        if(StringUtils.isEmpty(namespace)){
            namespace="local";
        }
        Properties properties = new Properties();
        // nacos服务器地址，127.0.0.1:8848
        properties.put(PropertyKeyConst.SERVER_ADDR, address);
        // 配置中心的命名空间id
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        ConfigService configService = NacosFactory.createConfigService(properties);
        return configService;
    }


}
