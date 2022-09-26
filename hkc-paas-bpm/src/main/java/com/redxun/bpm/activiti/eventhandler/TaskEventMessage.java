package com.redxun.bpm.activiti.eventhandler;

import com.redxun.bpm.core.entity.BpmTask;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.data.annotation.Transient;

/**
 * 任务事件消息
 */
@Getter
@Setter
@Accessors(chain = true)
public class TaskEventMessage extends BaseEventMessage {

    /**
     * 审批任务ID
     */
    protected String taskId="";

    @Transient
    private TaskEntity taskEntity;

    @Transient
    private BpmTask bpmTask;



}
