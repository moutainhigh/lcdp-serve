
package com.redxun.form.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.form.core.entity.FormBusInstData;
import com.redxun.form.core.service.FormBusInstDataServiceImpl;
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
@RequestMapping("/form/core/formBusInstData")
@Api(tags = "表单业务数据")
@ClassDefine(title = "表单业务数据", alias = "FormBusInstDataController", path = "/form/core/formBusInstData", packages = "core", packageName = "子系统名称")

public class FormBusInstDataController extends BaseController<FormBusInstData> {

    @Autowired
    FormBusInstDataServiceImpl formBusInstDataService;


    @Override
    public BaseService getBaseService() {
        return formBusInstDataService;
    }

    @Override
    public String getComment() {
        return "表单业务数据";
    }

}

