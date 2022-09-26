package com.redxun.bpm.activiti.processhandler;

import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.core.entity.*;

/**
 * 流程与任务处理器
 * @author ray
 */
public interface ProcessHandler {
    //处理器名称
    String getName();

    //结束时执行
    default void endHandle(BpmInst bpmInst) {
    }


    /**
     * 流程启动之后执行。
     * @param processConfig
     * @param cmd
     * @param bpmInst
     * @return
     */
    default void processStartAfterHandle(ProcessConfig processConfig, IExecutionCmd cmd, BpmInst bpmInst) {
    }


    /**
     * 流程启动之前执行。
     * ProcessStartCmd
     *
     * @param cmd
     */
    default void processStartPreHandle(IExecutionCmd cmd,BpmInst bpmInst) {
    }


    /**
     * 当前任务处理
     *
     * @param cmd    当前任务执行的
     * @param nodeId
     * @param busKey
     */
    default void taskAfterHandle(IExecutionCmd cmd, String nodeId, String busKey) {
    }

    /**
     * 当前任务处理
     *
     * @param cmd    当前任务执行的
     * @param task
     * @param busKey
     */
    default void taskPreHandle(IExecutionCmd cmd, BpmTask task, String busKey) {
    }
}
