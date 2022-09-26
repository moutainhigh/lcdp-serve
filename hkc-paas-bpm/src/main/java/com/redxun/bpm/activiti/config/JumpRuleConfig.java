package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 跳转规则配置
 * @author csx
 * "uid": "DJPMKHEOYC4H422W4QY06QU7WXSJPFMB",
 * 				"name": "未定义",
 * 				"destNodeId": "",
 * 				"destNodeName": "",
 * 				"scriptConfig": ""
 */
@Data
public class JumpRuleConfig implements Serializable {
    private String uid;
    /**
     *  规则名称
     */
    private String name;
    /**
     * 目标节点Id
     */
    private String destNodeId;
    /**
     * 目标节点名称
     */
    private String destNodeName;
    /**
     * 规则配置
     */
    private String scriptConfig;

}
