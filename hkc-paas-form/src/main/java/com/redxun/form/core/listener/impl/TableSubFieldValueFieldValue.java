package com.redxun.form.core.listener.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 从子表字段获取。
 * @author ray
 *
 */

@Component
public class TableSubFieldValueFieldValue implements ITableFieldValueHandler {

	@Resource
	FormBoEntityServiceImpl formBoEntityService;

	@Override
	public String getMapType() {
		return ITableFieldValueHandler.TYPE_SUB_FIELD;
	}

	@Override
	public String getMapTypeName() {
		return "从子表字段获取";
	}

	@Override
	public boolean isParameterize() {
		return true;
	}

	@Override
	public Object getFieldValue(String dataType, String newPkId, DataHolder dataHolder, JSONObject rowData,
								JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		String userMainTable = (String) extParams.get("userMainTable");
		String userSubTable = (String) extParams.get("userSubTable");
		Integer index = (Integer) extParams.get("index");
		if(StringUtils.isEmpty(userSubTable)) {
			return null;
		}
		FormBoEntity ent = formBoEntityService.get(userSubTable);
		JSONObject mainObj;
		if(StringUtils.isEmpty(userMainTable)){
			mainObj=dataHolder.getCurMain();
		}else{
			mainObj=dataHolder.getCurMain().getJSONObject(userMainTable);
		}
		JSONArray array = mainObj.getJSONArray("sub__"+ent.getAlias());
		String val = getArrayByFilterSql(array,mapValue,index);
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

	private String getArrayByFilterSql(JSONArray array,String mapValue,Integer index) {
		if(BeanUtil.isNotEmpty(array)) {
			if(index==null){
				index=0;
			}
			JSONObject row=array.getJSONObject(index);
			String[] ary=mapValue.split("-");
			if(ary.length>1){
				return row.getJSONObject(ary[0]).getString(ary[1]);
			}
			return row.getString(mapValue);
		}
		return null;
	}

}
