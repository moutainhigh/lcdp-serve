package com.redxun.bpm.core.service;

import com.redxun.bpm.core.entity.BpmAgentFlowDef;
import com.redxun.bpm.core.mapper.BpmAgentFlowDefMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [代理流程定义]业务服务类
*/
@Service
public class BpmAgentFlowDefServiceImpl extends SuperServiceImpl<BpmAgentFlowDefMapper, BpmAgentFlowDef> implements BaseService<BpmAgentFlowDef> {

    @Resource
    private BpmAgentFlowDefMapper bpmAgentFlowDefMapper;

    @Override
    public BaseDao<BpmAgentFlowDef> getRepository() {
        return bpmAgentFlowDefMapper;
    }
}
