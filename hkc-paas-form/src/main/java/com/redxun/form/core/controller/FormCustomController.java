package com.redxun.form.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.form.core.entity.FormCustom;
import com.redxun.form.core.service.FormCustomServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/form/core/formCustom")
@Api(tags = "表单定制")
@ClassDefine(title = "表单定制",alias = "formCustomController",path = "/form/core/formCustom",packages = "core",packageName = "表单管理")
public class FormCustomController extends BaseController<FormCustom> {

    @Autowired
    FormCustomServiceImpl formCustomService;

    @Override
    public BaseService getBaseService() {
        return formCustomService;
    }

    @Override
    public String getComment() {
        return "表单定制";
    }

    @Override
    protected JsonResult beforeSave(FormCustom ent) {
        boolean isExist= formCustomService.isExist(ent);
        if(isExist){
            return JsonResult.Fail("表单定制别名已存在！!");
        }
        return JsonResult.Success();
    }

    /**
     * 根据单据别名获取自定义表单
     * @param alias
     * @return
     */
    @ApiOperation("根据单据别名获取自定义表单")
    @GetMapping("/getByAlias")
    public FormCustom getByAlias(@RequestParam(value = "alias")String alias){
        return formCustomService.getByAlias(alias);
    }


}
