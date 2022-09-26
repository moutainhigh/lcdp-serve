package com.redxun.bpm.activiti.event.handler;

import com.redxun.bpm.activiti.event.EventType;
import org.activiti.engine.delegate.event.ActivitiEvent;

/**
 * Activiti事件处理器
 */
public interface EventHandler <T extends ActivitiEvent> {

    EventType getEventType();
    /**
     *  处理事件
     * @param eventEntity
     */
    void handle(T eventEntity);
}
