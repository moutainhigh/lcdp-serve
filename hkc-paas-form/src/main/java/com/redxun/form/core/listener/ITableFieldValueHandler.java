package com.redxun.form.core.listener;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.core.entity.DataHolder;


import java.util.Map;

/**
 * 表字段值
 * @author 
 *
 */
public interface ITableFieldValueHandler {

	static String TYPE_FIELD="field";
	static String TYPE_MAIN_FIELD="mainField";
	static String TYPE_SUB_FIELD="subField";
	static String TYPE_PK_ID="pkId";
	static String TYPE_MAIN_PK_ID="mainPkId";
	static String TYPE_MAIN_PK="mainPk";
	static String TYPE_SRC_PK_ID="srcPkId";
	static String TYPE_SEQ_VALUE="seqValue";
	static String TYPE_CUR_DATE="curDate";
	static String TYPE_CUR_USER_ID="curUserId";
	static String TYPE_CUR_USER_NAME="curUserName";
	static String TYPE_CUR_DEP_ID="curDepId";
	static String TYPE_CUR_DEP_NAME="curDepName";
	static String TYPE_CUR_INST_ID="curInstId";
	static String TYPE_CUR_INST_NAME="curInstName";
	static String TYPE_FIX_VALUE="fixValue";
	static String TYPE_SCRIPT_VALUE="scriptValue";
	
	/**
	 * 获得映射类型
	 * @return
	 */
	String getMapType();
	/**
	 * 获得映射类型名称
	 * @return
	 */
	String getMapTypeName();
	
	/**
	 * 是否参数化。
	 * @return
	 */
	boolean isParameterize();


	/**
	 * 获得字段值
	 * @param dataType
	 * @param newPkId
	 * @param dataHolder
	 * @param rowData
	 * @param oldRow
	 * @param mapValue
	 * @param extParams
	 * @return
	 */
	Object getFieldValue(String dataType, String newPkId, DataHolder dataHolder,
						 JSONObject rowData, JSONObject oldRow,
						 String mapValue, Map<String, Object> extParams);
}
