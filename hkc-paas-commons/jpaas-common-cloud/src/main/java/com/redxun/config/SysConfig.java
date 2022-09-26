package com.redxun.config;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.Executor;


/**
 * 系统参数读取类。
 * <pre>
 *     这个负责读取nacos 中配置的参数
 *     nacas 的 dataid 为 nacos-config.properties
 * </pre>
 */
@Slf4j
@Component
public class SysConfig implements EnvironmentAware{

    private static final String JPAAS_CONFIG = "nacos-config.properties";
    private static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    private  Properties properties=new Properties();

    @Autowired
    private  ConfigService configService;


    public String getVal(String key) throws NacosException, IOException {
        if(properties.size()==0){
            String config = configService.getConfig(JPAAS_CONFIG, DEFAULT_GROUP, 0L);
            StringReader reader=new StringReader(config);
            properties.load(reader);
        }
        return  properties.getProperty("props."+key);
    }


    @Override
    public void setEnvironment(Environment environment) {
        try{
            configService.addListener(JPAAS_CONFIG, DEFAULT_GROUP, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String config) {
                    try{
                        StringReader reader=new StringReader(config);
                        properties.load(reader);
                        log.debug("nacos properties reload");
                    }
                    catch (Exception ex){
                        log.error("receiveConfigInfo",ex);
                    }
                }
            });
        }
        catch (Exception ex){
            log.error("setEnvironment",ex);
        }

    }
}
