package com.redxun.common.annotation;
/**
 * <pre>
 * 描述：参数描述
 * 构建组：ent-base-core
 * 作者：hj
 * 邮箱:
 * 日期:2020年7月25日
 * 广州红迅软件有限公司（http://www.redxun.cn）
 * </pre>
 */
public @interface ParamDefine {
	/**
	 * 变量标题
	 * @return
	 */
	public String title();
	/**
	 * 变量名
	 * @return
	 */
	public String varName();
}
