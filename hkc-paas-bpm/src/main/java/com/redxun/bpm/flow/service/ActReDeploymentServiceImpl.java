package com.redxun.bpm.flow.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.flow.entity.ActReDeployment;
import com.redxun.bpm.flow.mapper.ActReDeploymentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [act_re_deployment]业务服务类
*/
@Service
public class ActReDeploymentServiceImpl extends SuperServiceImpl<ActReDeploymentMapper, ActReDeployment> implements BaseService<ActReDeployment> {

    @Resource
    private ActReDeploymentMapper actReDeploymentMapper;

    @Override
    public BaseDao<ActReDeployment> getRepository() {
        return actReDeploymentMapper;
    }
}
