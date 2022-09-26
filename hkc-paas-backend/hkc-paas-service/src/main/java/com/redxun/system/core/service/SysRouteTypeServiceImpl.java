package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysRouteType;
import com.redxun.system.core.mapper.SysRouteTypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [路由类型]业务服务类
*/
@Service
public class SysRouteTypeServiceImpl extends SuperServiceImpl<SysRouteTypeMapper, SysRouteType> implements BaseService<SysRouteType> {

    @Resource
    private SysRouteTypeMapper sysRouteTypeMapper;

    @Override
    public BaseDao<SysRouteType> getRepository() {
        return sysRouteTypeMapper;
    }
}
