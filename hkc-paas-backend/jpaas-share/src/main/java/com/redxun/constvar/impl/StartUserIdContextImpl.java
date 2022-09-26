package com.redxun.constvar.impl;

import com.redxun.common.tool.BeanUtil;
import com.redxun.constvar.ConstVarType;
import com.redxun.constvar.IConstVarService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 返回上下文的发起人的用户ID
 */
@Service
public class StartUserIdContextImpl implements IConstVarService {



	@Override
	public Object getValue(Map<String, Object> vars) {
		if(BeanUtil.isEmpty(vars)) {
			return "";
		}
		if(vars.containsKey("startUserId")){
			return vars.get("startUserId");
		}
		return "";
	}

	@Override
	public ConstVarType getType() {
		return new ConstVarType("[STARTUSERID]","发起人ID");
	}


}
