
package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.SysKettleJob;
import com.redxun.system.core.mapper.SysKettleJobMapper;
import com.redxun.system.core.service.job.JobModel;
import com.redxun.system.core.service.job.JobService;
import com.redxun.system.core.service.job.JobUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [KETTLE任务调度]业务服务类
*/
@Service
public class SysKettleJobServiceImpl extends SuperServiceImpl<SysKettleJobMapper, SysKettleJob> implements BaseService<SysKettleJob> {

    @Resource
    private SysKettleJobMapper sysKettleJobMapper;

    @Override
    public BaseDao<SysKettleJob> getRepository() {
        return sysKettleJobMapper;
    }

    @Resource
    JobService jobService;

    /**
     * 添加job任务
     * @param sysKettleJob
     */
    public JsonResult addJob(SysKettleJob sysKettleJob) {

        try {
            JobModel model=getJobModel(sysKettleJob);
            IUser curUser=ContextUtil.getCurrentUser();
            JsonResult result= jobService.addJob(model,curUser);
            if(!result.isSuccess()){
                return result;
            }
            insert(sysKettleJob);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult().setSuccess(false).setMessage("新增job任务失败!");
        }
        return new JsonResult().setSuccess(true).setMessage("新增job任务成功!");
    }


    /**
     * 更新job任务
     * @param sysKettleJob
     */
    public JsonResult updateJob(SysKettleJob sysKettleJob) {

        try {
            JobModel model=getJobModel(sysKettleJob);
            IUser curUser=ContextUtil.getCurrentUser();

            JsonResult result= jobService.updateJob(model,curUser);
            if(!result.isSuccess()){
                return result;
            }

            update(sysKettleJob);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult().setSuccess(false).setMessage("更新job任务失败!");
        }
        return new JsonResult().setSuccess(true).setMessage("更新job任务成功!");
    }

    public void deleteJob(List idList) {
        for (int i = 0; i < idList.size(); i++) {
            String id= (String) idList.get(i);
            JsonResult result= jobService.deleteJob(id);
            if(result.isSuccess()){
                delete(id);
            }
        }
    }

    private JobModel getJobModel(SysKettleJob sysKettleJob){
        String cron= JobUtil.getJobCron(sysKettleJob.getStrategy());
        JobModel jobModel=new JobModel();
        jobModel.setJobGroup("system-job");
        jobModel.setName(sysKettleJob.getName());
        jobModel.setId(sysKettleJob.getId());
        jobModel.setJobCron(cron);
        jobModel.setStatus(sysKettleJob.getStatus().toString());
        jobModel.setParams(sysKettleJob.getId());

        return jobModel;
    }

}
