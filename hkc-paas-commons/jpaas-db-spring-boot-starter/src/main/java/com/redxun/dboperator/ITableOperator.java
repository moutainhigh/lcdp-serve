package com.redxun.dboperator;

import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.Table;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据表操作接口。 对每一个数据库写一个实现，实现对不同数据库的统一操作。
 * 
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 */
public interface ITableOperator extends IDbType{

	/**
	 * 获取字段类型
	 * @param columnType		字段类型
	 * @param charLen           字符串长度
	 * @param intLen            整型长度
	 * @param decimalLen        小数长度
	 * @return
	 */
	String getColumnType(String columnType, int charLen, int intLen, int decimalLen);

	/**
	 * 获取字段类型
	 * @param columnType		字段类型
	 * @param charLen           字符串长度
	 * @param intLen            整型长度
	 * @param decimalLen        小数长度
	 * @param dbFieldType       数据库字段类型
	 * @return
	 */
	String getColumnType(String columnType, int charLen, int intLen, int decimalLen,String dbFieldType);

	/**
	 * 获取字段类型
	 *
	 * @param column	字段
	 * @return
	 */
	String getColumnType(Column column);

	/**
	 * 根据Tble创建表。
	 * @param table		表
	 * @throws SQLException
	 */
	void createTable(Table table) throws SQLException;

	/**
	 * 根据表名删除表。
	 * @param tableName
	 */
	void dropTable(String tableName) throws SQLException;

	/**
	 * 修改表的注释
	 * @param tableName            表名
	 * @param comment			        表注释
	 * @throws SQLException
	 */
	void updateTableComment(String tableName, String comment)
			throws SQLException;

	/**
	 * 在表中添加列。
	 * @param tableName            表名
	 * @param column               字段
	 * @throws SQLException
	 */
	void addColumn(String tableName, Column column) throws SQLException;

	/**
	 * 更新列。<br/>
	 * 可以修改列名，字段类型，字段是否非空，字段的注释。
	 *
	 * @param tableName		表名
	 * @param columnName	列名
	 * @param column		字段
	 * @throws SQLException
	 */
	void updateColumn(String tableName, String columnName, Column column)
			throws SQLException;

	/**
	 * 添加外键
	 *
	 * @param pkTableName	主键表
	 * @param fkTableName	外键表
	 * @param pkField	主键
	 * @param fkField	 外键
	 */
	void addForeignKey(String pkTableName, String fkTableName,
                       String pkField, String fkField);

	/**
	 * 删除外键
	 *
	 * @param tableName
	 * @param keyName
	 */
	void dropForeignKey(String tableName, String keyName);

	/**
	 * 根据表名，取得相应的主键的列表
	 *
	 * @param tableName		表名
	 * @return
	 * @throws SQLException
	 */
	List<String> getPKColumns(String tableName) throws SQLException;

	/**
	 * 根据表名列表，取得相应的主键的列列表的Map。Map中:key="name",value=表名；key=columns，value=
	 * 相应的主键的列列表。
	 *
	 * @param tableNames	表名列表
	 * @return
	 * @throws SQLException
	 */
	Map<String, List<String>> getPKColumns(List<String> tableNames)
			throws SQLException;



	/**
	 * 判断表是否存在。
	 * @param tableName
	 * @return
	 */
	boolean isTableExist(String tableName);

	/**
	 * 判断指定表的分区是否存在。
	 * @param tableName
	 * @param partition
	 * @return
	 */
	boolean isExsitPartition(String tableName, String partition);

	/**
	 * 创建分区。
	 * @param tableName
	 * @param partition
	 */
	void createPartition(String tableName, String partition);


	/**
	 * 是否支持分区。
	 * 判断表是否有创建分区。
	 * @param tableName
	 * @return
	 */
	boolean supportPartition(String tableName);

	/**
	 * 检查数据表中是否有数据。
	 * @param tableName
	 * @return
	 */
	boolean hasData(String tableName);

	/**
	 * 创建索引。
	 * @param table
	 * @param colName
	 */
	void createIndex(String name, String table, String colName);

	/**
	 * 删除列
	 * @param tableName
	 * @param column
	 */
	void dropColumn(String tableName, String column);

	/**
	 * 根据旧表生成新的表名。
	 *
	 * @param newTable
	 * @param oldTable
	 * @param pk
	 */
	default void createTableLike(String newTable, String oldTable, String pk) {

	}

	/**
	 * 获取创建表的语句。
	 * @param table
	 * @return
	 */
	List<String> getCreateTableSql(Table table);

	/**
	 * 获取添加列的语句。
	 * @param tableName
	 * @param column
	 * @return
	 */
	List<String> getAddColumnSql(String tableName, Column column);

	/**
	 * 获取更新列的SQL语句。
	 * @param tableName
	 * @param columnName
	 * @param column
	 * @return
	 */
	List<String> getUpdateColumnSql(String tableName, String columnName, Column column);

	/**
	 * 获取删除表sql。
	 * @param tableName
	 * @return
	 */
	String getDropTableSql(String tableName);

	/**
	 * 获取更新表注释的SQL语句。
	 * @param tableName
	 * @param comment
	 * @return
	 */
	String getUpdateTableCommentSql(String tableName, String comment);

	/**
	 * 获取添加外键的SQL语句。
	 * @param pkTableName
	 * @param fkTableName
	 * @param pkField
	 * @param fkField
	 * @return
	 */
	String getAddForeignKeySql(String pkTableName, String fkTableName, String pkField, String fkField);

	/**
	 * 获取删除外键语句。
	 * @param tableName
	 * @param keyName
	 * @return
	 */
	String getDropForeignKeySql(String tableName, String keyName);

	/**
	 * 获取删除列SQL
	 * @param tableName
	 * @param column
	 * @return
	 */
	String getDropColumnSql(String tableName, String column);

}
