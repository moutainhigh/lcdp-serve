package com.redxun.bpm.activiti.event.handler;

import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.eventhandler.EventHandlerExecutor;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**
 * 流程级的事件处理器基础类，实现一些基础的事件的方法
 * @param <T>
 */
public abstract class BaseProcessHandler<T extends ActivitiEvent> implements   EventHandler<T>{

    /**
     * 处理事件。
     * @param entity
     */
    protected void handEvent(ExecutionEntity entity){
        BpmDefService bpmDefService=SpringUtil.getBean(BpmDefService.class);
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();

        //获取流程级配置
        ProcessConfig processConfig= (ProcessConfig) cmd.getTransientVar(BpmConst.PROCESS_CONFIG);
        if(BeanUtil.isEmpty(processConfig)){
            processConfig = bpmDefService.getProcessConfig(entity.getProcessDefinitionId());
        }
        EventHandlerExecutor handlerExecutor= SpringUtil.getBean(EventHandlerExecutor.class);
        //处理配置的事件
        handlerExecutor.exeExecutionEvent(entity,processConfig.getEvents(),getEventType());
    }



}
