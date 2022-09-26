
package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmTransfer;
import com.redxun.bpm.core.mapper.BpmTransferMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [流程流转表]业务服务类
*/
@Service
public class BpmTransferServiceImpl extends SuperServiceImpl<BpmTransferMapper, BpmTransfer> implements BaseService<BpmTransfer> {

    @Resource
    private BpmTransferMapper bpmTransferMapper;

    @Override
    public BaseDao<BpmTransfer> getRepository() {
        return bpmTransferMapper;
    }

    public BpmTransfer getByTaskId(String taskId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TASK_ID_",taskId);
        return bpmTransferMapper.selectOne(queryWrapper);
    }

    public void delByTaskId(String taskId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TASK_ID_",taskId);
        bpmTransferMapper.delete(queryWrapper);
    }

    public void delByInstId(String instId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("INST_ID_",instId);
        bpmTransferMapper.delete(queryWrapper);
    }
}
