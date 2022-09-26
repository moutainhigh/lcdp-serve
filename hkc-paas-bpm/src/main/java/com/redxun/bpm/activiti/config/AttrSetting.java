package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 流程单据属性初始化设置
 */
@Data
public class AttrSetting implements Serializable {
    //单据别名
    private String alias="";
    //字段名
    private String name="";
    //初始设置
    private FieldSetting initSet;
    //保存设置
    private FieldSetting saveSet;


}
