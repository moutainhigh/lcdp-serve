package com.redxun.dboperator;

import com.alibaba.druid.pool.DruidDataSource;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.db.DbUtil;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.swing.*;

@Slf4j
public class TableMetaContext {

    private  static String tableMeta="_TableMeta";

    /**
     * 获取表的元数据。
     * @return
     */
    public static   ITableMeta getCurrentTableMeta(){
        String dbType= DataSourceUtil.getCurrentDbType();
        String op=dbType + tableMeta;
        ITableMeta tableMeta= SpringUtil.getBean(op,ITableMeta.class);
        JdbcTemplate jdbcTemplate= SpringUtil.getBean(JdbcTemplate.class);
        tableMeta.setJdbcTemplate(jdbcTemplate);
        return  tableMeta;
    }


    public static   ITableMeta getByDsAlias(String dsAlias){
        try{
            DataSource ds= DataSourceUtil.getDataSourcesByAlias(dsAlias);
            DataSourceProxy dataSource=(DataSourceProxy)ds;
            String dbType=dataSource.getDbType();
            String op=dbType + tableMeta;
            ITableMeta tableMeta= SpringUtil.getBean(op,ITableMeta.class);
            JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
            tableMeta.setJdbcTemplate(jdbcTemplate);

            return  tableMeta;
        }
        catch (Exception ex){
            log.error(ExceptionUtil.getExceptionMessage(ex));
            return null;
        }
    }
}
