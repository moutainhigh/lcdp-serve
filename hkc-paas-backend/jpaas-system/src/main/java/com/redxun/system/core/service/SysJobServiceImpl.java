
package com.redxun.system.core.service;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.SysJob;
import com.redxun.system.core.mapper.SysJobMapper;
import com.redxun.system.core.service.job.JobModel;
import com.redxun.system.core.service.job.JobService;
import com.redxun.system.core.service.job.JobUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [系统定时任务]业务服务类
*/
@Service
public class SysJobServiceImpl extends SuperServiceImpl<SysJobMapper, SysJob> implements BaseService<SysJob> {

    @Resource
    private SysJobMapper sysJobMapper;

    @Override
    public BaseDao<SysJob> getRepository() {
        return sysJobMapper;
    }

    @Resource
    JobService jobService;

    private JobModel getJobModel(SysJob sysJob){
        String cron= JobUtil.getJobCron(sysJob.getStrategy());
        JobModel jobModel=new JobModel();
        try {
            JsonResult result = jobService.getJob("system-job");
            if(!result.isSuccess()){
                return null;
            }
            JSONObject data = (JSONObject) result.getData();
            jobModel.setJobGroup("system-job");
            jobModel.setJobId(data.getString("id"));
            jobModel.setName(sysJob.getName());
            jobModel.setId(sysJob.getId());
            jobModel.setJobCron(cron);
            jobModel.setStatus(sysJob.getStatus().toString());
            jobModel.setExecutorHandler("sysJobTaskHandler");
            jobModel.setParams(sysJob.getJobTaskId());
            return jobModel;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加job任务
     * @param sysJob
     */
    public JsonResult addJob(SysJob sysJob) {
        try {
            JobModel model=getJobModel(sysJob);
            if(BeanUtil.isEmpty(model)){
                return new JsonResult().setSuccess(false).setMessage("新增job任务失败,未找到执行器!");
            }
            IUser curUser= ContextUtil.getCurrentUser();
            JsonResult result= jobService.addJob(model,curUser);
            if(!result.isSuccess()){
                return result;
            }
            insert(sysJob);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult().setSuccess(false).setMessage("新增job任务失败!");
        }
        return new JsonResult().setSuccess(true).setMessage("新增job任务成功!");
    }


    /**
     * 更新job任务
     * @param sysJob
     */
    public JsonResult updateJob(SysJob sysJob) {
        try {
            JobModel model=getJobModel(sysJob);
            if(BeanUtil.isEmpty(model)){
                return new JsonResult().setSuccess(false).setMessage("新增job任务失败,未找到执行器!");
            }
            IUser curUser=ContextUtil.getCurrentUser();

            JsonResult result= jobService.updateJob(model,curUser);
            if(!result.isSuccess()){
                return result;
            }

            update(sysJob);
        }catch (Exception e){
            e.printStackTrace();
            return new JsonResult().setSuccess(false).setMessage("更新job任务失败!");
        }
        return new JsonResult().setSuccess(true).setMessage("更新job任务成功!");
    }

    /**
     * 删除JOB.
     * @param idList
     */
    public void deleteJob(List idList) {
        for (int i = 0; i < idList.size(); i++) {
            String id= (String) idList.get(i);
            JsonResult result= jobService.deleteJob(id);
            if(result.isSuccess()){
                delete(id);
            }
        }
    }

}
