package com.redxun.constvar.impl;

import com.redxun.common.utils.ContextUtil;
import com.redxun.constvar.ConstVarType;
import com.redxun.constvar.IConstVarService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserIdContextImpl implements IConstVarService {



	@Override
	public ConstVarType getType() {
		return new ConstVarType("[USERID]","当前用户ID");
	}

	@Override
	public Object getValue(Map<String,Object> vars) {
		String userId= ContextUtil.getCurrentUserId();
		return userId;
	}

}
