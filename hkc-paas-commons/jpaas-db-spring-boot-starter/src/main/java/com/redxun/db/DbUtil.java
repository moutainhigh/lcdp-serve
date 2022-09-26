package com.redxun.db;

import com.redxun.common.base.entity.GridHeader;
import com.redxun.common.utils.SpringUtil;
import com.redxun.datasource.DataSourceContextHolder;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.MySQLCodec.Mode;
import com.redxun.dboperator.ITableMeta;
import com.redxun.dboperator.TableMetaContext;
import com.redxun.dboperator.model.Column;
import com.redxun.dboperator.model.Table;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.sqlparser.util.JdbcConstants;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.codecs.Codec;
import org.owasp.esapi.codecs.OracleCodec;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbUtil {

    private static OracleCodec ORACLE_CODEC=new OracleCodec();
    private static MySQLCodec MYSQL_CODEC=new MySQLCodec(Mode.STANDARD);
    private static SqlServerCodec SQLSERVER_CODEC=new SqlServerCodec();

    /**
    * @Description: 根据数据源的别名，获取数据库的类型
    * @param dsAlias 数据源别名
    * @return java.lang.String 数据库的类型，比如: oracle
    * @Author: Elwin ZHANG  @Date: 2022/1/7 17:13
    **/
    public static String getDbType(String dsAlias){
        String dbType="";
        try{
            DataSource dataSource = DataSourceUtil.getDataSourcesByAlias(dsAlias);
            DataSourceProxy dsProxy = (DataSourceProxy)dataSource;
            dbType=dsProxy.getDbType();
        }catch (Exception e){
        }finally {
            return dbType;
        }
    }

    /**
    * @Description:  根据数据源中的数据库类型，处理数据库的转义字符
    * @param value 原文本
     * @param dsAlias 数据源别名
    * @return java.lang.String 处理转义字符后的文本
    * @Author: Elwin ZHANG  @Date: 2021/8/6 17:26
    **/
    public static String handleEscapeChar(String value,String dsAlias){
        if(value==null){
            return "";
        }
        String result=value;
        String dbType=getDbType(dsAlias);
        //mysql
        if(JdbcConstants.MYSQL.equals(dbType) || JdbcConstants.MARIADB.equals(dbType)){
            ////处理单引号 和 反斜杠
            result=value.replace("'","''");
            return result.replace("\\","\\\\");
        }
        //oracle
        if(JdbcConstants.ORACLE.equals(dbType)){
            return handleOracleString(value);
        }
        //其他数据库
        return value.replace("'","''");
    }

    /**
    * @Description: 处理Oracle 数据库字符串，转义及clob超长时拆开
    * @param value 原值
    * @return java.lang.String 处理后的字符串
    * @Author: Elwin ZHANG  @Date: 2022/1/7 17:30
    **/
    public  static  String handleOracleString(String value ){
        int MAX_LEN=1900;   //最大字符串长度
        if(value.length()<=MAX_LEN){
            return handleOracleEscapeChar(value);
        }
        String result="";
        String lastStr=value;
        //拆成短字符拼接
        while (lastStr.length()>MAX_LEN){
            String curStr=lastStr.substring(0,MAX_LEN);
            lastStr =lastStr.substring(MAX_LEN);
            String escapeStr="to_clob("  + handleOracleEscapeChar(curStr) + ")";
            if(result.length()==0){
                result=escapeStr;
            }else {
                result =result + " || " + escapeStr;
            }
        }
        if(lastStr.length()>0){
            String escapeStr="to_clob("  + handleOracleEscapeChar(lastStr) + ")";
            if(result.length()==0){
                result=escapeStr;
            }else {
                result =result + " || " + escapeStr;
            }
        }
        return  result;
    }

    /**
    * @Description: 处理Oracle的转义字符
    * @param source 源字符串
    * @Author: Elwin ZHANG  @Date: 2022/1/7 18:09
    **/
    private static String handleOracleEscapeChar(String source){
        String result=source.replace("'","''").replace("&","' || chr(38) || '");
        return "'" + result +"'";
    }

    /**
     * 对SQL进行编码
     * @param sql
     * @return
     */
    public static String encodeSql(String sql){
        String type=DataSourceUtil.getCurrentDbType();
        Codec codec=null;
        if(SQLConst.DB_MYSQL.equals(type)){
            codec=MYSQL_CODEC;
        }else if(SQLConst.DB_ORACLE.equals(type)){
            codec=ORACLE_CODEC;
        }else if(SQLConst.DB_SQLSERVER.equals(type)){
            codec=SQLSERVER_CODEC;
        }else if(SQLConst.DB_DM.equals(type)){
            codec=ORACLE_CODEC;
        }else if(SQLConst.DB_POSTGRESQL.equals(type)){
            codec=MYSQL_CODEC;
        }

        sql = ESAPI.encoder().encodeForSQL(codec,sql);
        return sql;
    }

    /**
     * 自定义列表设计，大数据量情况下，根据不同数据库处理分页问题。
     * @param sql
     * @return
     */
    public static String preHandleSql(String sql){
        String type=DataSourceUtil.getCurrentDbType();
        if(SQLConst.DB_MYSQL.equals(type)||SQLConst.DB_POSTGRESQL.equals(type)){
            sql += " limit 5";
        }else if(SQLConst.DB_ORACLE.equals(type)){
            sql = "select * from ( "+sql+" ) where rownum < 5 order by rownum asc";
        }else if(SQLConst.DB_SQLSERVER.equals(type)){
            sql = sql.toLowerCase().replaceFirst("select","select top 5 ");
        }else if(SQLConst.DB_DM.equals(type)){
            sql += " limit 0,5";
        }
        return sql;
    }

    public static List<GridHeader> getGridHeader(String sql) throws IllegalAccessException,NoSuchFieldException, SQLException{
        JdbcTemplate jdbcTemplate=SpringUtil.getBean(JdbcTemplate.class);
        ITableMeta iTableMeta= TableMetaContext.getCurrentTableMeta();
        iTableMeta.setJdbcTemplate(jdbcTemplate);
        List<GridHeader> headers=new ArrayList<>();
        String orgSql=sql;
        try{
            SqlRowSet rowSet=jdbcTemplate.queryForRowSet(sql);
            SqlRowSetMetaData metaData=rowSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            //若为单独的表，可以读取其注释字段信息
            sql=sql.toUpperCase();
            int lastFromIndex=sql.lastIndexOf(" FROM ");
            Map<String,Column> columnMap=new HashMap<>();
            if(lastFromIndex>0){
                String fromSubSql=orgSql.substring(lastFromIndex+5).trim();
                int tableIndex=fromSubSql.indexOf(" ");
                String tableName=null;
                if(tableIndex>0){
                    tableName=fromSubSql.substring(0,tableIndex).trim();
                }else{
                    tableName=fromSubSql;
                }
                Table table =iTableMeta.getTableByName(tableName);
                List<Column> columns=table.getColumnList();
                for(Column c:columns){
                    columnMap.put(c.getFieldName(),c);
                }
            }
            for(int i=1;i<=columnCount;i++){
                String fieldName=metaData.getColumnLabel(i);
                String fieldType=metaData.getColumnTypeName(i);

                GridHeader header = new GridHeader();
                header.setFieldName(fieldName);
                header.setFieldType(fieldType);
                header.setLength(metaData.getColumnDisplaySize(i));
                header.setPrecision(metaData.getPrecision(i));

                Column c=columnMap.get(fieldName);
                if(c!=null){
                    header.setFieldLabel(c.getComment());
                    header.setIsNull(new Boolean(c.getIsNull()).toString());
                }
                header.setDbFieldType(metaData.getColumnType(i));
                headers.add(header);
            }
            return headers;
        }catch (Exception e){
            Connection conn=null;
            ResultSet resultSet=null;
            DataSource dataSource=null;
            try{
                String ds= DataSourceContextHolder.getDataSource();
                dataSource= DataSourceUtil.getDataSourcesByAlias(ds);
                conn = DataSourceUtils.getConnection(dataSource);

                PreparedStatement preparedStatement=conn.prepareStatement(orgSql);
                resultSet=preparedStatement.executeQuery();

                ResultSetMetaData meta = resultSet.getMetaData();
                int columnCount = meta.getColumnCount();
                for(int i=1;i<=columnCount;i++){
                    GridHeader gridHeader=new GridHeader();

                    int columnType=meta.getColumnType(i);
                    String columnName=meta.getColumnName(i);
                    String columnTypeName=meta.getColumnTypeName(i);
                    String columnLabel=meta.getColumnLabel(i);


                    gridHeader.setFieldLabel(columnLabel);
                    gridHeader.setDbFieldType(columnType);
                    gridHeader.setFieldName(columnName);
                    gridHeader.setFieldType(columnTypeName);
                    gridHeader.setLength(meta.getColumnDisplaySize(i));
                    gridHeader.setPrecision(meta.getPrecision(i));
                    gridHeader.setIsNull(meta.isNullable(i)==0?"false":"true");

                    headers.add(gridHeader);
                }
            }catch (Exception e1){
                e1.printStackTrace();
            }finally {
                JdbcUtils.closeResultSet(resultSet);
                DataSourceUtils.doReleaseConnection(conn,dataSource);
            }
        }
        return headers;
    }

}
