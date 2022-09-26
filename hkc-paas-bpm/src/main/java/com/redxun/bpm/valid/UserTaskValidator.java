package com.redxun.bpm.valid;

import java.util.List;

import com.redxun.common.tool.StringUtils;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.validation.ValidationError;
import org.activiti.validation.validator.ProcessLevelValidator;

public class UserTaskValidator extends ProcessLevelValidator {
    public UserTaskValidator() {
    }

    @Override
    protected void executeValidation(BpmnModel bpmnModel, Process process, List<ValidationError> errors) {
        List<UserTask> userTasks = process.findFlowElementsOfType(UserTask.class);
        for(UserTask userTask:userTasks){
            String name = StringUtils.isEmpty(userTask.getName())?userTask.getId():userTask.getName();
            if(StringUtils.isEmpty(userTask.getName())){
                this.addError(errors, "用户节点错误", process, userTask, "用户节点["+name+"]没有名称");
            }
            if(userTask.getIncomingFlows().isEmpty()){
                this.addError(errors, "用户节点错误", process, userTask, "用户节点["+name+"]没有画输入线");
            }
            if(userTask.getOutgoingFlows().isEmpty()){
                this.addError(errors, "用户节点错误", process, userTask, "用户节点["+name+"]没有画输出线");
            }
        }
    }
}
