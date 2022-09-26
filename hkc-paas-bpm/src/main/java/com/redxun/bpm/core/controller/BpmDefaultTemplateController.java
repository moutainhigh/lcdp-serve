
package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmDefaultTemplate;
import com.redxun.bpm.core.service.BpmDefaultTemplateServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmDefaultTemplate")
@Api(tags = "bpm_default_template")
@ClassDefine(title = "bpm_default_template",alias = "BpmDefaultTemplateController",path = "/bpm/core/bpmDefaultTemplate",packages = "core",packageName = "子系统名称")

public class BpmDefaultTemplateController extends BaseController<BpmDefaultTemplate> {

    @Autowired
    BpmDefaultTemplateServiceImpl bpmDefaultTemplateService;


    @Override
    public BaseService getBaseService() {
    return bpmDefaultTemplateService;
    }

    @Override
    public String getComment() {
    return "默认模板";
    }

    @Override
    protected JsonResult beforeSave(BpmDefaultTemplate ent) {
        if(bpmDefaultTemplateService.isExists(ent)){
            return JsonResult.Fail("模板已存在,不用重复添加!");
        }
        return super.beforeSave(ent);
    }

    @MethodDefine(title = "保存模板", path = "/saveTemplate", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "消息对象", varName = "template")})
    @AuditLog(operation = "保存默认模板")
    @PostMapping("saveTemplate")
    public JsonResult saveTemplate(@RequestBody BpmDefaultTemplate template){
        BpmDefaultTemplate defaultTemplate= bpmDefaultTemplateService.get(template.getId());
        defaultTemplate.setTemplate(template.getTemplate());
        bpmDefaultTemplateService.update(defaultTemplate);
        return JsonResult.Success("保存模板成功!");
    }
}

