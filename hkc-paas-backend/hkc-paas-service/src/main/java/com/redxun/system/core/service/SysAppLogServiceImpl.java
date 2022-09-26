package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysAppLog;
import com.redxun.system.core.mapper.SysAppLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [应用授权接口日志表]业务服务类
*/
@Service
public class SysAppLogServiceImpl extends SuperServiceImpl<SysAppLogMapper, SysAppLog> implements BaseService<SysAppLog> {

    @Resource
    private SysAppLogMapper sysAppLogMapper;

    @Override
    public BaseDao<SysAppLog> getRepository() {
        return sysAppLogMapper;
    }

}
