package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 意见控件设置是否可以写。
 */
@Data
public class OpinionAttrSetting implements Serializable {

    /**
     * 备注
     */
    private String comment="";
    /**
     * 名称
     */
    private String name="";

    /**
     * 意见打开。
     */
    private boolean setOpinion=false;

}