package com.redxun.bpm.activiti.event.handler;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.event.TaskCreateApplicationEvent;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.mapper.BpmCheckHistoryMapper;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.BpmTaskService;
import com.redxun.bpm.core.service.TaskExecutorService;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.springframework.context.MessageSource;

import java.util.*;

/**
 * 任务创建事件监听处理器
 */
@Slf4j
public class TaskCreatedEventHandler extends BaseTaskHandler<ActivitiEntityEvent> {

    @Override
    public EventType getEventType() {
        return EventType.TASK_CREATED;
    }

    @Override
    public void handle(ActivitiEntityEvent eventEntity) {

        BpmDefService bpmDefService=SpringUtil.getBean(BpmDefService.class);
        IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();
        TaskEntity taskEntity=(TaskEntityImpl) eventEntity.getEntity();

        ProcessConfig processConfig= (ProcessConfig) cmd.getTransientVar(BpmConst.PROCESS_CONFIG);
        if(BeanUtil.isEmpty(processConfig)){
            processConfig = bpmDefService.getProcessConfig(eventEntity.getProcessDefinitionId());
        }
        ExecutionEntity execution = taskEntity.getExecution();

        UserTaskConfig userTaskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(execution.getProcessDefinitionId(),execution.getCurrentActivityId());

        //设置任务执行人。
        setAsignee(taskEntity,userTaskConfig);

        //执行事件
        handEvent(taskEntity);

        //发布事件
        TaskCreateApplicationEvent ev = new TaskCreateApplicationEvent(taskEntity, userTaskConfig,processConfig);
        SpringUtil.publishEvent(ev);
    }

    /**
     * 设置任务节点的执行人员
     * @param taskEntity
     * @param config
     */
    void setAsignee(TaskEntity  taskEntity,UserTaskConfig config){

        IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();

        Boolean isMulti= (Boolean) cmd.getTransientVar(BpmConst.MULTI_SUB_PROCESS);
        if(isMulti!=null && isMulti ){
            setSubProcessMulti(taskEntity);
        }
        else{
            setNormalUsers(taskEntity,config);
        }
    }

    /**
     * 处理多实例子流程中的任务执行人员分配处理
     * @param taskEntity
     */
    private void setSubProcessMulti(TaskEntity taskEntity){
        Integer  loopCounter= (Integer) taskEntity.getVariable("loopCounter");
        String varName=BpmConst.SIGN_EXECUTOR_IDS +taskEntity.getTaskDefinitionKey();
        String jsonStr= (String) taskEntity.getVariable(varName);
        List<TaskExecutor> taskExecutors = JSONArray.parseArray(jsonStr, TaskExecutor.class);
        TaskExecutor taskExecutor=taskExecutors.get(loopCounter);

        BpmTaskService bpmTaskService= SpringUtil.getBean(BpmTaskService.class);
        bpmTaskService.createTasksAndAssign(taskEntity,taskExecutor);
    }

    /**
     * 设置普通任务执行人员
     * @param taskEntity
     * @param config
     */
    void setNormalUsers(TaskEntity  taskEntity,UserTaskConfig config){
        String nodeId=taskEntity.getTaskDefinitionKey();
        BpmTaskService bpmTaskService= SpringUtil.getBean(BpmTaskService.class);
        TaskExecutorService taskExecutorService=SpringUtil.getBean(TaskExecutorService.class);

        IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();
        //节点执行人
        Set taskExecutors=null;
        Map<String,Object> vars = taskEntity.getVariables();
        //判断是否优先从配置中取
        if(config.getSwitchOptions().contains("precedenceConfig")){
            //实现任务的人员动态计算
            ExecutionEntity execution = taskEntity.getExecution();
            vars.put("execution",execution);
            taskExecutors = taskExecutorService.getTaskExecutors(config.getUserConfigs(), vars);
        }else {
            //1. 优先从上下文传入的变量获取
            taskExecutors = (Set) cmd.getNodeExecutors().get(nodeId);
        }

        if(BeanUtil.isEmpty(taskExecutors)){
            taskExecutors= cmd.getExcutors();
        }

        //2. 从干预的节点人员中获取
        if(BeanUtil.isEmpty(taskExecutors)){
            taskExecutors = new LinkedHashSet();
            Set<TaskExecutor> nodeVarTaskExecutors= taskExecutorService.getExecutorFromVars(nodeId,vars);
            taskExecutors.addAll(nodeVarTaskExecutors);
        }

        //3. 如果当前请求为驳回的情况，那么从审批历史中获取人。
        if(TaskOptionType.BACK.name().equals(cmd.getCheckType())){
            //非多实例
            if(TaskInstanceType.normal.name().equals( config.getMultipleType())){
                taskExecutors=getBackExecutors(cmd,taskEntity);
            }
        }

        //3. 若为空，则从配置中获取
        if (BeanUtil.isEmpty(taskExecutors)) {
            //实现任务的人员动态计算
            ExecutionEntity execution = taskEntity.getExecution();
            vars.put("execution",execution);
            taskExecutors = taskExecutorService.getTaskExecutors(config.getUserConfigs(), vars);
        }

        /**
         * 执行人允许为空。
         */
        if(BeanUtil.isEmpty(taskExecutors)){
            if(!config.getSwitchOptions().contains("allowExecutorNull")){
                MessageUtil.triggerException("任务节点（"+taskEntity.getName()+"）找不到配置的执行人!","审批人信息为空请先配置执行人!");
            }
        }



        //动态创建任务并授权人员，包括多实例
        bpmTaskService.createTasksAndAssign(taskEntity, taskExecutors, config.getMultipleType());
    }

    /**
     * 获取甲硝唑退节点的执行人员
     * @param cmd
     * @param taskEntity
     * @return
     */
    private Set<TaskExecutor> getBackExecutors(IExecutionCmd cmd,TaskEntity taskEntity ){
        BpmInst bpmInst= (BpmInst) cmd.getTransientVar(BpmConst.BPM_INST);
        BpmCheckHistoryMapper checkHistoryMapper=SpringUtil.getBean(BpmCheckHistoryMapper.class);
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("INST_ID_",bpmInst.getInstId());
        wrapper.eq("NODE_ID_",taskEntity.getTaskDefinitionKey());

        wrapper.orderByDesc("COMPLETE_TIME_");

        List<BpmCheckHistory> list = checkHistoryMapper.selectList(wrapper);
        if(BeanUtil.isEmpty(list)){
            return Collections.EMPTY_SET;
        }
        BpmCheckHistory checkHistory=list.get(0);
        String userId= checkHistory.getHandlerId();
        IOrgService orgService=SpringUtil.getBean(IOrgService.class);
        OsUserDto userDto= orgService.getUserById(userId);

        TaskExecutor executor=TaskExecutor.getUser(userDto);

        Set<TaskExecutor> executorSet=new LinkedHashSet<>();
        executorSet.add(executor);

        return executorSet;


    }
}
