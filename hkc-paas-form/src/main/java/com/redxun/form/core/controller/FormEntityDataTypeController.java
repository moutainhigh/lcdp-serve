
package com.redxun.form.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.form.core.entity.FormCustomQuery;
import com.redxun.form.core.entity.FormEntityDataType;
import com.redxun.form.core.service.FormEntityDataTypeServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/form/core/formEntityDataType")
@Api(tags = "业务实体数据类型")
@ClassDefine(title = "业务实体数据类型",alias = "FormEntityDataTypeController",path = "/form/core/formEntityDataType",packages = "core",packageName = "表单管理")
public class FormEntityDataTypeController extends BaseController<FormEntityDataType> {

    @Autowired
    FormEntityDataTypeServiceImpl formEntityDataTypeService;


    @Override
    public BaseService getBaseService() {
        return formEntityDataTypeService;
    }

    @Override
    public String getComment() {
        return "业务实体数据类型";
    }

    @Override
    protected JsonResult beforeSave(FormEntityDataType ent) {
        boolean isExist= formEntityDataTypeService.isExist(ent);
        if(isExist){
            return  JsonResult.Fail("类型名称已存在!");
        }
        return  JsonResult.Success();
    }
}

