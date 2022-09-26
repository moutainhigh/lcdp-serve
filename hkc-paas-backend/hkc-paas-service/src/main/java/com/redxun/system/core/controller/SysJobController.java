
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.log.util.EntityUtil;
import com.redxun.system.core.entity.SysJob;
import com.redxun.system.core.service.SysJobServiceImpl;
import com.redxun.system.core.service.job.JobService;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/core/sysJob")
@Api(tags = "系统定时任务")
@ClassDefine(title = "系统定时任务",alias = "sysJobController",path = "/system/core/sysJob",packages = "core",packageName = "子系统名称")

public class SysJobController extends BaseController<SysJob> {

    @Autowired
    SysJobServiceImpl sysJobService;
    @Autowired
    JobService jobService;


    @Override
    public BaseService getBaseService() {
    return sysJobService;
    }

    @Override
    public String getComment() {
    return "系统定时任务";
    }

    @MethodDefine(title = "保存任务调度", path = "/save", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "KETTLE任务", varName = "sysKettleJob")})
    @ApiOperation(value="保存任务调度", notes="保存任务调度")
    @AuditLog(operation = "保存任务调度")
    @PostMapping("/save")
    public JsonResult save(@ApiParam @RequestBody SysJob sysJob, BindingResult validResult) throws Exception{
        boolean isAdd=false;
        if(StringUtils.isEmpty(sysJob.getId())){
            String pkId= IdGenerator.getIdStr();
            sysJob.setId(pkId);
            isAdd=true;
        }
        JsonResult result=null;
        if(isAdd){
            result = sysJobService.addJob(sysJob);
        }else{
            result =sysJobService.updateJob(sysJob);
        }
        return result;
    }

    @MethodDefine(title = "根据实体Id删除业务行信息", path = "/del", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实体ID", varName = "ids")})
    @ApiOperation(value="删除业务行数据", notes="根据实体Id删除业务行信息,parameters is {ids:'1,2'}")
    @AuditLog(operation = "删除业务行数据")
    @PostMapping("del")
    public JsonResult del(@RequestParam(value = "ids") String ids){

        if(StringUtils.isEmpty(ids)){
            return new JsonResult(false,"");
        }
        String[] aryId=ids.split(",");
        List list= Arrays.asList(aryId);

        JsonResult rtn= beforeRemove(list);
        if(!rtn.isSuccess()){
            return rtn;
        }


        JsonResult result=JsonResult.getSuccessResult("删除"+getComment()+"成功!");

        LogContext.put(Audit.OPERATION,"删除"+getComment());

        String detail="";

        LogContext.put(Audit.ACTION,Audit.ACTION_DEL);

        for (Object id: list) {
            String idStr=id.toString();
            SysJob sysKettleJob=sysJobService.get(idStr);
            if(sysKettleJob==null){
                continue;
            }
            String fieldInfo= EntityUtil.getInfo(sysKettleJob,false);
            detail+=fieldInfo +"\r\n";
        }
        LogContext.put(Audit.DETAIL,detail);

        sysJobService.deleteJob(list);
        return result;
    }


    @MethodDefine(title = "执行JOB", path = "/run", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实体ID", varName = "id")})
    @ApiOperation(value="执行JOB", notes="执行JOB,parameters is {ids:'1,2'}")
    @AuditLog(operation = "执行JOB")
    @PostMapping("run")
    public JsonResult run(@RequestParam(value = "id") String id){

        if(StringUtils.isEmpty(id)){
            return new JsonResult(false,"请传入任务ID");
        }

        SysJob job=sysJobService.get(id);

        jobService.trigger(id,job.getJobTaskId());

        String detail="执行"+job.getName()+"成功!";

        LogContext.put(Audit.PK,id);

        LogContext.put(Audit.DETAIL,detail);


        JsonResult result=JsonResult.getSuccessResult("执行"+job.getName()+"成功!");


        return result;
    }

}

