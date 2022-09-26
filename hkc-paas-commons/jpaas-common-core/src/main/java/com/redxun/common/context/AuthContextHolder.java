package com.redxun.common.context;


import com.redxun.common.base.entity.IUser;

/**
 * 认证上下文工具类。
 */
public class AuthContextHolder {

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
     * 清除上下文用户。
     */
    public static void clearUser(){
        userThreadLocal.remove();
    }
}
