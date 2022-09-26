
package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.mq.MessageModel;
import com.redxun.msgsend.util.MesAutoUtil;
import com.redxun.system.core.entity.SysLog;
import com.redxun.system.core.mapper.SysLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [系统日志]业务服务类
*/
@Service
public class SysMessageServiceImpl extends SuperServiceImpl<SysLogMapper, SysLog> implements BaseService<SysLog> {

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public BaseDao<SysLog> getRepository() {
        return sysLogMapper;
    }

    public void sendMessage(MessageModel model) {
        MesAutoUtil.sendMessage(JSONObject.toJSONString(model));
        return;
    }
}
