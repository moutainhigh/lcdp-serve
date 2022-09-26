package com.redxun.bpm.activiti.ext;

import lombok.Getter;
import lombok.Setter;

/**
 * Activiti节点
 */
@Setter
@Getter
public class ActNode {
    /**
     * 节点Id
     */
    private String nodeId="";
    /**
     * 节点名称
     */
    private String name="";
    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * 节点序号
     */
    private int orderNo=1;

}
