package com.redxun.form.core.listener.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.constant.SysConstant;
import com.redxun.common.script.GroovyEngine;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.FormulaUtil;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 从脚本获取值
 * @author ray
 *
 */
@Component
public class TableFieldValueScriptValue implements ITableFieldValueHandler {
	@Resource
	GroovyEngine groovyEngine;
	
	@Override
	public String getMapType() {
		return TYPE_SCRIPT_VALUE;
	}

	@Override
	public String getMapTypeName() {
		return "从脚本获取值";
	}

	
	

	@Override
	public boolean isParameterize() {
		return false;
	}

	@Override
	public Object getFieldValue(String dataType, String srcPkId, DataHolder dataHolder,
                                JSONObject rowData, JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
		if(StringUtils.isEmpty(mapValue)){
			return null;
		}
		Map<String,Object> params=new HashMap<>(SysConstant.INIT_CAPACITY_16);
		String userMainTable=(String)extParams.get("userMainTable");
		String userSubTable=(String)extParams.get("userSubTable");
		if(!StringUtils.isEmpty(userMainTable) && rowData.containsKey(userMainTable)){
            JSONObject mainRowData=rowData.getJSONObject(userMainTable);
            if(!StringUtils.isEmpty(userSubTable) && mainRowData.containsKey(userSubTable)) {
                params.put("cur", mainRowData.get(userSubTable));
            }
        }else{
			params.put("cur", rowData);
		}

        if(!StringUtils.isEmpty(userMainTable) && rowData.containsKey(userMainTable)){
            JSONObject mainOldRow=oldRow.getJSONObject(userMainTable);
            if(!StringUtils.isEmpty(userSubTable) && mainOldRow.containsKey(userSubTable)) {
                params.put("old", mainOldRow.get(userSubTable));
            }
        }else{
			params.put("old", oldRow);
		}

        if(!StringUtils.isEmpty(userMainTable) && dataHolder.getCurMain().containsKey(userMainTable)){
            JSONObject mainCur=dataHolder.getCurMain().getJSONObject(userMainTable);
            params.put("mainCur",mainCur);
        }else{
			params.put("mainCur", dataHolder.getCurMain());
		}

        if(!StringUtils.isEmpty(userMainTable) && dataHolder.getOriginMain().containsKey(userMainTable)){
            JSONObject mainOld=dataHolder.getOriginMain().getJSONObject(userMainTable);
            params.put("mainOld",mainOld);
        }else{
			params.put("mainOld", dataHolder.getOriginMain());
		}

		params.putAll(extParams);

		/**
		 * 表间公式工具类
		 */
		params.put("FormulaUtil", FormulaUtil.class);
		params.put("JSONObject",JSONObject.class);
		
		
		Object val=groovyEngine.executeScripts(mapValue, params);
		
		return val;
	}

}
