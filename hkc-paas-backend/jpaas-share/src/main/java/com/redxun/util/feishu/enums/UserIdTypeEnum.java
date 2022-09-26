package com.redxun.util.feishu.enums;

/**
 * 飞书用户id类型枚举
 * @author ycs
 * @version V1.0
 * @since 2022-06-13 16:20
 */
public enum UserIdTypeEnum {
    /**
     * 飞书用户id类型枚举
     */
    OPEN_ID("open_id","用户openid"),
    UNION_ID("union_id","用户unionid"),
    USER_ID("user_id","用户userid");

    String value;
    String label;

    UserIdTypeEnum(String value, String label) {
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
