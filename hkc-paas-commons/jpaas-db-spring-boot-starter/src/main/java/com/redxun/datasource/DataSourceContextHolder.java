package com.redxun.datasource;

import com.redxun.common.utils.SpringUtil;

/**
 * 数据源切换工具类。
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static void setDataSource(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static void setDefaultDataSource(){
        contextHolder.remove();
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }


}
