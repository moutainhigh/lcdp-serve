package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.form.core.entity.FormBoList;
import com.redxun.form.core.entity.FormRule;
import com.redxun.form.core.service.FormRuleServiceImpl;
import com.redxun.form.core.service.export.FormRuleExport;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.web.controller.BaseController;
import com.redxun.web.controller.IExport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/form/core/formRule")
@ClassDefine(title = "表单校验配置",alias = "formRuleController",path = "/form/core/formRule",packages = "core",packageName = "表单管理")
@Api(tags = "表单校验配置")
@ContextQuerySupport(tenant = ContextQuerySupport.BOTH,company = ContextQuerySupport.BOTH)
public class FormRuleController extends BaseController<FormRule> {

    @Autowired
    FormRuleServiceImpl formRuleService;
    @Autowired
    FormRuleExport formRuleExport;

    @Override
    public BaseService getBaseService() {
        return formRuleService;
    }

    @Override
    public String getComment() {
        return "表单校验配置";
    }

    @Override
    protected IExport getExport() {
        return formRuleExport;
    }



    @Override
    protected JsonResult beforeSave(FormRule ent) {
        String pkId=ent.getId();
        boolean isExist=formRuleService.isExist(ent.getAlias(),pkId);
        if(isExist){
            LogContext.addError("表单校验已存在!");
            return JsonResult.Fail("表单校验已存在!");
        }
        return JsonResult.Success();
    }

    @MethodDefine(title = "导入表单规则", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @ApiOperation("导入表单规则")
    @AuditLog(operation = "导入表单规则")
    @PostMapping("/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("zipFile");
        String checkName = file.getOriginalFilename();
        CharSequence formsolution = ".zip";
        if(!checkName.contains(formsolution)){
            return JsonResult.Fail("请选择正确的压缩包");
        }
        String appId=request.getParameter("appId");
        formRuleService.importFormRule(file,appId);
        return JsonResult.Success().setMessage("导入成功");
    }

}
