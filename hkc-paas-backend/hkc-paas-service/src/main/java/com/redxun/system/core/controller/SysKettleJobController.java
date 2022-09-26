
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
import com.redxun.system.core.entity.SysKettleJob;
import com.redxun.system.core.service.SysKettleJobServiceImpl;
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
@RequestMapping("/system/core/sysKettleJob")
@Api(tags = "KETTLE任务调度")
@ClassDefine(title = "KETTLE任务调度",alias = "SysKettleJobController",path = "/system/core/sysKettleJob",packages = "core",packageName = "系统管理")
public class SysKettleJobController extends BaseController<SysKettleJob> {

    @Autowired
    SysKettleJobServiceImpl sysKettleJobService;


    @Override
    public BaseService getBaseService() {
    return sysKettleJobService;
    }

    @Override
    public String getComment() {
    return "KETTLE任务调度";
    }


    @MethodDefine(title = "保存KETTLE任务", path = "/save", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "KETTLE任务", varName = "sysKettleJob")})
    @ApiOperation(value="保存KETTLE任务", notes="保存KETTLE任务")
    @AuditLog(operation = "保存KETTLE任务")
    @PostMapping("/save")
    public JsonResult save(@ApiParam @RequestBody SysKettleJob sysKettleJob, BindingResult validResult) throws Exception{
        boolean isAdd=false;
        if(StringUtils.isEmpty(sysKettleJob.getId())){
            String pkId= IdGenerator.getIdStr();
            sysKettleJob.setId(pkId);
            isAdd=true;
        }
        String opMsg="";
        if(isAdd){
            sysKettleJobService.addJob(sysKettleJob);
            opMsg="保存KETTLE任务成功！";
        }else{
            sysKettleJobService.updateJob(sysKettleJob);
            opMsg="更新KETTLE任务成功!";
        }
        return new JsonResult(true,sysKettleJob,opMsg);
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
            SysKettleJob sysKettleJob=sysKettleJobService.get(idStr);
            if(sysKettleJob==null){
                continue;
            }
            String fieldInfo=EntityUtil.getInfo(sysKettleJob,false);
            detail+=fieldInfo +"\r\n";
        }
        LogContext.put(Audit.DETAIL,detail);

        sysKettleJobService.deleteJob(list);
        return result;
    }

}

