package datart;

import com.redxun.common.ribbon.annotation.EnableFeignInterceptor;
import datart.core.common.ClassTransformer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignInterceptor
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.redxun.config","datart"})
public class JpaasDatartApplication {

    public static void main(String[] args) {
        ClassTransformer.transform();
        SpringApplication.run(JpaasDatartApplication.class, args);
    }

}