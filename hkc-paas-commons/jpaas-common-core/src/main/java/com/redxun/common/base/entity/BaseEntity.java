package com.redxun.common.base.entity;

import java.io.Serializable;

/**
 * 带主键都基础实体。
 * @author csx
 */
public  interface BaseEntity<T>  extends Serializable {

	/**
	 * 主键。
	 * @return
	 */
	T  getPkId() ;

	/**
	 * 设置主键。
	 * @param pkId
	 */
	void setPkId(T pkId);
	
}
