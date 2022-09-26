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
// * 获得当前部门ID
// * @author think
// *
// */
//public class TableFieldValueCurDepId implements ITableFieldValueHandler {
//
//	@Override
//	public String getMapType() {
//		return TYPE_CUR_DEP_ID;
//	}
//
//	@Override
//	public String getMapTypeName() {
//		return "获得当前部门ID";
//	}
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
//		OsUser curUser=(OsUser) ContextUtil.getCurrentUser();
//		return curUser.getMainGroupId();
//	}
//
//}
