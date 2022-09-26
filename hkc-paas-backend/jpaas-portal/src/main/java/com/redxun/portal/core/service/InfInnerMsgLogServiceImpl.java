package com.redxun.portal.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ContextUtil;
import com.redxun.portal.core.entity.InfInbox;
import com.redxun.portal.core.entity.InfInnerMsgLog;
import com.redxun.portal.core.mapper.InfInnerMsgLogMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [内部消息查看记录]业务服务类
*/
@Service
public class InfInnerMsgLogServiceImpl extends SuperServiceImpl<InfInnerMsgLogMapper, InfInnerMsgLog> implements BaseService<InfInnerMsgLog> {

    @Resource
    private InfInnerMsgLogMapper infInnerMsgLogMapper;

    @Override
    public BaseDao<InfInnerMsgLog> getRepository() {
        return infInnerMsgLogMapper;
    }

    InfInnerMsgLog getByMsgIdAndRecUserId(String msgId,String recUserId){
        return infInnerMsgLogMapper.getByMsgIdAndRecUserId(msgId,recUserId);
    }

    /**
     * 更新删除标记
     * @param infInboxes
     */
    public void updateIsDel(List<InfInbox> infInboxes){
        String curUserId = ContextUtil.getCurrentUserId();
        for (InfInbox infInbox : infInboxes) {
            InfInnerMsgLog infInnerMsgLog = infInnerMsgLogMapper.getByMsgIdAndRecUserId(infInbox.getMsgId(), curUserId);
            if(BeanUtil.isNotEmpty(infInnerMsgLog)){
                infInnerMsgLogMapper.updateIsDel(infInnerMsgLog.getId());
            }else {
                InfInnerMsgLog newInfInnerMsgLog=new InfInnerMsgLog();
                newInfInnerMsgLog.setMsgId(infInbox.getMsgId());
                newInfInnerMsgLog.setRecUserId(curUserId);
                newInfInnerMsgLog.setIsRead("no");
                newInfInnerMsgLog.setIsDel("yes");
                insert(newInfInnerMsgLog);
            }
        }
    }

}
