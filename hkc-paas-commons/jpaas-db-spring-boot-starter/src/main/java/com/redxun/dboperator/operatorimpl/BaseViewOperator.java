package com.redxun.dboperator.operatorimpl;

import com.redxun.dboperator.IViewOperator;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.DefaultColumn;
import com.redxun.dboperator.model.DefaultTable;
import com.redxun.dboperator.model.Table;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public abstract class BaseViewOperator implements IViewOperator {
    @Resource
    protected JdbcTemplate jdbcTemplate;

    @Override
    public void setJdbcTemplate(JdbcTemplate template){
        this.jdbcTemplate=template;
    }

    /**
     * 获取数据类型
     * @param type
     * @return
     */
    public abstract String getType(String type);

    @Override
    public Table getModelByViewName(String viewName) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;

        Table table = new DefaultTable();
        table.setTableName(viewName);
        table.setComment(viewName);
        Connection conn = null;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from "+viewName);
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            //从第二条记录开始
            for(int i=1;i<=count;i++){
                Column column = new DefaultColumn();
                String columnName = metaData.getColumnName(i);
                String typeName = metaData.getColumnTypeName(i);
                String dataType = getType(typeName);
                column.setFieldName(columnName);
                column.setColumnType(dataType);
                column.setComment(columnName);
                table.addColumn(column);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(rs != null){
                rs.close();
            }
            if(stmt != null){
                stmt.close();
            }
            JdbcUtils.closeConnection(conn);
        }
        return table;
    }

    protected  abstract Map<String, List<Column>> getColumnsByTableName(List<String> tableNames);

    protected void setColumns(List<Table> tableModels){
        List<String> tableNames = new ArrayList<>();
        // 获取所有表名
        for(Table table : tableModels){
            tableNames.add(table.getTableName());
        }
        // 填充列集合
        Map<String,List<Column>> tableColumnsMap = getColumnsByTableName(tableNames);
        for(Entry<String,List<Column>> entry : tableColumnsMap.entrySet()){
            for(Table table : tableModels){
                if(table.getTableName().equalsIgnoreCase(entry.getKey())){
                    table.setColumnList(entry.getValue());
                }
            }
        }
    }

    protected  void convertToMap(Map<String,List<Column>> map,List<Column> columnModels){
        for(Column column : columnModels){
            String tableName = column.getTableName();
            if(map.containsKey(tableName)){
                map.get(tableName).add(column);
            }else{
                List<Column> cols = new ArrayList<>();
                cols.add(column);
                map.put(tableName,cols);
            }
        }
    }
}
