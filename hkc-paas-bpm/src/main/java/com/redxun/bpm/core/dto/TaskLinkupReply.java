package com.redxun.bpm.core.dto;

import lombok.Data;

/**
 * 回复任务沟通
 */
@Data
public class TaskLinkupReply {
    private String taskId;
    private String msgTypes;
    private String opinion;
    private String opFiles;
}
