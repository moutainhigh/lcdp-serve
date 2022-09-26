package com.redxun.log.service.impl;

import com.redxun.log.model.Audit;
import com.redxun.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.time.format.DateTimeFormatter;

/**
 * 审计日志实现类-打印日志
 *
 */
@Slf4j
@ConditionalOnProperty(name = "redxun.audit-log.log-type", havingValue = "logger")
public class LoggerAuditServiceImpl implements IAuditService {
    private static final String MSG_PATTERN = "{}|{}|{}|{}|{}|{}|{}|{}";

    /**
     * 格式为：{时间}|{应用名}|{类名}|{方法名}|{用户id}|{用户名}|{租户id}|{操作信息}
     * 例子：2020-02-04 09:13:34.650|user-center|com.central.user.controller.SysUserController|saveOrUpdate|1|admin|webApp|新增用户:admin
     */
    @Override
    public void save(Audit audit) {
        log.debug(MSG_PATTERN
                , audit.getCreateTime()
                , audit.getAppName(), audit.getClassName(), audit.getMethodName()
                , audit.getCreateBy(), audit.getUserName(), audit.getTenantId()
                , audit.getOperation());
    }
}
