package com.redxun.bpm.activiti.config;

import java.io.Serializable;

/**
 * 节点配置接口
 */
public interface NodeConfig extends Serializable {
    /**
     * 获取节点类型
     * @return
     */
    String getType();

}
