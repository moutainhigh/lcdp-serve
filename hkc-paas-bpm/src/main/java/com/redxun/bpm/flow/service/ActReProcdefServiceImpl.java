package com.redxun.bpm.flow.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.flow.entity.ActReProcdef;
import com.redxun.bpm.flow.mapper.ActReProcdefMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [act_re_procdef]业务服务类
*/
@Service
public class ActReProcdefServiceImpl extends SuperServiceImpl<ActReProcdefMapper, ActReProcdef> implements BaseService<ActReProcdef> {

    @Resource
    private ActReProcdefMapper actReProcdefMapper;

    @Override
    public BaseDao<ActReProcdef> getRepository() {
        return actReProcdefMapper;
    }
}
