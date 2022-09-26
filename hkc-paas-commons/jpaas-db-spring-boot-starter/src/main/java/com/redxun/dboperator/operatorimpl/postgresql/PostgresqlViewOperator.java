package com.redxun.dboperator.operatorimpl.postgresql;

import com.redxun.common.tool.StringUtils;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.DefaultTable;
import com.redxun.dboperator.model.Table;
import com.redxun.dboperator.operatorimpl.BaseViewOperator;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value="postgresql_ViewOperator")
@Scope("prototype")
public class PostgresqlViewOperator extends BaseViewOperator {


    // private static final String
    private static final String sqlAllView = "SELECT TABLE_NAME FROM information_schema.tables WHERE TABLE_TYPE LIKE 'VIEW'";

    private final String SQL_GET_COLUMNS = "select table_name  ,column_name as COLUMN_NAME, is_nullable as IS_NULLABLE, data_type as DATA_TYPE," +
            "coalesce(character_maximum_length) as LENGTH,numeric_precision as PRECISIONS,numeric_scale as SCALE," +
            "case when b.pk_name is null then b.pk_name else 'PRI' end as COLUMN_KEY,c.DeText as COLUMN_COMMENT" +
            "from information_schema.columns" +
            "left join (" +
            "    select pg_attr.attname as colname,pg_constraint.conname as pk_name from pg_constraint  " +
            "    inner join pg_class on pg_constraint.conrelid = pg_class.oid " +
            "    inner join pg_attribute pg_attr on pg_attr.attrelid = pg_class.oid and  pg_attr.attnum = pg_constraint.conkey[1] " +
            "    inner join pg_type on pg_type.oid = pg_attr.atttypid" +
            "    where pg_class.relname = '%s' and pg_constraint.contype='p' " +
            ") b on b.colname = information_schema.columns.column_name" +
            "left join (" +
            "    select attname,description as DeText from pg_class" +
            "    left join pg_attribute pg_attr on pg_attr.attrelid= pg_class.oid" +
            "    left join pg_description pg_desc on pg_desc.objoid = pg_attr.attrelid and pg_desc.objsubid=pg_attr.attnum" +
            "    where pg_attr.attnum>0 and pg_attr.attrelid=pg_class.oid and pg_class.relname='%s'" +
            ")c on c.attname = information_schema.columns.column_name" +
            "where table_schema='public' and table_name='%s' order by ordinal_position asc ";




    @Override
    public void createOrRep(String viewName, String sql) throws Exception {
        String getSql = "CREATE OR REPLACE VIEW " + viewName + " as (" + sql + ")";
        jdbcTemplate.execute(getSql);
    }


    @Override
    public List<String> getViews(String viewName) throws SQLException {
        String sql = "select * from  pg_views WHERE schemaname ='public'";
        if (StringUtils.isNotEmpty(viewName)) {
            sql += " AND viewname LIKE '" + viewName + "%'";
        }
        List<String> list = new ArrayList<String>();
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        for (Map<String, Object> line : results) {
            list.add(line.get("viewname").toString());
        }

        return list;
    }




    @Override
    public List<Table> getViewsByName(String viewName)
            throws Exception {
        String sql = sqlAllView;
        if (StringUtils.isNotEmpty(viewName)) {
            sql += " AND TABLE_NAME LIKE '" + viewName + "%'";
        }

        RowMapper<Table> rowMapper = new RowMapper<Table>() {
            @Override
            public Table mapRow(ResultSet rs, int row) throws SQLException {
                Table table = new DefaultTable();
                table.setTableName(rs.getString("table_name"));
                table.setComment(table.getTableName());
                return table;
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
        Map<String, List<Column>> map = new HashMap<String, List<Column>>();
        if (tableNames != null && tableNames.size() == 0) {
            return map;
        }
        List<Column> columns = new ArrayList<>();
        for (String tableName : tableNames) {
            String sql = String.format(SQL_GET_COLUMNS, tableName);
            columns.addAll(jdbcTemplate.query(sql, new PostgresqlColumnMap()));
        }
        convertToMap(map, columns);

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
}
