package com.redxun.job.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "xxl-job")
public class XxlJobProperties {
    /**
     * xxl job admin properties.
     */
    private AdminProperties admin = new AdminProperties();
    /**
     * xxl job executor properties.
     */
    private ExecutorProperties executor = new ExecutorProperties();


    /**
     * xxl job mode properties, single: use netty, nacos: use nacos
     */
    private String mode="single";


    /**
     * xxl-job admin properties.
     */
    @Data
    static public class AdminProperties {
        /**
         * xxl job admin address.
         */
        private String adminAddresses = "http://127.0.0.1:7878/job";
    }

    /**
     * xxl-job executor properties.
     */
    @Data
    static public class ExecutorProperties {
        /**
         * xxl job registry name.
         */
        private String appName = "xxl-job-executor";
        /**
         * xxl job registry ip.
         */
        private String ip;

        private String address;
        /**
         * xxl job registry port.
         */
        private Integer port = 7474;
        /**
         * xxl job admin registry access token.
         */
        private String accessToken;
        /**
         * xxl job log files path.
         */
        private String logPath = "logs/jpaas-job";
        /**
         * xxl job log files retention days.
         */
        private Integer logRetentionDays = 30;
    }
}
