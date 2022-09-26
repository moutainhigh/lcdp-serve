package com.redxun.bpm.activiti.config;

import lombok.Data;

/**
 * 服务任务节点配置
 */
@Data
public class ServiceTaskConfig extends AbstractNodeConfig {

    /**
     * 任务类型
     */
    private String taskType="";

    /**
     * 配置数据。
     */
    private String setting="";


    @Override
    public String getType() {
        return BpmNodeTypeEnums.SERVICE_TASK.getTypeName();
    }
}
