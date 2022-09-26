package com.redxun;

import com.redxun.common.ribbon.annotation.EnableFeignInterceptor;
import com.redxun.log.mq.LogInput;
import com.redxun.log.mq.LogOutput;
import com.redxun.msgsend.mq.MsgInput;
import com.redxun.msgsend.mq.MsgOutput;
import com.redxun.system.mq.ApiLogInput;
import com.redxun.system.mq.ErrLogInput;
import com.redxun.system.mq.SysInputOutput;
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
import org.springframework.retry.annotation.EnableRetry;

/**
 * paas服务器入口类
 *
 * @author 0175056
 */
@EnableFeignInterceptor
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableRetry
@EnableBinding({MailInputOutput.class, SysInputOutput.class, LogOutput.class, LogInput.class,
        ApiLogInput.class,   ErrLogOutput.class, ErrLogInput.class,
        MsgOutput.class, MsgInput.class})
public class HkcPaasServiceApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(HkcPaasServiceApplication.class, args);
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
}
