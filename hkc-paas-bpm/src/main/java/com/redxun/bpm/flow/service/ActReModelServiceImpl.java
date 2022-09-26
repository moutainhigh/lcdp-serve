package com.redxun.bpm.flow.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.flow.entity.ActReModel;
import com.redxun.bpm.flow.mapper.ActReModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [act_re_model]业务服务类
*/
@Service
public class ActReModelServiceImpl extends SuperServiceImpl<ActReModelMapper, ActReModel> implements BaseService<ActReModel> {

    @Resource
    private ActReModelMapper actReModelMapper;

    @Override
    public BaseDao<ActReModel> getRepository() {
        return actReModelMapper;
    }
}
