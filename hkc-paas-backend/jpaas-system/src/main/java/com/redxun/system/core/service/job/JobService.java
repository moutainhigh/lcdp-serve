package com.redxun.system.core.service.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.HttpClientUtil;
import com.redxun.common.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 定时任务操作类。
 */
@Service
public class JobService {

    @Value("${job.center:http://localhost:7878/job}")
    private String jobCenter;



    /**
     * 构造job参数。
     * @param jobModel
     * @param curUser
     * @return
     */
    private Map<String,String> getJobMap(JobModel jobModel,IUser curUser){

        Map<String,String> jobMap=new HashMap<>();

        jobMap.put("id", jobModel.getId());
        // 执行器主键ID
        jobMap.put("jobGroup", jobModel.getJobId());
        // 任务描述
        jobMap.put("jobDesc", jobModel.getName());

        // 负责人
        jobMap.put("author", curUser.getAccount());
        // 报警邮件
        jobMap.put("alarmEmail", curUser.getEmail());

        // 执行器路由策略
        jobMap.put("executorRouteStrategy", "FIRST");
        // 执行器，任务Handler名称 固定为KettleJobHandler
        jobMap.put("executorHandler", jobModel.getExecutorHandler());
        // todo 执行器，任务参数
        jobMap.put("executorParam",jobModel.getParams());
        // 阻塞处理策略
        jobMap.put("executorBlockStrategy", "SERIAL_EXECUTION");
        // 任务执行超时时间，单位秒
        jobMap.put("executorTimeout", "0");
        // 失败重试次数
        jobMap.put("executorFailRetryCount", "1");
        // GLUE类型    #com.xxl.job.core.glue.GlueTypeEnum
        jobMap.put("glueType", "BEAN");
        // GLUE备注
        jobMap.put("glueRemark", "执行Kettle任务");

        // 调度状态：0-停止，1-运行
        jobMap.put("triggerStatus", jobModel.getStatus());
        // 上次调度时间
        jobMap.put("triggerLastTime", "0");
        // 下次调度时间
        jobMap.put("triggerNextTime", "0");
        // 调度类型
        jobMap.put("scheduleType", "NONE");
        jobMap.put("scheduleConf", "");
        jobMap.put("cronGen_display", "");
        jobMap.put("schedule_conf_CRON", jobModel.getJobCron());
        jobMap.put("schedule_conf_FIX_RATE", "");
        jobMap.put("schedule_conf_FIX_DELAY", "");
        jobMap.put("childJobId", "");
        jobMap.put("DO_NOTHING", "");
        jobMap.put("misfireStrategy", "DO_NOTHING");
        return jobMap;
    }

    /**
     * 添加JOB
     * @param jobModel
     * @param user
     * @return
     * @throws Exception
     */
    public JsonResult addJob(JobModel jobModel, IUser user) throws Exception {
        Map<String, String> jobMap=getJobMap(jobModel,user);
        String url = jobCenter + "/jobinfo/add?authorization="+ TokenUtil.getToken();
        String res = HttpClientUtil.postFromUrl(url, jobMap);
        JSONObject jsonObject = JSON.parseObject(res);
        if(!"200".equals(jsonObject.getString("code"))){
            return new JsonResult().setSuccess(false).setMessage("新增job任务失败!");
        }
        return JsonResult.Success();
    }

    /**
     * 更新JOB
     * @param jobModel
     * @param user
     * @return
     * @throws Exception
     */
    public JsonResult updateJob(JobModel jobModel, IUser user) throws Exception {
        Map<String, String> jobMap=getJobMap(jobModel,user);
        String url = jobCenter + "/jobinfo/update?authorization="+TokenUtil.getToken();
        String res = HttpClientUtil.postFromUrl(url, jobMap);
        JSONObject jsonObject = JSON.parseObject(res);
        if(!"200".equals(jsonObject.getString("code"))){
            return new JsonResult().setSuccess(false).setMessage("更新job任务失败!");
        }
        return JsonResult.Success();
    }

    /**
     * 根据ID删除JOB
     * @param id
     * @return
     * @throws Exception
     */
    public JsonResult deleteJob(String id)  {
            String url = jobCenter + "/jobinfo/remove?id="+id+"&authorization="+TokenUtil.getToken();
            try{
                String res = HttpClientUtil.getFromUrl(url, null);
                JSONObject jsonObject = JSON.parseObject(res);

                if("200".equals(jsonObject.getString("code"))){
                    return JsonResult.Success();
                }
                return JsonResult.Fail(jsonObject.toJSONString());
            }
            catch (Exception ex){
                return JsonResult.Fail(ExceptionUtil.getExceptionMessage(ex));
            }
    }

    /**
     * 执行JOB
     * @param id
     * @param jobTaskId
     * @return
     */
    public JsonResult trigger(String id,String jobTaskId)  {
        Map<String,String> params=new HashMap<>();
        params.put("id",id);
        params.put("executorParam",jobTaskId);
        params.put("authorization",TokenUtil.getToken());
        String url = jobCenter + "/jobinfo/trigger";
        try{
            String res = HttpClientUtil.postFromUrl(url, params);
            JSONObject jsonObject = JSON.parseObject(res);

            if("200".equals(jsonObject.getString("code"))){
                return JsonResult.Success();
            }
            return JsonResult.Fail(jsonObject.toJSONString());
        }
        catch (Exception ex){
            return JsonResult.Fail(ExceptionUtil.getExceptionMessage(ex));
        }
    }

    /**
     * 获取JOB
     * @param appName
     * @return
     * @throws Exception
     */
    public JsonResult getJob(String appName) throws Exception {
        Map<String, String> params=new HashMap<>();
        params.put("appname",appName);
        params.put("start","0");
        params.put("length","10");
        String url = jobCenter + "/jobgroup/pageList?authorization="+ TokenUtil.getToken();
        String res = HttpClientUtil.postFromUrl(url, params);
        JSONObject jsonObject = JSON.parseObject(res);
        if(jsonObject.getInteger("recordsFiltered")==0){
            return new JsonResult().setSuccess(false).setMessage("获取job任务失败!");
        }
        JSONArray data = jsonObject.getJSONArray("data");
        return JsonResult.Success().setData(data.get(0));
    }


}

