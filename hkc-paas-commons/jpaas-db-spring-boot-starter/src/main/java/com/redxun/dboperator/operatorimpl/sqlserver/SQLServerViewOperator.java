package com.redxun.dboperator.operatorimpl.sqlserver;

import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.DefaultTable;
import com.redxun.dboperator.model.Table;
import com.redxun.dboperator.operatorimpl.BaseViewOperator;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * SQLServer 视图操作的实现类。
 * 
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 * 
 */
@Component(value="sqlserver_ViewOperator")
@Scope("prototype")
public class SQLServerViewOperator extends BaseViewOperator {

	private final String sqlAllView = "select name from sysobjects where xtype='V'";


	private final String SQL_GET_COLUMNS_BATCH = "SELECT "
			+ "B.NAME TABLE_NAME,A.NAME NAME, C.NAME TYPENAME, A.MAX_LENGTH LENGTH, A.IS_NULLABLE IS_NULLABLE,A.PRECISION PRECISION,A.SCALE SCALE,  "
			+ "(SELECT COUNT(*) FROM SYS.IDENTITY_COLUMNS WHERE SYS.IDENTITY_COLUMNS.OBJECT_ID = A.OBJECT_ID AND A.COLUMN_ID = SYS.IDENTITY_COLUMNS.COLUMN_ID) AS AUTOGEN, "
			+ "(SELECT CAST(VALUE AS VARCHAR) FROM SYS.EXTENDED_PROPERTIES WHERE SYS.EXTENDED_PROPERTIES.MAJOR_ID = A.OBJECT_ID AND SYS.EXTENDED_PROPERTIES.MINOR_ID = A.COLUMN_ID) AS DESCRIPTION, "
			+ " 0 AS IS_PK " + "FROM  "
			+ "SYS.COLUMNS A, SYS.VIEWS B, SYS.TYPES C " + "WHERE  "
			+ "A.OBJECT_ID = B.OBJECT_ID  "
			+ "AND A.SYSTEM_TYPE_ID=C.SYSTEM_TYPE_ID "
			+ "AND C.NAME<>'SYSNAME' ";

	
	@Override
	public void createOrRep(String viewName, String sql) throws Exception {
	
		String sql_drop_view = "IF EXISTS (SELECT * FROM sysobjects WHERE xtype='V'"
				+ " AND name = '" + viewName + "')" + " DROP VIEW " + viewName;

		String viewSql = "CREATE VIEW " + viewName + " AS " + sql;
		jdbcTemplate.execute(sql_drop_view);
		jdbcTemplate.execute(viewSql);
	}

	@Override
	public List<String> getViews(String viewName) throws SQLException {
		String sql = sqlAllView;
		if (StringUtils.isNotEmpty(viewName)) {
			sql += " and name like '" + viewName + "%'";
		}
		return this.jdbcTemplate.queryForList(sql, String.class);
	}

	

	
	@Override
	public List<Table> getViewsByName(String viewName)
			throws Exception {
		String sql = sqlAllView;
		if (StringUtils.isNotEmpty(viewName)) {
			sql += " AND NAME LIKE '" + viewName + "%'";
		}

		RowMapper<Table> rowMapper = new RowMapper<Table>() {
			@Override
			public Table mapRow(ResultSet rs, int row) throws SQLException {
				Table tableModel = new DefaultTable();
				tableModel.setTableName(rs.getString("NAME"));
				tableModel.setComment(tableModel.getTableName());
				return tableModel;
			}
		};
		List<Table> tableModels = jdbcTemplate.query(sql, rowMapper);
		
		setColumns(tableModels);
		
		return tableModels;
	}

	/**
	 * 根据表名获取列。此方法使用批量查询方式。
	 * 
	 * @param tableNames
	 * @return
	 */
	@Override
	protected Map<String, List<Column>> getColumnsByTableName(
			List<String> tableNames) {
		String sql = SQL_GET_COLUMNS_BATCH;
		Map<String, List<Column>> map = new HashMap<String, List<Column>>();
		if (tableNames != null && tableNames.size() == 0)  {
			return map;
		}
		
		StringBuffer buf = new StringBuffer();
		for (String str : tableNames) {
			buf.append("'" + str + "',");
		}
		buf.deleteCharAt(buf.length() - 1);
		sql += " AND B.NAME IN (" + buf.toString() + ") ";
	
		List<Column> columnModels= jdbcTemplate.query(sql, new SQLServerColumnMap());
		
		convertToMap(map,columnModels);
		
		return map;
	}

	
	@Override
	public String getType(String type) {
		type = type.toLowerCase();
		if (type.indexOf("number") > -1){
			return Column.COLUMN_TYPE_NUMBER;
		}
		else if (type.indexOf("date") > -1) {
			return Column.COLUMN_TYPE_DATE;
		} else if (type.indexOf("char") > -1) {
			return Column.COLUMN_TYPE_VARCHAR;
		}
		return Column.COLUMN_TYPE_VARCHAR;
	}

	
	@Override
	public Table getModelByViewName(String viewName) throws Exception {
		return super.getModelByViewName(viewName);
	}

}
