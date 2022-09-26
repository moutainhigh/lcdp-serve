
package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysKettleLog;
import com.redxun.system.core.mapper.SysKettleLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [KETTLE日志]业务服务类
*/
@Service
public class SysKettleLogServiceImpl extends SuperServiceImpl<SysKettleLogMapper, SysKettleLog> implements BaseService<SysKettleLog> {

    @Resource
    private SysKettleLogMapper sysKettleLogMapper;

    @Override
    public BaseDao<SysKettleLog> getRepository() {
        return sysKettleLogMapper;
    }

}
