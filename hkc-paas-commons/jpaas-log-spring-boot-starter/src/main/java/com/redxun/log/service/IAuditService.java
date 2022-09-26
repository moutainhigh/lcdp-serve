package com.redxun.log.service;

import com.redxun.log.model.Audit;

/**
 * 审计日志接口
 *
 */
public interface IAuditService {
    void save(Audit audit);
}
