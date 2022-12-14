package com.redxun.bpm.activiti.cmd;

import com.redxun.bpm.core.service.BpmTaskService;
import com.redxun.common.utils.SpringUtil;
import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManagerImpl;

/**
 * 删除任务执行
 */
public class DeleteTaskCmd extends NeedsActiveTaskCmd<String> {

    public DeleteTaskCmd(String taskId){
        super(taskId);
    }
    @Override
    public String execute(CommandContext commandContext, TaskEntity currentTask){
        //获取所需服务
        TaskEntityManagerImpl taskEntityManager = (TaskEntityManagerImpl)commandContext.getTaskEntityManager();
        //获取当前任务的来源任务及来源节点信息
        ExecutionEntity executionEntity = currentTask.getExecution();
        //删除当前任务,来源任务
        taskEntityManager.deleteTask(currentTask, "jumpReason", false, false);
        //同时需要删除原来的旧的任务
        BpmTaskService bpmTaskService=SpringUtil.getBean(BpmTaskService.class);
        bpmTaskService.delByActTaskId(currentTask.getId());
        return executionEntity.getId();
    }


}
