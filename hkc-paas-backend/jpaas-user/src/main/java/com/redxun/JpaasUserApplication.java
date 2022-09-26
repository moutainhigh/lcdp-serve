package com.redxun;

import com.redxun.common.ribbon.annotation.EnableFeignInterceptor;
import com.redxun.log.mq.LogOutput;
import com.redxun.msgsend.mq.MsgOutput;
import com.redxun.user.mq.MailInputOutput;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yjy
 */
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignInterceptor
@EnableFeignClients
@SpringBootApplication
@EnableBinding({MailInputOutput.class,LogOutput.class, ErrLogOutput.class, MsgOutput.class})
public class JpaasUserApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(JpaasUserApplication.class);
        //启动完成后执行授权接口监听器。
        springApplication.run(args);
    }


    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
}
