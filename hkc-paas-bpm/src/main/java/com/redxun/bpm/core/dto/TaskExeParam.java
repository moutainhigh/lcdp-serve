package com.redxun.bpm.core.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 任务用户对象查询参数
 */
@Data
public class TaskExeParam {
    /**
     * 任务Id
     */
    private String taskId;
    /**
     * 审批动作
     */
    private String checkType;
    /**
     * 表单对象
     */
    private JSONObject formData=new JSONObject();
}
