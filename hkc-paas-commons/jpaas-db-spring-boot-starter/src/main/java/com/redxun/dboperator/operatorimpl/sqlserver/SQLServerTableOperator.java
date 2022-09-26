package com.redxun.dboperator.operatorimpl.sqlserver;


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
 * SQLServer 数据库表操作的实现。
 *
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 *
 *
 */
@Component(value="sqlserver_TableOperator")
public class SQLServerTableOperator extends BaseTableOperator {




	@Override
	public void createTable(Table model) throws SQLException {
		List<Column> columnList = model.getColumnList();

		// 建表语句
		StringBuffer createTableSql = new StringBuffer();
		// 主键字段
		String pkColumn = null;

		// 列注释
		List<String> columnCommentList = new ArrayList<String>();
		// 建表开始
		createTableSql.append("CREATE TABLE " + model.getTableName() + " (\n");
		for (int i = 0; i < columnList.size(); i++) {
			// 建字段
			Column cm = columnList.get(i);
			createTableSql.append("    ").append(cm.getFieldName()).append("    ");
			createTableSql.append(getColumnType(cm.getColumnType(),
					cm.getCharLen(), cm.getIntLen(), cm.getDecimalLen()));
			createTableSql.append(" ");

			// alter table Table_2 add a datetime default getdate() not null ;
			if (StringUtils.isNotEmpty(cm.getDefaultValue())) {
				if(Column.COLUMN_TYPE_NUMBER.equals(cm.getColumnType())||Column.COLUMN_TYPE_INT.equals(cm.getColumnType())){
					createTableSql.append(" DEFAULT " + cm.getDefaultValue());
				}else{
					createTableSql.append(" DEFAULT '" + cm.getDefaultValue()+"' ");
				}

			}

			// 非空
			if (!cm.getIsNull()) {
				createTableSql.append(" NOT NULL ");
			}
			// 主键
			if (cm.getIsPk()) {
				if (pkColumn == null) {
					pkColumn = cm.getFieldName();
				} else {
					pkColumn += "," + cm.getFieldName();
				}
			}
			// if (cm.getIsFk()) {
			// fkList.add("    FOREIGN KEY (" + cm.getName() + ") REFERENCES " +
			// cm.getFkRefTable() + "(" + cm.getFkRefColumn() + ")");
			//
			// }
			// 字段注释
			if (cm.getComment() != null && cm.getComment().length() > 0) {
				StringBuffer comment = new StringBuffer(
						"EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'");
				comment.append(cm.getComment())
						.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
						.append(model.getTableName())
						.append("', @level2type=N'COLUMN', @level2name=N'")
						.append(cm.getFieldName()).append("'");
				columnCommentList.add(comment.toString());
			}
			createTableSql.append(",\n");
		}

		//逻辑删除
		if(DbLogicDelete.getLogicDelete()){
			createTableSql.append("    DELETED_ NUMERIC(10) DEFAULT 0 ");
			createTableSql.append(",\n");
		}

		// 建主键
		if (pkColumn != null) {
			createTableSql.append("    CONSTRAINT PK_").append(model.getTableName())
					.append(" PRIMARY KEY (").append(pkColumn).append(")");
		}
		// 建外键
		// for (String fk : fkList) {
		// createTableSql.append(",\n" + fk);
		// }

		// 建表结束
		createTableSql.append("\n)");
		jdbcTemplate.execute(createTableSql.toString());

		// 表注释
		if (model.getComment() != null && model.getComment().length() > 0) {
			StringBuffer tableComment = new StringBuffer(
					"EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'");
			tableComment
					.append(model.getComment())
					.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
					.append(model.getTableName()).append("'");
			jdbcTemplate.execute(tableComment.toString());
		}
		for (String columnComment : columnCommentList) {
			jdbcTemplate.execute(columnComment);
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
			return "VARCHAR(" + charLen + ')';
		} else if (Column.COLUMN_TYPE_NUMBER.equals(columnType)) {
			return "NUMERIC(" + (intLen + decimalLen) + "," + decimalLen + ")";
		} else if (Column.COLUMN_TYPE_DATE.equals(columnType)) {
			return "DATETIME";
		} else if (Column.COLUMN_TYPE_INT.equals(columnType)) {
			return "NUMERIC(" + intLen + ")";
		} else if (Column.COLUMN_TYPE_CLOB.equals(columnType)) {
			return "TEXT";
		} else {
			return "";
		}
	}

	@Override
	public String getColumnType(String columnType, int charLen, int intLen, int decimalLen, String dbFieldType) {
		if (Column.COLUMN_TYPE_VARCHAR.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() + "(" + charLen + ')' : "VARCHAR(" + charLen + ')';
		} else if (Column.COLUMN_TYPE_NUMBER.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() + "(" + (intLen + decimalLen) + "," + decimalLen + ")" : "NUMERIC(" + (intLen + decimalLen) + "," + decimalLen + ")";
		} else if (Column.COLUMN_TYPE_DATE.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() : "DATETIME";
		} else if (Column.COLUMN_TYPE_INT.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() + "(" + intLen + ')' : "NUMERIC(" + intLen + ")";
		} else if (Column.COLUMN_TYPE_CLOB.equals(columnType)) {
			return StringUtils.isNotEmpty(dbFieldType) ? dbFieldType.toUpperCase() : "TEXT";
		} else {
			return "";
		}
	}


	@Override
	public void dropTable(String tableName) throws SQLException {
		String sql = "IF OBJECT_ID(N'" + tableName
				+ "', N'U') IS NOT NULL  DROP TABLE " + tableName;
		jdbcTemplate.execute(sql);
	}

	@Override
	public void updateTableComment(String tableName, String comment)
			throws SQLException {
		// 假如不存在表的注释 ,会抛出异常
		StringBuffer commentSql = new StringBuffer(
				"EXEC sys.sp_updateextendedproperty @name=N'MS_Description', @value=N'");
		commentSql
				.append(comment)
				.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
				.append(tableName).append("'");

		jdbcTemplate.execute(commentSql.toString());

	}


	@Override
	public void addColumn(String tableName, Column model) throws SQLException {
		StringBuffer alterSql = new StringBuffer();
		alterSql.append("ALTER TABLE ").append(tableName);
		alterSql.append(" ADD ");
		alterSql.append(model.getFieldName()).append(" ");
		alterSql.append(getColumnType(model.getColumnType(),
				model.getCharLen(), model.getIntLen(), model.getDecimalLen()));
		// 设置默认值
		if (StringUtils.isNotEmpty(model.getDefaultValue())) {
			alterSql.append(" DEFAULT " + model.getDefaultValue());
		}

		// if (!model.getIsNull()) {
		// alterSql.append(" NOT NULL ");
		// }
		alterSql.append("\n");
		jdbcTemplate.execute(alterSql.toString());
		if (model.getComment() != null && model.getComment().length() > 0) {
			StringBuffer comment = new StringBuffer(
					"EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'");
			comment.append(model.getComment())
					.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
					.append(tableName)
					.append("', @level2type=N'COLUMN', @level2name=N'")
					.append(model.getFieldName()).append("'");
			jdbcTemplate.execute(comment.toString());
		}
	}


	@Override
	public void updateColumn(String tableName, String columnName, Column model)
			throws SQLException {
		List<String> sqlList=new ArrayList<>();
		for(String sql:sqlList){
			jdbcTemplate.execute(sql);
		}

	}


	@Override
	public void addForeignKey(String pkTableName, String fkTableName,
							  String pkField, String fkField) {
		//String shortTableName = fkTableName.replaceFirst("(?im)"+ DbUtil.getTablePre(), "");
		String shortTableName = fkTableName.replaceFirst("(?im)", "");
		String sql = "  ALTER TABLE " + fkTableName + " ADD CONSTRAINT fk_"
				+ shortTableName + " FOREIGN KEY (" + fkField + ") REFERENCES "
				+ pkTableName + " (" + pkField + ")   ON DELETE CASCADE";
		jdbcTemplate.execute(sql);

	}


	@Override
	public void dropForeignKey(String tableName, String keyName) {
		String sql = getDropForeignKeySql(tableName,keyName);
		jdbcTemplate.execute(sql);
	}


	@Override
	public List<String> getPKColumns(String tableName) throws SQLException {
		String sql = "SELECT C.COLUMN_NAME COLUMN_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS PK ,INFORMATION_SCHEMA.KEY_COLUMN_USAGE C "
				+ "WHERE 	PK.TABLE_NAME = '%S' "
				+ "AND	CONSTRAINT_TYPE = 'PRIMARY KEY' "
				+ "AND	C.TABLE_NAME = PK.TABLE_NAME "
				+ "AND	C.CONSTRAINT_NAME = PK.CONSTRAINT_NAME ";
		sql = String.format(sql, tableName);

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
	public Map<String, List<String>> getPKColumns(List<String> tableNames)
			throws SQLException {
		StringBuffer sb = new StringBuffer();
		for (String name : tableNames) {
			sb.append("'");
			sb.append(name);
			sb.append("',");
		}
		sb.deleteCharAt(sb.length() - 1);

		String sql = "SELECT C.COLUMN_NAME COLUMN_NAME FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS PK ,INFORMATION_SCHEMA.KEY_COLUMN_USAGE C "
				+ "WHERE 	PK.TABLE_NAME in( %S )"
				+ "AND	CONSTRAINT_TYPE = 'PRIMARY KEY' "
				+ "AND	C.TABLE_NAME = PK.TABLE_NAME "
				+ "AND	C.CONSTRAINT_NAME = PK.CONSTRAINT_NAME ";
		sql = String.format(sql, sb.toString());

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
		String sql = "select count(1) from sysobjects where name='"
				+ tableName.toUpperCase() + "'";
		return jdbcTemplate.queryForObject(sql, Integer.class) > 0;

	}

	@Override
	public boolean isExsitPartition(String tableName, String partition) {
		return false;
	}

	@Override
	public void createPartition(String tableName, String partition) {

	}


	@Override
	public boolean supportPartition(String tableName) {
		return false;
	}

	@Override
	public void createTableLike(String newTable, String oldTable, String pk) {

		String sql="select * into "+newTable+" from "+oldTable +" where 1=2 ";
		jdbcTemplate.execute(sql);

		String sqlPk="alter table "+newTable+" add constraint PK_"+newTable+" primary key("+pk+")";
		jdbcTemplate.execute(sqlPk);
	}

	@Override
	public List<String> getCreateTableSql(Table model) {


		List<String> sqlList=new ArrayList<>();

		List<Column> columnList = model.getColumnList();

		// 建表语句
		StringBuffer createTableSql = new StringBuffer();
		// 主键字段
		String pkColumn = null;

		// 列注释
		List<String> columnCommentList = new ArrayList<String>();
		// 建表开始
		createTableSql.append("CREATE TABLE " + model.getTableName() + " (\n");
		for (int i = 0; i < columnList.size(); i++) {
			// 建字段
			Column cm = columnList.get(i);
			createTableSql.append("    ").append(cm.getFieldName()).append("    ");
			createTableSql.append(getColumnType(cm.getColumnType(),
					cm.getCharLen(), cm.getIntLen(), cm.getDecimalLen()));
			createTableSql.append(" ");

			// alter table Table_2 add a datetime default getdate() not null ;
			if (StringUtils.isNotEmpty(cm.getDefaultValue())) {
				if(Column.COLUMN_TYPE_NUMBER.equals(cm.getColumnType())||Column.COLUMN_TYPE_INT.equals(cm.getColumnType())){
					createTableSql.append(" DEFAULT " + cm.getDefaultValue());
				}else{
					createTableSql.append(" DEFAULT '" + cm.getDefaultValue()+"' ");
				}

			}

			// 非空
			if (!cm.getIsNull()) {
				createTableSql.append(" NOT NULL ");
			}
			// 主键
			if (cm.getIsPk()) {
				if (pkColumn == null) {
					pkColumn = cm.getFieldName();
				} else {
					pkColumn += "," + cm.getFieldName();
				}
			}
			// if (cm.getIsFk()) {
			// fkList.add("    FOREIGN KEY (" + cm.getName() + ") REFERENCES " +
			// cm.getFkRefTable() + "(" + cm.getFkRefColumn() + ")");
			//
			// }
			// 字段注释
			if (cm.getComment() != null && cm.getComment().length() > 0) {
				StringBuffer comment = new StringBuffer(
						"EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'");
				comment.append(cm.getComment())
						.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
						.append(model.getTableName())
						.append("', @level2type=N'COLUMN', @level2name=N'")
						.append(cm.getFieldName()).append("'");
				columnCommentList.add(comment.toString());
			}
			createTableSql.append(",\n");
		}
		// 建主键
		if (pkColumn != null) {
			createTableSql.append("    CONSTRAINT PK_").append(model.getTableName())
					.append(" PRIMARY KEY (").append(pkColumn).append(")");
		}
		// 建外键
		// for (String fk : fkList) {
		// createTableSql.append(",\n" + fk);
		// }

		// 建表结束
		createTableSql.append("\n)");

		sqlList.add(createTableSql.toString());

		sqlList.addAll(columnCommentList);

		// 表注释
		if (model.getComment() != null && model.getComment().length() > 0) {
			StringBuffer tableComment = new StringBuffer(
					"EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'");
			tableComment
					.append(model.getComment())
					.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
					.append(model.getTableName()).append("'");

			sqlList.add(tableComment.toString());

		}


		return sqlList;
	}

	@Override
	public List<String> getAddColumnSql(String tableName, Column model) {

		List<String> sqlList=new ArrayList<>();

		StringBuffer alterSql = new StringBuffer();
		alterSql.append("ALTER TABLE ").append(tableName);
		alterSql.append(" ADD ");
		alterSql.append(model.getFieldName()).append(" ");
		alterSql.append(getColumnType(model.getColumnType(),
				model.getCharLen(), model.getIntLen(), model.getDecimalLen()));
		// 设置默认值
		if (StringUtils.isNotEmpty(model.getDefaultValue())) {
			alterSql.append(" DEFAULT " + model.getDefaultValue());
		}

		// if (!model.getIsNull()) {
		// alterSql.append(" NOT NULL ");
		// }
		alterSql.append("\n");
		sqlList.add(alterSql.toString());

		if (model.getComment() != null && model.getComment().length() > 0) {
			StringBuffer comment = new StringBuffer(
					"EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'");
			comment.append(model.getComment())
					.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
					.append(tableName)
					.append("', @level2type=N'COLUMN', @level2name=N'")
					.append(model.getFieldName()).append("'");
			sqlList.add(comment.toString());
		}

		return sqlList;
	}

	@Override
	public List< String> getUpdateColumnSql(String tableName, String columnName, Column model) {

		List<String> sqlList=new ArrayList<>();
		// 修改列名
		if (!columnName.equals(model.getFieldName())) {
			StringBuffer modifyName = new StringBuffer("EXEC sp_rename '");
			modifyName.append(tableName).append(".[").append(columnName)
					.append("]','").append(model.getFieldName())
					.append("', 'COLUMN'");
			sqlList.add(modifyName.toString());
		}

		// 修改列的大小,此处不修改列的类型,若修改列的类型则在前面部分已抛出异常
		StringBuffer alterSql = new StringBuffer();
		alterSql.append("ALTER TABLE ").append(tableName);
		alterSql.append(" ALTER COLUMN " + model.getFieldName()).append(" ");
		alterSql.append(getColumnType(model.getColumnType(),
				model.getCharLen(), model.getIntLen(), model.getDecimalLen()));
		if (!model.getIsNull()) {
			alterSql.append(" NOT NULL ");
		}

		sqlList.add(alterSql.toString());

		// 修改注释
		if (model.getComment() != null && model.getComment().length() > 0) {
			// 更新字段注释假如不存在该列的注释 ,会抛出异常
			StringBuffer comment = new StringBuffer(
					"EXEC sys.sp_updateextendedproperty @name=N'MS_Description', @value=N'");
			comment.append(model.getComment())
					.append("' ,@level0type=N'SCHEMA', @level0name=N'dbo', @level1type=N'TABLE', @level1name=N'")
					.append(tableName)
					.append("', @level2type=N'COLUMN', @level2name=N'")
					.append(model.getFieldName()).append("'");
			sqlList.add(comment.toString());
		}

		return sqlList;
	}

	@Override
	public String getUpdateTableCommentSql(String tableName, String comment) {
		return null;
	}

	@Override
	public String getAddForeignKeySql(String pkTableName, String fkTableName, String pkField, String fkField) {
//		String shortTableName = fkTableName.replaceFirst("(?im)"+ DbUtil.getTablePre(), "");
		String shortTableName = fkTableName.replaceFirst("(?im)", "");
		String sql = "  ALTER TABLE " + fkTableName + " ADD CONSTRAINT fk_"
				+ shortTableName + " FOREIGN KEY (" + fkField + ") REFERENCES "
				+ pkTableName + " (" + pkField + ")   ON DELETE CASCADE";
		return sql;
	}

	@Override
	public String getDropForeignKeySql(String tableName, String keyName) {
		String sql = "ALTER   TABLE   " + tableName + "   DROP   CONSTRAINT  "
				+ keyName;
		return sql;
	}


}
