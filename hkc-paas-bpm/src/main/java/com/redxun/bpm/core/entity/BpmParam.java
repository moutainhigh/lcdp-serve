package com.redxun.bpm.core.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 流程相关参数
 */
@Data
public class BpmParam {
    /**
     * 流程定义Id
     */
    String defId;
    /**
     * 流程实例Id
     */
    String instId;
    /**
     * 流程任务Id
     */
    String taskId;
    /**
     * 显示历史信息
     */
    Boolean showHis=false;
    /**
     * 是否流程预演
     */
    Boolean preview=false;
    /**
     * 表单对象
     */
    private JSONObject formData=new JSONObject();

    /**
     * 设置发起人Id
     */
    String startUserId;
}
