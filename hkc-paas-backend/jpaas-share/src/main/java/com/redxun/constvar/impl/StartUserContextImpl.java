package com.redxun.constvar.impl;


import com.redxun.api.org.IOrgService;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.constvar.ConstVarType;
import com.redxun.constvar.IConstVarService;
import com.redxun.dto.user.OsUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 返回上下文的发起人的用户名称
 */
@Service
public class StartUserContextImpl implements IConstVarService {

	@Autowired
	IOrgService orgService;



	@Override
	public ConstVarType getType() {
		return new ConstVarType("[STARTUSER]","发起人");
	}

	@Override
	public Object getValue(Map<String, Object> vars) {
		if(BeanUtil.isEmpty(vars)) {
			return "";
		}
		if(vars.containsKey("startUserId")){
			String startUserId= (String) vars.get("startUserId");
			if(StringUtils.isEmpty(startUserId)){
				return "";
			}
			OsUserDto user=orgService.getUserById(startUserId);
			return user.getFullName();
		}
		return "";
	}

}
