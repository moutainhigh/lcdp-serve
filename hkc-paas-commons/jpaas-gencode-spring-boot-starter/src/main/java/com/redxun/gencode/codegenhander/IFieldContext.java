package com.redxun.gencode.codegenhander;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 处理器
 */
@Service
public class IFieldContext {

    @Autowired
    private  static  final Map<String, IFieldHndler> STRATEGY_MAP = new ConcurrentHashMap<>();


    public IFieldContext(Map<String, IFieldHndler> map) {
        IFieldContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> IFieldContext.STRATEGY_MAP.put(v.getAttrName(), v));
    }


    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static IFieldHndler getByType(String type){
        IFieldHndler calcService   =STRATEGY_MAP.get(type);
        return calcService;
    }
}
