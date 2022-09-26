package com.redxun.bpm.core.service;

import com.redxun.bpm.core.entity.BpmInstRouter;
import com.redxun.bpm.core.mapper.BpmInstRouterMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [流程实例路由]业务服务类
*/
@Service
public class BpmInstRouterServiceImpl extends SuperServiceImpl<BpmInstRouterMapper, BpmInstRouter> implements BaseService<BpmInstRouter> {

    @Resource
    private BpmInstRouterMapper bpmInstRouterMapper;

    @Override
    public BaseDao<BpmInstRouter> getRepository() {
        return bpmInstRouterMapper;
    }
}
