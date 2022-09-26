package com.redxun.form.core.listener.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 当前时间
 * @author ray
 *
 */
@Component
public class TableFieldValueCurDate implements ITableFieldValueHandler {
	
	@Override
	public String getMapType() {
		return TYPE_CUR_DATE;
	}

	@Override
	public String getMapTypeName() {
		return "当前时间";
	}

	


	@Override
	public boolean isParameterize() {
		return true;
	}

	@Override
	public Object getFieldValue(String dataType, String srcPkId, DataHolder dataHolder,
                                JSONObject rowData, JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		return new Date();
	}

}
