package com.redxun.constvar.impl;


import com.redxun.constvar.ConstVarType;
import com.redxun.constvar.IConstVarService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 返回上下文变量--当前日期格式化字符串
 */
@Service
public class CurDateFormatContextImpl implements IConstVarService {

	@Override
	public ConstVarType getType() {
		return new ConstVarType("[CURDATEFORMAT]","当前日期(格式化)");
	}

	@Override
	public Object getValue(Map<String,Object> vars) {
		String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		return format;
	}

}
