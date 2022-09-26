
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.form.core.entity.FormEntRelation;
import com.redxun.form.core.entity.FormSolution;
import com.redxun.form.core.service.FormEntRelationServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
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
@RequestMapping("/form/core/formEntRelation")
@Api(tags = "数据关联删除约束")
@ClassDefine(title = "数据关联删除约束", alias = "FormEntRelationController", path = "/form/core/formEntRelation", packages = "core", packageName = "子系统名称")

public class FormEntRelationController extends BaseController<FormEntRelation> {

    @Autowired
    FormEntRelationServiceImpl formEntRelationService;


    @Override
    public BaseService getBaseService() {
        return formEntRelationService;
    }

    @Override
    public String getComment() {
        return "数据关联删除约束";
    }

    @Override
    protected JsonResult beforeSave(FormEntRelation formEntRelation){
        boolean result= formEntRelationService.isExist(formEntRelation);
        if(result){
            return JsonResult.Fail("实体【"+formEntRelation.getEntName()+"】已存在约束!");
        }
        return JsonResult.Success();
    }

    /**
     * 根据表单方案别名获取判断数据是否能删除
     * @param jsonObject
     * @return
     */
    @MethodDefine(title = "根据表单方案别名获取判断数据是否能删除", path = "/isDeleteByFormSolAlias", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "根据表单方案别名获取判断数据是否能删除", varName = "jsonObject")})
    @ApiOperation("根据表单方案别名获取判断数据是否能删除")
    @AuditLog(operation = "根据表单方案别名获取判断数据是否能删除")
    @PostMapping(value = "isDeleteByFormSolAlias")
    public JsonResult isDeleteByFormSolAlias(@RequestBody JSONObject jsonObject) {
        JsonResult jsonResult = formEntRelationService.isDeleteByFormSolAlias(jsonObject);
        return jsonResult;

    }
}

