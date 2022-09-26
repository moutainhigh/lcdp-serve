package com.redxun.common.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * spring获取bean工具类
 *
 * @author 作者 yjy
 */
@Component
public class SpringUtil implements ApplicationContextAware, EnvironmentAware {

    private static ApplicationContext applicationContext = null;

    private static Environment environment = null;

    /**
     * 通过Class
     * @param cla
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> cla) {
        assertApplicationContext();
        return applicationContext.getBean(cla);
    }

    /**
     * 根据接口获取关联的类列表。
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> Collection<T> getBeans(Class cls){
       Map<String, T> map= applicationContext.getBeansOfType(cls);
       return  map.values();
    }

    /**
     * 根据名称与类名
     * @param name
     * @param classType
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> classType) {
        assertApplicationContext();
        return applicationContext.getBean(name, classType);
    }

    /**
     * 根据Bean名称查找
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name) {
        assertApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 获取环境变量中的属性
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        assertApplicationContext();
        return environment.getProperty(key);
    }

    /**
     * 设置ApplicationContext
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 发布事件。
     * @param event
     */
    public static void publishEvent(ApplicationEvent event){
        assertApplicationContext();
        if(applicationContext!=null){
            applicationContext.publishEvent(event);
        }
    }

    private static void assertApplicationContext() {
        if (applicationContext == null) {
            throw new RuntimeException("applicaitonContext属性为null,请检查是否注入了SpringContextHolder!");
        }
    }

    public static ApplicationContext getApplicationContext() {
        assertApplicationContext();
        return applicationContext;
    }

    @Override
    public void setEnvironment(Environment ent) {
        environment=ent;
    }
}
