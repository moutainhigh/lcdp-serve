package com.redxun.oauth2.common.util;

/**
 * 密码理否忽略解析工具类
 * 目前主要用于实现自动登录不需要输入密码时，告诉密码解析器不需要匹配密码即返回密码匹配成功的功能
 */
public class PasswordUtil {
    /**
     * 是否忽略密码解析的线程工具类
     */
    private static ThreadLocal<Boolean> ignoreLocal=new ThreadLocal<Boolean>();

    /**
     * 是否忽略
     * @param ignore
     */
    public static void setIgnore(Boolean ignore){
        ignoreLocal.set(ignore);
    }

    public static Boolean getIgnore(){
        return ignoreLocal.get();
    }

    /**
     * 清空线程
     */
    public static void clear(){
         ignoreLocal.remove();
    }
}
