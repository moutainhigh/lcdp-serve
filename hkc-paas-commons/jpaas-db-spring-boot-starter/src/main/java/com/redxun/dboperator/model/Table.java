package com.redxun.dboperator.model;

import java.util.List;

/**
 * 表对象。
 * 
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 */
public interface Table {
	
	String CUSTOM_TABLE_PRE="W_";
	
	String CUSTOM_COLUMN_PRE="F_";
	
	String CUSTOM_COLUMN_SUFFIX="_";
	
	String TYPE_TABLE="table";
	
	String TYPE_VIEW="view";
	

	/**
	 * 返回表名
	 * 
	 * @return
	 */
	String getTableName();
	
	String getEntName();
	//对象类型table,view
	String getType();
	
	void setEntName(String entName);

	/**
	 * 返回注释
	 * 
	 * @return
	 */
	String getComment();

	/**
	 * 返回字段列表
	 * 
	 * @return
	 */
	List<Column> getColumnList();

	/**
	 * 返回主键
	 * 
	 * @return
	 */
	List<Column> getPrimayKey();

	/**
	 * 设置表名
	 * @param name
	 */
	void setTableName(String name);

	/**
	 * 设置表注释
	 * @param comment
	 */
	void setComment(String comment);

	/**
	 * 设置字段列表
	 * @param columns
	 */
	void setColumnList(List<Column> columns);
	
	/**
	 * 增加字段
	 * @param column
	 */
	void addColumn(Column column);
	
	/**
	 * 是否主表。
	 * @return
	 */
	boolean isMain();
	
	/**
	 * 设置主表。
	 * @param isMain
	 */
	void setMain(boolean isMain);
	
	/**
	 * 设置表类型。
	 * @param type
	 */
	void setType(String type);

}
