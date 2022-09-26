package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.ActRuVariable;
import com.redxun.bpm.core.mapper.ActRuVariableMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [act_ru_variable]业务服务类
*/
@Service
public class ActRuVariableServiceImpl extends SuperServiceImpl<ActRuVariableMapper, ActRuVariable> implements BaseService<ActRuVariable> {

    @Resource
    private ActRuVariableMapper actRuVariableMapper;

    @Override
    public BaseDao<ActRuVariable> getRepository() {
        return actRuVariableMapper;
    }


    public void delByExecutionId(String executionId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("EXECUTION_ID_",executionId);
        actRuVariableMapper.delete(wrapper);
    }
}
