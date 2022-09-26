//package com.redxun.form.core.listener.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.redxun.form.core.entity.DataHolder;
//import com.redxun.form.core.listener.ITableFieldValueHandler;
//
//import java.util.Map;
//
///**
// * 当前机构名称
// * @author think
// *
// */
//public class TableFieldValueCurInstName implements ITableFieldValueHandler {
//
//	@Override
//	public String getMapType() {
//		return TYPE_CUR_INST_NAME;
//	}
//
//	@Override
//	public String getMapTypeName() {
//		return "当前机构名称";
//	}
//
//
//
//
//
//	@Override
//	public boolean isParameterize() {
//		return true;
//	}
//
//	@Override
//	public Object getFieldValue(String dataType, String srcPkId, DataHolder dataHolder,
//                                JSONObject rowData, JSONObject oldRow, String mapValue, Map<String,Object> extParams) {
//		return ContextUtil.getTenant().getTenantName();
//	}
//
//}
