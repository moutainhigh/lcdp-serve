package com.redxun.common.constant;

/**
 * @模块包 (com.redxun.common.constant)
 * @创建人 csx
 * @创建时间 2019/12/26
 * @描述 Boolean 常量
 **/
public enum MBoolean {
    YES("YES"),
    NO("NO"),
    Y("Y"),
    N("N"),
    TRUE("TRUE"),
    FALSE("FALSE"),
    TRUE_LOWER("true"),
    FALSE_LOWER("false"),
    DISABLED("DISABLED"),
    ENABLED("ENABLED");

    public String val = "";

    MBoolean(String val){
        this.val=val;
    }

    public static void main(String[] args) {
        System.err.println(MBoolean.N.val);
    }
}
