package com.redxun.bpm.activiti.user.enums;

/**
 * 用户组的值的类型，值为ID或变量Key
 */
public enum GroupValueType {
    ID("id"),
    KEY("key");

    /**
     * 变量类型
     */
    private String keyVal;

    public String getKeyVal(){
        return keyVal;
    }
    GroupValueType(String keyVal){
        this.keyVal=keyVal;
    }
}
