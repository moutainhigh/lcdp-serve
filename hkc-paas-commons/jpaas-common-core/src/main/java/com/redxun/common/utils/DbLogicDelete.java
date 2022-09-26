package com.redxun.common.utils;

/**
 * 功能: TODO
 *
 * @author ASUS
 * @date 2022/7/19 18:09
 */
public class DbLogicDelete {

    private static ThreadLocal<Boolean>     deleteLogic=new ThreadLocal<>();

    public static void setLogicDelete(Boolean isDelete){
        deleteLogic.set(isDelete);
    }

    public static Boolean getLogicDelete(){
        Boolean isLogic= deleteLogic.get();
        if(isLogic!=null){
            return isLogic;
        }
        //nacos全局配置
        boolean logicDel= SysPropertiesUtil.getBoolean("logicDel");
        //各个微服务配置
        String jpaasLogicDel= SpringUtil.getProperty("redxun.logicDel.isLogic");
        //微服务存在配置，以微服务为准，否则以全局为准
        if("true".equals(jpaasLogicDel)){
            deleteLogic.set(true);
            return  true;
        }else if("false".equals(jpaasLogicDel)){
            deleteLogic.set(false);
            return  false;
        }
        //微服务没有配置，以nacos全局配置为准
        deleteLogic.set(logicDel);
        return  logicDel;
    }

    public static void clear(){
         deleteLogic.remove();
    }
}
