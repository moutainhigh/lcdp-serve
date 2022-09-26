package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.core.entity.FormBoList;
import com.redxun.form.core.entity.FormBoPmt;
import com.redxun.form.core.entity.GlobalVar;
import com.redxun.form.core.service.FormBoListServiceImpl;
import com.redxun.form.core.service.FormBoPmtServiceImpl;
import com.redxun.form.core.service.GlobalVarServiceImpl;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/form/core/globalVar")
@ClassDefine(title = "代码生成全局变量",alias = "globalVarController",path = "/form/core/globalVar",packages = "core",packageName = "表单管理")
@Api(tags = "代码生成全局变量")
public class GlobalVarController extends BaseController<GlobalVar> {

    @Autowired
    GlobalVarServiceImpl    globalVarService;


    @Override
    public BaseService getBaseService() {
        return globalVarService;
    }

    @Override
    public String getComment() {
        return "代码生成全局变量";
    }

    @PostMapping("/saveConfig")
    public JsonResult saveConfig(@RequestBody String config){
        String userId= ContextUtil.getCurrentUserId();
        GlobalVar globalVar=globalVarService.getByUserId(userId);
        if(globalVar==null){
            globalVar=new GlobalVar();
            globalVar.setConfig(config);
            globalVarService.insert(globalVar);
        }
        else{
            globalVar.setConfig(config);
            globalVarService.update(globalVar);
        }
        return JsonResult.Success("保存全局变量成功!");

    }

}
