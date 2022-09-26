package com.redxun.bpm.core.dto;

import lombok.Data;

/**
 * 沟通任务接收参数类型
 */
@Data
public class TaskLinkup {
    //任务Id
    private String taskId;
    //接收用户账号
    private String toUserAccounts;
    //意见
    private String opinion;
    //消息类型
    private String msgTypes;
    //附件
    private String opFiles;

}
