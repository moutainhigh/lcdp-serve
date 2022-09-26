package com.redxun.common.constant;

/**
 * Boolean 常量
 */
public enum BooleanConstants {
    YES("YES",new Short("1")),
    NO("NO",new Short("0"));

    private String name;
    private Short value;

    BooleanConstants(String name,Short value){
        this.name=name;
        this.value=value;
    }

    public String getName() {
        return name;
    }

    public Short getValue() {
        return value;
    }
}
