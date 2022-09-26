package com.redxun.form.core.listener.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

;

/**
 * 当前用户名称
 * @author ray
 *
 */
@Component
public class TableFieldValueCurUserName implements ITableFieldValueHandler {
	
	@Override
	public String getMapType() {
		return TYPE_CUR_USER_NAME;
	}

	@Override
	public String getMapTypeName() {
		return "当前用户名称";
	}

	

	@Override
	public boolean isParameterize() {
		return true;
	}

	@Override
	public Object getFieldValue(String dataType, String srcPkId, DataHolder dataHolder,
                                JSONObject rowData, JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		return ContextUtil.getCurrentUser().getFullName();
	}

}
