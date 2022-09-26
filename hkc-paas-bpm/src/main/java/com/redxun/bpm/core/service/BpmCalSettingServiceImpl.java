package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmCalSetting;
import com.redxun.bpm.core.mapper.BpmCalSettingMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [日历设定]业务服务类
*/
@Service
public class BpmCalSettingServiceImpl extends SuperServiceImpl<BpmCalSettingMapper, BpmCalSetting> implements BaseService<BpmCalSetting> {

    @Resource
    private BpmCalSettingMapper bpmCalSettingMapper;

    @Override
    public BaseDao<BpmCalSetting> getRepository() {
        return bpmCalSettingMapper;
    }

    public BpmCalSetting getIsCommon(String isCommon){
        QueryWrapper queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("IS_COMMON_",isCommon);
        return  bpmCalSettingMapper.selectOne(queryWrapper);
    }
    public BpmCalSetting getByName(String name){
        QueryWrapper queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("CAL_NAME_",name);
        return  bpmCalSettingMapper.selectOne(queryWrapper);
    }
}
