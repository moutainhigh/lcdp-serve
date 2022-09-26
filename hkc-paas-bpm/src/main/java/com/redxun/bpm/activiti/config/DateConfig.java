package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 日期配置
 *
 * @author hujun
 */
@Data
public class DateConfig implements Serializable {
    /**
     * 催办名称
     */
    private String day;
    /**
     * 到期动作
     */
    private Integer hour;
    /**
     * 事件
     */
    private Integer minute;
}
