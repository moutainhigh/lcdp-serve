package com.redxun.bpm.core.entity;

import lombok.Data;

import java.util.List;

/**
 * 流程图相关信息
 */
@Data
public class BpmImageResult {
    /**
     * 流程历史定义
     */
    String bpmnXml;
    /**
     * 流程审批历史
     */
    List<BpmCheckHistory> histories;

    public BpmImageResult(){

    }

    public BpmImageResult(String bpmnXml,List<BpmCheckHistory> histories){
        this.bpmnXml = bpmnXml;
        this.histories=histories;
    }
}
