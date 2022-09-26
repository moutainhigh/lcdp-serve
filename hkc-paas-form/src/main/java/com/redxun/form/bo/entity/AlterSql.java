package com.redxun.form.bo.entity;

/**
 * 执行的SQL.
 */
public class AlterSql {

    /**
     *延迟执行。
     */
    private boolean delayExecute=false;

    /**
     * 需要执行的SQL
     */
    private String sql="";

    /**
     *  add del upd
     */
    private String type="";

    public AlterSql() {
    }


    public AlterSql(boolean delayExecute, String sql,String type) {
        this.delayExecute = delayExecute;
        this.sql = sql;
        this.type = type;
    }

    public boolean isDelayExecute() {
        return delayExecute;
    }

    public void setDelayExecute(boolean delayExecute) {
        this.delayExecute = delayExecute;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public  static AlterSql getDelaySql(String sql,String type){
        return new AlterSql(true,sql,type);
    }

    public  static AlterSql getNoDelaySql(String sql,String type){
        return new AlterSql(false,sql,type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
