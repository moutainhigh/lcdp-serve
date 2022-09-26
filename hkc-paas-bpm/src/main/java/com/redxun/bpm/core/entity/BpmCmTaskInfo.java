package com.redxun.bpm.core.entity;

import lombok.Data;

import java.util.Date;

/**
 * 回复任务明细
 */
@Data
public class BpmCmTaskInfo {
    /**
     * 任务Id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 沟通人
     */
    private String cmUserId;
    /**
     * 沟通用户名
     */
    private String cmUserName;
    /**
     * 意见
     */
    private String opinion;
    /**
     * 沟通回复时间
     */
    private Date cmTime;

    public BpmCmTaskInfo(){

    }

    public BpmCmTaskInfo(BpmCheckHistory bpmCheckHistory){
        this.taskId=bpmCheckHistory.getTaskId();
        this.taskName=bpmCheckHistory.getNodeName();
        this.cmUserId=bpmCheckHistory.getCreateBy();
        this.opinion=bpmCheckHistory.getRemark();
        this.cmTime=bpmCheckHistory.getCreateTime();
    }

    public BpmCmTaskInfo(BpmTask bpmTask){
        this.taskId=bpmTask.getTaskId();
        this.taskName=bpmTask.getName();
        this.cmUserId=bpmTask.getAssignee();
        this.opinion="";
        this.cmTime=bpmTask.getCreateTime();
    }
}
