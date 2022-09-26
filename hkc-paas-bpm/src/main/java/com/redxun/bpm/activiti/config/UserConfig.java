package com.redxun.bpm.activiti.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户配置
 */
@Data
public class UserConfig implements Serializable {

    public final static String CALC_NOT="no";
    public final static String CALC_YES="yes";
    public final static String CALC_DELAY="delay";

    public final static String LOGIC_NOT="no";
    public final static String LOGIC_AND="and";
    public final static String LOGIC_OR="or";

    //策略类型
    private String type;
    //配置
    private String config;
    //配置显示
    private String display="";
    //计算类型 计算,不计算,延迟
    private String calcType="";
    // 并，交集，排除
    private String logic="";

}
