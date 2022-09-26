package com.redxun.form.core.listener.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 从字段获取
 * @author ray
 *
 */
@Component
public class TableFieldValueFieldValue implements ITableFieldValueHandler {

	@Override
	public String getMapType() {
		return TYPE_FIELD;
	}

	@Override
	public String getMapTypeName() {
		return "从字段获取";
	}

	

	@Override
	public boolean isParameterize() {
		return true;
	}

	@Override
	public Object getFieldValue(String dataType, String srcPkId, DataHolder dataHolder,
                                JSONObject rowData, JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		String userMainTable = (String) extParams.get("userMainTable");
		if(rowData==null){
			return null;
		}
		String val;
		String[] ary=mapValue.split("-");
		if(StringUtils.isEmpty(userMainTable)){
			if(ary.length>1){
				val=rowData.getJSONObject(ary[0]).getString(ary[1]);
			}else {
				val=rowData.getString(mapValue);
			}
		}else{
			if(ary.length>1){
				val = rowData.getJSONObject(userMainTable).getJSONObject(ary[0]).getString(ary[1]);
			}else {
				val=rowData.getJSONObject(userMainTable).getString(mapValue);
			}
		}
		if("string".equals(dataType)){
			return val;
		}else if("number".equals(dataType)){
			if(StringUtils.isEmpty(val)){
				return null;
			}
			return new Double(val);
		}else if("date".equals(dataType)){
			Object v=rowData.get(mapValue);
			if(BeanUtil.isEmpty(v)){
				return null;
			}
			if(v instanceof Date){
				return new SimpleDateFormat( DateUtils.DATE_FORMAT_FULL).format(v);
			}
			return DateUtils.parseDate(v.toString());

		}else{
			return val;
		}
	}

}
