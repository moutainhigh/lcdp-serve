package com.redxun.bpm.activiti.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程或节点的事件配置
 */
@Data
public class EventConfig implements Serializable {
    /**
     * 事件配置调用的动作名称
     */
    private String action;
    /**
     * 处理器类型。
     */
    private String handerType="";
    /**
     * 是异步？
     */
    private String async;
    /**
     * 执行的配置
     */
    private JSONObject config;

    /**
     * 配置描述。
     */
    private String description="";

    /**
     * 节点的配置。
     *  这个在全局节点设置有效。
     */
    private List<String> nodeConfig=new ArrayList<>();

}
