package com.redxun.dboperator.operatorimpl;

import com.redxun.dboperator.ITableMeta;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.Table;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 数据表元数据抽象类。 用于读取数据库表的元数据信息。 1.查询数据库中的表列表。 2.取得表的详细数据。
 * 
 * <pre>
 * 作者：redxun
 * 版权：广州红迅软件有限公司版权所有
 * </pre>
 * 
 */
public abstract class BaseTableMeta  implements ITableMeta {

	protected JdbcTemplate jdbcTemplate;

	@Override
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;
	}

	protected abstract Map<String, List<Column>> getColumnsByTableName(List<String> tableNames);
	
	protected void setComlumns(List<Table> tableModels){
		List<String> tableNames = new ArrayList<String>();
		// get all table names
		for (Table table : tableModels) {
			tableNames.add(table.getTableName());
		}
		// batch get table columns
		Map<String, List<Column>> tableColumnsMap = getColumnsByTableName(tableNames);
		// extract table columns from paraTypeMap by table name;
		for (Entry<String, List<Column>> entry : tableColumnsMap.entrySet()) {
			// set TableModel's columns
			for (Table table : tableModels) {
				if (table.getTableName().equalsIgnoreCase(entry.getKey())) {
					table.setColumnList(entry.getValue());
				}
			}
		}
	}
	
	
	protected void convertToMap(Map<String, List<Column>> map,List<Column> columnModels){
		for (Column columnModel : columnModels) {
			String tableName = columnModel.getTableName();
			if (map.containsKey(tableName)) {
				map.get(tableName).add(columnModel);
			} else {
				List<Column> cols = new ArrayList<Column>();
				cols.add(columnModel);
				map.put(tableName, cols);
			}
		}
	}
}
