package com.redxun.bpm.activiti.event.handler;

import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.eventhandler.EventHandlerExecutor;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * 基础任务执行处理器
 * @param <T>
 */
public abstract class BaseTaskHandler <T extends ActivitiEvent> implements   EventHandler<T> {

    /**
     * 执行任务事件
     * @param taskEntity
     */
    protected void handEvent(TaskEntity taskEntity){
        BpmDefService bpmDefService=SpringUtil.getBean(BpmDefService.class);
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();

        ProcessConfig processConfig= (ProcessConfig) cmd.getTransientVar(BpmConst.PROCESS_CONFIG);
        if(BeanUtil.isEmpty(processConfig)){
            processConfig = bpmDefService.getProcessConfig(taskEntity.getProcessDefinitionId());
        }

        UserTaskConfig    userTaskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(taskEntity.getProcessDefinitionId(),taskEntity.getTaskDefinitionKey());

        EventHandlerExecutor handlerExecutor= SpringUtil.getBean(EventHandlerExecutor.class);

        /**
         * 当事件为完成时，如果时会签节点，那么这里执行环节完成事件。
         */
        EventType eventType=getEventType();
        boolean isNormal= BpmConst.NODE_NORMAL.equals( userTaskConfig.getMultipleType());
        if(!isNormal  && EventType.TASK_COMPLETED.equals(eventType)){
            eventType=EventType.MULTI_COMPLETED;
        }

        //执行任务事件。
        handlerExecutor.executeTask(taskEntity,userTaskConfig,eventType);
        //执行全局的事件
        handlerExecutor.executeGlobalTask(taskEntity,processConfig,getEventType());
    }

}
