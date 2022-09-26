package com.redxun.bpm.activiti.user;

import lombok.Data;

/**
 * 任务执行者类型
 */
@Data
public class ExecutorType {

    public ExecutorType() {
    }

    public ExecutorType(String type, String name,int order) {
        this.type = type;
        this.name = name;
        this.order = order;
    }

    /**
     * 执行者类型Key
     */
    private String type="";
    /**
     * 执行者类型名称
     */
    private String name="";
    /**
     * 序号
     */
    private int order=0;

}
