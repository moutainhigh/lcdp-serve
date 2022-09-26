package com.redxun.common.base.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * sql model对象。
 * 用于封装SQL与参数值传给SQLMapper来执行返回记录值
 */
public class SqlModel {
    /**
     * 数据源别名
     */
    private String dsName="";

    /**
     * sql语句
     */
    private  String sql="";

    /**
     * 参数
     */
    private Map<String,Object> params=new HashMap<>();

    public SqlModel(){

    }

    public SqlModel(String sql){
        this.sql=sql;
    }

    public SqlModel(String sql,Map<String,Object> params){
        this.sql=sql;
        this.params=params;
    }

    public String getSql() {
        return sql;
    }

    /**
     * 设置SQL
     * @param sql
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * 添加参数
     * @param key
     * @param obj
     */
    public void addParam(String key,Object obj){
        this.params.put(key,obj);
    }

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }
}
