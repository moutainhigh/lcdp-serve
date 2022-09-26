package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * Bo业务字段参数配置
 */
@Data
public class FieldSetting  implements Serializable {
    /**
     * 值类型
     */
    private String valType="";
    /**
     * 字段值显示内容
     */
    private String text="";
    /**
     * 字段值
     */
    private String val="";
}
