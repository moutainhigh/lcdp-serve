package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 网类节点的配置
 */
@Data
public class ExclusiveGatewayConfig extends GatewayConfig {
    /**
     * 网关节点跳出分支配置
     */
    private List<NodeOutcomeConfig> outs=new ArrayList<>();

    @Override
    public String getType() {
        return BpmNodeTypeEnums.EXCLUSIVE_GATEWAY.getTypeName();
    }
}
