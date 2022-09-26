package com.redxun.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * ClassLoader工具类
 */
@Slf4j
public class ClassLoaderUtil {
    public static ClassLoader getClassLoader(String url) {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            URLClassLoader classLoader = new URLClassLoader(new URL[]{}, ClassLoaderUtil.class.getClassLoader());
            method.invoke(classLoader, new URL(url));
            return classLoader;
        } catch (Exception e) {
            log.error("getClassLoader-error", e);
            return null;
        }
    }
    /**
     * 获得对应接口实现类
     * @param jarUrl jar地址
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> loadClassByInterface(String jarUrl, Class<T> clazz){
        ClassLoader classLoader = getClassLoader(jarUrl);
        ServiceLoader<T> set= ServiceLoader.load(clazz,classLoader);
        List<T> list=new ArrayList<T>();
        Iterator<T> it=set.iterator();
        while(it.hasNext()){
            list.add(it.next());
        }
        return list;
    }
}