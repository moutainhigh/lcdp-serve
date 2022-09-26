package com.redxun.bpm.core.dto;

import lombok.Data;

/**
 * 任务节点跳转
 */
@Data
public class TaskNodeJump {
    //任务Id
    private String taskId;
    //流程实例Id
    private String actInstId;
    //跳转至任务Id
    private String destNodeId;
    //接收用户Ids
    private String toUserIds;
    //意见
    private String opinion;
    //消息类型
    private String msgTypes;
}
