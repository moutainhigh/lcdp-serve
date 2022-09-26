package com.redxun.bpm.core.service.impl.taskhandler;

import com.redxun.bpm.activiti.ActInstService;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.impl.RejectSourceService;
import com.redxun.common.utils.MessageUtil;
import org.activiti.bpmn.model.FlowNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 任务干预跳转处理器
 * @author csx
 *
 */
@Component
public class InterposeTaskHandler extends BaseTaskHandler{

    @Autowired
    ActRepService actRepService;
    @Resource
    ActInstService actInstService;

    @Autowired
    RejectSourceService rejectSourceService;

    @Override
    public boolean canHandler( String checkType) {
        if(TaskOptionType.INTERPOSE.name().equals(checkType)){
            return true;
        }
        return false;
    }

    @Override
    public void handTask(BpmTask bpmTask, IExecutionCmd cmd, UserTaskConfig taskConfig) {
        String actDefId = bpmTask.getActDefId();
        ProcessNextCmd nextCmd = (ProcessNextCmd) cmd;
        /**
         * 获取该节点
         */
        FlowNode flowNode = actRepService.getFlowNode(actDefId, nextCmd.getDestNodeId());

        if (flowNode == null || flowNode.getId().equals(bpmTask.getKey())) {
            MessageUtil.triggerException("跳至的目标节点不存在！");
            return;
        }

        String jumpNodeId = flowNode.getId();

        /**
         * 通过流程节点实现节点间跳转，只支持没有子流程与并行外的跳转
         */
        actInstService.doJumpToTargetNode(bpmTask.getActTaskId(),jumpNodeId);

    }

}
