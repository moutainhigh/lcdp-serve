package com.redxun.log.service.impl;

import com.redxun.log.model.Audit;
import com.redxun.log.mq.LogOutput;
import com.redxun.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;

/**
 * 审计日志实现类-数据库
 *
 */
@Slf4j
@ConditionalOnProperty(name = "redxun.audit-log.log-type", havingValue = "mq", matchIfMissing = true)
public class MqAuditServiceImpl implements IAuditService {

    @Autowired(required = false)
    LogOutput logInputOutput;


    @Async
    @Override
    public void save(Audit audit) {
        logInputOutput.logOutput().send(MessageBuilder.withPayload(audit).build());
    }
}
