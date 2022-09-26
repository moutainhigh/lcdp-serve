package com.redxun.form.core.listener.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.DateUtils;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 固定值
 * @author ray
 *
 */
@Component
public class TableFieldValueFixValue implements ITableFieldValueHandler {

	@Override
	public String getMapType() {
		return TYPE_FIX_VALUE;
	}

	@Override
	public String getMapTypeName() {
		return "固定值";
	}

	

	@Override
	public boolean isParameterize() {
		return true;
	}

	@Override
	public Object getFieldValue(String dataType, String srcPkId, DataHolder dataHolder,
                                JSONObject rowData, JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		if(StringUtils.isEmpty(mapValue)){
			return null;
		}
		if("string".equals(dataType)){
			return mapValue;
		}else if("number".equals(dataType)){
			return new Double(mapValue);
		}else if("date".equals(dataType)){
			return DateUtils.parseDate(mapValue);
		}else{
			return mapValue;
		}
	}

}
