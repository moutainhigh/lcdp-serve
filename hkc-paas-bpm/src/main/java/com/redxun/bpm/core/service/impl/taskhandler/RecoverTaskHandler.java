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

/**
 * 任务追回处理
 */
@Component
public class RecoverTaskHandler extends BaseTaskHandler {

    @Autowired
    ActRepService actRepService;

    @Autowired
    RejectSourceService rejectSourceService;

    @Autowired
    ActInstService actInstService;

    @Override
    public boolean canHandler( String checkType) {
        if(TaskOptionType.RECOVER.name().equals(checkType)){
            return  true;
        }
        return false;
    }

    @Override
    public void handTask(BpmTask bpmTask, IExecutionCmd cmd, UserTaskConfig taskConfig) {
        String actDefId=bpmTask.getActDefId();
        ProcessNextCmd nextCmd=(ProcessNextCmd)cmd;
        /**
         * 获取该节点
         */
        FlowNode flowNode= actRepService.getFlowNode(actDefId,nextCmd.getDestNodeId());

        if(flowNode==null || flowNode.getId().equals(bpmTask.getKey())){
            MessageUtil.triggerException ("任务无法追回，该任务节点不允许回到自身节点或该节点不存在！");
            return;
        }

        String backNodeId=flowNode.getId();

        String nextJumpType= ((ProcessNextCmd) cmd).getNextJumpType();
        if(ProcessNextCmd.NEXT_JUMP_OPTION_NORMAL.equals(nextJumpType)){
            actInstService.doJumpToTargetNode(bpmTask.getActTaskId(),backNodeId);
        }
        else{
            //原路返回处理
            BpmRuPath ruPath= bpmRuPathService.getLatestByInstIdNodeId(bpmTask.getInstId(),backNodeId);
            rejectSourceService.handSource(bpmTask,ruPath);
        }


    }
}
