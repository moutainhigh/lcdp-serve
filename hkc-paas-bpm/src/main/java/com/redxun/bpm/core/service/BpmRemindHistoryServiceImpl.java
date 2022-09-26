package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmRemindHistory;
import com.redxun.bpm.core.mapper.BpmRemindHistoryMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
* [催办历史表]业务服务类
*/
@Service
public class BpmRemindHistoryServiceImpl extends SuperServiceImpl<BpmRemindHistoryMapper, BpmRemindHistory> implements BaseService<BpmRemindHistory> {

    @Resource
    private BpmRemindHistoryMapper bpmRemindHistoryMapper;

    @Override
    public BaseDao<BpmRemindHistory> getRepository() {
        return bpmRemindHistoryMapper;
    }

    public Integer getByStartEnd(String instId, Date start, Date end) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("REMINDER_INST_ID_",instId);
        queryWrapper.between("CREATE_TIME_",start,end);
        return bpmRemindHistoryMapper.selectCount(queryWrapper);
    }

    /**
     * 删除备份的数据
     * @param instId
     * @param tableId
     */
    public void delArchiveByInstId(String instId,Integer tableId) {
        bpmRemindHistoryMapper.delArchiveByInstId(instId,tableId);
    }
}
