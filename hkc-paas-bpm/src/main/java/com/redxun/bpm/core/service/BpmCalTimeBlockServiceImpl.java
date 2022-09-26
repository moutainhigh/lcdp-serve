package com.redxun.bpm.core.service;

import com.redxun.bpm.core.entity.BpmCalTimeBlock;
import com.redxun.bpm.core.mapper.BpmCalTimeBlockMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [工作时间段设定]业务服务类
*/
@Service
public class BpmCalTimeBlockServiceImpl extends SuperServiceImpl<BpmCalTimeBlockMapper, BpmCalTimeBlock> implements BaseService<BpmCalTimeBlock> {

    @Resource
    private BpmCalTimeBlockMapper bpmCalTimeBlockMapper;

    @Override
    public BaseDao<BpmCalTimeBlock> getRepository() {
        return bpmCalTimeBlockMapper;
    }
}
