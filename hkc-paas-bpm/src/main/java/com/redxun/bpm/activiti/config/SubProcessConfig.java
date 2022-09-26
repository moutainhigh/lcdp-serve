package com.redxun.bpm.activiti.config;

/**
 * 子流程配置
 */
public class SubProcessConfig extends AbstractNodeConfig {

    @Override
    public String getType() {
         return BpmNodeTypeEnums.SUB_PROCESS.getTypeName();
    }
}
