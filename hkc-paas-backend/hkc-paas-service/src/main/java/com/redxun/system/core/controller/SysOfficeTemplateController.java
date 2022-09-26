package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.system.core.entity.SysOfficeTemplate;
import com.redxun.system.core.service.SysOfficeTemplateService;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/system/core/sysOfficeTemplate")
@ClassDefine(title = "office模板",alias = "sysOfficeTemplateController",path = "/system/core/sysOfficeTemplate",packages = "core",packageName = "系统管理")
@Api(tags = "Office模板")
public class SysOfficeTemplateController extends BaseController<SysOfficeTemplate> {

    @Autowired
    SysOfficeTemplateService sysOfficeTemplateService;

    @Override
    public BaseService getBaseService() {
        return sysOfficeTemplateService;
    }

    @Override
    public String getComment() {
        return "Office模板";
    }

}
