package com.redxun.bpm.core.enums;

/**
 * 对话框相应枚举
 */
public enum DialogEmnu {
    DIALOG_USER_ID("userId","通过用户对话框获取用户ID","USER"),
    DIALOG_USER_NO("userNo","通过用户对话框获取用户编号","USER"),
    DIALOG_GROUP_ID("groupId","通过用户组对话框获取用户组ID","GROUP"),
    DIALOG_GROUP_KEY("key","通过用户组对话框获取用户组KEY","GROUP"),
    DIALOG_INTERFACE("apiId","通过第三方接口对话框获取接口","INTERFACE");

    String field;
    String title;
    String type;

    DialogEmnu(String field,String title,String type){
        this.field=field;
        this.title=title;
        this.type=type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
