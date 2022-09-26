package com.redxun.bpm.activiti.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 流程任务人员计算执行者Holder类
 */
@Service
public class ExecutorCalcContext {

    @Autowired
    private  static  final Map<String, ITaskExecutorCalc> STRATEGY_MAP = new ConcurrentHashMap<>();


    public ExecutorCalcContext(Map<String, ITaskExecutorCalc> map) {
        ExecutorCalcContext.STRATEGY_MAP.clear();
        map.forEach((k, v)-> ExecutorCalcContext.STRATEGY_MAP.put(v.getType().getType(), v));
    }


    /**
     * 获取处理器。
     * @param type
     * @return
     */
    public  static ITaskExecutorCalc getExecutorCalcByType(String type){
        ITaskExecutorCalc calcService   =STRATEGY_MAP.get(type);
        return calcService;
    }

    /**
     * 消息类型。
     * @return
     */
    public  static List<ExecutorType> getTypes(){
        List<ExecutorType> list=new ArrayList<>();
        STRATEGY_MAP.values().forEach(p->{
            list.add(p.getType());
        });
        list.sort(new Comparator<ExecutorType>() {
            @Override
            public int compare(ExecutorType o1, ExecutorType o2) {
                return o1.getOrder()-o2.getOrder();
            }
        });
        return list;
    }
}
