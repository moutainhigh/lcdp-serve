package com.redxun.gencode.codegenhander;


import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassCodeDef {

    /**
     * 类标识
     * @return
     */
    String alias();

    /**
     * 类包名
     * @return
     */
    String classPackage();

    /**
     * 类创建类型：
     * @return
     */
    String createType();


}
