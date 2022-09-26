package com.redxun.bpm.activiti.eventhandler;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.Event;
import com.redxun.bpm.activiti.config.EventConfig;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.mq.BpmInputOutput;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 事件处理执行者
 */
@Component
public class EventHandlerExecutor {

    @Resource
    BpmInputOutput inputOutput;


    /**
     * 执行任务自定义事件。
     * @param bpmTask
     * @param taskConfig
     * @param eventType
     */
    public void executeTask(BpmTask bpmTask, UserTaskConfig taskConfig,EventType eventType ) {
        List<Event> events = taskConfig.getEvents();
        exeTaskEvent(bpmTask,events,eventType);
    }



    /**
     * 执行任务自定义事件。
     * @param taskEntity
     * @param taskConfig
     * @param eventType
     */
    public void executeTask(TaskEntity taskEntity, UserTaskConfig taskConfig,EventType eventType ) {
        List<Event> events = taskConfig.getEvents();
        exeTaskEvent(taskEntity,events,eventType);
    }

    private void exeTaskEvent(TaskEntity taskEntity,List<Event> events, EventType evType){

        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        if(BeanUtil.isEmpty(events)){
            return;
        }
        String eventType=evType.name();
        Event event= getEventByType( eventType,events);
        if(event==null){
            return;
        }

        String checkType=cmd.getCheckType();

        List<EventConfig> eventConfigs = event.getEventConfigs();
        if(BeanUtil.isEmpty(eventConfigs)){
            return;
        }
        for(EventConfig config:eventConfigs){
            String action=config.getAction();
            //配置了action，并且 当前的action 类型和定义的类型不一致就跳过。
            if(StringUtils.isNotEmpty(action) && !action.equals(checkType)){
                continue;
            }
            TaskEventMessage taskEventMessage=getEventMessage(taskEntity,config,evType,cmd);
            IEventHandler handler= EventHandlerContext.getEventHandler(config.getHanderType()) ;
            if(handler==null){
                continue;
            }
            if(MBoolean.TRUE_LOWER.equals(config.getAsync()) ){
                //发送消息到队列
                sendToMq(taskEventMessage);
            }
            else{
                handler.handEvent(taskEventMessage);
            }
        }
    }

    /**
     * 执行任务事件
     * @param bpmTask
     * @param events
     * @param evType
     */
    private void exeTaskEvent(BpmTask bpmTask, List<Event> events, EventType evType){

        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        if(BeanUtil.isEmpty(events)){
            return;
        }
        String eventType=evType.name();
        Event event= getEventByType( eventType,events);
        if(event==null){
            return;
        }

        String checkType=cmd.getCheckType();

        List<EventConfig> eventConfigs = event.getEventConfigs();
        if(BeanUtil.isEmpty(eventConfigs)){
            return;
        }
        for(EventConfig config:eventConfigs){
            String action=config.getAction();
            //配置了action，并且 当前的action 类型和定义的类型不一致就跳过。
            if(StringUtils.isNotEmpty(action) && !action.equals(checkType)){
                continue;
            }
            TaskEventMessage taskEventMessage=getEventMessage(bpmTask,cmd.getVars(),config,evType,cmd);
            IEventHandler handler= EventHandlerContext.getEventHandler(config.getHanderType()) ;
            if(handler==null){
                continue;
            }
            if(MBoolean.TRUE_LOWER.equals(config.getAsync()) ){
                //发送消息到队列
                sendToMq(taskEventMessage);
            }
            else{
                handler.handEvent(taskEventMessage);
            }
        }
    }

    /**
     * 发送事件至MQ中
     * @param eventMessage
     */
    private void sendToMq(BaseEventMessage eventMessage){
        inputOutput.eventOutput().send(MessageBuilder.withPayload(eventMessage).build());
    }

    /**
     * 获取事件配置。
     * @param eventType
     * @param events
     * @return
     */
    private Event getEventByType(String eventType,List<Event> events){
        for(Event event:events) {
            if (JSONObject.parseObject(event.getEventType()).getString("id").equals(eventType)) {
                return  event;
            }
        }
        return null;
    }

    /**
     * 执行全局任务事件。
     * @param taskEntity
     * @param globalConfig
     * @param eventType
     */
    public void executeGlobalTask(TaskEntity taskEntity, ProcessConfig globalConfig, EventType eventType ) {
        List<Event> events = globalConfig.getEvents();
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        if(BeanUtil.isEmpty(events)){
            return;
        }
        String eventTypeName="GLOBAL_"+eventType.name();
        Event event= getEventByType( eventTypeName,events);
        if(event==null){
            return;
        }

        String checkType=cmd.getCheckType();

        List<EventConfig> eventConfigs = event.getEventConfigs();
        if(BeanUtil.isEmpty(eventConfigs)){
            return;
        }
        String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
        boolean execute=false;

        for(EventConfig config:eventConfigs){
            String action=config.getAction();
            //配置了action，并且 当前的action 类型和定义的类型不一致就跳过。
            if(StringUtils.isNotEmpty(action) && !action.equals(checkType)){
                continue;
            }
            //获取节点配置
            List<String> nodeConfig = config.getNodeConfig();
            if(nodeConfig.size()==0){
                continue;
            }
            for (String nodeId : nodeConfig) {
                if(taskDefinitionKey.equals(nodeId)){
                    execute=true;
                    break;
                }
            }
            if(!execute){
                continue;
            }

            TaskEventMessage taskEventMessage=getEventMessage(taskEntity,config,eventType,cmd);
            IEventHandler handler= EventHandlerContext.getEventHandler(config.getHanderType()) ;
            if(handler==null){
                continue;
            }
            if(MBoolean.TRUE_LOWER.equals(config.getAsync()) ){
                //发送消息到队列
                sendToMq(taskEventMessage);
            }
            else{
                handler.handEvent(taskEventMessage);
            }

        }

    }


    /**
     * 执行全局任务事件。
     * @param bpmTask
     * @param globalConfig
     * @param eventType
     */
    public void executeGlobalTask(BpmTask bpmTask,  ProcessConfig globalConfig, EventType eventType ) {
        List<Event> events = globalConfig.getEvents();
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        if(BeanUtil.isEmpty(events)){
            return;
        }
        String eventTypeName="GLOBAL_"+eventType.name();
        Event event= getEventByType( eventTypeName,events);
        if(event==null){
            return;
        }

        String checkType=cmd.getCheckType();

        List<EventConfig> eventConfigs = event.getEventConfigs();
        if(BeanUtil.isEmpty(eventConfigs)){
            return;
        }
        String taskDefinitionKey = bpmTask.getKey();

        for(EventConfig config:eventConfigs){
            String action=config.getAction();
            //配置了action，并且 当前的action 类型和定义的类型不一致就跳过。
            if(StringUtils.isNotEmpty(action) && !action.equals(checkType)){
                continue;
            }
            //获取节点配置
            List<String> nodeConfig = config.getNodeConfig();
            if(BeanUtil.isEmpty(nodeConfig)){
                return;
            }
            boolean execute=false;
            for (String nodeId : nodeConfig) {
                if(taskDefinitionKey.equals(nodeId)){
                    execute=true;
                    break;
                }
            }

            if(!execute){
                continue;
            }

            TaskEventMessage taskEventMessage=getEventMessage(bpmTask,cmd.getVars(),config,eventType,cmd);
            IEventHandler handler= EventHandlerContext.getEventHandler(config.getHanderType()) ;
            if(handler==null){
                continue;
            }
            if(MBoolean.TRUE_LOWER.equals(config.getAsync()) ){
                //发送消息到队列
                sendToMq(taskEventMessage);
            }
            else{
                handler.handEvent(taskEventMessage);
            }
        }
    }


    /**
     * 获取任务的事件消息
     * @param bpmTask
     * @param vars
     * @param eventConfig
     * @param eventType
     * @param cmd
     * @return
     */
    private TaskEventMessage getEventMessage(BpmTask bpmTask, Map<String,Object> vars,
                                             EventConfig eventConfig, EventType eventType, IExecutionCmd cmd){
        TaskEventMessage message=new TaskEventMessage();

        message.setEventConfig(eventConfig);
        message.setBpmTask(bpmTask);
        message.setTaskId(bpmTask.getTaskId());
        message.setVars(vars);
        message.setStart(eventType.equals(EventType.TASK_CREATED));
        message.setActDefId(bpmTask.getActDefId());
        message.setActInstId(bpmTask.getActInstId());
        message.setNodeId(bpmTask.getKey());
        message.setNodeName(bpmTask.getName());
        //审批类型
        message.setAction(cmd.getCheckType());
        message.setFormData(cmd.getBoDataMap());
        message.setExecutionId(bpmTask.getExecutionId());

        return  message;

    }

    /**
     * 获取任务的事件消息
     * @param taskEntity
     * @param eventConfig
     * @param eventType
     * @param cmd
     * @return
     */
    private TaskEventMessage getEventMessage(TaskEntity taskEntity, EventConfig eventConfig, EventType eventType,IExecutionCmd cmd){

        TaskEventMessage message=new TaskEventMessage();

        message.setEventConfig(eventConfig);
        message.setTaskEntity(taskEntity);
        message.setTaskId(taskEntity.getId());
        message.setVars(taskEntity.getVariables());
        message.setStart(eventType.equals(EventType.TASK_CREATED));
        message.setActDefId(taskEntity.getProcessDefinitionId());
        message.setActInstId(taskEntity.getProcessInstanceId());
        message.setNodeId(taskEntity.getTaskDefinitionKey());
        message.setNodeName(taskEntity.getName());
        //审批类型
        message.setAction(cmd.getCheckType());
        message.setFormData(cmd.getBoDataMap());
        message.setExecutionId(taskEntity.getExecutionId());

        return  message;

    }

    /**
     * 获取事件消息
     * @param entity
     * @param eventConfig
     * @param evType
     * @param cmd
     * @return
     */
    private ExecutionEventMessage getEventMessage(ExecutionEntity entity, EventConfig eventConfig, EventType evType, IExecutionCmd cmd){

        ExecutionEventMessage message=new ExecutionEventMessage();

        message.setEventConfig(eventConfig);
        message.setExecution(entity);

        message.setVars(entity.getVariables());
        message.setStart(evType.equals(EventType.PROCESS_STARTED));
        message.setActDefId(entity.getProcessDefinitionId());
        message.setActInstId(entity.getProcessInstanceId());
        message.setNodeId(entity.getCurrentActivityId());
        message.setNodeName(entity.getName());
        //审批类型
        message.setAction(cmd.getCheckType());
        message.setFormData(cmd.getBoDataMap());
        message.setExecutionId(entity.getId());

        return  message;

    }

    /**
     * 执行事件
     * @param entity
     * @param events
     * @param evType
     */
    public void exeExecutionEvent(ExecutionEntity entity,List<Event> events, EventType evType){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        if(BeanUtil.isEmpty(events)){
            return;
        }
        String eventType=evType.name();
        Event event= getEventByType( eventType,events);
        if(event==null){
            return;
        }

        List<EventConfig> eventConfigs = event.getEventConfigs();
        if(BeanUtil.isEmpty(eventConfigs)){
            return;
        }
        for(EventConfig config:eventConfigs){

            String action=config.getAction();
            //配置了action，并且 当前的action 类型和定义的类型不一致就跳过。
            if(StringUtils.isNotEmpty(action) && !action.equals(cmd.getCheckType())){
                continue;
            }

            ExecutionEventMessage eventMessage=getEventMessage(entity,config,evType,cmd);
            IEventHandler handler= EventHandlerContext.getEventHandler(config.getHanderType()) ;
            if(handler==null){
                continue;
            }
            if(MBoolean.TRUE_LOWER.equals( config.getAsync())){
                sendToMq(eventMessage);
            }
            else {
                handler.handEvent(eventMessage);
            }

        }

    }



}
