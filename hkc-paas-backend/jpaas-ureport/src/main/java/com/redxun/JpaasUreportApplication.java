package com.redxun;
import com.redxun.common.ribbon.annotation.EnableFeignInterceptor;
import com.redxun.web.mq.ErrLogOutput;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ServletComponentScan
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignInterceptor
@EnableFeignClients
@SpringBootApplication
@EnableBinding({  ErrLogOutput.class})
public class JpaasUreportApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaasUreportApplication.class, args);
    }
}
