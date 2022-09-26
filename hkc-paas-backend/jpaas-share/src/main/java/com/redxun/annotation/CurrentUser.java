package com.redxun.annotation;

import java.lang.annotation.*;

/**
 * 功能: 当前用户注解
 *
 * @author ray
 * @date 2022/4/28 10:49
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
