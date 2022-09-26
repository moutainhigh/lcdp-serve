package com.redxun.bpm.core.service;

import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.IExecutionCmd;

/**
 * 处理任务服务。
 */
public interface ITaskHandler {

    /**
     * 获取类型。
     * @return
     */
    default boolean canHandler( String checkType){
        return false;
    };

    /**
     * 任务办理。
     * @param bpmTask
     * @param cmd
     */
    default void handTask(BpmTask bpmTask, IExecutionCmd cmd,  UserTaskConfig userTaskConfig){};
}
