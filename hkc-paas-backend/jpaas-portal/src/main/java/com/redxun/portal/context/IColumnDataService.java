package com.redxun.portal.context;

/**
 * 获取栏目数据信息。
 * @author ray
 *
 */
public interface IColumnDataService {


	public static final String FUNC_TYPE="func";
	public static final String SQL_TYPE="sql";
	public static final String INTERFACE_TYPE="interface";


	
	/**
	 * 获取类型
	 * @return
	 */
	String getType();
	
	/**
	 * 获取类型名称。
	 * @return
	 */
	String getName();
	/**
	 * 设置查询对象
	 * @return
	 */
	void setSettingValue(String setting,String colId);
	
	/**
	 * 获取策略配置数据。
	 * @return
	 */
	Object getData();
}
