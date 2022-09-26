package com.redxun.bpm.activiti.ext;

import com.redxun.bpm.activiti.config.ServiceTaskConfig;
import com.redxun.bpm.core.service.BpmDefService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * activiti的Service节点的调用执行
 *
 */
@Component(value = "rxServiceTask")
public class RxServiceTask  implements JavaDelegate {

    @Resource
    BpmDefService bpmDefService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String actDefId=delegateExecution.getProcessDefinitionId();
        String nodeId=delegateExecution.getCurrentActivityId();
        ServiceTaskConfig config=(ServiceTaskConfig) bpmDefService.getNodeConfig(actDefId,nodeId);
        if(config==null){
            return;
        }
        IServiceTask serviceTask= ServiceConext.getTaskHandler(config.getTaskType());
        serviceTask.handle(delegateExecution,config.getSetting());
    }
}
