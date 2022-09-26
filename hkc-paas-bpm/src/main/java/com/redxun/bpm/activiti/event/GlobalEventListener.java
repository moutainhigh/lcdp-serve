package com.redxun.bpm.activiti.event;

import com.redxun.bpm.activiti.event.handler.*;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 全局事件监听器
 * @author csx
 *
 */
@Slf4j
public class GlobalEventListener implements ActivitiEventListener {

    //事件类型，处理器映射
    Map<String, EventHandler> eventTypes=new HashMap<>();

    public GlobalEventListener(){
        initEventHandlers();
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        String eventType=event.getType().name();
        //加上监听事件处理
        EventHandler handler=eventTypes.get(eventType);
        if(handler!=null){
            handler.handle(event);
        }
    }

    @Override
    public boolean isFailOnException() {
        return true;
    }

    /**
     * 初始化事件处理器
     */
    public void initEventHandlers(){
        //注册流程实例创建事件处理器
        eventTypes.put(EventType.PROCESS_STARTED.name(), new ProcessStartedEventHandler());
        //注册流程实例完成结束事件处理器
        eventTypes.put(EventType.PROCESS_COMPLETED.name(),new ProcessCompletedEventHandler());
        //注册流程任务创建事件处理器
        eventTypes.put(EventType.TASK_CREATED.name(),new TaskCreatedEventHandler());
        //注册流程任务完成事件处理器
        eventTypes.put(EventType.TASK_COMPLETED.name(),new TaskCompletedEventHandler());
        //注册流程活动开始事件处理器
        eventTypes.put(EventType.ACTIVITY_STARTED.name(),new ActivityStartedEventHandler());
        //注册流程活动结束事件处理器
        eventTypes.put(EventType.ACTIVITY_COMPLETED.name(),new ActivityCompletedEventHandler());
    }
}
