package com.redxun.bpm.activiti.eventhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件处理器上下文
 */
@Component
public class EventHandlerContext {

    @Autowired
    private  static   final Map<String, IEventHandler> STRATEGY_MAP = new ConcurrentHashMap<>();

    public EventHandlerContext(Map<String, IEventHandler> map) {
        EventHandlerContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> EventHandlerContext.STRATEGY_MAP.put(v.getType().getKey(), v));
    }

    /**
     * 获取处理器。
     * @param plugin
     * @return
     */
    public  static IEventHandler getEventHandler(String plugin){
        IEventHandler eventHandler=STRATEGY_MAP.get(plugin);
        return eventHandler;
    }

    /**
     * 返回事件处理器类型列表。
     * @return
     */
    public static List<EventHanderType> getEventHandlerTypes(){
        List<EventHanderType> list=new ArrayList<>(STRATEGY_MAP.size());
        STRATEGY_MAP.values().forEach(k->{
            list.add(k.getType());
        });
        return  list;
    }
}
