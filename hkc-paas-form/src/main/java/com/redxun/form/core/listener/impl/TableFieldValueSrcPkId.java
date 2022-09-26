package com.redxun.form.core.listener.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 源表主键字段值,表示从源表数据中获取主键。
 * @author ray
 *
 */
@Component
public class TableFieldValueSrcPkId implements ITableFieldValueHandler {
	
	@Override
	public String getMapType() {
		return TYPE_SRC_PK_ID;
	}

	@Override
	public String getMapTypeName() {
		return "源表主键字段值";
	}

	

	@Override
	public boolean isParameterize() {
		return true;
	}

	@Override
	public Object getFieldValue(String dataType, String srcPkId, DataHolder dataHolder,
                                JSONObject rowData, JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		return rowData.getString(FormBoEntity.FIELD_PK);
	}

}
