package com.redxun.system.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysTransferLog;
import com.redxun.system.core.mapper.SysTransferLogMapper;
import com.redxun.system.feign.OsUserClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * [权限转移日志表]业务服务类
 */
@Service
public class SysTransferLogServiceImpl extends SuperServiceImpl<SysTransferLogMapper, SysTransferLog> implements BaseService<SysTransferLog> {

    @Resource
    private SysTransferLogMapper sysTransferLogMapper;
    @Resource
    private OsUserClient osUserClient;

    @Override
    public BaseDao<SysTransferLog> getRepository() {
        return sysTransferLogMapper;
    }

    public IPage queryUser(QueryFilter queryFilter) {
        IPage page = query(queryFilter);
        List<SysTransferLog> list = page.getRecords();
        for (SysTransferLog log : list) {
            log.setAuthorPersonName(osUserClient.getById(log.getAuthorPerson()).getFullName());
            log.setTargetPersonName(osUserClient.getById(log.getTargetPerson()).getFullName());
        }
        return page;
    }

    public SysTransferLog getUser(String pkId) {
        SysTransferLog log = get(pkId);
        log.setAuthorPersonName(osUserClient.getById(log.getAuthorPerson()).getFullName());
        log.setTargetPersonName(osUserClient.getById(log.getTargetPerson()).getFullName());
        return log;
    }
}
