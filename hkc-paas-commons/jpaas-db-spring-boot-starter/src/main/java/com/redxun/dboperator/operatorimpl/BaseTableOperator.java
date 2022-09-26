package com.redxun.dboperator.operatorimpl;


import com.redxun.dboperator.ITableOperator;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.Table;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 操作数据表基类。
 *
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 *
 */
public abstract class BaseTableOperator  implements ITableOperator {

	protected JdbcTemplate jdbcTemplate;

	@Override
	public void setJdbcTemplate(JdbcTemplate template){
		this.jdbcTemplate=template;
	}


	@Override
	public boolean hasData(String tableName) {
		String sql="select count(*) from " + tableName;
		int rtn= jdbcTemplate.queryForObject(sql, Integer.class);
		return rtn>0;
	}

	protected String replaceLineThrough(String partition){
		return partition.toUpperCase().replaceAll("-", "");
	}

	@Override
	public void createIndex(String name, String table, String colName) {
		String sql="CREATE INDEX "+name+" ON "+table+" ("+colName+")";
		jdbcTemplate.execute(sql);
	}

	@Override
	public void dropColumn(String tableName,String column){
		String sql=getDropColumnSql(tableName,column);
		jdbcTemplate.execute(sql);
	}

	@Override
	public String getDropTableSql(String tableName) {
		String sql = "drop table if exists " + tableName +";";
		return sql;
	}

	@Override
	public String getDropColumnSql(String tableName, String column) {
		String sql="ALTER TABLE "+tableName+" DROP COLUMN " + column +";";
		return sql;
	}

	@Override
	public List<String> getCreateTableSql(Table table) {
		throw new RuntimeException("不支持此方法");

	}

	@Override
	public List<String> getAddColumnSql(String tableName, Column column) {
		throw new RuntimeException("不支持此方法");
	}

	@Override
	public List<String> getUpdateColumnSql(String tableName, String columnName, Column column) {
		throw new RuntimeException("不支持此方法");
	}

	@Override
	public String getUpdateTableCommentSql(String tableName, String comment) {
		throw new RuntimeException("不支持此方法");
	}

	@Override
	public String getAddForeignKeySql(String pkTableName, String fkTableName, String pkField, String fkField) {
		throw new RuntimeException("不支持此方法");
	}

	@Override
	public String getDropForeignKeySql(String tableName, String keyName) {
		throw new RuntimeException("不支持此方法");
	}
}
