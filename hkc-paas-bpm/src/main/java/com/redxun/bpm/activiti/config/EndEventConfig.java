package com.redxun.bpm.activiti.config;

import lombok.Data;

/**
 * 流程结束事件配置
 */
@Data
public class EndEventConfig extends AbstractNodeConfig {

    @Override
    public String getType() {
        return BpmNodeTypeEnums.END_EVENT.getTypeName();
    }
}
