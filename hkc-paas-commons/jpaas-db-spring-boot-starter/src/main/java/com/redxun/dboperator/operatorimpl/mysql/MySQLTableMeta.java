package com.redxun.dboperator.operatorimpl.mysql;

import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.DefaultTable;
import com.redxun.dboperator.model.Table;
import com.redxun.dboperator.operatorimpl.BaseTableMeta;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL 表元数据的实现类
 * 
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 * 
 */
@Component(value="mysql_TableMeta")
@Scope("prototype")
public class MySQLTableMeta extends BaseTableMeta {

	private final String SQL_GET_COLUMNS = "SELECT"
			+ " TABLE_NAME,COLUMN_NAME,IS_NULLABLE,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH LENGTH,"
			+ " NUMERIC_PRECISION PRECISIONS,NUMERIC_SCALE SCALE,COLUMN_KEY,COLUMN_COMMENT "
			+ " FROM " + " INFORMATION_SCHEMA.COLUMNS "
			+ " WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='%s' ";

	private final String SQL_GET_COLUMNS_BATCH = "SELECT"
			+ " TABLE_NAME,COLUMN_NAME,IS_NULLABLE,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH LENGTH,"
			+ " NUMERIC_PRECISION PRECISIONS,NUMERIC_SCALE SCALE,COLUMN_KEY,COLUMN_COMMENT "
			+ " FROM " + " INFORMATION_SCHEMA.COLUMNS "
			+ " WHERE TABLE_SCHEMA=DATABASE() ";

	private final String sqlComment = "select table_name,table_comment  from information_schema.tables t where t.table_schema=DATABASE() and table_name='%s' ";

	private final String sqlAllTable = "select table_name,table_comment from information_schema.tables t where t.table_type='BASE TABLE' AND t.table_schema=DATABASE()";



	@Override
	public Table getTableByName(String tableName) {
		Table model = getTableModel(tableName);
		// 获取列对象
		List<Column> columnList = getColumnsByTableName(tableName);
		model.setColumnList(columnList);
		return model;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, String> getTablesByName(String tableName) {
		String sql = sqlAllTable;
		if (StringUtils.isNotEmpty(tableName)) {
			sql += " AND TABLE_NAME LIKE '%" + tableName + "%'";
		}
		List list = jdbcTemplate.query(sql, 
				new RowMapper<Map<String, String>>() {
					@Override
					public Map<String, String> mapRow(ResultSet rs, int row)
							throws SQLException {
						String tableName = rs.getString("table_name");
						String comments = rs.getString("table_comment");
						Map<String, String> map = new HashMap<String, String>();
						map.put("name", tableName);
						map.put("comments", comments);
						return map;
					}
				});
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> tmp = (Map<String, String>) list.get(i);
			String name = tmp.get("name");
			String comments = tmp.get("comments");
			comments = getComments(comments, name);
			map.put(name, comments);
		}

		return map;
	}

	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, String> getTablesByName(List<String> names) {
		StringBuffer sb = new StringBuffer();
		for (String name : names) {
			sb.append("'");
			sb.append(name);
			sb.append("',");
		}
		sb.deleteCharAt(sb.length() - 1);
		String sql = sqlAllTable + " and  lower(table_name) in (" + sb.toString().toLowerCase() + ")";

		List list = jdbcTemplate.query(sql,
				new RowMapper<Map<String, String>>() {
					@Override
					public Map<String, String> mapRow(ResultSet rs, int row)
							throws SQLException {
						String tableName = rs.getString("table_name");
						String comments = rs.getString("table_comment");
						Map<String, String> map = new HashMap<String, String>();
						map.put("tableName", tableName);
						map.put("tableComment", comments);
						return map;
					}
				});
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> tmp = (Map<String, String>) list.get(i);
			String name = tmp.get("tableName");
			String comments = tmp.get("tableComment");
			map.put(name, comments);
		}

		return map;
	}

	/**
	 * 根据表名获取tableModel。
	 * 
	 * @param tableName
	 * @return
	 */
	private Table getTableModel(final String tableName) {
		
		String sql = String.format(sqlComment, tableName);
		
		Table table = jdbcTemplate.queryForObject(sql,
				new RowMapper<Table>() {
					@Override
					public Table mapRow(ResultSet rs, int row)
							throws SQLException {
						Table table = new DefaultTable();
						String comments = rs.getString("table_comment");
						comments = getComments(comments, tableName);
						table.setTableName(tableName);
						table.setComment(comments);
						return table;
					}
				});
		if (BeanUtil.isEmpty(table)) {
			table = new DefaultTable();
		}

		return table;
	}

	/**
	 * 根据表名获取列
	 * 
	 * @param tableName
	 * @return
	 */
	private List<Column> getColumnsByTableName(String tableName) {
		String sql = String.format(SQL_GET_COLUMNS, tableName);
		// sqlColumns语句的column_key包含了column是否为primary
		// key，并在MySqlColumnMap中进行了映射。
		List<Column> list = jdbcTemplate.query(sql,new MySQLColumnMap());
		for (Column model : list) {
			model.setTableName(tableName);
		}
		return list;
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
		if (tableNames != null && tableNames.size() == 0) {
			return map;
		}

		StringBuffer buf = new StringBuffer();
		for (String str : tableNames) {
			buf.append("'" + str + "',");
		}
		buf.deleteCharAt(buf.length() - 1);
		sql += " AND TABLE_NAME IN (" + buf.toString() + ") ";
			
		List<Column> columns = jdbcTemplate.query(sql,new MySQLColumnMap());
		
		convertToMap(map, columns);
		
		return map;
	}

	/**
	 * 获取注释
	 * 
	 * @param comments
	 * @param defaultValue
	 * @return
	 */
	public static String getComments(String comments, String defaultValue) {
		if (StringUtils.isEmpty(comments)) {
			return defaultValue;
		}
		int idx = comments.indexOf("InnoDB free");
		if (idx > -1)  {
			comments = StringUtils.remove(comments.substring(0, idx).trim(), ";");
		}
		if (StringUtils.isEmpty(comments)) {
			comments = defaultValue;
		}
		return comments;
	}

	@Override
	public List<Table> getTableModelByName(String tableName) throws Exception {
		String sql = sqlAllTable;
		if (StringUtils.isNotEmpty(tableName))
			sql += " AND TABLE_NAME LIKE '%" + tableName + "%'";
		RowMapper<Table> rowMapper = new RowMapper<Table>() {
			@Override
			public Table mapRow(ResultSet rs, int row) throws SQLException {
				Table table = new DefaultTable();
				table.setTableName(rs.getString("TABLE_NAME"));
				String comments = rs.getString("TABLE_COMMENT");
				comments = getComments(comments, table.getTableName());
				table.setComment(comments);
				return table;
			}
		};
		List<Table> tables = jdbcTemplate.query (sql,  rowMapper);
		
		setComlumns(tables);
		
		return tables;
	}
}
