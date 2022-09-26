package com.redxun.bpm.activiti.event.handler;

import com.redxun.bpm.activiti.config.NodeConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.BpmInstServiceImpl;
import com.redxun.bpm.core.service.BpmRuPathServiceImpl;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import org.activiti.engine.delegate.event.ActivitiActivityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Activity活动开始事件处理器
 * @author csx
 */
public class ActivityStartedEventHandler implements EventHandler<ActivitiActivityEvent> {

    Logger logger= LoggerFactory.getLogger(ActivityStartedEventHandler.class);
    @Override
    public EventType getEventType() {
        return EventType.ACTIVITY_STARTED;
    }

    @Override
    public void handle(ActivitiActivityEvent eventEntity) {
        logger.info("==enter ActivityStartedEventHandler handler==" + eventEntity.toString());
        createRuPath(eventEntity);

        //表示为子流程多实例。
        if(eventEntity.getBehaviorClass().indexOf("MultiInstanceBehavior")!=-1 && "subProcess".equals( eventEntity.getActivityType())){
            IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();
            cmd.getTransientVars().put(BpmConst.MULTI_SUB_PROCESS,true);
        }



    }

    /**
     * 创建执行路径
     * @param eventEntity
     */
    public void createRuPath(ActivitiActivityEvent eventEntity){
        IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();

        String checkType=cmd.getCheckType();

        if(TaskOptionType.BACK.name().equals(checkType) ||
                TaskOptionType.BACK_TO_STARTOR.name().equals(checkType) ||
                TaskOptionType.BACK.name().equals(checkType) ){
            return;
        }

        BpmDefService bpmDefService= SpringUtil.getBean(BpmDefService.class);
        BpmRuPathServiceImpl bpmRuPathService=SpringUtil.getBean(BpmRuPathServiceImpl.class);

        BpmInstServiceImpl bpmInstService = SpringUtil.getBean(BpmInstServiceImpl.class);
        BpmInst bpmInst = bpmInstService.getByActInstId(eventEntity.getProcessInstanceId());
        String defId=cmd.getDefId();
        String instId=cmd.getInstId();
        if(BeanUtil.isNotEmpty(bpmInst)){
            defId=bpmInst.getDefId();
            instId=bpmInst.getInstId();
        }

        String activityId=eventEntity.getActivityId();
        String nodeType=eventEntity.getActivityType();
        String nodeName=eventEntity.getActivityName();

        //创建执行路径
        BpmRuPath path=new BpmRuPath();
        path.setPathId(IdGenerator.getIdStr());
        path.setActDefId(eventEntity.getProcessDefinitionId());
        path.setActInstId(eventEntity.getProcessInstanceId());
        path.setExecutionId(eventEntity.getExecutionId());
        path.setNodeId(activityId);
        path.setNodeType(nodeType);
        path.setNodeName(nodeName);
        path.setInstId(instId);
        path.setStartTime(new Date());
        path.setDefId(defId);

        if(cmd instanceof ProcessNextCmd){//若为启动时，需要从线程中获得
            path.setNextJumpType(((ProcessNextCmd)cmd).getNextJumpType());
        }

        //获取流程节点的定义
        NodeConfig nodeConfig=bpmDefService.getNodeConfig(eventEntity.getProcessDefinitionId(),eventEntity.getActivityId());
        if(nodeConfig!=null && nodeConfig instanceof UserTaskConfig){
            UserTaskConfig userTaskConfig=(UserTaskConfig)nodeConfig;
            path.setMultipleType(userTaskConfig.getMultipleType());
        }

        String preNodeId=cmd.getPreNodeId();

        //在同一线程中，上一任务完成后，进入下一任务，其上一节点即为本节点的父路径
        BpmRuPath parentPath=null;
        if(cmd!=null && StringUtils.isNotEmpty(preNodeId)){
            //先再当前路径中根据exuction 查找，这个主要时解决 子流程多实例的情况。
            parentPath=bpmRuPathService.getByExeutionAndNode(instId,preNodeId,eventEntity.getExecutionId());
            if(parentPath==null){
                parentPath=bpmRuPathService.getLatestByInstIdNodeId(instId,cmd.getPreNodeId());
            }
        }

        if(parentPath!=null){
            path.setParentId(parentPath.getPathId());
            Integer maxLevel=bpmRuPathService.getMaxLevelByInst(instId) ;
            if(maxLevel==null) {
                maxLevel=1;
            }
            int level=maxLevel+1;
            path.setLevel(level);
        }else{
            path.setLevel(1);
            path.setParentId("0");
        }

        bpmRuPathService.insert(path);
    }
}
