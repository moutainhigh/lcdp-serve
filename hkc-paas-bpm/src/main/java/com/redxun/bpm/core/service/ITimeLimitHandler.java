package com.redxun.bpm.core.service;

/**
 * 时间计算处理器
 *
 * @author lenovo
 */
public interface ITimeLimitHandler {

    /**
     * 根据上下文变量任务到期时间，时间单位为分钟。
     *
     * @param userId              当前执行人
     * @param depId               当前人部门
     * @param processDefinitionId 流程定义ID
     * @param nodeId              节点ID
     * @param instId              流程实例ID
     * @param taskId              任务ID
     * @return
     */
    Integer getExpireTimeLimit(String userId, String depId, String processDefinitionId, String nodeId, String instId, String taskId);

    /**
     * 根据上下文变量计算消息发送时间，时间单位为分钟。
     *
     * @param userId              当前执行人
     * @param depId               当前人部门
     * @param processDefinitionId 流程定义ID
     * @param nodeId              节点ID
     * @param instId              流程实例ID
     * @param taskId              任务ID
     * @return
     */
    Integer getSendTimeLimit(String userId, String depId, String processDefinitionId, String nodeId, String instId, String taskId);
}
