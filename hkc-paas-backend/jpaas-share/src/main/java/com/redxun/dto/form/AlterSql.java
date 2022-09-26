package com.redxun.dto.form;

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

    public AlterSql() {
    }


    public AlterSql(boolean delayExecute, String sql) {
        this.delayExecute = delayExecute;
        this.sql = sql;
    }

    /**
     * 是否延迟运行
     * @return
     */
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

    public  static AlterSql getDelaySql(String sql){
        return new AlterSql(true,sql);
    }

    public  static AlterSql getNoDelaySql(String sql){
        return new AlterSql(false,sql);
    }
}
