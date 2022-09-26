package com.redxun.constvar.impl;

import com.redxun.api.org.IOrgService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.utils.ContextUtil;
import com.redxun.constvar.ConstVarType;
import com.redxun.constvar.IConstVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 用户组的上下文变量值，返回当前用户主部门ID
 */
@Component
public class OsGroupContextImpl implements IConstVarService {

	@Autowired
	IOrgService orgService;


	@Override
	public ConstVarType getType() {
		return new ConstVarType("[USERGROUP]","用户主部门ID");
	}

	@Override
	public Object getValue(Map<String,Object> vars) {
		IUser user=ContextUtil.getCurrentUser();
		return user.getDeptId();

	}

}
