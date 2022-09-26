package com.redxun.system.messagehandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理器定义入口
 * @author zyg
 */
@Service
public class MessageHandlerContext {

    @Autowired
    private  static  final Map<String, IMessageHandler> STRATEGY_MAP = new ConcurrentHashMap<>();

    public MessageHandlerContext(Map<String, IMessageHandler> map) {
        MessageHandlerContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> MessageHandlerContext.STRATEGY_MAP.put(v.getType().getType(), v));
    }

    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static IMessageHandler getMessageHandler(String type){
        IMessageHandler consumer   =STRATEGY_MAP.get(type);
        return consumer;
    }

    /**
     * 消息类型。
     * @return
     */
    public  static List<MessageType> getTypes(){
        List<MessageType> list=new ArrayList<>();
        STRATEGY_MAP.values().forEach(p->{
            list.add(p.getType());
        });
        return list;
    }
}
