package com.redxun.bpm.script.cls;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法描述定义
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)   
@Documented
@Inherited
public @interface MethodDefine {
	/**
	 * 方法描述
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public String title();

	/**
	 * 描述
	 * @return
	 */
	public String description() default "";


}
