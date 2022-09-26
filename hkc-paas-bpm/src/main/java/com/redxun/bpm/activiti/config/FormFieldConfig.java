package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 表单字段配置
 * @author  csx
 * {
 * 	"name": "code",
 * 	"label": "编码",
 *  "read": "none",
 * 	"write": "none",
 * 	"require": "none"
 *  },
 */
@Data
public class FormFieldConfig implements Serializable {
    private String name;
    private String label;
    private String read;
    private String write;
    private String require;

}
