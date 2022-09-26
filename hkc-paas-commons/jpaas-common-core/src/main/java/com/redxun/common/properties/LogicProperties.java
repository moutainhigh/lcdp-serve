package com.redxun.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 逻辑删除配置
 * @author szw
 * @date 2022/07/21
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "redxun.logicDel")
@RefreshScope
public class LogicProperties {
    /**
     * 是否开启逻辑删除
     */
    private String isLogicDel;
}
