
package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysLog;
import com.redxun.system.core.mapper.SysLogMapper;
import com.redxun.system.ext.log.ILogResume;
import com.redxun.system.ext.log.LogResumeContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [系统日志]业务服务类
*/
@Service
public class SysLogServiceImpl extends SuperServiceImpl<SysLogMapper, SysLog> implements BaseService<SysLog> {

    @Resource
    private SysLogMapper sysLogMapper;

    @Override
    public BaseDao<SysLog> getRepository() {
        return sysLogMapper;
    }

    public JsonResult resumeById(String id,String extParams) {
        SysLog sysLog=get(id);
        if(sysLog==null || !MBoolean.NO.name().equals(sysLog.getIsResume())){
            return JsonResult.Success();
        }
        ILogResume logResume=LogResumeContext.getLogResume(sysLog.getBusType());
        if(logResume==null){
            return JsonResult.Success();
        }
        try {
            sysLog.setIsResume(MBoolean.YES.name());
            update(sysLog);
            logResume.resume(sysLog.getAction(),sysLog.getDetail(),extParams);
        }catch (Exception e){
            return JsonResult.Fail(e.getMessage());
        }
        return JsonResult.Success();
    }
}
