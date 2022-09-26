package com.redxun.bpm.core.service.impl;

import com.redxun.bpm.core.service.ITaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TaskHandlerContext {

    @Autowired
    private  static   final List< ITaskHandler> STRATEGY_LIST = new ArrayList<>();

    public TaskHandlerContext(Map<String, ITaskHandler> map) {
        TaskHandlerContext.STRATEGY_LIST.clear();
        map.forEach((k, v)-> {
            TaskHandlerContext.STRATEGY_LIST.add(v);
        });
    }

    /**
     * 获取处理器。
     * @param checkType
     * @return
     */
    public  static ITaskHandler getJumpType( String checkType){
        for(ITaskHandler taskHandler: TaskHandlerContext.STRATEGY_LIST){
            boolean rtn=taskHandler.canHandler(checkType);
            if(rtn){
                return taskHandler;
            }
        }
        return null;
    }
}
