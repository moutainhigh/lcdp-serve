package com.redxun.bpm.core.ext.FormStatus;

import com.redxun.bpm.activiti.config.AbstractNodeConfig;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.IExecutionCmd;

/**
 * 流程表单状态
 */
public interface IFormStatus {



    /**
     * 类型
     * @return
     */
    FormStatusType getType();

    String getStatus(AbstractNodeConfig config, IExecutionCmd cmd);


}
