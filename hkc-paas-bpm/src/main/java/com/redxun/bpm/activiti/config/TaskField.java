package com.redxun.bpm.activiti.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 任务字段
 */
@Getter
@Setter
public class TaskField {
    /**
     * 是否单选
     */
    private Integer isSingle=1;
    /**
     * 标签名
     */
    private String label="";
    /**
     * 值
     */
    private String value="";

}
