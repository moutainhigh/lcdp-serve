package com.redxun.annotation;


import java.lang.annotation.*;

/**
 * 在方法上拦截是否启用全局事务。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalTrans {
}
