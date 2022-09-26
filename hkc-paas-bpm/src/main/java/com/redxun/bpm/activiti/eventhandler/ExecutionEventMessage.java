package com.redxun.bpm.activiti.eventhandler;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.data.annotation.Transient;

/**
 * Activiti执行事件
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExecutionEventMessage extends BaseEventMessage {

    @Transient
    private DelegateExecution execution;


}
