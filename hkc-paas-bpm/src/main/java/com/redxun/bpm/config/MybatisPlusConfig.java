package com.redxun.bpm.config;

import com.redxun.db.config.DefaultMybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis扫描配置入口
 * @author yjy
 * @date 2018/12/10
 */
@Configuration
@MapperScan({"com.redxun.bpm.*.mapper*"})
public class MybatisPlusConfig extends DefaultMybatisPlusConfig {
}
