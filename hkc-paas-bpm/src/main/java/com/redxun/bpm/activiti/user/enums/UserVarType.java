package com.redxun.bpm.activiti.user.enums;

/**
 * 用户变量类型，如值来自单据或流程变量
 */
public enum UserVarType {
    FORM("form"),
    FLOW_VAR("flowVar");
    /**
     * 变量类型
     */
    private String varType;

    UserVarType(String varType){
        this.varType=varType;
    }
    public String getVarType() {
        return varType;
    }
}
