
package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmTemporaryOpinion;
import com.redxun.bpm.core.mapper.BpmTemporaryOpinionMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [流程意见暂存]业务服务类
*/
@Service
public class BpmTemporaryOpinionServiceImpl extends SuperServiceImpl<BpmTemporaryOpinionMapper, BpmTemporaryOpinion> implements BaseService<BpmTemporaryOpinion> {

    @Resource
    private BpmTemporaryOpinionMapper bpmTemporaryOpinionMapper;

    @Override
    public BaseDao<BpmTemporaryOpinion> getRepository() {
        return bpmTemporaryOpinionMapper;
    }

    public BpmTemporaryOpinion getByTaskIdAndUserId(String taskId, String userId) {
        return bpmTemporaryOpinionMapper.getByTaskIdAndUserId(taskId,userId);
    }

    public void delByTaskId(String taskId) {
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("TASK_ID_",taskId);
        bpmTemporaryOpinionMapper.delete(wrapper);
    }
}
