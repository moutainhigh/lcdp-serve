package com.redxun.bpm.activiti.event;

/**
 * 流程事件
 */
public enum EventType {
    PROCESS_STARTED,
    ACTIVITY_STARTED,
    ACTIVITY_COMPLETED,
    TASK_CREATED,
    TASK_ASSIGNED,
    TASK_COMPLETED,
    //会签完成
    MULTI_COMPLETED,
    PROCESS_COMPLETED;

    public static void main(String[] args) {
        System.err.println(EventType.TASK_CREATED.name());
    }



}
