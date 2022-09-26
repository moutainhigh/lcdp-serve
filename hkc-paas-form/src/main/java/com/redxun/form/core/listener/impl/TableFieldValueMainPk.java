package com.redxun.form.core.listener.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 主表主键字段值(新建表单时使用)
 * @author ray
 *
 */
@Component
public class TableFieldValueMainPk implements ITableFieldValueHandler {
	
	@Override
	public String getMapType() {
		return TYPE_MAIN_PK;
	}

	@Override
	public String getMapTypeName() {
		return "主表主键字段值(新建表单时使用)";
	}


	@Override
	public boolean isParameterize() {
		return true;
	}

	@Override
	public Object getFieldValue(String dataType, String newPkId, DataHolder dataHolder,
                                JSONObject rowData, JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		return newPkId;
	}

}
