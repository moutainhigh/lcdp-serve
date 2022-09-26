package com.redxun.mq;


import com.redxun.bpm.activiti.config.EventConfig;
import com.redxun.bpm.activiti.eventhandler.EventHandlerContext;
import com.redxun.bpm.activiti.eventhandler.BaseEventMessage;
import com.redxun.bpm.activiti.eventhandler.IEventHandler;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class MqConsumer {
    /**
     * 处理异步事件。
     * @param message
     */
    @StreamListener(BpmInputOutput.INPUT_EVENT)
    public void handEventMsg(BaseEventMessage message){
        EventConfig eventConfig = message.getEventConfig();
        String handerType = eventConfig.getHanderType();
        IEventHandler handler= EventHandlerContext.getEventHandler(handerType) ;
        if(handler==null){
            return;
        }
        handler.handEvent(message);
    }
}
