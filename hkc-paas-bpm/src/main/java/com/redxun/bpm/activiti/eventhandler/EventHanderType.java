package com.redxun.bpm.activiti.eventhandler;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 事件处理器类型
 */
@Getter
@Setter
@Accessors(chain = true)
public class EventHanderType {
    /**
     * Key
     */
    private String key="";
    /**
     * 名称
     */
    private String name="";

    public EventHanderType() {
    }

    public EventHanderType(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
