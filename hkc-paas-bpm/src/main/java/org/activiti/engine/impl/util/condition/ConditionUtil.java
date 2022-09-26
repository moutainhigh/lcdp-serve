package org.activiti.engine.impl.util.condition;

import com.redxun.bpm.activiti.config.GatewayConfig;
import com.redxun.bpm.activiti.config.NodeConfig;
import com.redxun.bpm.activiti.config.NodeOutcomeConfig;
import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.SpringUtil;
import com.redxun.util.SysUtil;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.delegate.DelegateExecution;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**

 */
public class ConditionUtil {

  public static boolean hasTrueCondition(SequenceFlow sequenceFlow, DelegateExecution execution) {

    BpmDefService bpmDefService= SpringUtil.getBean(BpmDefService.class);
    ProcessScriptEngine processScriptEngine=SpringUtil.getBean(ProcessScriptEngine.class);

    String actDefId= execution.getProcessDefinitionId();
    String nodeId=execution.getCurrentActivityId();
    NodeConfig config=  bpmDefService.getNodeConfig(actDefId,nodeId);
    //网关节点才判断条件。
    if(!(config instanceof GatewayConfig)){
      return true;
    }
    GatewayConfig gatewayConfig=(GatewayConfig)config;
    List<NodeOutcomeConfig> outs = gatewayConfig.getOuts();

    Map<String, String> nodeMap = outs.stream().collect(Collectors.toMap(p->p.getTargetNodeId(), p -> p.getCondition()));
    Map<String,Object> contextData= ActivitiUtil.getContextData(execution);

    String targetNodeId=sequenceFlow.getTargetFlowElement().getId();

    if(nodeMap.containsKey(targetNodeId)){
      String script=nodeMap.get(targetNodeId);
      if(StringUtils.isEmpty(script)){
        return true;
      }

      Object boolVal= processScriptEngine.exeScript(script,contextData);
      if (boolVal instanceof Boolean) {
        if (((Boolean) boolVal)) {// 符合条件
          return (boolean)boolVal;
        }
      }
      return false;
    }
    return  true;

  }



}
