package com.redxun.oauth2.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 系统安全的参数配置
 * @author yjy
 * @date 2019/1/4
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "redxun.security")
@RefreshScope
public class SecurityProperties {

    private AuthProperties auth = new AuthProperties();

    /**
     * 忽略的URL的配置，这下面的URL都不需要进行登录验证，可以直接访问。
     */
    private PermitProperties ignore = new PermitProperties();


    private ValidateCodeProperties code = new ValidateCodeProperties();
}
