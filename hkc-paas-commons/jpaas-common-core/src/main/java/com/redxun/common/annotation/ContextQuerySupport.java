package com.redxun.common.annotation;

import java.lang.annotation.*;

/**
 * 功能: 通用查询注解。
 * @author ray
 * @date 2022/6/14 11:01
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ContextQuerySupport {

    /**
     * 只访问当前（租户，公司)的数据。
     */
    String CURRENT="current";
    /**
     * 只访问公共的数据
     */
    String PUBLIC="public";
    /**
     * 同时访问公共和当前的数据。
     */
    String BOTH="both";

    /**
     * 不适用此策略。
     */
    String NONE="none";


    /**
     * 根据租户查询数据
     * @return
     */
    String tenant() default CURRENT;

    /**
     * 根据分公司查询数据
     * @return
     */
    String company() default CURRENT;

}
