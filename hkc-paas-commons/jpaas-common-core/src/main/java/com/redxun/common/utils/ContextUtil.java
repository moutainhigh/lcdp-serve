package com.redxun.common.utils;

import com.redxun.common.base.entity.IUser;

/**
 * 上下文工具类。
 * 主要用于登录后获取登录用户的上下文参数
 */
public class ContextUtil {

    private static ThreadLocal<IUser>     userThreadLocal=new ThreadLocal<>();

    /**
     * 设置上下文用户。
     * @param user
     */
    public static void setCurrentUser(IUser user){
        userThreadLocal.set(user);
    }

    /**
     * 获取上下文用户。
     * @return
     */
    public static IUser getCurrentUser(){
        IUser user= userThreadLocal.get();
        return  user;
    }

    /**
     * 获取当前租户ID。
     * @return
     */
    public static String  getCurrentTenantId(){
        IUser user= getCurrentUser();
        if(user==null){
            return "";
        }
        return  user.getTenantId();
    }

    public static String getCurrentUserId(){
        IUser user= userThreadLocal.get();
        if(user==null){
            return  null;
        }
        return  user.getUserId();
    }


    /**
     * 返回当前用户ID
     * @return
     */
    public static String getComanyId(){
        IUser user= userThreadLocal.get();
        if(user==null){
            return  null;
        }
        return  user.getCompanyId();
    }

    /**
     * 清除上下文用户。
     */
    public static void clearUser(){
        userThreadLocal.remove();
    }
}
