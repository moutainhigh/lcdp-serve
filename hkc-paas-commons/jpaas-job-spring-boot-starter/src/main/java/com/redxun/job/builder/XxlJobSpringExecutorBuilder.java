package com.redxun.job.builder;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;

/**
 * XXlJob Spring 执行者构建器
 */
public class XxlJobSpringExecutorBuilder {
    /**
     * target object.
     */
    private XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
    /**
     * xxl job admin address.
     */
    private String adminAddresses;
    /**
     * xxl job registry name.
     */
    private String appName;

    private String address;
    /**
     * xxl job registry ip.
     */
    private String ip;
    /**
     * xxl job registry port.
     */
    private Integer port;
    /**
     * xxl job admin retistry access token.
     */
    private String accessToken;
    /**
     * xxl job log files path.
     */
    private String logPath;
    /**
     * xxl job log files retention days.
     */
    private Integer logRetentionDays;

    private String mode;

    public XxlJobSpringExecutorBuilder withAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
        return this;
    }

    public XxlJobSpringExecutorBuilder withAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public XxlJobSpringExecutorBuilder withIp(String ip) {
        this.ip = ip;
        return this;
    }

    public XxlJobSpringExecutorBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public XxlJobSpringExecutorBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public XxlJobSpringExecutorBuilder withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public XxlJobSpringExecutorBuilder withLogPath(String logPath) {
        this.logPath = logPath;
        return this;
    }

    public XxlJobSpringExecutorBuilder withLogRetentionDays(int logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
        return this;
    }

    public XxlJobSpringExecutorBuilder withMode(String mode) {
        this.mode = mode;
        return this;
    }


    public XxlJobSpringExecutor build() {
        executor.setAdminAddresses(adminAddresses);
        executor.setAppname(appName);
        executor.setIp(ip);
        executor.setPort(port);
        executor.setAddress(address);
        executor.setAccessToken(accessToken);
        executor.setLogPath(logPath);
        executor.setLogRetentionDays(logRetentionDays);
        executor.setMode(mode);
        return this.executor;
    }
}
