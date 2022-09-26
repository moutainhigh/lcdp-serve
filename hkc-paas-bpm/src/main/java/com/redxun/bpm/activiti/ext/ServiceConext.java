package com.redxun.bpm.activiti.ext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务任务上下文。
 *
 */
@Service
public class ServiceConext  {

    @Autowired
    private  static   final Map<String, IServiceTask> STRATEGY_MAP = new ConcurrentHashMap<>();

    private static final  List<ServiceType> SERVICE_TYPES=new ArrayList<>(6);


    public ServiceConext(Map<String, IServiceTask> map) {
        ServiceConext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> {
            ServiceConext.STRATEGY_MAP.put(v.getType().getType(), v);
            SERVICE_TYPES.add(v.getType());
        });
    }

    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static IServiceTask getTaskHandler(String type){
        IServiceTask attrHandler=STRATEGY_MAP.get(type);
        return attrHandler;
    }

    /**
     * 获取平台中的服务类型。
     * @return
     */
    public static List<ServiceType> getServiceTypes(){
        return SERVICE_TYPES;
    }
}
