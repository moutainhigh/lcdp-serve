package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 意见配置。
 */
@Data
public class OpinionSetting implements Serializable {
    /**
     * bo别名
     */
    private  String alias="";
    /**
     * bo名称
     */
    private String name="";

    /**
     * 意见配置。
     */
    private List<OpinionAttrSetting> attrData;
}