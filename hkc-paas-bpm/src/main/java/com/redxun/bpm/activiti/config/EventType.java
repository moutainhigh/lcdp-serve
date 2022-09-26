package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 事件类型
 *
 */
@Data
public class EventType implements Serializable {
    /**
     * 事件Id
     */
    private String id;
    /**
     * 事件名称
     */
    private String name;
}
