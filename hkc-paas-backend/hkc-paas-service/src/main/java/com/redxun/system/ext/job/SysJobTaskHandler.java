package com.redxun.system.ext.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.db.CommonDao;
import com.redxun.system.core.entity.SysJobTask;
import com.redxun.system.core.service.SysJobTaskServiceImpl;
import com.redxun.system.core.service.job.ISysJobHandler;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 执行定时任务。
 */
@Slf4j
@Component
public class SysJobTaskHandler extends IJobHandler {

    @Autowired
    SysJobTaskServiceImpl jobTaskService;
    @Autowired
    CommonDao commonDao;
    @Autowired
    GroovyEngine groovyEngine;

    /**
     * 参数为 任务定义ID
     * @return
     * @throws Exception
     */
    @Override
    @XxlJob("sysJobTaskHandler")
    public void execute() throws Exception {

        //获取参数
        String jobTaskId = XxlJobHelper.getJobParam();
        log.info("接收调度中心参数....[{}]", jobTaskId);

        if(StringUtils.isEmpty(jobTaskId)){
            log.error("没有输入JOB定义ID");
            return ;
        }
        SysJobTask task = jobTaskService.get(jobTaskId);
        if(task==null){
            log.error("没有找到任务:"+ jobTaskId);
            return ;
        }

        if("0".equals(task.getStatus())){
            log.error("任务:"+ task.getName() +"已经禁用!");
            return ;
        }
        //执行任务。
        ReturnT rtn=handJob(task);

        return ;
    }

    /**
     * 执行任务。
     * @param task
     * @return
     * @throws ClassNotFoundException
     */
    ReturnT handJob(SysJobTask task )  {

        if(SysJobTask.TYPE_CLASS.equals( task.getType())){
            handClass(task);
        }

        if(SysJobTask.TYPE_SQL.equals( task.getType())){
            handSql(task);
        }

        if(SysJobTask.TYPE_SCRIPT.equals( task.getType())){
            handScript(task);
        }
        return ReturnT.SUCCESS;
    }

    /**
     * 执行SQL。
     * <pre>
     * sql 结构。
     * [{alias:"",sql:""}]
     * </pre>
     * @param task
     * @return
     */
    private ReturnT handSql(SysJobTask task){

        if(StringUtils.isEmpty( task.getContent())){
            log.error("SQL配置为空!");
            return ReturnT.FAIL;
        }

        JSONArray ary=JSONArray.parseArray(task.getContent());
        for(int i=0;i<ary.size();i++){
            JSONObject json=ary.getJSONObject(i);
            String alias=json.getString("alias");
            String sql=json.getString("sql");

            if(StringUtils.isEmpty(alias)){
                commonDao.execute(sql);
            }
            else{
                commonDao.execute(alias,sql);
            }
        }

        return ReturnT.SUCCESS;

    }

    /**
     * 执行脚本。
     * @param task
     * @return
     */
    private ReturnT handScript(SysJobTask task){
        groovyEngine.executeScripts(task.getContent(),null);

        log.debug(task.getContent());

        return ReturnT.SUCCESS;
    }

    /**
     * 执行类。
     * @param task
     * @return
     */
    private ReturnT handClass(SysJobTask task)   {
        Class cls=null;
        try{
            cls= Class.forName(task.getContent());
        }
        catch (ClassNotFoundException ex ){
            log.error(ExceptionUtil.getExceptionMessage(ex));
            return ReturnT.FAIL;
        }
        try{
            ISysJobHandler jobHandler= (ISysJobHandler) SpringUtil.getBean(cls);
            jobHandler.handJob();
            return ReturnT.SUCCESS;
        }
        catch (Exception ex){
            log.error(ExceptionUtil.getExceptionMessage(ex));
            return ReturnT.FAIL;
        }

    }

}
