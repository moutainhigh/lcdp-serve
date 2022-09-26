package com.redxun.constvar.impl;


import com.redxun.constvar.ConstVarType;
import com.redxun.constvar.IConstVarService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 上下文变量--当前日期
 */
@Service
public class CurDateContextImpl implements IConstVarService {

	@Override
	public ConstVarType getType() {
		return new ConstVarType("[CURDATE]","当前日期");
	}

	@Override
	public Object getValue(Map<String,Object> vars) {
		return new Date();
	}

}
