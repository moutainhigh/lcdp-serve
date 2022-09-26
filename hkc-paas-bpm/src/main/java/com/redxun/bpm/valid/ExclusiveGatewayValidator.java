package com.redxun.bpm.valid;

import java.util.List;

import com.redxun.common.tool.StringUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.validation.ValidationError;
import org.activiti.validation.validator.ProcessLevelValidator;

public class ExclusiveGatewayValidator extends ProcessLevelValidator {
    public ExclusiveGatewayValidator() {
    }

    @Override
    protected void executeValidation(BpmnModel bpmnModel, Process process, List<ValidationError> errors) {
        List<ExclusiveGateway> gateways = process.findFlowElementsOfType(ExclusiveGateway.class);
        for(ExclusiveGateway exclusiveGateway:gateways){
            if(exclusiveGateway.getIncomingFlows().isEmpty()){
                String name = StringUtils.isEmpty(exclusiveGateway.getName())?exclusiveGateway.getId():exclusiveGateway.getName();
                this.addError(errors, "分支节点错误", process, exclusiveGateway, "分支节点["+name+"]没有画输入线");
            }
        }
    }
}

