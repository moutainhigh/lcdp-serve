package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 变量配置
 * 数据格式如：
     "uid": "VQIYHIAU8HGH7IWOFWSQ0KD72H8MVECT",
     "label": "a",
     "field": "b",
     "key": "a",
     "datatype": "String",
     "expression": "a",
 */
@Data
public class VariableConfig implements Serializable {
    /**
     * 变量标签值
     */
    private String label;
    /**
     * 绑定单据字段名
     */
    private String field;
    /**
     * 字段key
     */
    private String key;
    /**
     * 数据类型
     */
    private String datatype;
    /**
     * 表达式
     */
    private String expression;
}
