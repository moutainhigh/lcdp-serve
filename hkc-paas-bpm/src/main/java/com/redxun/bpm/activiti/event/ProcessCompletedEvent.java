package com.redxun.bpm.activiti.event;

import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.core.entity.BpmInst;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.context.ApplicationEvent;

/**
 * 流程结束事件。
 */
public class ProcessCompletedEvent  extends ApplicationEvent {

    private ExecutionEntity entity;

    private BpmInst bpmInst;

    public ProcessCompletedEvent(final ProcessConfig config, ExecutionEntity entity) {
        super(config);
        this.entity=entity;
    }

    public ProcessCompletedEvent(final ProcessConfig config, ExecutionEntity entity,BpmInst bpmInst) {
        super(config);
        this.entity=entity;
        this.bpmInst=bpmInst;
    }

    public ProcessCompletedEvent(final ProcessConfig config, BpmInst bpmInst) {
        super(config);
        this.bpmInst=bpmInst;
    }

    public ExecutionEntity getEntity(){
        return  entity;
    }

    public BpmInst getBpmInst(){
        return this.bpmInst;
    }
}
