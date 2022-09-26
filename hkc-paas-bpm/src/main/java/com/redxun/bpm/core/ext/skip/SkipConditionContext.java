package com.redxun.bpm.core.ext.skip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author zyg
 */
@Service
public class SkipConditionContext {

    @Autowired
    private  static  final Map<String, ISkipCondition> STRATEGY_MAP = new ConcurrentHashMap<>();


    public SkipConditionContext(Map<String, ISkipCondition> map) {
        SkipConditionContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> SkipConditionContext.STRATEGY_MAP.put(v.getType().getType(), v));
    }


    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static ISkipCondition getConditionByType(String type){
        ISkipCondition condition   =STRATEGY_MAP.get(type);
        return condition;
    }

    /**
     * 消息类型。
     * @return
     */
    public  static List<SkipType> getTypes(){
        List<SkipType> list=new ArrayList<>();
        STRATEGY_MAP.values().forEach(p->{
            list.add(p.getType());
        });
        return list;
    }
}
