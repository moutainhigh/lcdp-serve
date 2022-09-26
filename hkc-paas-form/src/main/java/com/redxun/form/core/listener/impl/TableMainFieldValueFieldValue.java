package com.redxun.form.core.listener.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 从主表字段获取。
 * @author ray
 *
 */

@Component
public class TableMainFieldValueFieldValue  implements ITableFieldValueHandler {

	@Override
	public String getMapType() {
		return ITableFieldValueHandler.TYPE_MAIN_FIELD;
	}

	@Override
	public String getMapTypeName() {
		return "从主表字段获取";
	}

	@Override
	public boolean isParameterize() {
		return true;
	}

	@Override
	public Object getFieldValue(String dataType, String newPkId, DataHolder dataHolder, JSONObject rowData,
								JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		String userMainTable = (String) extParams.get("userMainTable");
		String val;
		JSONObject curMain=dataHolder.getCurMain();
		String[] ary=mapValue.split("-");
		if(StringUtils.isEmpty(userMainTable)){
			if(ary.length>1){
				val=curMain.getJSONObject(ary[0]).getString(ary[1]);
			}else {
				val = curMain.getString(mapValue);
			}
		}else{
			if(ary.length>1){
				val = curMain.getJSONObject(userMainTable).getJSONObject(ary[0]).getString(ary[1]);
			}else {
				val = curMain.getJSONObject(userMainTable).getString(mapValue);
			}
		}
		if("string".equals(dataType) || val==null){
			return val;
		}else if("number".equals(dataType)){
			return new Double(val);
		}else if("date".equals(dataType)){
			return DateUtils.parseDate(val);
		}else{
			return val;
		}
	}

}
