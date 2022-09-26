package com.redxun.bpm.activiti.config;

import lombok.Data;

/**
 * 选择网关配置
 */
@Data
public class InclusiveGatewayConfig extends GatewayConfig{

    @Override
    public String getType() {
        return BpmNodeTypeEnums.INCLUSIVE_GATEWAY.getTypeName();
    }
}
