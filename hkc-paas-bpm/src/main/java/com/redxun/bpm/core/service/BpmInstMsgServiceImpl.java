package com.redxun.bpm.core.service;

import com.redxun.bpm.core.entity.BpmInstMsg;
import com.redxun.bpm.core.mapper.BpmInstMsgMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [流程沟通留言板]业务服务类
*/
@Service
public class BpmInstMsgServiceImpl extends SuperServiceImpl<BpmInstMsgMapper, BpmInstMsg> implements BaseService<BpmInstMsg> {

    @Resource
    private BpmInstMsgMapper bpmInstMsgMapper;

    @Override
    public BaseDao<BpmInstMsg> getRepository() {
        return bpmInstMsgMapper;
    }

    public List<BpmInstMsg> getByInstId(String instId){
        return bpmInstMsgMapper.getByInstId(instId);
    }

    /**
     * 删除备份的数据
     * @param instId
     * @param tableId
     */
    public void delArchiveByInstId(String instId,Integer tableId) {
        bpmInstMsgMapper.delArchiveByInstId(instId,tableId);
    }
}
