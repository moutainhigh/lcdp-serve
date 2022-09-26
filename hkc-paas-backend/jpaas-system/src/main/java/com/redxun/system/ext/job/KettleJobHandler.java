package com.redxun.system.ext.job;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.system.core.entity.SysKettleDef;
import com.redxun.system.core.entity.SysKettleJob;
import com.redxun.system.core.entity.SysKettleLog;
import com.redxun.system.core.service.SysKettleDefServiceImpl;
import com.redxun.system.core.service.SysKettleJobServiceImpl;
import com.redxun.system.core.service.SysKettleLogServiceImpl;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class KettleJobHandler extends IJobHandler {

    @Autowired
    SysKettleJobServiceImpl sysKettleJobService;
    @Autowired
    SysKettleDefServiceImpl sysKettleDefService;
    @Autowired
    SysKettleLogServiceImpl sysKettleLogService;

    @Override
    @XxlJob("KettleJobHandler")
    public void execute() throws Exception {

        String param = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数....[{}]", param);

        SysKettleJob sysKettleJob = sysKettleJobService.get(param);
        if(BeanUtil.isEmpty(sysKettleJob)){
            return ;
        }
        SysKettleDef sysKettleDef = sysKettleDefService.get(sysKettleJob.getKettleId());
        //保存日志
        SysKettleLog sysKettleLog=new SysKettleLog();
        sysKettleLog.setId(IdGenerator.getIdStr());
        sysKettleLog.setStartTime(new Date());
        sysKettleLog.setKettleId(sysKettleJob.getKettleId());
        sysKettleLog.setKettleType(sysKettleDef.getType());
        sysKettleLog.setKettleJobId(sysKettleJob.getId());
        sysKettleLog.setKettleJobName(sysKettleJob.getName());
        sysKettleLog.setTenantId("1");

        //运行Kettle
        JsonResult jsonResult = sysKettleDefService.runKettle(sysKettleJob.getKettleId(),sysKettleJob.getLoglevel());

        sysKettleLog.setLog(jsonResult.getMessage());
        sysKettleLog.setStatus(jsonResult.isSuccess()?1:0);
        sysKettleLog.setEndTime(new Date());
        //计算时间差
        long diff = sysKettleLog.getEndTime().getTime() - sysKettleLog.getStartTime().getTime();
        sysKettleLog.setDruation(diff);
        sysKettleLogService.insert(sysKettleLog);
        if(!jsonResult.isSuccess()){
            return ;
        }
        return ;
    }
}
