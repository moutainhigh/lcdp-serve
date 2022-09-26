package com.redxun.constvar.impl;

import com.redxun.common.base.entity.IUser;
import com.redxun.common.utils.ContextUtil;
import com.redxun.constvar.ConstVarType;
import com.redxun.constvar.IConstVarService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserNameContextImpl implements IConstVarService {

	@Override
	public ConstVarType getType() {
		return new ConstVarType("[USERNAME]", "用户名");
	}

	@Override
	public Object getValue(Map<String, Object> vars) {
		IUser user= ContextUtil.getCurrentUser();
		return user.getFullName();
	}
}
