package com.redxun.bpm.activiti.config;
import lombok.Data;

/**
 * 流程实例流程标题规则
 */
@Data
public class SubjectRuleItem {
    /**
     * false则为输入型的解析
     * true则为字段或表单的解析
     */
    private Boolean ruleType;
    /**
     * 标题分割
     */
    private String ruleSplitor;
}
