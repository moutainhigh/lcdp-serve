package com.redxun.bpm.activiti.cmd;

import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import java.util.List;

/**
 * 实现流程的自由跳转
 */
public class SetFLowNodeAndGoCmd implements Command<Void> {
    //跳转的流程节点
    private FlowNode flowElement;
    //流程实例的excecutionId
    private String executionId;

    public SetFLowNodeAndGoCmd(FlowNode flowElement,String executionId){
        this.flowElement = flowElement;
        this.executionId = executionId;
    }

    @Override
    public Void execute(CommandContext commandContext) {
        //获取目标节点的来源连线
        List<SequenceFlow> flows = flowElement.getIncomingFlows();
        if(flows==null || flows.size()<1){
            throw new ActivitiException("回退错误，目标节点没有来源连线");
        }
        //随便选一条连线来执行，时当前执行计划为，从连线流转到目标节点，实现跳转
        ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findById(executionId);
        executionEntity.setCurrentFlowElement(flows.get(0));
        commandContext.getAgenda().planTakeOutgoingSequenceFlowsOperation(executionEntity, true);
        return null;
    }
}
