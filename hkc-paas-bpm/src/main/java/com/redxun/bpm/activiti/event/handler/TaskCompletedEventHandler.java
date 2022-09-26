package com.redxun.bpm.activiti.event.handler;

import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.event.TaskCompleteApplicationEvent;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.BpmTaskService;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

/**
 * 流程任务完成事件
 */
public class TaskCompletedEventHandler extends BaseTaskHandler<ActivitiEntityEvent> {


    @Override
    public EventType getEventType() {
        return EventType.TASK_COMPLETED;
    }

    @Override
    public void handle(ActivitiEntityEvent eventEntity) {
        BpmDefService bpmDefService=SpringUtil.getBean(BpmDefService.class);
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();

        TaskEntityImpl taskEntity = (TaskEntityImpl) eventEntity.getEntity();
        //获取流程全局配置
        ProcessConfig processConfig= (ProcessConfig) cmd.getTransientVar(BpmConst.PROCESS_CONFIG);
        if(BeanUtil.isEmpty(processConfig)){
            processConfig = bpmDefService.getProcessConfig(eventEntity.getProcessDefinitionId());
        }
        //获取任务节点配置
        UserTaskConfig    userTaskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(taskEntity.getProcessDefinitionId(),taskEntity.getTaskDefinitionKey());

        BpmTask bpmTask= (BpmTask) cmd.getTransientVar(BpmConst.BPM_APPROVE_TASK);
        //处理任务完成。
        handTaskComplete(bpmTask,cmd);
        //处理事件
        handEvent(taskEntity);

        TaskCompleteApplicationEvent ev = new TaskCompleteApplicationEvent(taskEntity, userTaskConfig,processConfig);
        SpringUtil.publishEvent(ev);
    }

    /**
     * 删除人员，记录审批意见。
     * @param bpmTask
     * @param cmd
     */
    private void  handTaskComplete(BpmTask bpmTask,IExecutionCmd cmd){
        BpmTaskService bpmTaskService = SpringUtil.getBean(BpmTaskService.class);

        //删除根据ACT_TASK_派生的任务,这种情况会删除会签任务。
        bpmTaskService.delByActTaskId(bpmTask.getActTaskId());

    }
}
