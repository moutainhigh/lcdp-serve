package com.redxun.bpm.core.service.impl;

import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.service.IRejectHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RejectHandlerContext {

    @Autowired
    private  static  final List<IRejectHandler> STRATEGY_LIST = new ArrayList<>();

    public RejectHandlerContext(Map<String, IRejectHandler> map) {
        RejectHandlerContext.STRATEGY_LIST.clear();
        map.forEach((k, v)-> {
            RejectHandlerContext.STRATEGY_LIST.add(v);
        });
    }

    /**
     * 获取处理器。
     * @param parentPath
     * @return
     */
    public boolean handle(BpmTask bpmTask, BpmRuPath parentPath){
        for(IRejectHandler handler:RejectHandlerContext.STRATEGY_LIST){
            boolean rtn=handler.canHandler(parentPath);
            if(rtn){
                 handler.handSource(bpmTask,parentPath);
                return true;
            }
        }
        return false;
    }
}


