//package com.redxun.form.core.listener.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.redxun.common.utils.ContextUtil;
//import com.redxun.form.core.entity.DataHolder;
//import com.redxun.form.core.listener.ITableFieldValueHandler;
//
//import java.util.Map;
//
///**
// * 当前当前租户ID
// * @author think
// *
// */
//public class TableFieldValueCurInstId implements ITableFieldValueHandler {
//
//	@Override
//	public String getMapType() {
//		return TYPE_CUR_INST_ID;
//	}
//
//	@Override
//	public String getMapTypeName() {
//		return "当前当前租户ID";
//	}
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
//		String tenantId= ContextUtil.getCurrentTenantId();
//		return tenantId;
//	}
//
//}
