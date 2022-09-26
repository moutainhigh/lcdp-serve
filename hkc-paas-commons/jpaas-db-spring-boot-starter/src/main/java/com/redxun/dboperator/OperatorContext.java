package com.redxun.dboperator;

import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.datasource.DataSourceUtil;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * 操作上下文
 */
@Service
@Slf4j
public class OperatorContext {
    private static String tableOperator="_TableOperator";
    private static String viewOperator="_ViewOperator";

    /**
     * 根据当前数据库类型实现对不同的表进行操作处理
     * @return
     */
    public static   ITableOperator getCurrentTableOperator(){
        String dbType=DataSourceUtil.getCurrentDbType();
        String op=dbType + tableOperator;
        ITableOperator tableOperator= SpringUtil.getBean(op,ITableOperator.class);
        return  tableOperator;
    }

    /**
     * 根据别名获取表操作类。
     * @param dsAlias
     * @return
     */
    public static ITableOperator getByDsAlias(String dsAlias){
        try{
            DataSource ds= DataSourceUtil.getDataSourcesByAlias(dsAlias);
            DataSourceProxy dataSource=(DataSourceProxy)ds;
            String dbType=dataSource.getDbType();
            String op=dbType + tableOperator;
            ITableOperator tableOperator= SpringUtil.getBean(op,ITableOperator.class);

            JdbcTemplate jdbcTemplate=new JdbcTemplate(ds);
            tableOperator.setJdbcTemplate(jdbcTemplate);

            return  tableOperator;
        }
        catch (Exception ex){
            log.error(ExceptionUtil.getExceptionMessage(ex));
            return null;
        }
    }

    /**
     * 设置当前视图操作类
     * @return
     */
    public static IViewOperator getCurrentViewOperator(){
        String dbType=DataSourceUtil.getCurrentDbType();
        String op=dbType + viewOperator;
        IViewOperator viewOperator= SpringUtil.getBean(op,IViewOperator.class);
        return  viewOperator;
    }

}
