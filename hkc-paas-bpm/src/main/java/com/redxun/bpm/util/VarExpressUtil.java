package com.redxun.bpm.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author hujun
 */
public class VarExpressUtil {

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
