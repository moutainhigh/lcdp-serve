package com.redxun.log.service.impl;

import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.log.model.Audit;
import com.redxun.log.mq.LogOutput;
import com.redxun.log.properties.LogDbProperties;
import com.redxun.log.service.IAuditService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;

import javax.sql.DataSource;

/**
 * 审计日志实现类-数据库
 *
 */
@Slf4j
@ConditionalOnProperty(name = "redxun.audit-log.log-type", havingValue = "db")
@ConditionalOnClass(JdbcTemplate.class)
public class DbAuditServiceImpl implements IAuditService {

    String sql="insert into SYS_LOG (ID_,APP_NAME_,MODULE_,SUB_MODULE_,CLASS_NAME_,METHOD_NAME_," +
            "ACTION_,PK_VALUE_,IP_,OPERATION_,DETAIL_,USER_NAME_,TENANT_ID_,CREATE_DEP_ID_,CREATE_BY_,CREATE_TIME_,DURATION_)" +
            " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


    private JdbcTemplate jdbcTemplate;

    public DbAuditServiceImpl(@Autowired(required = false) LogDbProperties logDbProperties) {
        //优先使用配置的日志数据源，否则使用默认的数据源
        if (logDbProperties != null && StringUtils.isNotEmpty(logDbProperties.getJdbcUrl())) {
            DataSource dataSource = new HikariDataSource(logDbProperties);
            this.jdbcTemplate = new JdbcTemplate(dataSource);
        }
    }

    @Async
    @Override
    public void save(Audit audit) {
        jdbcTemplate.update(sql,IdGenerator.getIdStr(),audit.getAppName(),audit.getModule(),audit.getSubModule(),
                audit.getClassName(),audit.getMethodName(),audit.getAction(),audit.getPkValue(),audit.getIp(),
                audit.getOperation(),audit.getDetail(),audit.getUserName(),audit.getTenantId(),audit.getCreateDepId(),
                audit.getCreateBy(),audit.getCreateTime(),audit.getDuration());
    }
}
