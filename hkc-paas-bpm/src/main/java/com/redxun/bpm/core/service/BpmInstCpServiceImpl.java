package com.redxun.bpm.core.service;

import com.redxun.bpm.core.entity.BpmInstCp;
import com.redxun.bpm.core.mapper.BpmInstCpMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [流程抄送人员]业务服务类
*/
@Service
public class BpmInstCpServiceImpl extends SuperServiceImpl<BpmInstCpMapper, BpmInstCp> implements BaseService<BpmInstCp> {

    @Resource
    private BpmInstCpMapper bpmInstCpMapper;

    @Override
    public BaseDao<BpmInstCp> getRepository() {
        return bpmInstCpMapper;
    }

    /**
     * 更新为已读。
     * @param cpId
     */
    public void updReaded(String cpId){
       BpmInstCp bpmInstCp= this.bpmInstCpMapper.selectById(cpId);
       if(MBoolean.YES.name().equals( bpmInstCp.getIsRead())){
           return;
       }
       bpmInstCp.setIsRead(MBoolean.YES.name());
       bpmInstCpMapper.updateById(bpmInstCp);
    }

    /**
     * 删除备份的数据
     * @param instId
     * @param tableId
     */
    public void delArchiveByInstId(String instId,Integer tableId) {
        bpmInstCpMapper.delArchiveByInstId(instId,tableId);
    }
}
