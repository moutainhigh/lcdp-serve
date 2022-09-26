package com.redxun.bpm.activiti.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 任务字段配置
 */
@Getter
@Setter
public class TaskFieldConfig {
    /**
     * Bo别名
     */
    private String boAlias="";
    /**
     * 字段列表
     */
    private List<TaskField> fields;
}
