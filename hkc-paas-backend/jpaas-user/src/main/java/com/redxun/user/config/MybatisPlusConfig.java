package com.redxun.user.config;

import com.redxun.db.config.DefaultMybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yjy
 * @date 2018/12/10
 */
@Configuration
@MapperScan({"com.redxun.user.org.mapper*"})
public class MybatisPlusConfig extends DefaultMybatisPlusConfig {
}
