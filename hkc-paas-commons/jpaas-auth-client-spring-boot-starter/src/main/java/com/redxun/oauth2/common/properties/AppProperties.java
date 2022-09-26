package com.redxun.oauth2.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 安全中的系统参数配置
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "redxun.app")
@RefreshScope
public class AppProperties {

    private String clientId="webApp";

    private String clientSecret="webApp";
    //应用密钥(明文)
    private String clientSecretStr;
    //回调地址
    private String webServerRedirectUri;
    //令牌有效期
    private Integer accessTokenValidity;
    //令牌刷新有效期
    private Integer refreshTokenValidity;
    //资源限定串(逗号分割)
    private String resourceIds;
    //范围
    private String scope = "all";
    //5种oauth授权方式(authorization_code,password,refresh_token,client_credentials)
    private String authorizedGrantTypes = "";
    //权限
    private String authorities;
    private String additionalInformation = "{}";
    //是否自动授权 是-true
    private boolean autoapprove =false;


}
