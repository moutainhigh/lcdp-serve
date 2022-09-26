package com.redxun.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MyBatis参数配置
 */
@Component
@ConfigurationProperties(prefix = "mybatis")
@Setter
@Getter
public class MyBatisConfig {

    /**
     * mapping 文件。
     */
    private String mapperLocations="";
}
