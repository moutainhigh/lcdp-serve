package com.redxun.constvar;

import java.util.Map;

/**
 * 上下文常量接口
 * @author ray
 *
 */
public interface IConstVarService {

	/**
	 * 变量类型
	 * @return
	 */
	ConstVarType getType();
	/**
	 * 获取值
	 * @return
	 */
	Object getValue(Map<String, Object> vars);

}
