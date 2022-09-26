package com.redxun.log.properties;

import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 日志数据源配置
 * logType=db时生效(非必须)，如果不配置则使用当前数据源
 *
 * @author yjy
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "redxun.audit-log.datasource")
@RefreshScope
public class LogDbProperties extends HikariConfig {

}
