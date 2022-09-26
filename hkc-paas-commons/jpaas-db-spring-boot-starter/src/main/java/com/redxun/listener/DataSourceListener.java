package com.redxun.listener;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.datasource.DataSourceConstant;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.datasource.DynamicDataSource;
import com.redxun.datasource.DynamicDataSourceRegister;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

/**
 * 发布数据源的时候对数据源进行变更。
 * @author hujun
 */
@Slf4j
@Component
public class DataSourceListener  implements CommandLineRunner, Ordered {

    @Autowired
    private ConfigService configService;
    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private DynamicDataSourceRegister dynamicDataSourceRegister;
    @Autowired
    private DynamicDataSource dynamicDataSource;

    public DataSourceListener()  {
    }

    /**
     * 添加或更新数据源。
     */
    private void addDataSource() {
        String config="{}";
        try{
            config=configService.getConfig(DataSourceConstant.DATASOURCE_TEMP,DataSourceConstant.DEFAULT_GROUP,0);
        }
        catch (Exception ex){
            log.error(ExceptionUtil.getExceptionMessage(ex));
        }

        JSONObject conf = JSONObject.parseObject(config);
        // 添加数据实例到容器中
        for (String key : conf.keySet()) {
            try {
                JSONObject json = conf.getJSONObject(key);
                String appName = json.getString("appName");
                if(!appName.contains(applicationName)) {
                    continue;
                }
                //移除数据源。
                removeDatasource(key);
                //如果禁止的数据源，则跳过。
                String enable= json.getString(DataSourceConstant.ENABLE);
                if(MBoolean.FALSE_LOWER.val.equals(enable)){
                    continue;
                }
                //新建数据源。
                DruidDataSource dataSource = (DruidDataSource) DataSourceUtil.buildDataSource(key, json.getString("setting"));
                if(dataSource!=null){
                    //数据源进行代理。
                    DataSource dsProxy= dynamicDataSourceRegister.registerDataSource(key,dataSource);
                    DataSourceUtil.addDataSource(key, dsProxy, true);

                    //发布事件。
                    DataSourceUpdEvent updEvent=new DataSourceUpdEvent(key);
                    SpringUtil.publishEvent(updEvent);
                }

            } catch (Exception e) {
                //错误信息
                log.error(ExceptionUtil.getExceptionMessage(e));
            }
        }

    }

    /**
     * 如果存在则将原来的数据源关闭，删除重建。
     */
    private void removeDatasource(String key) throws NoSuchFieldException, IllegalAccessException {
        /**
         * 如果存在则将原来的数据源关闭，删除重建。
         */
        boolean isExist= dynamicDataSource.isDataSourceExist(key);
        if(isExist){
            DataSourceProxy dataSource=(DataSourceProxy) dynamicDataSource.getDataSource(key);
            DruidDataSource druidDataSource= (DruidDataSource) dataSource.getTargetDataSource();
            druidDataSource.close();
            druidDataSource=null;
            dataSource=null;
            //移除数据源。
            dynamicDataSource.removeDataSource(key);
        }
    }

    @Override
    public void run(String... args) throws Exception {

        configService.addListener(DataSourceConstant.DATASOURCE_TEMP, DataSourceConstant.DEFAULT_GROUP, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String config) {

                addDataSource();
            }
        });
    }

    @Override
    public int getOrder() {
        return 1;
    }
}










