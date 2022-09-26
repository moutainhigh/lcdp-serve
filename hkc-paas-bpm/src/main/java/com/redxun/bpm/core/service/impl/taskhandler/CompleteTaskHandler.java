package com.redxun.bpm.core.service.impl.taskhandler;

import com.redxun.bpm.activiti.ActInstService;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.event.TaskCompleteApplicationEvent;
import com.redxun.bpm.activiti.eventhandler.EventHandlerExecutor;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.BpmMultiTaskService;
import com.redxun.bpm.core.service.BpmTaskService;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import org.activiti.engine.TaskService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 完成任务处理器
 */
@Component
public class CompleteTaskHandler extends BaseTaskHandler {

    @Resource
    TaskService taskService;
    @Resource
    ActInstService actInstService;

    @Resource
    BpmMultiTaskService bpmMultiTaskService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    BpmDefService bpmDefService;

    @Resource
    EventHandlerExecutor eventHandlerExecutor;

    @Override
    public boolean canHandler( String checkType) {
        if(TaskOptionType.AGREE.name().equals(checkType)
                || TaskOptionType.REFUSE.name().equals(checkType)
                || TaskOptionType.SKIP.name().equals(checkType)
                ||  TaskOptionType.ABSTAIN.name().equals(checkType)
                || TaskOptionType.OVERTIME_AUTO_AGREE.name().equals(checkType) ){
            return  true;
        }
        return false;
    }

    @Override
    public void handTask(BpmTask bpmTask, IExecutionCmd cmd, UserTaskConfig taskConfig) {
        ProcessNextCmd nextCmd=(ProcessNextCmd)cmd;
        String taskType=bpmTask.getTaskType();
        BpmInst bpmInst=bpmInstService.get(bpmTask.getInstId());
        //如果实例是锁定状态，改成运行状态。
        if(BpmInstStatus.LOCKED.name().equals( bpmInst.getStatus())){
            bpmInst.setStatus(BpmInstStatus.RUNNING.name());
            bpmInstService.update(bpmInst);
        }
        //任务状态为驳回状态。
        if(BpmTask.TYPE_REJECT_TASK.equals(taskType)){
            //找到之前的哪个任务。
            BpmTask preTask=bpmTaskService.get(bpmTask.getPreTaskId());
            //执行全局事件
            ProcessConfig processConfig = bpmDefService.getProcessConfig(preTask.getActDefId());

            //执行事件(执行任务开始事件)
            executeEvent(processConfig,preTask,EventType.TASK_CREATED);

            //更新之前的任务状态。
            bpmTaskService.updTaskStatusByInstId(bpmTask.getPreTaskId(),BpmTask.STATUS_HANDLE);

            //执行完成事件
            executeEvent(processConfig,bpmTask,EventType.TASK_COMPLETED);

            //删除当前处理任务
            bpmTaskService.delByTaskId(bpmTask.getTaskId());

        }
        else{
            if(BpmTask.TYPE_PARALLEL_TASK.equals(bpmTask.getTaskType())
                    || BpmTask.TYPE_SEQUENTIAL_TASK.equals(bpmTask.getTaskType())){
                bpmMultiTaskService.handMultiTask(bpmTask,taskConfig, (ProcessNextCmd) cmd);
            }
            else if(BpmTask.TYPE_TRANSFER_TASK.equals(bpmTask.getTaskType())){
                bpmMultiTaskService.handTransTask(bpmTask,(ProcessNextCmd)cmd);
            }
            else if(StringUtils.isNotEmpty( nextCmd.getDestNodeId())) {
                actInstService.doJumpToTargetNode(bpmTask.getActTaskId(),nextCmd.getDestNodeId());
            }
            else{
                taskService.setVariables(bpmTask.getActTaskId(),cmd.getVars());
                taskService.complete(bpmTask.getActTaskId());
            }
            bpmRuPathService.updRuPath(bpmTask);
        }

    }

    private void executeEvent(ProcessConfig processConfig,BpmTask bpmTask, EventType eventType){
        UserTaskConfig completeTaskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        eventHandlerExecutor.executeTask(bpmTask,completeTaskConfig, eventType);
        //执行全局事件
        eventHandlerExecutor.executeGlobalTask( bpmTask, processConfig, eventType );
        TaskCompleteApplicationEvent ev = new TaskCompleteApplicationEvent(bpmTask, completeTaskConfig,processConfig);
        SpringUtil.publishEvent(ev);
    }



}
