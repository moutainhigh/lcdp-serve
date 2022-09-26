package com.redxun.bpm.core.entity;

/**
 * 流程实例状态
 * @author ray
 */
public enum BpmInstStatus {
    /*草稿*/
    DRAFTED,
    /*运行*/
    RUNNING,
    /*暂停*/
    SUPSPEND,
    /*作废*/
    CANCEL,
    /*成功结束*/
    SUCCESS_END,
    /*异常结束*/
    ERROR_END,
    /**
     * 再驳回时锁定。
     */
    LOCKED,
    /* 提交 */
    SUBMIT
}
