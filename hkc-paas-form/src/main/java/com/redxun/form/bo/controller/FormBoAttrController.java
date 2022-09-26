package com.redxun.form.bo.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.form.bo.entity.FormBoAttr;
import com.redxun.form.bo.entity.FormBoEntity;
import com.redxun.form.bo.service.FormBoAttrServiceImpl;
import com.redxun.form.bo.service.FormBoEntityServiceImpl;
import com.redxun.form.core.entity.FormPc;
import com.redxun.form.core.service.FormPcServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/form/bo/formBoAttr")
@ClassDefine(title = "业务实体属性",alias = "formBoAttrController",path = "/form/bo/formBoAttr",packages = "bo",packageName = "业务模型管理")
@Api(tags = "业务实体属性")
public class FormBoAttrController extends BaseController<FormBoAttr> {

    @Autowired
    FormBoAttrServiceImpl formBoAttrServiceImpl;
    @Autowired
    FormBoEntityServiceImpl formBoEntityServiceImpl;
    @Autowired
    FormPcServiceImpl formPcServiceImpl;


    @Override
    public BaseService getBaseService() {
        return formBoAttrServiceImpl;
    }

    @Override
    public String getComment() {
        return "业务实体属性";
    }

    /**
     *
     * @param formId 表单Id
     * @return
     */
    @MethodDefine(title = "根据表单ID获取单据的属性列表", path = "/getByFormId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "表单Id", varName = "formId")})
    @ApiOperation("根据表单ID获取单据的属性列表")
    @GetMapping(value = "getByFormId")
    public List<FormBoAttr> getByFormId(@RequestParam(value = "formId")String formId){
        FormPc formPc= formPcServiceImpl.get(formId);
        if(FormPc.FORM_TYPE_SEL_DEV.equals(formPc.getType()) || FormPc.CONTAI_TYPE_.equals(formPc.getType()) ){
            return new ArrayList<>();
        }
        String boDefId=formPc.getBodefId();
        FormBoEntity boEntity= formBoEntityServiceImpl.getMainEntByDefId(boDefId);
        List<FormBoAttr> attrList= formBoAttrServiceImpl.getByEntId(boEntity.getId());
        addBoCreateByAttr(attrList);
        return attrList;
    }

    /**
     *
     * @param entId 实体ID
     * @return
     */
    @MethodDefine(title = "根据实体ID获取单据的属性列表", path = "/getByEntId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "实体ID", varName = "ent")})
    @ApiOperation("根据实体ID获取单据的属性列表")
    @GetMapping(value = "getByEntId")
    public List<FormBoAttr> getByEntId(@RequestParam(value = "entId")String entId){
        return formBoAttrServiceImpl.getByEntId(entId);
    }


    /**
     * 添加创建人属性
     * @param attrList
     */
    private void  addBoCreateByAttr(List<FormBoAttr> attrList){
        FormBoAttr attr=new FormBoAttr();
        attr.setName(FormBoEntity.FIELD_CREATE_BY);
        attr.setComment("创建人");
        attrList.add(attr);
    }

}