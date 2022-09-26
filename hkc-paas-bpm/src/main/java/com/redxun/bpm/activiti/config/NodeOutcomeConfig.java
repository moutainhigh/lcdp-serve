package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 跳出节点的配置
 * @author csx
 *              "condition": "amt<=2000",
 * 				"outSeqId": "SequenceFlow_1r679yj",
 * 				"outSeqName": "amt<=2000",
 * 				"targetNodeId": "UserTask_1xbj6e5",
 * 				"targetName": "B"
 */
@Data
public class NodeOutcomeConfig implements Serializable {

    /**
     * 目标节点Id
     */
    private String targetNodeId;
    /**
     * 目标节点名称
     */
    private String targetName;
    /**
     * 跳转的条件
     */
    private String condition;
}
