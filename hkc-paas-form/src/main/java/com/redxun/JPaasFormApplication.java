package com.redxun;

import com.redxun.common.ribbon.annotation.EnableFeignInterceptor;
import com.redxun.log.mq.LogOutput;
import com.redxun.msgsend.mq.MsgOutput;
import com.redxun.web.mq.ErrLogOutput;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 *
 * x
 * @author：zyg
 * @date:2019/2/21
 */
@EnableAsync
@EnableFeignInterceptor
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableBinding({ LogOutput.class,  ErrLogOutput.class, MsgOutput.class})
public class JPaasFormApplication {

    public static void main(String[]args) {

        SpringApplication.run(JPaasFormApplication.class, args);
    }


    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
}
