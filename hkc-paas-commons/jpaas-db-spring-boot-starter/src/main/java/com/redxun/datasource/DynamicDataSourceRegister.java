package com.redxun.datasource;


import com.alibaba.druid.pool.DruidDataSource;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.datasource.monitor.DruidSourceRegister;
import io.micrometer.core.instrument.MeterRegistry;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 动成数据源注册管理
 */
@Component
public class DynamicDataSourceRegister implements EnvironmentAware {

    @Value("${datasource.monitor}")
    private boolean enableMonitor=false;

    @Autowired
    MeterRegistry registry;

    /**
     * 自定义数据源
     */
    private Map<String, DataSource> customDataSources = new HashMap<>();
    /**
     * 应用程序上下文变量
     */
    @Resource
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    ConfigurableBeanFactory beanFactory;

    /**
     * 加载多数据源配置
     */
    @Override
    public void setEnvironment(Environment env) {
        initDefalutDatasource(env);
        //初始化其他的数据源。
        initCustomDataSources(env);
    }

    /**
     * 根据环境亦变量初始化默认的数据源
     * @param env
     */
    private void initDefalutDatasource(Environment env) {
        DataSource defaultDataSource = DataSourceUtil.buildDataSource(env, "defaultDs", "default");
        registerDataSource("default", defaultDataSource);
    }

    /**
     * 注册seata 数据源。
     *
     * @param name
     * @param dataSource
     */
    public DataSourceProxy registerDataSource(String name, DataSource dataSource) {
        registerBean(name, dataSource);
        String proxyName = name + "Proxy";

        DataSourceProxy proxyDs = new DataSourceProxy(dataSource);
        registerBean(proxyName, proxyDs);

        //是否允许监控
        if(enableMonitor){
            DruidSourceRegister.registerDataSource((DruidDataSource) dataSource,registry);
        }

        return proxyDs;
    }

    /**
     * 构建动态数据源
     * @return
     */
    @Bean(DataSourceUtil.GLOBAL_DATASOURCE)
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(3);

        DataSource defaultDsProxy = (DataSource) applicationContext.getBean("defaultProxy");
        dataSourceMap.put(DataSourceUtil.DEFAULT_DATASOURCE, defaultDsProxy);

        if (BeanUtil.isNotEmpty(customDataSources)) {
            for (Map.Entry<String, DataSource> ent : customDataSources.entrySet()) {
                DataSource customDsProxy = registerDataSource(ent.getKey(), ent.getValue());
                dataSourceMap.put(ent.getKey(), customDsProxy);
            }
        }

        dynamicDataSource.setDefaultTargetDataSource(defaultDsProxy);
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    /**
     * 注册Bean
     * @param name
     * @param object
     */
    private void registerBean(String name, Object object) {
        DefaultSingletonBeanRegistry beanDefReg = (DefaultSingletonBeanRegistry) beanFactory;
        if (beanDefReg.containsSingleton(name)) {
            beanDefReg.destroySingleton(name);
        }

        beanFactory.registerSingleton(name, object);
    }


    /**
     * 初始化其他的数据源。
     *
     * @param env
     */
    private void initCustomDataSources(Environment env) {
        String customDataSource = env.getProperty("customDataSource");
        if (StringUtils.isEmpty(customDataSource)) {
            return;
        }
        String[] aryDs = customDataSource.split(",");
        for (int i = 0; i < aryDs.length; i++) {
            String name = aryDs[i];
            DataSource ds = DataSourceUtil.buildDataSource(env, name, name);
            if (ds != null) {
                customDataSources.put(name, ds);
            }
        }
    }

}
