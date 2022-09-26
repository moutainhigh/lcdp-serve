package com.redxun.bpm.core.entity;

import lombok.Data;

/**
 * 流程变量实例
 * @author mansan
 * @Email: chshxuan@163.com
 * @Copyright (c) 2014-2022 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 */
@Data
public class ActVarInst {
	//变量名
	private String key;
	//类型
	private String type;
	//值
	private Object val;
	
	public ActVarInst() {
	
	}
	
	public ActVarInst(String key, String type, Object val){
		this.key=key;
		this.type=type;
		this.val=val;
	}

}
