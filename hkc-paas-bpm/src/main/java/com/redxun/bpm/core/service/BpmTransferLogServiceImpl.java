
package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmTransferLog;
import com.redxun.bpm.core.mapper.BpmTransferLogMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [流转任务日志表]业务服务类
*/
@Service
public class BpmTransferLogServiceImpl extends SuperServiceImpl<BpmTransferLogMapper, BpmTransferLog> implements BaseService<BpmTransferLog> {

    @Resource
    private BpmTransferLogMapper bpmTransferLogMapper;

    @Override
    public BaseDao<BpmTransferLog> getRepository() {
        return bpmTransferLogMapper;
    }

    public List<BpmTransferLog> getByTaskId(String taskId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TASK_ID_",taskId);
        return bpmTransferLogMapper.selectList(queryWrapper);
    }

    public void delByTaskId(String taskId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TASK_ID_",taskId);
        bpmTransferLogMapper.delete(queryWrapper);
    }

    public void updateStatusByTransTaskId(String taskId,String opinion, String status) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TRANS_TASK_ID_",taskId);
        BpmTransferLog log=new BpmTransferLog();
        log.setStatus(status);
        log.setRemark(opinion);
        bpmTransferLogMapper.update(log,queryWrapper);
    }
}
