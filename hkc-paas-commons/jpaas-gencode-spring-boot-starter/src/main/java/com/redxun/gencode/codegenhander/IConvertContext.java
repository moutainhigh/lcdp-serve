package com.redxun.gencode.codegenhander;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * json与实体互相解析执行者Holder类
 */
@Service
public class IConvertContext {

    @Autowired
    private  static  final Map<String, IConvertHandler> STRATEGY_MAP = new ConcurrentHashMap<>();


    public IConvertContext(Map<String, IConvertHandler> map) {
        IConvertContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> IConvertContext.STRATEGY_MAP.put(v.getCreateType(), v));
    }


    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static IConvertHandler getByType(String type){
        IConvertHandler calcService   =STRATEGY_MAP.get(type);
        return calcService;
    }
}
