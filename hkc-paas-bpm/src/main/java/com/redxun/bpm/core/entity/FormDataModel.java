package com.redxun.bpm.core.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 用于前端获取表单数据
 */
@Data
public class FormDataModel {
    private String taskId;
    private JSONObject formData;
}
