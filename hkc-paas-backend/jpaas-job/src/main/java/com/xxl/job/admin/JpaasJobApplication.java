package com.xxl.job.admin;

import com.redxun.common.ribbon.annotation.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author csx
 */

@EnableFeignInterceptor
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.redxun.config","com.redxun.common.utils","com.xxl"})
public class JpaasJobApplication {

	public static void main(String[] args) {
        SpringApplication.run(JpaasJobApplication.class, args);
	}

}