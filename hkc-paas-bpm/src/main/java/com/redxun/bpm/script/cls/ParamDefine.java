package com.redxun.bpm.script.cls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * 函数变量定义
 *
 */
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamDefine {
	/**
	 * 变量名
	 * @return
	 */
	public String varName();
	/**
	 * 变量描述
	 * @return
	 */
	public String description();

}
