package com.redxun.form.core.listener;


import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.SqlModel;
import com.redxun.common.utils.SpringUtil;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.CommonDao;

import java.util.List;

/**
 * @author hujun
 */
public class FormulaUtil {

    /**
     * 判断是否在字符串数组中。
     *
     * @param str  a
     * @param args "a","b","c"
     * @return
     */
    public static boolean in(String str, String... args) {
        for (String s : args) {
            if (str.equals(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断数据是否存在。
     *
     * @param tableName
     * @param fieldName
     * @param val
     * @return
     */
    public static boolean isExist(String tableName, String fieldName, Object val) {
        CommonDao dao = SpringUtil.getBean(CommonDao.class);
        String dbType = DataSourceUtil.getDefaultDbType();
        String sql = "";
        if ("mysql".equals(dbType)) {
            sql = "SELECT 1 amount FROM " + tableName + " where " + fieldName + "=#{val} LIMIT 1 ";
        } else if ("oracle".equals(dbType)) {
            sql = "SELECT 1 amount FROM " + tableName + " WHERE ROWNUM <= 1 and " + fieldName + "=#{val}";
        }
        SqlModel model = new SqlModel(sql);
        model.addParam("val", val);
        List list = dao.query(model);
        int size = list.size();
        return size > 0;
    }

    /**
     * 数据是否存在。
     *
     * @param sql
     * @return
     */
    public static boolean isExistBySql(String sql) {
        CommonDao dao = SpringUtil.getBean(CommonDao.class);
        SqlModel model = new SqlModel(sql);
        List list = dao.query(model);
        int size = list.size();
        return size > 0;
    }

    /**
     * 将字符串转成JSON对象。
     * @param str
     * @return
     */
    public static JSONObject parseObject(String str) {
        return  JSONObject.parseObject(str);
    }


    /**
     * 获取JSON数据。
     * @param str
     * @param label
     * @return
     */
    public static String getValue(String str,boolean label){
        JSONObject json=JSONObject.parseObject(str);
        if(label){
            return  json.getString("label");
        }
        else {
            return  json.getString("value");
        }
    }

    public static String getLabel(String str){
        String val=getValue(str,true);
        return val;
    }

    /**
     * 获取JSON值
     * @param str
     * @return
     */
    public static String getValue(String str){
        String val=getValue(str,false);
        return val;
    }


}
