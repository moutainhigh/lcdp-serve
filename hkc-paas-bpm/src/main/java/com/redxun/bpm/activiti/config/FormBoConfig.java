package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 表单的Bo配置
 * @author csx
 */
@Data
public class FormBoConfig implements Serializable {
    /**
     * 类型
     */
    private String type;
    /**
     * 名称
     */
    private String name;
    /**
     * 描述
     */
    private String descp;
    /**
     * 单据字段配置
     */
    private List<FormFieldConfig> fieldConfigs=new ArrayList<>();
}
