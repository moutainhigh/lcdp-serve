package com.redxun.form.core.listener.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.feign.PublicClient;
import com.redxun.form.core.entity.DataHolder;
import com.redxun.form.core.listener.ITableFieldValueHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 从流水号获值
 * @author ray
 *
 */
@Component
public class TableFieldValueSeqValue implements ITableFieldValueHandler {
	@Resource
	PublicClient publicClient;
	@Override
	public String getMapType() {
		return TYPE_SEQ_VALUE;
	}

	@Override
	public String getMapTypeName() {
		return "从流水号获值";
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
		return publicClient.genSeqNo(mapValue);
	}

}
