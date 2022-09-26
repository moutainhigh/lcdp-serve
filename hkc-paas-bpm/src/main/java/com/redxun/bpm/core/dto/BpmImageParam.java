package com.redxun.bpm.core.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 *  任务所需传入参数
 */
@Data
public class BpmImageParam {
    /**
     * 任务Id
     */
    private String taskId;
    /**
     * 流程实例Id
     */
    private String instId;
    /**
     * 流程定义Id
     */
    private String defId;
    /**
     * 节点Id
     */
    private String nodeId;
    /**
     * 表单对象
     */
    private JSONObject formData=new JSONObject();
}
