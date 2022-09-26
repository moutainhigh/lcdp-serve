package com.redxun.oauth2.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redxun.oauth2.common.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 将应用配置进行固定。
 */
@Configuration
@Slf4j
public class AppConfig {

    @Autowired
    private AppProperties appProperties;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 按应用获取应用的Details
     * @return
     */
    @Bean
    public ClientDetails getByApp(){
        BaseClientDetails details = new BaseClientDetails(appProperties.getClientId(),
                appProperties.getResourceIds(),
                appProperties.getScope(),
                appProperties.getAuthorizedGrantTypes(),
                appProperties.getAuthorities(),
                appProperties.getWebServerRedirectUri());

        details.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(appProperties.getScope()));
        details.setClientSecret(appProperties.getClientSecret());

        if (appProperties.getRefreshTokenValidity() != null) {
            details.setRefreshTokenValiditySeconds(appProperties.getRefreshTokenValidity());
        }

        if (appProperties.getAccessTokenValidity() != null) {
            details.setAccessTokenValiditySeconds(appProperties.getAccessTokenValidity());
        }

        String json = appProperties.getAdditionalInformation();
        if (!StringUtils.isEmpty(json)) {
            try {
                Map<String, Object> additionalInformation = (Map)objectMapper.readValue(json, Map.class);
                details.setAdditionalInformation(additionalInformation);
            } catch (Exception var6) {
                log.warn("Could not decode JSON for additional information: " + details, var6);
            }
        }
        String scopes = appProperties.getScope();
        if (scopes != null) {
            details.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(scopes));
        }
        return details;
    }
}
