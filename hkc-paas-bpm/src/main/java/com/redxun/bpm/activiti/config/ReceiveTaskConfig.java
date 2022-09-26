package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 接收任务节点配置
 */
@Data
public class ReceiveTaskConfig extends AbstractNodeConfig {

    @Override
    public String getType() {
        return BpmNodeTypeEnums.RECEIVE_TASK.getTypeName();
    }
}
