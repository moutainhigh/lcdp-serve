package com.redxun.constvar.impl;


import com.redxun.common.tool.IdGenerator;
import com.redxun.constvar.ConstVarType;
import com.redxun.constvar.IConstVarService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 上下文系统的唯一ID
 * @author ray
 *
 */
@Service
public class UidContextImpl implements IConstVarService {


	@Override
	public Object getValue(Map<String, Object> vars) {
		return IdGenerator.getIdStr();
	}

	@Override
	public ConstVarType getType() {
		return new ConstVarType("[UID]","唯一ID");
	}


}
