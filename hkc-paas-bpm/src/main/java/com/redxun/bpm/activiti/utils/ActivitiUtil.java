package com.redxun.bpm.activiti.utils;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.dto.bpm.BpmConst;
import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.FlowElementsContainer;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Activiti工具类
 *
 */
public class ActivitiUtil {


    /**
     * 返回上下文数据。
     * 上下文数据有：
     * json：表单数据
     * vars：流程变量
     * cmd ：cmd对象。
     * @param execution
     * @return
     */
    public static Map<String,Object> getContextData(DelegateExecution execution){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();

        Map<String,Object> model=new HashMap<String,Object>();
        Map<String,Object> vars=execution.getVariables();
        if(cmd!=null){
            JSONObject json=cmd.getBoDataMap();
            if(json!=null){
                Set<Map.Entry<String, Object>> ents = json.entrySet();
                for(Map.Entry<String, Object> ent : ents){
                    model.put(ent.getKey(),ent.getValue());
                }
            }
            model.put("cmd", cmd);
        }
        model.put("vars", vars);
        return model;
    }

    /**
     * 获取上下数据。
     * @param vars
     * @return
     */
    public static Map<String,Object> getConextData(Map<String,Object> vars){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();

        Map<String,Object> model=new HashMap<String,Object>();
        model.put("vars", vars);
        if(cmd==null){
            return model;
        }

        JSONObject json=cmd.getBoDataMap();
        if(json!=null){
            Set<Map.Entry<String, Object>> ents = json.entrySet();
            for(Map.Entry<String, Object> ent : ents){
                model.put(ent.getKey(),ent.getValue());
            }
        }
        model.put("cmd", cmd);


        return model;
    }

    /**
     * 获取节点子流程类型。
     * @param execution
     * @return
     */
    public static String getSubProcessType(DelegateExecution execution){
        FlowElementsContainer parentContainer = execution.getCurrentFlowElement().getParentContainer();
        if(parentContainer instanceof SubProcess){
            Object behavior = ((SubProcess) parentContainer).getBehavior();
            if(behavior instanceof MultiInstanceActivityBehavior){
                if(behavior instanceof ParallelMultiInstanceBehavior){
                    return BpmConst.NODE_PARALLEL;
                }
                return BpmConst.NODE_SEQUENTIAL;
            }
            return BpmConst.NODE_NORMAL;
        }
        return "";
    }

    /**
     * 获取节点多实例类型。
     * @param execution
     * @return
     */
    public static String getNodeType(DelegateExecution execution){
        Activity currentFlowElement = (Activity) execution.getCurrentFlowElement();

        Object behavior = currentFlowElement .getBehavior();
        if(behavior instanceof MultiInstanceActivityBehavior){
            if(behavior instanceof ParallelMultiInstanceBehavior){
                return BpmConst.NODE_PARALLEL;
            }
            return BpmConst.NODE_SEQUENTIAL;
        }
        return BpmConst.NODE_NORMAL;

    }


}
