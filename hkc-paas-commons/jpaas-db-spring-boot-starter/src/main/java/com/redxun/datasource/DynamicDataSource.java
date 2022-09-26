package com.redxun.datasource;

import com.redxun.common.utils.ExceptionUtil;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 动态数据源
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }

    /**
     * 访问私有属性字段。
     * @param instance
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private static Object getValue(Object instance, String fieldName) throws IllegalAccessException, NoSuchFieldException {
        Field field =AbstractRoutingDataSource.class.getDeclaredField(fieldName);
        // 参数值为true，禁用访问控制检查
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * 获取默认的数据源。
     * @return
     */
    public  DataSource getDefaultSource(){
        DataSource dataSource=null;
        try{
            dataSource =(DataSource) getValue(this,"resolvedDefaultDataSource");
        }
        catch (Exception ex){
            log.error(ExceptionUtil.getExceptionMessage(ex));
        }
        return dataSource;
    }

    public Map<Object, Object> getDataSource() throws IllegalAccessException, NoSuchFieldException{

        Map<Object, Object> targetDataSources =(Map<Object, Object>) getValue(this,DataSourceUtil.TARGET_DATASOURCES);

        return targetDataSources;
    }

    /**
     * 获取当前数据库的类型。
     * @return
     */
    public String getDbType(){
        DataSource dataSource=this.determineTargetDataSource();
        DataSourceProxy druidDataSource=(DataSourceProxy)dataSource;
        return druidDataSource.getDbType();
    }


    /**
     * 指定数据源是否存在。
     * @param key
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * boolean
     */
    public boolean isDataSourceExist(String key) throws IllegalAccessException, NoSuchFieldException{
        Map<Object, Object> targetDataSources =(Map<Object, Object>) getValue(this, DataSourceUtil.TARGET_DATASOURCES);
        return targetDataSources.containsKey(key);
    }

    public DataSource getDataSource(String key) throws IllegalAccessException, NoSuchFieldException{
        Map<Object, Object> targetDataSources =(Map<Object, Object>) getValue(this, DataSourceUtil.TARGET_DATASOURCES);
        DataSource dataSource= (DataSource) targetDataSources.get(key);
        return  dataSource;
    }

    /**
     * 根据别名删除数据源。
     * @param key
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * void
     */
    public void removeDataSource(String key) throws IllegalAccessException, NoSuchFieldException{
        Map<Object, Object> targetDataSources =(Map<Object, Object>) getValue(this,DataSourceUtil.TARGET_DATASOURCES);

        if(key.equals(DataSourceUtil.GLOBAL_DATASOURCE) || key.equals(DataSourceUtil.DEFAULT_DATASOURCE)) {
            throw new DataSourceException("datasource name :" + key +" can't be removed!");
        }
        targetDataSources.remove(key);
        setTargetDataSources(targetDataSources);
    }


    /**
     * 添加数据源。
     * @param key
     * @param dataSource
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * void
     */
    public void addDataSource(String key,Object dataSource) throws IllegalAccessException, NoSuchFieldException{
        Map<Object, Object> targetDataSources =(Map<Object, Object>) getValue(this, DataSourceUtil.TARGET_DATASOURCES);
        boolean rtn=isDataSourceExist(key);
        if(rtn){
            throw new DataSourceException("datasource name :" + key +"is exists!");
        }
        targetDataSources.put(key, dataSource);
        setTargetDataSources(targetDataSources);
        afterPropertiesSet();
    }
}
