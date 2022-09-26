package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmRemindInst;
import com.redxun.bpm.core.mapper.BpmRemindInstMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * [催办实例表]业务服务类
 */
@Service
public class BpmRemindInstServiceImpl extends SuperServiceImpl<BpmRemindInstMapper, BpmRemindInst> implements BaseService<BpmRemindInst> {

    @Resource
    private BpmRemindInstMapper bpmRemindInstMapper;

    @Override
    public BaseDao<BpmRemindInst> getRepository() {
        return bpmRemindInstMapper;
    }


    public List<BpmRemindInst> getRemindInst(boolean isExpire) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ne("STATUS_","finish");
        if(isExpire){
            queryWrapper.le("EXPIRE_DATE_",new Date());
        }else{
            queryWrapper.gt("EXPIRE_DATE_",new Date());
        }
        return bpmRemindInstMapper.selectList(queryWrapper);
    }

    /**
     * 删除备份的数据
     * @param instId
     * @param tableId
     */
    public void delArchiveByInstId(String instId,Integer tableId) {
        bpmRemindInstMapper.delArchiveByInstId(instId,tableId);
    }
}
