package com.redxun.common.annotation;

import com.redxun.common.constant.HttpMethodConstants;

import java.lang.annotation.*;

/**
 * <pre>
 * 描述：方法描述
 * 构建组：ent-base-core
 * 作者：hj
 * 邮箱:
 * 日期:2020年7月25日
 * 广州红迅软件有限公司（http://www.redxun.cn）
 * </pre>
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
     * 方法路径
	 * @return
     */
	public String path();

    /**
     * 方法类型
	 * @return
     */
	public HttpMethodConstants method();
	/**
	 * 变量参数
	 * @return
	 */
	public ParamDefine[] params() default {};



}
