package com.redxun.config;

import com.redxun.db.config.DefaultMybatisPlusConfig;
import io.seata.spring.annotation.GlobalTransactionScanner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yjy
 * @date 2018/12/10
 */
@Configuration
@MapperScan({"com.redxun.form.bo.mapper*","com.redxun.form.core.mapper*"})
public class MybatisPlusConfig extends DefaultMybatisPlusConfig {






}
