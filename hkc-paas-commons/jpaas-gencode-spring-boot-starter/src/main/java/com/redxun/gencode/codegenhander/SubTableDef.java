package com.redxun.gencode.codegenhander;


import java.lang.annotation.*;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SubTableDef {

    /**
     * 表名
     * @return
     */
    String tableName();

    /**
     * 类名
     * @return
     */
    Class className();

    /**
     * 子表类型
     * onetoone,onetomany
     * @return
     */
    String type();


}
