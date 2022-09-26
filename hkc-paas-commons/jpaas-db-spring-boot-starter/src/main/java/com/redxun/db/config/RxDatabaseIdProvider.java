package com.redxun.db.config;

import com.redxun.datasource.DynamicDataSource;
import org.apache.ibatis.mapping.DatabaseIdProvider;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 获取数据源上下文的数据库类型，这个可以使用再mybatis 配置文件中指定  dataBaseId
 */
public class RxDatabaseIdProvider implements DatabaseIdProvider {
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 获取动态数据源
     * @param dataSource
     * @return
     * @throws SQLException
     */
    @Override
    public String getDatabaseId(DataSource dataSource) throws SQLException {
        DynamicDataSource dynamicDataSource=(DynamicDataSource)dataSource;
        String type=  dynamicDataSource.getDbType();
        return type;

    }
}
