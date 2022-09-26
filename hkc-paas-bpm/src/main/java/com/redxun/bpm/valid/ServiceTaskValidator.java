package com.redxun.bpm.valid;

import java.util.List;

import com.redxun.common.tool.StringUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.validation.ValidationError;
import org.activiti.validation.validator.ProcessLevelValidator;

public class ServiceTaskValidator extends ProcessLevelValidator {
    public ServiceTaskValidator() {
    }

    @Override
    protected void executeValidation(BpmnModel bpmnModel, Process process, List<ValidationError> errors) {
        List<ServiceTask> serviceTasks = process.findFlowElementsOfType(ServiceTask.class);
        for(ServiceTask serviceTask:serviceTasks){
            String name = StringUtils.isEmpty(serviceTask.getName())?serviceTask.getId():serviceTask.getName();
            if(StringUtils.isEmpty(serviceTask.getName())){
                this.addError(errors, "服务节点错误", process, serviceTask, "服务节点["+name+"]没有名称");
            }
            if(serviceTask.getIncomingFlows().isEmpty()) {
                this.addError(errors, "服务节点错误", process, serviceTask, "服务节点["+name+"]没有画输入线");
            }
            if(serviceTask.getOutgoingFlows().isEmpty()){
                this.addError(errors, "服务节点错误", process, serviceTask, "服务节点["+name+"]没有画输出线");
            }
        }
    }
}

