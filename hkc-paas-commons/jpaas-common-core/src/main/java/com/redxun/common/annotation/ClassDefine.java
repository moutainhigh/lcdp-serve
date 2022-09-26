package com.redxun.common.annotation;

import java.lang.annotation.*;

/**
 * <pre> 
 * 描述：类描述
 * 构建组：ent-base-core
 * 作者：hj
 * 邮箱:
 * 日期:2020年7月25日
 * 广州红迅软件有限公司（http://www.redxun.cn）
 * </pre>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)   
@Documented
@Inherited
public @interface ClassDefine {
	/**
	 * 类描述
	 * @return 
	 * String
	 * @exception 
	 * @since  1.0.0
	 */
	public String title();

	/**
	 * 类别名
	 * @return
	 * String
	 * @exception
	 * @since  1.0.0
	 */
	public String alias();

	/**
	 * 类路径
	 * @return
	 */
	public String path();

	/**
	 * 包名
	 * @return
	 */
	public String packages() default "";

	/**
	 * 包名称
	 * @return
	 */
	public String packageName() default "";
}