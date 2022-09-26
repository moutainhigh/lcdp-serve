
package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysErrorLog;
import com.redxun.system.core.mapper.SysErrorLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [错误日志]业务服务类
*/
@Service
public class SysErrorLogServiceImpl extends SuperServiceImpl<SysErrorLogMapper, SysErrorLog> implements BaseService<SysErrorLog> {

    @Resource
    private SysErrorLogMapper sysErrorLogMapper;

    @Override
    public BaseDao<SysErrorLog> getRepository() {
        return sysErrorLogMapper;
    }

}
