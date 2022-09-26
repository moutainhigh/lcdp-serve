package com.redxun.bpm.activiti.config;

import lombok.Data;

/**
 * 开始节点属性配置
 */
@Data
public class StartEventConfig extends AbstractNodeConfig {
    /**
     * 活动开始脚本
     */
    private String startScript;
    /**
     * 活动结束脚本
     */
    private String completeScript;

    @Override
    public String getType() {
        return BpmNodeTypeEnums.START_EVENT.getTypeName();
    }
}
