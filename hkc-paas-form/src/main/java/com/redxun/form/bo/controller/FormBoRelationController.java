package com.redxun.form.bo.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.form.bo.entity.FormBoRelation;
import com.redxun.form.bo.service.FormBoRelationServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/form/bo/formBoRelation")
@ClassDefine(title = "业务实体关系",alias = "formBoRelationController",path = "/form/bo/formBoRelation",packages = "bo",packageName = "业务模型管理")
@Api(tags = "业务实体关系")
public class FormBoRelationController extends BaseController<FormBoRelation> {

    @Autowired
    FormBoRelationServiceImpl formBoRelationServiceImpl;

    @Override
    public BaseService getBaseService() {
        return formBoRelationServiceImpl;
    }

    @Override
    public String getComment() {
        return "业务实体关系";
    }

}