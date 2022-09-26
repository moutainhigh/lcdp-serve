package com.redxun.dboperator.operatorimpl.postgresql;

import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.DefaultTable;
import com.redxun.dboperator.model.Table;
import com.redxun.dboperator.operatorimpl.BaseTableMeta;
import com.redxun.dboperator.operatorimpl.mysql.MySQLColumnMap;
import org.checkerframework.checker.units.qual.A;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component(value="postgresql_TableMeta")
public class PostgresqlTableMeta extends BaseTableMeta {

    private final String SQL_GET_COLUMNS = "select table_name  ,column_name as COLUMN_NAME, is_nullable as IS_NULLABLE, data_type as DATA_TYPE," +
            "coalesce(character_maximum_length) as LENGTH,numeric_precision as PRECISIONS,numeric_scale as SCALE," +
            "case when b.pk_name is null then b.pk_name else 'PRI' end as COLUMN_KEY,c.DeText as COLUMN_COMMENT" +
            " from information_schema.columns" +
            " left join (" +
            "    select pg_attr.attname as colname,pg_constraint.conname as pk_name from pg_constraint  " +
            "    inner join pg_class on pg_constraint.conrelid = pg_class.oid " +
            "    inner join pg_attribute pg_attr on pg_attr.attrelid = pg_class.oid and  pg_attr.attnum = pg_constraint.conkey[1] " +
            "    inner join pg_type on pg_type.oid = pg_attr.atttypid" +
            "    where pg_class.relname = '%s' and pg_constraint.contype='p' " +
            ") b on b.colname = information_schema.columns.column_name" +
            " left join (" +
            "    select attname,description as DeText from pg_class" +
            "    left join pg_attribute pg_attr on pg_attr.attrelid= pg_class.oid" +
            "    left join pg_description pg_desc on pg_desc.objoid = pg_attr.attrelid and pg_desc.objsubid=pg_attr.attnum" +
            "    where pg_attr.attnum>0 and pg_attr.attrelid=pg_class.oid and pg_class.relname='%s'" +
            ")c on c.attname = information_schema.columns.column_name" +
            " where table_schema='public' and table_name='%s' order by ordinal_position asc ";


    private final String sqlComment = "select relname as table_name,cast(obj_description(relfilenode,'pg_class') as varchar) as table_comment from pg_class c where  relkind = 'r' and relname not like 'pg_%' and relname = ";

    private final String sqlAllTable = "select relname as table_name,cast(obj_description(relfilenode,'pg_class') as varchar) as table_comment from pg_class c where  relkind = 'r' and relname not like 'pg_%' ";



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
            sql += " AND relname LIKE '%" + tableName + "%'";
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
        String sql = sqlAllTable + " and  lower(relname) in (" + sb.toString().toLowerCase() + ")";

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
     * @param tableName 1
     * @return
     */
    private Table getTableModel(final String tableName) {
        String name  = tableName.toLowerCase();
        String sql = sqlComment+"'"+name+"'";

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

//    public static void main(String[] args) {
//        String str="a %s b %s";
//        String o= String.format(str, "aa","aa");
//        System.err.println(o);
//    }

    /**
     * 根据表名获取列
     *
     * @param tableName 1
     * @return
     */
    private List<Column> getColumnsByTableName(String tableName) {
        String name= tableName.toLowerCase();
        String sql = String.format(SQL_GET_COLUMNS, name,name,name);
        // sqlColumns语句的column_key包含了column是否为primary
        // key，并在MySqlColumnMap中进行了映射。
        List<Column> list = jdbcTemplate.query(sql,new PostgresqlColumnMap());
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
        Map<String, List<Column>> map = new HashMap<String, List<Column>>();
        if (tableNames != null && tableNames.size() == 0) {
            return map;
        }
        List<Column> columns = new ArrayList<>();
        for (String tableName : tableNames) {
            String name= tableName.toLowerCase();
            String sql = String.format(SQL_GET_COLUMNS, name,name,name);
            columns.addAll(jdbcTemplate.query(sql, new PostgresqlColumnMap()));
        }
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
            sql += " AND relname LIKE '%" + tableName + "%'";
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
