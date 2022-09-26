package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关节点配置
 */
@Data
public class GatewayConfig  extends AbstractNodeConfig{

    private List<NodeOutcomeConfig> outs=new ArrayList<>();

    @Override
    public String getType() {
        return null;
    }
}
