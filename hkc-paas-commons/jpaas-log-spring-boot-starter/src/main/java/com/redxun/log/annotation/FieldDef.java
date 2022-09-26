package com.redxun.log.annotation;

import com.redxun.common.tool.DateUtils;
import com.redxun.log.service.INameService;

import java.lang.annotation.*;

/**
 * 实例记录日志字段注解。
 * @author ray
 */
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldDef {

    /**
     * 注释
     * @return
     */
    String comment() default  "";

    /**
     * 是否添加时记录
     * @return
     */
    boolean add() default true;

    /**
     * 更新时记录
     * @return
     */
    boolean upd() default true;

    /**
     * 删除时记录
     * @return
     */
    boolean del() default true;


    /**
     * 名字处理器
     * @return
     */
    Class<? extends INameService> nameClass() default INameService.class;

    /**
     * 日期格式
     * @return
     */
    String format() default DateUtils.DATE_FORMAT_YMD;

    /**
     * 后缀
     * @return
     */
    String suffix() default "";

}
