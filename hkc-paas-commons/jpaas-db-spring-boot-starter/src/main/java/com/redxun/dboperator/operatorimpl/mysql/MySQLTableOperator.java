package com.redxun.dboperator.operatorimpl.mysql;

import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.Table;
import com.redxun.dboperator.operatorimpl.BaseTableOperator;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mysql数据库表操作的实现。
 *
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 *
 */
@Component(value="mysql_TableOperator")
@Scope("prototype")
public class MySQLTableOperator extends BaseTableOperator {



	@Override
	public void createTable(Table table) throws SQLException {
		List<String> sqlList=getCreateTableSql(table);
		for(String sql:sqlList){
			jdbcTemplate.execute(sql);
		}
	}


	@Override
	public String getColumnType(Column column) {
		//有数据库字段类型
		if(StringUtils.isNotEmpty(column.getDbFieldType())){
			return getColumnType(column.getColumnType(), column.getCharLen(), column.getIntLen(), column.getDecimalLen(),column.getDbFieldType());
		}else {
			return getColumnType(column.getColumnType(), column.getCharLen(), column.getIntLen(), column.getDecimalLen());
		}
	}




	@Override
	public String getColumnType(String columnType, int charLen, int intLen, int decimalLen) {
		if (Column.COLUMN_TYPE_VARCHAR.equals(columnType)) {
			return "VARCHAR(" + charLen + ')';
		} else if (Column.COLUMN_TYPE_NUMBER.equals(columnType)) {
			return "DECIMAL(" + (intLen + decimalLen) + "," + decimalLen + ")";
		} else if (Column.COLUMN_TYPE_DATE.equals(columnType)) {
			return "datetime NULL";
		} else if (Column.COLUMN_TYPE_INT.equals(columnType)) {
			return "BIGINT(" + intLen + ")";
		} else if (Column.COLUMN_TYPE_CLOB.equals(columnType)) {
			return "TEXT";
		} else {
			return "";
		}
	}

	@Override
	public String getColumnType(String columnType, int charLen, int intLen, int decimalLen,String dbFieldType) {
		if (Column.COLUMN_TYPE_VARCHAR.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() + "(" + charLen + ')' : "VARCHAR(" + charLen + ')';
		} else if (Column.COLUMN_TYPE_NUMBER.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() + "(" + (intLen + decimalLen) + "," + decimalLen + ")" : "DECIMAL(" + (intLen + decimalLen) + "," + decimalLen + ")";
		} else if (Column.COLUMN_TYPE_DATE.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() + " NULL" : "datetime NULL";
		} else if (Column.COLUMN_TYPE_INT.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() + "(" + intLen + ')' : "BIGINT(" + intLen + ")";
		} else if (Column.COLUMN_TYPE_CLOB.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() : "TEXT";
		} else {
			return "";
		}
	}


	@Override
	public void dropTable(String tableName) throws SQLException {
		if (!isTableExist(tableName)) {
			return;
		}
		String sql = getDropTableSql(tableName);
		jdbcTemplate.execute(sql);
	}


	@Override
	public void updateTableComment(String tableName, String comment) throws SQLException {
		String sql=getUpdateTableCommentSql(tableName,comment);
		jdbcTemplate.execute(sql);
	}


	@Override
	public void addColumn(String tableName, Column model) throws SQLException {
		List<String> sqlList=getAddColumnSql(tableName,model);
		for (String sql:sqlList){
			jdbcTemplate.execute(sql);
		}
	}


	@Override
	public void updateColumn(String tableName, String columnName, Column column) throws SQLException {

		List<String> sqlList=getUpdateColumnSql(tableName,columnName,column);
		for(String sql:sqlList){
			jdbcTemplate.execute(sql);
		}
	}


	@Override
	public void addForeignKey(String pkTableName, String fkTableName, String pkField, String fkField) {
		String sql=getAddForeignKeySql(pkTableName,fkTableName,pkField,fkField);
		jdbcTemplate.execute(sql);
	}


	@Override
	public void dropForeignKey(String tableName, String keyName) {
		String sql =getDropForeignKeySql(tableName,keyName);
		jdbcTemplate.execute(sql);
	}


	@Override
	public List<String> getPKColumns(String tableName) throws SQLException {
		String schema = getSchema();
		String sql = "SELECT k.column_name " + "FROM information_schema.table_constraints t " + "JOIN information_schema.key_column_usage k " + "USING(constraint_name,table_schema,table_name) " + "WHERE t.constraint_type='PRIMARY KEY' " + "AND t.table_schema='" + schema + "' " + "AND t.table_name='" + tableName + "'";
		List<String> columns = jdbcTemplate.query(sql, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				String column = rs.getString(1);
				return column;
			}
		});
		return columns;
	}


	@Override
	public Map<String, List<String>> getPKColumns(List<String> tableNames) throws SQLException {
		StringBuffer sb = new StringBuffer();
		for (String name : tableNames) {
			sb.append("'");
			sb.append(name);
			sb.append("',");
		}
		sb.deleteCharAt(sb.length() - 1);

		String schema = getSchema();
		String sql = "SELECT t.table_name,k.column_name " + "FROM information_schema.table_constraints t " + "JOIN information_schema.key_column_usage k " + "USING(constraint_name,table_schema,table_name) " + "WHERE t.constraint_type='PRIMARY KEY' " + "AND t.table_schema='" + schema + "' " + "AND t.table_name in (" + sb.toString().toUpperCase() + ")";

		Map<String, List<String>> columnsMap = new HashMap<String, List<String>>();

		List<Map<String, String>> maps = jdbcTemplate.query(sql, new RowMapper<Map<String, String>>() {
			@Override
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				String table = rs.getString(1);
				String column = rs.getString(2);
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", table);
				map.put("column", column);
				return map;
			}
		});

		for (Map<String, String> map : maps) {
			if (columnsMap.containsKey(map.get("name"))) {
				columnsMap.get(map.get("name")).add(map.get("column"));
			} else {
				List<String> cols = new ArrayList<String>();
				cols.add(map.get("column"));
				columnsMap.put(map.get("name"), cols);
			}
		}

		return columnsMap;
	}


	@Override
	public boolean isTableExist(String tableName) {
		String schema = getSchema();
		String sql = "select count(1) from information_schema.TABLES t where t.TABLE_SCHEMA='" + schema + "' and table_name ='" + tableName.toUpperCase() + "'";
		boolean rtn= jdbcTemplate.queryForObject(sql.toString(), Integer.class) > 0;
		return rtn;
	}

	/**
	 * 取得当前连接的Schema
	 *
	 * @return
	 */
	private String getSchema() {
		String schema = null;
		Connection conn=null;
		DataSource dataSource=null;
		try {
			dataSource=jdbcTemplate.getDataSource();
			conn= dataSource.getConnection();
			schema = conn.getCatalog();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtils.releaseConnection(conn,dataSource);
		}
		return schema;
	}

	@Override
	public boolean isExsitPartition(String tableName, String partition) {
		partition=replaceLineThrough(partition);

		String sql="select count(*) from information_schema.partitions  where table_schema = schema() "
				+ "and table_name=? and partition_name =?";

		String[] args=new String[2];
		args[0]=tableName;
		args[1]="P_" + partition;
		Integer rtn= jdbcTemplate.queryForObject(sql, args, Integer.class);
		return rtn>0;
	}

	@Override
	public void createPartition(String tableName, String partition) {
		partition=replaceLineThrough(partition);
		String sql="alter table "+tableName+" add partition (partition P_"+partition+" values in ('"+partition+"')) ";
		jdbcTemplate.update(sql);
	}



	@Override
	public boolean supportPartition(String tableName) {
		String sql="select count(*) from information_schema.partitions  where table_schema = schema() "
				+ "and table_name=? ";

		Integer rtn= jdbcTemplate.queryForObject(sql, Integer.class, tableName);
		return rtn>0;
	}

	@Override
	public void createTableLike(String newTable, String oldTable, String pk) {
		String sql=" create table "+ newTable +" like "+oldTable;
		jdbcTemplate.execute(sql);
	}

	@Override
	public List<String>  getCreateTableSql(Table table) {
		List<String> sqlList=new ArrayList<>();
		List<Column> columnList = table.getColumnList();

		// 建表语句
		StringBuffer sb = new StringBuffer();
		// 主键字段
		String pkColumn = null;
		// 建表开始
		sb.append("CREATE TABLE " + table.getTableName() + " (\n");
		for (int i = 0; i < columnList.size(); i++) {
			// 建字段
			Column cm = columnList.get(i);
			sb.append(cm.getFieldName()).append(" ");
			String columnType="";
			//有数据库字段类型
			if(StringUtils.isNotEmpty(cm.getDbFieldType())){
				columnType= getColumnType(cm.getColumnType(), cm.getCharLen(), cm.getIntLen(), cm.getDecimalLen(),cm.getDbFieldType());
			}else {
				columnType= getColumnType(cm.getColumnType(), cm.getCharLen(), cm.getIntLen(), cm.getDecimalLen());
			}
			sb.append(columnType);
			sb.append(cm.getIsRequired() == 1 ? " NOT NULL " : " ");
			sb.append(" ");

			String defaultValue = cm.getDefaultValue();

			// 添加默认值。
			if (StringUtils.isNotEmpty(defaultValue)) {
				if (Column.COLUMN_TYPE_NUMBER.equals(cm.getColumnType()) || Column.COLUMN_TYPE_INT.equals(cm.getColumnType())) {
					sb.append(" default " + defaultValue);
				} else {
					sb.append(" default '" + defaultValue + "' ");
				}
			}
			// 主键
			if (cm.getIsPk()) {
				if (pkColumn == null) {
					pkColumn = cm.getFieldName();
				} else {
					pkColumn += "," + cm.getFieldName();
				}
			}
			// 字段注释
			if (cm.getComment() != null && cm.getComment().length() > 0) {
				sb.append(" COMMENT '" + cm.getComment() + "'");
			}
			sb.append(",\n");
		}

		//逻辑删除
		if(DbLogicDelete.getLogicDelete()){
			sb.append("DELETED_ INT(10)   default 0  COMMENT '逻辑删除'");
			sb.append(",\n");
		}

		// 建主键
		if (pkColumn != null) {
			sb.append(" PRIMARY KEY (" + pkColumn + ")");
		}

		sb.append("\n)");

		// 表注释
		if (table.getComment() != null && table.getComment().length() > 0) {
			sb.append(" COMMENT='" + table.getComment() + "'");
		}
		// 建表结束
		sb.append(";");

		sqlList.add(sb.toString());
		return sqlList;
	}

	@Override
	public List< String> getAddColumnSql(String tableName, Column model) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ").append(tableName);
		sb.append(" ADD (");
		sb.append(model.getFieldName()).append(" ");
		String columnType="";
		//有数据库字段类型
		if(StringUtils.isNotEmpty(model.getDbFieldType())){
			columnType= getColumnType(model.getColumnType(), model.getCharLen(), model.getIntLen(), model.getDecimalLen(),model.getDbFieldType());
		}else {
			columnType= getColumnType(model.getColumnType(), model.getCharLen(), model.getIntLen(), model.getDecimalLen());
		}
		sb.append(columnType);
		if (model.getIsRequired()==1) {//必填
			sb.append(" NOT NULL ");
		}
		if (model.getComment() != null && model.getComment().length() > 0) {
			sb.append(" COMMENT '" + model.getComment() + "'");
		}
		sb.append(");");

		List< String> sqlList=new ArrayList<>();

		sqlList.add(sb.toString());

		return sqlList;
	}

	@Override
	public List< String> getUpdateColumnSql(String tableName, String columnName, Column column) {
		StringBuffer sb = new StringBuffer();
		String columnType="";
		//有数据库字段类型
		if(StringUtils.isNotEmpty(column.getDbFieldType())){
			columnType= getColumnType(column.getColumnType(), column.getCharLen(), column.getIntLen(), column.getDecimalLen(),column.getDbFieldType());
		}else {
			columnType= getColumnType(column.getColumnType(), column.getCharLen(), column.getIntLen(), column.getDecimalLen());
		}
		sb.append("ALTER TABLE ").append(tableName).append(" CHANGE " + columnName + " " + column.getFieldName()).append(" ").append(columnType);
		if (!column.getIsNull()) {
			sb.append(" NOT NULL ");
		}

		if (column.getComment() != null && column.getComment().length() > 0) {
			sb.append(" COMMENT '" + column.getComment() + "'");
		}

		sb.append(";");

		List<String> sqlList=new ArrayList<>();
		sqlList.add(sb.toString());

		return sqlList;
	}

	@Override
	public String getUpdateTableCommentSql(String tableName, String comment) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ").append(tableName).append(" COMMENT '").append(comment).append("';\n");
		return sb.toString();
	}

	@Override
	public String getAddForeignKeySql(String pkTableName, String fkTableName, String pkField, String fkField) {
//		String shortTableName = fkTableName.replaceFirst("(?im)" + DbUtil.getTablePre(), "");
		String shortTableName = fkTableName.replaceFirst("(?im)" , "");
		String sql = "ALTER TABLE " + fkTableName + " ADD CONSTRAINT fk_" + shortTableName + " FOREIGN KEY (" + fkField + ") REFERENCES " + pkTableName + " (" + pkField + ") ON DELETE CASCADE";
		return sql;
	}

	@Override
	public String getDropForeignKeySql(String tableName, String keyName) {
		String sql = "ALTER TABLE " + tableName + " DROP FOREIGN KEY " + keyName;
		return sql;
	}


	@Override
	public boolean hasData(String tableName) {
		String sql="select count(*) from " + tableName + " limit 1";
		int rtn= jdbcTemplate.queryForObject(sql,Integer.class);
		return rtn>0;
	}
}
