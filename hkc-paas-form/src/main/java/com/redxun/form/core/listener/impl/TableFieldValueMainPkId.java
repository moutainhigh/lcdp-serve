package com.redxun.form.core.listener.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 来源表单主表主键字段值
 * @author ray
 *
 */
@Component
public class TableFieldValueMainPkId implements ITableFieldValueHandler {
	
	@Override
	public String getMapType() {
		return TYPE_MAIN_PK_ID;
	}

	@Override
	public String getMapTypeName() {
		return "来源表单主表主键字段值";
	}


	@Override
	public boolean isParameterize() {
		return true;
	}

	@Override
	public Object getFieldValue(String dataType, String srcPkId, DataHolder dataHolder,
                                JSONObject rowData, JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		return dataHolder.getCurMain().getString(FormBoEntity.FIELD_PK);
	}

}
