package com.redxun.bpm.core.entity;

import lombok.Data;

/**
 * 计算任务审批页的沟通配置
 */
@Data
public class TaskLinkupParam {
    /**
     * 是否允许发起沟通
     */
    private Boolean canStartLinkup=true;
    /**
     * 是否允许撤回沟通
     */
    private Boolean canRevokeLinkup=false;
    /**
     * 是否允许回复沟通
     */
    private Boolean canReplyLinkup=false;



}
