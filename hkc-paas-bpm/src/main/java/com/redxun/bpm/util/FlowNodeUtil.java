package com.redxun.bpm.util;

import com.redxun.common.tool.StringUtils;
import org.activiti.bpmn.model.FlowNode;

/**
 * 流程节点的工具类
 */
public class FlowNodeUtil {

    /**
     * 返回流程节点的名称
     * @param flowNode
     * @return
     */
    public static String getFlowNodeName(FlowNode flowNode){
        if(flowNode==null){
            return "";
        }

        if(StringUtils.isNotEmpty(flowNode.getName())){
           return flowNode.getName();
        }

        int index=flowNode.getClass().getName().lastIndexOf(".");

        String name= flowNode.getClass().getName().substring(index+1);

        return  name;
    }

    /**
     * 返回流程节点的类型
     * @param flowNode
     * @return
     */
    public static String getFlowNodeType(FlowNode flowNode){
        int index=flowNode.getClass().getName().lastIndexOf(".");
        String name= flowNode.getClass().getName().substring(index);
        return name;
    }
}
