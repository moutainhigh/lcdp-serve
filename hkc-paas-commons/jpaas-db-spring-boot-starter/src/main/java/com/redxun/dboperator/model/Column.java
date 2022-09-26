package com.redxun.dboperator.model;

/**
 * 列对象。 用于产生数据库列。
 * 
 * <pre>
 * 作者：ray
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 */
public interface Column {

	// 字段常用变量
	/** 字符串 */
	String COLUMN_TYPE_VARCHAR = "varchar";
	/** 大文本 */
	String COLUMN_TYPE_CLOB = "clob";
	/** 数字 */
	String COLUMN_TYPE_NUMBER = "number";
	/** 整型 */
	String COLUMN_TYPE_INT = "int";
	/** 日期 */
	String COLUMN_TYPE_DATE = "date";

	String COLUMN_TYPE_TEXT = "text";

	String COLUMN_TYPE_BYTEA = "bytea";
	/**
	 * 列名
	 * 
	 * @return
	 */
    String getFieldName();
	
	/**
	 * 获取原列名
	 * @return
	 */
	String getOriginName();
	
	/**
	 * 设置原列名。
	 * @param originName
	 */
	void setOriginName(String originName);

	/**
	 * 列注释
	 * 
	 * @return
	 */
    String getComment();

	/**
	 * 是否主键
	 * 
	 * @return
	 */
    boolean getIsPk();

	/**
	 * 是否可为空
	 * 
	 * @return
	 */
    boolean getIsNull();

	/**
	 * 列类型
	 * 
	 * @return
	 */
    String getColumnType();
    /**
	 * 数据库类型
	 *
	 * @return
	 */
    String getDbFieldType();

	/**
	 * 字符串长度
	 * 
	 * @return
	 */
    int getCharLen();

	/**
	 * 整数位长度
	 * 
	 * @return
	 */
    int getIntLen();

	/**
	 * 小数位
	 * 
	 * @return
	 */
    int getDecimalLen();

	/**
	 * 默认值
	 * 
	 * @return
	 */
    String getDefaultValue();

	/**
	 * 表名
	 * 
	 * @return
	 */
    String getTableName();

	/**
	 * 设置 列名
	 * 
	 * @param name
	 */
    void setFieldName(String name);

	/**
	 * 设置类类型
	 * 
	 * @param columnType
	 */
    void setColumnType(String columnType);

	/**
	 * 设置类类型
	 *
	 * @param dbFieldType
	 */
	void setDbFieldType(String dbFieldType);

	/**
	 * 设置列注释
	 * 
	 * @param comment
	 */
    void setComment(String comment);

	/**
	 * 设置 是否为空
	 * 
	 * @param isNull
	 */
    void setIsNull(boolean isNull);

	/**
	 * 设置是否是主键
	 * 
	 * @param isPk
	 */
    void setIsPk(boolean isPk);

	/**
	 * 设置字符串长度
	 * 
	 * @param charLen
	 */
    void setCharLen(int charLen);

	/**
	 * 设置 整数的长度
	 * 
	 * @param intLen
	 */
    void setIntLen(int intLen);

	/**
	 * 设置 小数长度
	 * 
	 * @param decimalLen
	 */
    void setDecimalLen(int decimalLen);

	/**
	 * 默认值
	 * 
	 * @param defaultValue
	 */
    void setDefaultValue(String defaultValue);

	/**
	 * 表名
	 * 
	 * @param tableName
	 */
    void setTableName(String tableName);

	int getIsRequired();

	void setIsRequired(int isRequired);

}
