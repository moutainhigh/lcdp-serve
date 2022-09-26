package com.redxun.bpm.core.ext.FormStatus;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author gjh
 */
@Service
public class FormStatusContext {

    @Autowired
    private  static  final Map<String, IFormStatus> STRATEGY_MAP = new ConcurrentHashMap<>();


    public FormStatusContext(Map<String, IFormStatus> map) {
        FormStatusContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> FormStatusContext.STRATEGY_MAP.put(v.getType().getType(), v));
    }


    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static IFormStatus getConditionByType(String type){
        IFormStatus condition  =STRATEGY_MAP.get(type);
        return condition;
    }

    /**
     * 消息类型。
     * @return
     */
    public  static List<FormStatusType> getTypes(){
        List<FormStatusType> list=new ArrayList<>();
        STRATEGY_MAP.values().forEach(p->{
            list.add(p.getType());
        });
        return list;
    }
}
