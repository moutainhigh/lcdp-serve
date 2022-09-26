package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmCheckFile;
import com.redxun.bpm.core.mapper.BpmCheckFileMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [审批意见附件]业务服务类
*/
@Service
public class BpmCheckFileService extends SuperServiceImpl<BpmCheckFileMapper, BpmCheckFile> implements BaseService<BpmCheckFile> {

    @Resource
    private BpmCheckFileMapper bpmCheckFileMapper;

    @Override
    public BaseDao<BpmCheckFile> getRepository() {
        return bpmCheckFileMapper;
    }

    public List<BpmCheckFile> getByInstId(String instId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("INST_ID_",instId);
        return bpmCheckFileMapper.selectList(queryWrapper);
    }

    public List<BpmCheckFile> getByHisId(String hisId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("JUMP_ID_",hisId);
        return bpmCheckFileMapper.selectList(queryWrapper);
    }

    public List<BpmCheckFile> getByArchiveLog(String hisId, Integer tableId) {
        return bpmCheckFileMapper.getByArchiveLog(hisId,tableId);
    }
}
