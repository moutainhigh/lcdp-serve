package com.redxun.cache.aspect;

import com.redxun.cache.CacheUtil;
import com.redxun.cache.annotation.Cacheable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 增加一个缓存切面。
 * <pre>
 *     这个缓存切面作用：
 *     1.当需要获取数据时，先根据参数去缓存中查找，如果缓存中有数据，那么直接返回。
 *     2.如果查不到，则调用原来的逻辑获取数据。
 *     3.获取数据后，将这个数据添加到缓存中
 *     注意:
 *     如果一个方法只有一个参数和返回结果需要被缓存，那么可以使用该注解
 *
 * </pre>
 */
@Aspect
@Slf4j
public class CacheAspect {

    @Pointcut("@annotation(com.redxun.cache.annotation.Cacheable)")
    public void allMethod(){}


    @Around(value = "allMethod()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        if(joinPoint.getArgs().length>1){
            Object rtn= joinPoint.proceed();
            return rtn;
        }

        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method=methodSignature.getMethod();

        Cacheable cacheable= method.getAnnotation(Cacheable.class);
        String region=cacheable.region();

        String id= (String) joinPoint.getArgs()[0];
        Object obj= CacheUtil.get(region,id);
        if(obj!=null){
            return obj;
        }
        Object rtn= joinPoint.proceed();
        CacheUtil.set(region,id,rtn);
        return rtn;
    }
}
