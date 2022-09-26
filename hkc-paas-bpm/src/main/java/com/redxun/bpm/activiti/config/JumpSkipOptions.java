package com.redxun.bpm.activiti.config;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 跳过选项
 */
@Setter
@Getter
public class JumpSkipOptions implements Serializable {

    /**
     * 跳转规则
     */
    private String type="";

    /**
     * 变量规则对应的公式
     */
    private String expression="";
}
