package com.redxun.bpm.flow.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.flow.entity.ActGeBytearray;
import com.redxun.bpm.flow.mapper.ActGeBytearrayMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [act_ge_bytearray]业务服务类
*/
@Service
public class ActGeBytearrayServiceImpl extends SuperServiceImpl<ActGeBytearrayMapper, ActGeBytearray> implements BaseService<ActGeBytearray> {

    @Resource
    private ActGeBytearrayMapper actGeBytearrayMapper;

    @Override
    public BaseDao<ActGeBytearray> getRepository() {
        return actGeBytearrayMapper;
    }

    /**
     * 根据deployid查找流程定义。
     * @param deployId
     * @return
     */
    public  ActGeBytearray getByDeployId(String deployId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("DEPLOYMENT_ID_",deployId);
        wrapper.like("NAME_","%bpmn20.xml");
        ActGeBytearray bytearray= actGeBytearrayMapper.selectOne(wrapper);

        return  bytearray;
    }
}
