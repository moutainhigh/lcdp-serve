package com.redxun.job.autoconfigure;

import com.redxun.job.builder.XxlJobSpringExecutorBuilder;
import com.redxun.job.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXlJob自动配置
 */
@Configuration
@EnableConfigurationProperties({XxlJobProperties.class})
@Slf4j
public class XxlJobAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "xxl-job",name = "enabled",havingValue = "true")
    public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties prop) {
        log.info("============ xxl job config init...==============");

        return new XxlJobSpringExecutorBuilder()
                .withAdminAddresses(prop.getAdmin().getAdminAddresses())
                .withAppName(prop.getExecutor().getAppName())
                .withIp(prop.getExecutor().getIp())
                .withPort(prop.getExecutor().getPort())
                .withAccessToken(prop.getExecutor().getAccessToken())
                .withLogPath(prop.getExecutor().getLogPath())
                .withMode(prop.getMode())
                .withAddress(prop.getExecutor().getAddress())
                .withLogRetentionDays(prop.getExecutor().getLogRetentionDays())
                .build();
    }
}
