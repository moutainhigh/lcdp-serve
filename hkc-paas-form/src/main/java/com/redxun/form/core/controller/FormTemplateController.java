package com.redxun.form.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.form.core.entity.FormTemplate;
import com.redxun.form.core.service.FormTemplateServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/form/core/formTemplate")
@ClassDefine(title = "表单模版",alias = "formTemplateController",path = "/form/core/formTemplate",packages = "core",packageName = "表单管理")
@Api(tags = "表单模版")
public class FormTemplateController extends BaseController<FormTemplate> {


    @Override
    protected void handleFilter(QueryFilter filter) {
        String tenantId= ContextUtil.getCurrentTenantId();
        filter.addQueryParam("Q_TENANT_ID__S_IN", tenantId +",0");
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            filter.addQueryParam("Q_DELETED__S_EQ","0");
        }
    }

    @Autowired
    FormTemplateServiceImpl formTemplateService;

    @Override
    public BaseService getBaseService() {
        return formTemplateService;
    }

    @Override
    public String getComment() {
        return "表单模版";
    }

}
