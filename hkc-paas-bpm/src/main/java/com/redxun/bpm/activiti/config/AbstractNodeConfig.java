package com.redxun.bpm.activiti.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 节点抽象属性配置
 */
@Getter
@Setter
public abstract class AbstractNodeConfig implements NodeConfig{
    /**
     * 节点Id
     */
    private String key;
    /**
     * 节点类型
     */
    private String nodeType;
    /**
     * 节点名
     */
    private String name;

    /**
     * 序号。
     */
    private int orderNo=1;



}
