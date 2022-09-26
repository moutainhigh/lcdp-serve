package com.redxun.ureport.config;

import com.redxun.db.config.DefaultMybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author yjy
 * @date 2018/12/10
 */
@Configuration
@MapperScan({"com.redxun.ureport.core.mapper*"})
public class MybatisPlusConfig extends DefaultMybatisPlusConfig {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;





}
