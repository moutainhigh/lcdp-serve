package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysRouting;
import com.redxun.system.core.mapper.SysRoutingMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [路由]业务服务类
*/
@Service
public class SysRoutingServiceImpl extends SuperServiceImpl<SysRoutingMapper, SysRouting> implements BaseService<SysRouting> {

    @Resource
    private SysRoutingMapper sysRoutingMapper;

    @Override
    public BaseDao<SysRouting> getRepository() {
        return sysRoutingMapper;
    }

    public List<SysRouting> getRoutingByType(String routeType) {
        return sysRoutingMapper.getRoutingByType(routeType);
    }

    public void deleteByRoutIds(List<String> list){
        sysRoutingMapper.deleteByRoutIds(list);
    }
}
