package com.redxun.bpm.activiti.utils;

import com.redxun.bpm.activiti.config.BpmNodeTypeEnums;
import com.redxun.bpm.activiti.config.NodeConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程节点配置工具类
 */
public class BpmNodeConfigUtil {
    /**
     * BPM节点配置
     */
    private static Map<String,Class<? extends NodeConfig>> nodeTypeConfigTypeMap=new HashMap<>();

    static {
        BpmNodeTypeEnums[] nums=BpmNodeTypeEnums.values();
        for(BpmNodeTypeEnums n:nums){
            nodeTypeConfigTypeMap.put(n.getTypeName(),n.getConfigClass());
        }
    }

    /**
     * 通过节点类型获得配置应映的实例类型
     * @param nodeType
     * @return
     */
    public static Class<? extends NodeConfig> getNodeConfigInstClass(String nodeType){
        return nodeTypeConfigTypeMap.get(nodeType);
    }
}
