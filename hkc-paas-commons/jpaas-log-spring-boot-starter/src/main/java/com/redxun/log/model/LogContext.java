package com.redxun.log.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志上下文信息工具类。
 */
public class LogContext {

    private static ThreadLocal<Map<String,Object>> paramsLocal=new ThreadLocal<>();

    public static void put(String key,Object val){
        Map<String,Object> params=paramsLocal.get();
        if(params==null){
            params=new HashMap<>();
            params.put(key,val);
            paramsLocal.set(params);
        }
        else{
            params.put(key,val);
        }
    }

    public static Map<String, Object> get(){
        Map<String,Object> params=paramsLocal.get();

        return params;
    }

    /**
     * 根据键获取值。
     * @param key
     * @return
     */
    public static String getByKey(String key){
        Map<String,Object> params=paramsLocal.get();
        if(params==null){
            return "";
        }
        if(!params.containsKey(key)){
            return "";
        }
        return params.get(key).toString();
    }

    /**
     * 添加错误信息。
     * @param message
     */
    public static void addError(String message){
        put(Audit.DETAIL,message);
        put(Audit.STATUS,Audit.STATUS_FAIL);
    }


    public static void clear(){
        paramsLocal.remove();
    }

}
