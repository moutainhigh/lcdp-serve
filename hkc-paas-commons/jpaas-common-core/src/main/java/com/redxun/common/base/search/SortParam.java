package com.redxun.common.base.search;

/**
 * 字段排序参数
 */
public class SortParam {

    public final static String SORT_ASC = "ASC";

    public final static String SORT_DESC = "DESC";

    /**
     * 字段属性名称
     */
    private String property;
    /**
     * 字段方向排序 ASC/DESC
     */
    private String direction;

    public SortParam() {

    }

    public SortParam(String property, String direction) {
        this.property = property;
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getSql(){
        String orderSql=this.property + " " + this.direction;
        return orderSql;
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
