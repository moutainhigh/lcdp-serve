package com.redxun.listener;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.datasource.DataSourceConstant;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.datasource.DynamicDataSourceRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 启动的时候创建数据源。
 */
@Component
@Slf4j
public class DataSourceStartup implements CommandLineRunner, Ordered {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    ConfigService configService;



    @Autowired
    private DynamicDataSourceRegister dynamicDataSourceRegister;

    @Override
    public void run(String... args) throws Exception {
        addDataSource();
    }


    private void addDataSource() {
        try {
            String config = configService.getConfig(DataSourceConstant.DATASOURCE_CONFIG, DataSourceConstant.DEFAULT_GROUP, 0L);
            JSONObject conf = JSONObject.parseObject(config);
            if (BeanUtil.isEmpty(conf)) {
                return;
            }
            // 添加数据实例到容器中
            for (String key : conf.keySet()) {
                try {
                    JSONObject json = conf.getJSONObject(key);
                    String appName = json.getString("appName");
                    if(!appName.contains(applicationName)) {
                        continue;
                    }

                    DataSource dataSource = DataSourceUtil.buildDataSource(key, json.getString("setting"));
                    if(dataSource!=null){
                        //数据源进行代理。
                        DataSource dsProxy= dynamicDataSourceRegister.registerDataSource(key,dataSource);
                        DataSourceUtil.addDataSource(key, dsProxy, true);
                    }

                } catch (Exception e) {
                    //错误信息
                    log.error(ExceptionUtil.getExceptionMessage(e));
                }
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.getExceptionMessage(e));
        }
    }



    @Override
    public int getOrder() {
        return 2;
    }
}
