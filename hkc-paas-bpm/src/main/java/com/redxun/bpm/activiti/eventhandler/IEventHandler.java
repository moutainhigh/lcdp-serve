package com.redxun.bpm.activiti.eventhandler;

/**
 * 事件调用接口。
 *
 * @author ray
 */
public interface IEventHandler {

    /**
     * 处理器类型。
     * @return
     */
    EventHanderType getType();

    /**
     * 处理事件。
     * @param message
     */
    void handEvent(BaseEventMessage message);


}
