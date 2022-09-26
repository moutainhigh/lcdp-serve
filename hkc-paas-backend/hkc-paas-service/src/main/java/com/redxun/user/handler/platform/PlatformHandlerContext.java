package com.redxun.user.handler.platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理器定义入口
 * @author zyg
 */
@Service
public class PlatformHandlerContext {

    @Autowired
    private  static  final Map<String, IPlatformHandler> STRATEGY_MAP = new ConcurrentHashMap<>();
    //spring自动注入
    public PlatformHandlerContext(Map<String, IPlatformHandler> map) {
        PlatformHandlerContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> PlatformHandlerContext.STRATEGY_MAP.put(v.getPlatformType(), v));
    }

    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static IPlatformHandler getPlatformHandler(String type){
        IPlatformHandler consumer   =STRATEGY_MAP.get(type);
        return consumer;
    }


}
