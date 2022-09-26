package com.redxun.common.utils;

import com.redxun.config.SysConfig;


/**
 * 系统参数获取工具类。
 */
public class SysPropertiesUtil {

    private static SysConfig sysConfig = SpringUtil.getBean(SysConfig.class);

    /**
     * 根据键获取系统参数的值。
     * @param key
     * @return
     */
    public static String getString(String key) {
        String value="";
        try {
            value= sysConfig.getVal(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据键获取整数参数值
     * @param key
     * @return
     */
    public static Integer getInt(String key) {
        Integer value=0;
        try {
            value= Integer.valueOf(sysConfig.getVal(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据键获取布尔参数值。
     * @param key
     * @return
     */
    public static Boolean getBoolean(String key) {
        Boolean value=false;
        try {
            value=Boolean.valueOf(sysConfig.getVal(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 根据键获取长整型参数值。
     * @param key
     * @return
     */
    public static Long getLong(String key) {
        Long value=0L;
        try {
            value=Long.valueOf(sysConfig.getVal(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取组织分级管理开关配置
     *
     * @return
     */
    public static Boolean getSupportGradeConfig() {
        Boolean rtn=SysPropertiesUtil.getBoolean("supportGrade");
        if(rtn==null ){
            return false;
        }
        return rtn;
    }
}
