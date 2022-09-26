package com.redxun.bpm.activiti.eventhandler;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.EventConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础事件消息
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseEventMessage implements Serializable {
    /**
     * 事件设定
     */
    private EventConfig eventConfig;

    /**
     * 动作。
     */
    private String action="";

    /**
     * 表单数据。
     */
    private JSONObject formData;

    /**
     * 流程变量
     */
    private Map<String,Object> vars=new HashMap<>();

    /**
     * 当前执行ID
     */
    private String executionId="";

    /**
     * 当前实例ID
     */
    protected String actInstId="";

    /**
     * 当前定义ID
     */
    protected String actDefId="";

    /**
     * 流程节点ID
     */
    protected String nodeId="";

    /**
     * 节点ID
     */
    protected String nodeName="";


    /**
     * 是否是启动流程。
     */
    private boolean start=true;

}
