package com.redxun.util.feishu.enums;

/**
 * 部门id类型枚举
 * @author ycs
 * @version V1.0
 * @since 2022-06-13 16:20
 */
public enum DepartmentIdTypeEnum {
    /**
     * 飞书部门id类型枚举
     */
    DEPARTMENT_ID("department_id","department_id"),
    OPEN_DEPARTMENT_ID("open_department_id","open_department_id");

    String value;
    String label;

    DepartmentIdTypeEnum(String value, String label) {
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
