
package com.redxun.form.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.form.core.entity.FormDatasourceShare;
import com.redxun.form.core.service.FormDatasourceShareServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/form/core/formDatasourceShare")
@Api(tags = "数据源共享")
@ClassDefine(title = "数据源共享", alias = "FormDatasourceShareController", path = "/form/core/formDatasourceShare", packages = "core", packageName = "子系统名称")

public class FormDatasourceShareController extends BaseController<FormDatasourceShare> {

    @Autowired
    FormDatasourceShareServiceImpl formDatasourceShareService;


    @Override
    public BaseService getBaseService() {
        return formDatasourceShareService;
    }

    @Override
    public String getComment() {
        return "数据源共享";
    }

}

