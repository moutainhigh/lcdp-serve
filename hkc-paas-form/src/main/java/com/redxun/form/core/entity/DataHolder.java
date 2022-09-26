package com.redxun.form.core.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据包。
 * @author ray
 *
 */
@Getter
@Setter
public class DataHolder {
	
	public final static String ACTION_NEW="new";
	public final static String ACTION_UPD="upd";
	public  final static String ACTION_DEL="del";
	
	
	/**
	 * 动作
	 * new,upd,del
	 */
	private String action="";

	/**
	 * 主表。
	 */
	private String mainTable="";
	/**
	 * 原主表数据。
	 */
	private JSONObject originMain=new JSONObject();

	/**
	 * 当前主表数据。
	 */
	private JSONObject curMain=new JSONObject();
	
	/**
	 * 当主表插入时，记录主表数据的新主键。
	 */
	private String newPk="";
	/**
	 * 子表数据管理。
	 */
	private Map<String,SubDataHolder> subDataMap=new HashMap<String,SubDataHolder>();

	/**
	 * 添加子表数据。
	 * @param tableName
	 * @param subDataHolder
	 */
	public void addSubData(String tableName,SubDataHolder subDataHolder){
		subDataMap.put(tableName, subDataHolder);
	}
	
	/**
	 * 获取子表数据。
	 * @param tableName
	 * @return
	 */
	public SubDataHolder getSubData(String tableName){
		return subDataMap.get(tableName);
	}

}
