package com.redxun.config;

import com.redxun.common.utils.SpringUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties(prefix="formasynctask")
@RefreshScope
public class FormAsyncTaskProperties {
    /**
     *  线程池维护线程的最小数量|最大数量|队列最大长度.
     */
    private String corePoolJson="5|20|50";

    public String getCorePoolJson() {
        return corePoolJson;
    }

    public void setCorePoolJson(String corePoolJson) {
        this.corePoolJson = corePoolJson;
        SpringUtil.publishEvent(new PropertiesEvent(this));
    }
}
