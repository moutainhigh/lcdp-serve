package com.redxun.oauth.handler.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理器定义入口
 * @author zyg
 */
@Service
public class SocialHandlerContext {

    @Autowired
    private  static  final Map<String, ISocialHandler> STRATEGY_MAP = new ConcurrentHashMap<>();
    //spring自动注入
    public SocialHandlerContext(Map<String, ISocialHandler> map) {
        SocialHandlerContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> SocialHandlerContext.STRATEGY_MAP.put(v.getType(), v));
    }

    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static ISocialHandler getSocialHandler(String type){
        ISocialHandler consumer   =STRATEGY_MAP.get(type);
        return consumer;
    }


}
