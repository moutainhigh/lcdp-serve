package com.redxun.bpm.core.ext.FormStatus.impl;

import com.redxun.bpm.activiti.config.AbstractNodeConfig;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.ext.FormStatus.FormStatusType;
import com.redxun.bpm.core.ext.FormStatus.IFormStatus;
import org.springframework.stereotype.Component;

/**
 * 默认表单状态配置
 * @author gjh
 */
@Component
public class DefaultFormStatus implements IFormStatus {

    @Override
    public FormStatusType getType() {
        return new FormStatusType("default","默认表单状态配置");
    }

    @Override
    public String getStatus(AbstractNodeConfig config, IExecutionCmd cmd) {
        String status="N"+config.getOrderNo();
        //意见类型
        String checkType = cmd.getCheckType();
        status+="_"+checkType;
        return status;
    }
}
