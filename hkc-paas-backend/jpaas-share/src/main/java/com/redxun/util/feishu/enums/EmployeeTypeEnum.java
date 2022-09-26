package com.redxun.util.feishu.enums;

/**
 * 飞书用户id类型枚举
 * @author ycs
 * @version V1.0
 * @since 2022-06-13 16:20
 */
public enum EmployeeTypeEnum {
    /**
     * 飞书用户id类型枚举
     */
    REGULAR("1","正式员工"),
    PRACTICE("2","实习生"),
    OUTSOURCED("3","外包"),
    LABOUR("4","劳务"),
    ADVISER("5","顾问");

    String value;
    String label;

    EmployeeTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
