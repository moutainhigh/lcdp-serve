package com.redxun.form.core.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FieldValueHandlerContext {


    @Autowired
    private  static   final Map<String, ITableFieldValueHandler> STRATEGY_MAP  = new ConcurrentHashMap<>();

    public FieldValueHandlerContext(Map<String, ITableFieldValueHandler> map) {
        FieldValueHandlerContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> FieldValueHandlerContext.STRATEGY_MAP.put(v.getMapType(), v));
    }

    /**
     * 获取处理器。
     * @param plugin
     * @return
     */
    public  static ITableFieldValueHandler getValueHandler(String plugin){
        ITableFieldValueHandler attrHandler=STRATEGY_MAP.get(plugin);
        return  attrHandler;
    }
}
