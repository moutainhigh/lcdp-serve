package com.redxun.log.annotation;

import java.lang.annotation.*;


/**
 * @author ray
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {
    /**
     * 操作信息
     */
    String operation();

    /**
     * freemark表达式。
     * @return
     */
    String expression() default "";
}
