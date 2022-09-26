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
 * 上下文变量值，返回当前用户主部门名称
 */
@Component
public class OsGroupNameContextImpl implements IConstVarService {

	@Autowired
	IOrgService orgService;


	@Override
	public ConstVarType getType() {
		return new ConstVarType("[USERGROUPNAME]","用户主部门名称");
	}

	@Override
	public Object getValue(Map<String,Object> vars) {
		IUser user=ContextUtil.getCurrentUser();
		return user.getDeptName();

	}

}
