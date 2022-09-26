package com.redxun.common.utils;

import com.redxun.common.exception.BusinessException;

import java.util.ArrayList;
import java.util.List;

/**
 * 平台消息工具。
 */
public class MessageUtil {

    private static ThreadLocal<String> titleLocal=new ThreadLocal<>();

    /**
     * 显示错误。
     */
    private static ThreadLocal<Boolean> showError=new ThreadLocal<>();

    private static ThreadLocal<List<String>> messageLocal=new ThreadLocal<>();

    /**
     * 初始化消息。
     */
    public static void init(){
        titleLocal.remove();
        showError.remove();
        messageLocal.set(new ArrayList<String>());
    }


    /**
     * 添加消息。
     * @param msg
     */
    public  static void addMessage(String msg){
        if(messageLocal.get()==null) {
            init();
        }
        messageLocal.get().add(msg);
    }

    public static String getTitle(){
        return titleLocal.get();
    }


    /**
     * 获取消息。
     * @return
     */
    public  static String getMessage(){
        List<String> list=  messageLocal.get();
        String str="";
        for(String msg : list){
            str+=msg +"\r\n";
        }
        return  str;
    }

    /**
     * 抛出异常。
     * @param message
     */
    public static void triggerException( String message){
        addMessage(message);
        throw new BusinessException(message);
    }

    /**
     * 抛出异常。
     * @param message
     */
    public static void triggerException(String title, String message){
        titleLocal.set(title);
        addMessage(message);
        throw new BusinessException(message);
    }

    /**
     * 抛出异常
     * @param title     标题
     * @param message   消息体
     * @param showErr   是否显示错误信息。
     */
    public static void triggerException(String title, String message,Boolean showErr){
        titleLocal.set(title);
        showError.set(showErr);
        addMessage(message);
        throw new BusinessException(message);
    }

    /**
     * 是否显示错误。
     * @return
     */
    public static Boolean getShowError(){
        Boolean show= showError.get();
        if(show==null){
            return  true;
        }
        return show;
    }

    public static  void clear(){
        titleLocal.remove();
        messageLocal.remove();
        showError.remove();
    }


}
