package com.redxun.bpm.activiti.cmd;

import com.redxun.bpm.activiti.ActRepService;
import com.redxun.common.utils.SpringUtil;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import java.util.List;

/**
 * 跳转到指定的节点。
 */
public class JumpToTargetCmd implements Command<Void> {
    //目标节点ID
    private String targetId;
    //流程实例的executionId
    private String executionId;

    public JumpToTargetCmd(String  targetId,String executionId){
        this.targetId = targetId;
        this.executionId = executionId;
    }


    @Override
    public Void execute(CommandContext commandContext) {

        ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findById(executionId);

        ActRepService actRepService= SpringUtil.getBean(ActRepService.class);
        //获取目标节点实现跳转
        FlowNode flowElement =actRepService.getFlowNode(executionEntity.getProcessDefinitionId(),targetId);

        //获取目标节点的来源连线
        List<SequenceFlow> flows = flowElement.getIncomingFlows();
        if(flows==null || flows.size()<1){
            throw new ActivitiException("回退错误，目标节点没有来源连线");
        }
        executionEntity.setCurrentFlowElement(flows.get(0));
        commandContext.getAgenda().planTakeOutgoingSequenceFlowsOperation(executionEntity, true);
        return null;

    }
}
