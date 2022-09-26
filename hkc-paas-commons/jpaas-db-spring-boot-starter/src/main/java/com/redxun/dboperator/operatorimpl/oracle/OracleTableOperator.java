package com.redxun.dboperator.operatorimpl.oracle;


import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.Table;
import com.redxun.dboperator.operatorimpl.BaseTableOperator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * oracle 数据库表操作的接口实现。
 *
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 *
 */
@Component(value="oracle_TableOperator")
public class OracleTableOperator extends BaseTableOperator {



	@Override
	public void createTable(Table table) throws SQLException {
		List<String> sqlList=getCreateTableSql(table);
		for (String sql : sqlList) {
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
	public String getColumnType(String columnType, int charLen, int intLen,
								int decimalLen) {
		if (Column.COLUMN_TYPE_VARCHAR.equals(columnType)) {
			return "VARCHAR2(" + charLen + ')';
		} else if (Column.COLUMN_TYPE_NUMBER.equals(columnType)) {
			return "NUMBER(" + (intLen + decimalLen) + "," + decimalLen + ")";
		} else if (Column.COLUMN_TYPE_DATE.equals(columnType)) {
			return "DATE";
		} else if (Column.COLUMN_TYPE_INT.equals(columnType)) {
			return "NUMBER(" + intLen + ")";
		} else if (Column.COLUMN_TYPE_CLOB.equals(columnType)) {
			return "CLOB";
		} else {
			return "VARCHAR2(200)";
		}
	}

	@Override
	public String getColumnType(String columnType, int charLen, int intLen, int decimalLen, String dbFieldType) {
		if (Column.COLUMN_TYPE_VARCHAR.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() + "(" + charLen + ')' : "VARCHAR2(" + charLen + ')';
		} else if (Column.COLUMN_TYPE_NUMBER.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() + "(" + (intLen + decimalLen) + "," + decimalLen + ")"
					: "NUMBER(" + (intLen + decimalLen) + "," + decimalLen + ")";
		} else if (Column.COLUMN_TYPE_DATE.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() : "DATE";
		} else if (Column.COLUMN_TYPE_INT.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() + "(" + intLen + ')' : "INTEGER(" + intLen + ")";
		} else if (Column.COLUMN_TYPE_CLOB.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() : "CLOB";
		} else {
			return "VARCHAR2(200)";
		}
	}


	@Override
	public void dropTable(String tableName) throws SQLException {
		String selSql = "select count(*) amount from user_objects where object_name = upper('"
				+ tableName + "')";
		int rtn = jdbcTemplate.queryForObject(selSql, Integer.class);
		if (rtn <= 0) {
			return;
		}
		String sql = getDropTableSql(tableName);
		jdbcTemplate.execute(sql);

	}

	@Override
	public String getDropTableSql(String tableName) {
		String sql = "drop table " + tableName ;
		return sql;
	}


	@Override
	public void updateTableComment(String tableName, String comment)
			throws SQLException {
		String sql=getUpdateTableCommentSql(tableName,comment);
		jdbcTemplate.execute(sql);
	}


	@Override
	public void addColumn(String tableName, Column model) throws SQLException {
		List<String> sqlList=getAddColumnSql(tableName,model);
		for(String sql:sqlList){
			jdbcTemplate.execute(sql);
		}


	}

	@Override
	public void updateColumn(String tableName, String columnName, Column column)
			throws SQLException {
		// 修改列名
		if (!columnName.equals(column.getFieldName())) {
			StringBuffer modifyName = new StringBuffer("ALTER TABLE ")
					.append(tableName);
			modifyName.append(" RENAME COLUMN ").append(columnName)
					.append(" TO ").append(column.getFieldName());
			jdbcTemplate.execute(modifyName.toString());
		}

		// 修改列的大小,此处不修改列的类型,若修改列的类型则在前面部分已抛出异常
		StringBuffer sb = new StringBuffer();
		// alter table test01 modify(test_01 NUMBER(1));
		sb.append("ALTER TABLE ").append(tableName);
		sb.append(" MODIFY(" + column.getFieldName()).append(" ");
		sb.append(getColumnType(column.getColumnType(), column.getCharLen(),
				column.getIntLen(), column.getDecimalLen()));
		if (!column.getIsNull()) {
			sb.append(" NOT NULL ");
		}
		sb.append(")");

		jdbcTemplate.execute(sb.toString());

		// 修改注释
		if (column.getComment() != null && column.getComment().length() > 0) {
			jdbcTemplate.execute("COMMENT ON COLUMN " + tableName + "."
					+ column.getFieldName() + " IS'" + column.getComment() + "'");
		}
	}


	@Override
	public void addForeignKey(String pkTableName, String fkTableName,
							  String pkField, String fkField) {
		String sql=getAddForeignKeySql(pkTableName,fkTableName,pkField,fkField);
		jdbcTemplate.execute(sql);
	}


	@Override
	public void dropForeignKey(String tableName, String keyName) {
		String sql = getDropForeignKeySql(tableName,keyName);
		jdbcTemplate.execute(sql);
	}


	@Override
	public List<String> getPKColumns(String tableName) throws SQLException {
		String sql = "SELECT cols.column_name"
				+ " FROM USER_CONSTRAINTS CONS, USER_CONS_COLUMNS COLS"
				+ " WHERE UPPER(cols.table_name) = UPPER('" + tableName + "')"
				+ " AND cons.constraint_type = 'P'"
				+ " AND cons.constraint_name = cols.constraint_name"
				+ " AND CONS.OWNER = COLS.OWNER";
		List<String> columns = jdbcTemplate.query(sql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
		return columns;
	}


	@Override
	public Map<String, List<String>> getPKColumns(List<String> tableNames)
			throws SQLException {
		StringBuffer sb = new StringBuffer();
		for (String name : tableNames) {
			sb.append("'");
			sb.append(name);
			sb.append("',");
		}
		sb.deleteCharAt(sb.length() - 1);

		String sql = "SELECT cols.table_name,cols.column_name"
				+ " FROM USER_CONSTRAINTS CONS, USER_CONS_COLUMNS COLS"
				+ " WHERE UPPER(cols.table_name) in ("
				+ sb.toString().toUpperCase() + ")"
				+ " AND cons.constraint_type = 'P'"
				+ " AND cons.constraint_name = cols.constraint_name"
				+ " AND CONS.OWNER = COLS.OWNER";

		Map<String, List<String>> columnsMap = new HashMap<String, List<String>>();

		List<Map<String, String>> maps = jdbcTemplate.query(sql,
				new RowMapper<Map<String, String>>() {
					@Override
					public Map<String, String> mapRow(ResultSet rs, int rowNum)
							throws SQLException {
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
		StringBuffer sql = new StringBuffer();
		sql.append("select COUNT(1) from user_tables t where t.TABLE_NAME='")
				.append(tableName.toUpperCase()).append("'");
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class) > 0;
	}

	@Override
	public boolean isExsitPartition(String tableName, String partition) {
		String sql="select count(*) from user_tab_partitions where table_name = ? and partition_name = ?";

		String[] args=new String[2];
		args[0]=tableName;
		args[1]="P_" + partition.toUpperCase();
		Integer rtn= jdbcTemplate.queryForObject(sql, args, Integer.class);
		return rtn>0;
	}

	@Override
	public void createPartition(String tableName, String partition) {
		String sql="ALTER TABLE "+tableName+" ADD PARTITION P_"+partition.toUpperCase()+" VALUES ( '"+partition+"') NOCOMPRESS ";
		jdbcTemplate.update(sql);
	}

	@Override
	public boolean supportPartition(String tableName) {
		String sql="select count(*) from user_tab_partitions where table_name = ? ";
		Integer rtn= jdbcTemplate.queryForObject(sql, Integer.class, tableName);
		return rtn>0;
	}

	@Override
	public boolean hasData(String tableName) {
		String sql="select  count(*) from "+tableName+" where rownum =1";
		int rtn= jdbcTemplate.queryForObject(sql,Integer.class);
		return rtn>0;
	}

	@Override
	public void createTableLike(String newTable, String oldTable, String pk) {
		String sql=" create table "+newTable+"  as  select * from "+  oldTable +" where ROWNUM<1 ";
		jdbcTemplate.execute(sql);

		sql=" alter table "+ newTable +" add constraint PK_"+newTable +" primary key (pk) ";
		jdbcTemplate.execute(sql);
	}

	@Override
	public List<String> getCreateTableSql(Table table) {

		List<String> sqlList=new ArrayList<>();

		List<Column> columnList = table.getColumnList();
		// 建表语句
		StringBuffer sb = new StringBuffer();
		// 主键字段
		String pkColumn = null;

		// 例注释
		List<String> columnCommentList = new ArrayList<String>();
		// 建表开始
		sb.append("CREATE TABLE " + table.getTableName() + " (\n");
		for (int i = 0; i < columnList.size(); i++) {
			// 建字段
			Column column = columnList.get(i);
			sb.append("    ").append(column.getFieldName()).append("    ");
			sb.append(getColumnType(column.getColumnType(),
					column.getCharLen(), column.getIntLen(),
					column.getDecimalLen()));
			sb.append(" ");

			// 主键
			if (column.getIsPk()) {
				if (pkColumn == null) {
					pkColumn = column.getFieldName();
				} else {
					pkColumn += "," + column.getFieldName();
				}
			}
			// 添加默认值
			if (StringUtils.isNotEmpty(column.getDefaultValue())) {
				if(Column.COLUMN_TYPE_NUMBER.equals(column.getColumnType())||Column.COLUMN_TYPE_INT.equals(column.getColumnType())){
					sb.append(" DEFAULT " + column.getDefaultValue());
				}else{
					sb.append(" DEFAULT '" + column.getDefaultValue()+"' ");
				}
			}


			// 字段注释
			if (column.getComment() != null && column.getComment().length() > 0) {
				// createTableSql.append(" COMMENT '" + cm.getComment() + "'");
				String comment = "COMMENT ON COLUMN " + table.getTableName() + "."
						+ column.getFieldName() + " IS '" + column.getComment()
						+ "'\n";
				columnCommentList.add(comment);
			}
			sb.append(",\n");
		}

		//逻辑删除
		if(DbLogicDelete.getLogicDelete()){
			sb.append("    DELETED_ NUMBER(10) DEFAULT 0 ");
			sb.append(",\n");
			String comment = "COMMENT ON COLUMN " + table.getTableName() + ".DELETED_ IS '逻辑删除'\n";
			columnCommentList.add(comment);
		}

		// 创建主键
		if (pkColumn != null) {
			sb.append("    CONSTRAINT PK_").append(table.getTableName())
					.append(" PRIMARY KEY (").append(pkColumn).append(")");
		}

		// 建表结束
		sb.append("\n)");

		sqlList.add(sb.toString());

		sqlList.addAll(columnCommentList);

		String tableComment="COMMENT ON TABLE " + table.getTableName()
				+ " IS '" + table.getComment() + "'\n";

		sqlList.add(tableComment);

		return sqlList;
	}

	@Override
	public List<String> getAddColumnSql(String tableName, Column model) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ").append(tableName);
		sb.append(" ADD ");
		sb.append(model.getFieldName()).append(" ");
		sb.append(getColumnType(model.getColumnType(), model.getCharLen(),
				model.getIntLen(), model.getDecimalLen()));

		// 添加默认值
		if (StringUtils.isNotEmpty(model.getDefaultValue())) {
			sb.append(" DEFAULT " + model.getDefaultValue());
		}
		// 添加是否为空
		// if (!model.getIsNull()) {
		// sb.append(" NOT NULL ");
		// }
		sb.append("\n");

		List<String> sqlList=new ArrayList<>();

		sqlList.add(sb.toString());


		if (model.getComment() != null && model.getComment().length() > 0) {
			sqlList.add("COMMENT ON COLUMN " + tableName + "."
					+ model.getFieldName() + " IS '" + model.getComment() + "'");
		}

		return  sqlList;
	}

	@Override
	public List<String> getUpdateColumnSql(String tableName, String columnName, Column column) {

		List<String> sqlList=new ArrayList<>();
		// 修改列名
		if (!columnName.equals(column.getFieldName())) {
			StringBuffer modifyName = new StringBuffer("ALTER TABLE ")
					.append(tableName);
			modifyName.append(" RENAME COLUMN ").append(columnName)
					.append(" TO ").append(column.getFieldName());

			sqlList.add(modifyName.toString());
		}

		// 修改列的大小,此处不修改列的类型,若修改列的类型则在前面部分已抛出异常
		StringBuffer sb = new StringBuffer();
		// alter table test01 modify(test_01 NUMBER(1));
		sb.append("ALTER TABLE ").append(tableName);
		sb.append(" MODIFY(" + column.getFieldName()).append(" ");
		sb.append(getColumnType(column.getColumnType(), column.getCharLen(),
				column.getIntLen(), column.getDecimalLen()));
		if (!column.getIsNull()) {
			sb.append(" NOT NULL ");
		}
		sb.append(")");

		sqlList.add(sb.toString());



		// 修改注释
		if (column.getComment() != null && column.getComment().length() > 0) {
			sqlList.add("COMMENT ON COLUMN " + tableName + "."
					+ column.getFieldName() + " IS'" + column.getComment() + "'");
		}

		return sqlList;
	}

	@Override
	public String getUpdateTableCommentSql(String tableName, String comment) {
		StringBuffer sb = new StringBuffer();
		sb.append("COMMENT ON TABLE ").append(tableName).append(" IS '")
				.append(comment).append("'\n");
		return sb.toString();
	}

	@Override
	public String getAddForeignKeySql(String pkTableName, String fkTableName, String pkField, String fkField) {
		//String shortTableName = fkTableName.replaceFirst("(?im)" +DbUtil.getTablePre() , "");
		String shortTableName = fkTableName.replaceFirst("(?im)"  , "");
		String sql = " ALTER TABLE " + fkTableName + " ADD CONSTRAINT fk_"
				+ shortTableName + " FOREIGN KEY (" + fkField + ") REFERENCES "
				+ pkTableName + " (" + pkField + ") ON DELETE CASCADE";
		return sql;
	}

	@Override
	public String getDropForeignKeySql(String tableName, String keyName) {
		String sql = "ALTER   TABLE   " + tableName + "   DROP   CONSTRAINT  "
				+ keyName;
		return sql;
	}



}
