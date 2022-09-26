package com.redxun.bpm.activiti.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程与任务节点的处理按钮配置
 *
 * "uid": "2PA2TFP97QU1QJ971JJ29ZHP4U5AZGSJ",
 * 				"id": "",
 * 				"name": "",
 * 				"icon": "",
 * 				"type": "",
 * 				"jsMethod": "",
 * 				"editable": true,
 * 				"closeable": true
 */
@Data
public class ButtonConfig implements Serializable {
    //唯一标识键
    private String uid;
    //ID
    private String id;
    //名称
    private String name;
    //图标
    private String icon;
    //图片样式
    private String style;
    //类型
    private String type;
    //执行JS方法
    private String method;
    //需要配置
    private Boolean needConfig;
    //配置
    private JSONObject config;
    //预先定义
    private Boolean preDef;


}
