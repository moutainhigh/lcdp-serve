package com.redxun.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http 对象上下文对象的获取。
 *
 * @author ray
 *
 */
public class HttpContextUtil {

    private static ThreadLocal<HttpServletRequest> servletRequestThreadLocal=new ThreadLocal<>();

    private static ThreadLocal<HttpServletResponse> servletResponseThreadLocal =new ThreadLocal<>();

    public  static void setContext(HttpServletRequest request,HttpServletResponse response){
        servletRequestThreadLocal.set(request);
        servletResponseThreadLocal.set(response);
    }

    /**
     * 获取当前请求request对象。
     * @return
     */
    public  static HttpServletRequest getRequest(){

        return  servletRequestThreadLocal.get();
    }

    /**
     * 获取当前请求request对象。
     * @return
     */
    public  static HttpServletResponse getReponse(){
        return   servletResponseThreadLocal.get();
    }

    public  static void clear(){
        servletRequestThreadLocal.remove();
        servletResponseThreadLocal.remove();
    }

    /**
     * 获取上下文参数。
     * @param paramName
     * @return
     */
    public  static String  getParameter(String paramName){
        HttpServletRequest request = getRequest();
        return  request.getParameter(paramName);
    }

    /**
     * 获取请求属性。
     * @param attr
     * @return
     */
    public  static Object  getAttribute(String attr){
        HttpServletRequest request = getRequest();
        return  request.getAttribute(attr);
    }


}
