
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.form.core.entity.FormChangeLog;
import com.redxun.form.core.service.FormChangeLogServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.log.annotation.AuditLog;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/form/core/formChangeLog")
@Api(tags = "表单变更记录")
@ClassDefine(title = "表单变更记录", alias = "FormChangeLogController", path = "/form/core/formChangeLog", packages = "core", packageName = "子系统名称")

public class FormChangeLogController extends BaseController<FormChangeLog> {

    @Autowired
    FormChangeLogServiceImpl formChangeLogService;


    @Override
    public BaseService getBaseService() {
        return formChangeLogService;
    }

    @Override
    public String getComment() {
        return "表单变更记录";
    }


    @MethodDefine(title = "根据数据源别名获取表单变更记录", path = "/getFormChangeLog", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "数据源别名", varName = "dsAlias"),@ParamDefine(title = "参数", varName = "params")})
    @ApiOperation("根据数据源别名获取表单变更记录")
    @AuditLog(operation = "根据数据源别名获取表单变更记录")
    @PostMapping(value = "getFormChangeLog")
    public JsonResult getFormChangeLog(@RequestParam(value = "dsAlias") String dsAlias,@RequestParam(value = "params") String params){
        return formChangeLogService.getFormChangeLog(dsAlias,params);
    }



    @MethodDefine(title = "租户同步表单", path = "/syncForm", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "提交数据", varName = "jsonObject")})
    @ApiOperation("租户同步表单")
    @AuditLog(operation = "租户同步表单")
    @PostMapping(value = "syncForm")
    public JsonResult syncForm(@RequestBody JSONObject jsonObject){
        if(BeanUtil.isEmpty(jsonObject)){
            return new JsonResult(false,"表单变更记录ID为空!");
        }
        return  formChangeLogService.syncForm(jsonObject);
    }

    @MethodDefine(title = "根据数据源别名获取已执行的记录", path = "/getExecutedLog", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "数据源别名", varName = "dsAlias")})
    @ApiOperation("根据数据源别名获取已执行的记录")
    @AuditLog(operation = "根据数据源别名获取已执行的记录")
    @GetMapping(value = "getExecutedLog")
    public JsonResult getExecutedLog(@RequestParam(value = "dsAlias") String dsAlias){
        return formChangeLogService.getExecutedLog(dsAlias);
    }

    @MethodDefine(title = "设置忽略", path = "/setIgnore", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "数据源别名", varName = "dsAlias"),@ParamDefine(title = "变更记录ID", varName = "changeLogId")
                    ,@ParamDefine(title = "忽略", varName = "ignore")})
    @ApiOperation("设置忽略")
    @AuditLog(operation = "设置忽略")
    @PostMapping(value = "setIgnore")
    public JsonResult setIgnore(@RequestParam(value = "dsAlias") String dsAlias,@RequestParam(value = "changeLogId") String changeLogId,
                                @RequestParam(value = "ignore") Boolean ignore){
        return formChangeLogService.setIgnore(dsAlias,changeLogId,ignore);
    }


    @MethodDefine(title = "根据数据源别名获取获取忽略的记录", path = "/getIgnoreFormChangeLog", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "数据源别名", varName = "dsAlias")})
    @ApiOperation("根据数据源别名获取获取忽略的记录")
    @AuditLog(operation = "根据数据源别名获取获取忽略的记录")
    @GetMapping(value = "getIgnoreFormChangeLog")
    public JsonResult getIgnoreFormChangeLog(@RequestParam(value = "dsAlias") String dsAlias){
        return formChangeLogService.getIgnoreFormChangeLog(dsAlias);
    }
}

