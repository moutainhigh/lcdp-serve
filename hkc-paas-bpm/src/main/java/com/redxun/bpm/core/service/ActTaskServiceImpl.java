package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.ActTask;
import com.redxun.bpm.core.mapper.ActTaskMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [act_ru_task]业务服务类
*/
@Service
public class ActTaskServiceImpl extends SuperServiceImpl<ActTaskMapper, ActTask> implements BaseService<ActTask> {

    @Resource
    private ActTaskMapper actTaskMapper;

    @Override
    public BaseDao<ActTask> getRepository() {
        return actTaskMapper;
    }


    /**
     * 根据流程实例ID删除任务。
     * @param instId
     */
    public void delByInstId(String instId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("PROC_INST_ID_",instId);
        actTaskMapper.delete(wrapper);

    }

    /**
     * 根据executionId 删除。
     * @param executionId
     */
    protected void  delByExecutionId(String executionId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("EXECUTION_ID_",executionId);
        actTaskMapper.delete(wrapper);
    }
}
