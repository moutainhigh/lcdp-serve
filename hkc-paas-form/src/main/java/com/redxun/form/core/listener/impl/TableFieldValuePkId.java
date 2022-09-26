package com.redxun.form.core.listener.impl;


import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.IdGenerator;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 由系统产生的主键字段值
 * @author ray
 *
 */
@Component
public class TableFieldValuePkId implements ITableFieldValueHandler {
	
	@Override
	public String getMapType() {
		return TYPE_PK_ID;
	}

	@Override
	public String getMapTypeName() {
		return "由系统产生的主键字段值";
	}

	

	@Override
	public boolean isParameterize() {
		return true;
	}

	@Override
	public Object getFieldValue(String dataType, String srcPkId, DataHolder dataHolder,
                                JSONObject rowData, JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		return IdGenerator.getIdStr();
	}

}
