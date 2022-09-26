package com.redxun.common.base.entity;

/**
 * Cookie实体类
 *
 */
public class CookieModel {
    /**
     * Cookie name
     */
    private String name = "";
    /**
     * Cookie value
     */
    private String value = "";
    /**
     * Cookie path
     */
    private String path = "";
    /**
     * Cookie 是否只读
     */
    private boolean httpOnly = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    @Override
    public String toString() {
        return "CookieModel [name=" + name + ", value=" + value + "]";
    }


}
