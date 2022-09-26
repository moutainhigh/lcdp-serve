package com.redxun.bpm.script.cls;

import java.lang.annotation.*;

/**
 * <pre> 
 * 描述：脚本分类描述
 * 作者：csx
 *
 * </pre>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)   
@Documented
@Inherited
public @interface ClassScriptType {
	/**
	 * 脚本类前置
	 * @return
	 */
	 String type();

	/**
	 * 脚本分类描述
	 * @return
	 * String
	 * @exception
	 * @since  1.0.0
	 */
	String description();

}