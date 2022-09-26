
package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.system.core.entity.SysLog;
import com.redxun.system.core.service.SysLogServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/system/core/sysLog")
@ClassDefine(title = "日志管理",alias = "sysLogController",path = "/system/core/sysLog",packages = "system",packageName = "系统管理")
@Api(tags = "系统日志")
@ContextQuerySupport(company = ContextQuerySupport.NONE)
public class SysLogController extends BaseController<SysLog> {

    @Autowired
    SysLogServiceImpl sysLogService;


    @Override
    public BaseService getBaseService() {
    return sysLogService;
    }

    @Override
    public String getComment() {
    return "系统日志";
    }

    @MethodDefine(title = "根据日志恢复数据", path = "/resumeByIds", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "恢复参数", varName = "json")})
    @ApiOperation(value = "根据日志恢复数据")
    @PostMapping("/resumeByIds")
    public JsonResult resumeByIds(@RequestBody JSONObject json){
        String ids=json.getString("ids");
        if(StringUtils.isEmpty(ids)){
            return new JsonResult(false,"");
        }
        String extParams=null;
        if(json.containsKey("extParams")){
            extParams=json.getString("extParams");
        }
        String[] aryId=ids.split(",");
        for(String id:aryId){
            JsonResult result=sysLogService.resumeById(id,extParams);
            if(!result.isSuccess()){
                return result;
            }
        }
        return JsonResult.Success("恢复成功！");
    }

}

