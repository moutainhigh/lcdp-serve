package com.redxun.db.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import com.redxun.datasource.DataSourceUtil;
import com.redxun.datasource.MyBatisConfig;
import com.redxun.logicdel.RxSqlInjector;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * mybatis-plus配置
 * @author yjy
 * @date 2018/12/13
 */
@Configuration
public class DefaultMybatisPlusConfig {

    @Value(value = "${redxun.dblog:true}")
    private boolean showLog=true;

    @Autowired
    private MyBatisConfig myBatisConfig;

    /**
     * 新多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        // 用了分页插件必须设置 MybatisConfiguration#useDeprecatedExecutor = false
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseDeprecatedExecutor(false);
    }

    /**
     * 数据源
     * @return
     */
    @Bean
    public  DatabaseIdProvider databaseIdProvider(){
        DatabaseIdProvider provider=new RxDatabaseIdProvider();
        return provider;
    }





    /**
     * 解决使用seata 后分页的问题。
     * @param dataSource
     * @return
     * @throws Exception
     */
    @Bean
    public  SqlSessionFactory sqlSessionFactory(@Qualifier(DataSourceUtil.GLOBAL_DATASOURCE) DataSource dataSource,
                                               @Qualifier("mybatisPlusInterceptor") MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean=new MybatisSqlSessionFactoryBean();

        factoryBean.setPlugins(new Interceptor[]{mybatisPlusInterceptor});
        factoryBean.setDataSource(dataSource);

        MybatisConfiguration configuration= new MybatisConfiguration();
        configuration.setCallSettersOnNulls(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        if(showLog){
            configuration.setLogImpl(StdOutImpl.class);
        }
        else{
            configuration.setLogImpl(NoLoggingImpl.class);
        }
        factoryBean.setConfiguration(configuration);

        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(myBatisConfig.getMapperLocations());

        factoryBean.setMapperLocations(resources);

        //扩展字段设置。
        GlobalConfig globalConfig=new GlobalConfig();

        RxSqlInjector phoenixSqlInjector = new RxSqlInjector();

        globalConfig.setSqlInjector(phoenixSqlInjector);

        globalConfig.setMetaObjectHandler(new DateMetaObjectHandler());

        factoryBean.setGlobalConfig(globalConfig);

        //设置databaseIdProvider
        factoryBean.setDatabaseIdProvider(databaseIdProvider());

        return factoryBean.getObject();


    }



    @Bean(value = "localJdbcTemplate")
    public JdbcTemplate registerJdbcTemplate(@Qualifier(DataSourceUtil.GLOBAL_DATASOURCE) DataSource dataSource){
        JdbcTemplate jdbcTemplate= new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        return jdbcTemplate;
    }




}
