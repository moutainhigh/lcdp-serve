package com.redxun.bpm.activiti.event.handler;

import com.redxun.bpm.activiti.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiProcessStartedEvent;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**
 * 流程启动事件监听处理器
 *
 */
@Slf4j
public class ProcessStartedEventHandler extends BaseProcessHandler<ActivitiProcessStartedEvent> {

    @Override
    public EventType getEventType() {
        return EventType.PROCESS_STARTED;
    }

    @Override
    public void handle(ActivitiProcessStartedEvent eventEntity) {
        //处理事件
        handEvent((ExecutionEntity) eventEntity.getEntity());
    }
}
