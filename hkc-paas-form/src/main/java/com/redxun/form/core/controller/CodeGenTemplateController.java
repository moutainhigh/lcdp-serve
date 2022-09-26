
package com.redxun.form.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.form.core.entity.CodeGenTemplate;
import com.redxun.form.core.service.CodeGenTemplateServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
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
@RequestMapping("/form/core/codeGenTemplate")
@Api(tags = "代码生成模板")
@ClassDefine(title = "代码生成模板",alias = "CodeGenTemplateController",path = "/form/core/codeGenTemplate",packages = "core",packageName = "子系统名称")
public class CodeGenTemplateController extends BaseController<CodeGenTemplate> {

    @Autowired
    CodeGenTemplateServiceImpl codeGenTemplateService;


    @Override
    public BaseService getBaseService() {
        return codeGenTemplateService;
    }

    @Override
    public String getComment() {
        return "代码生成模板";
    }



}

