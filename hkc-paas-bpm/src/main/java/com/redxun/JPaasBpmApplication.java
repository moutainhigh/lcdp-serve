package com.redxun;

import com.redxun.common.ribbon.annotation.EnableFeignInterceptor;
import com.redxun.log.mq.LogOutput;
import com.redxun.msgsend.mq.MsgOutput;
import com.redxun.mq.BpmInputOutput;
import com.redxun.web.mq.ErrLogOutput;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

//import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * 模块：jpaas
 * 包名：com.redxun.bpm.config.com.redxun.bpm
 * 功能描述：JPAAS BPM 功能入口
 *
 * @author：csx
 * @date:2019/2/21
 */
@EnableFeignInterceptor
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
@EnableBinding({BpmInputOutput.class, LogOutput.class, ErrLogOutput.class, MsgOutput.class})
public class JPaasBpmApplication {

    public static void main(String[]args) throws UnknownHostException {

        ConfigurableApplicationContext application=SpringApplication.run(JPaasBpmApplication.class,args);

        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if(path==null){
            path="";
        }
        log.info("\n****************************************************************\n\t" +
                "Application JPaas-Bpm is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "swagger-ui: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "*****************************************************************");


    }


    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
}
