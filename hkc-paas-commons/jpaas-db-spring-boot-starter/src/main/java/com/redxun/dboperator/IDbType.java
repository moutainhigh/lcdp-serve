package com.redxun.dboperator;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据库类型接口
 */
public interface IDbType {

    /**
     * 设置spring 的JdbcTemplate
     * @param template
     */
    void setJdbcTemplate(JdbcTemplate template);
}
