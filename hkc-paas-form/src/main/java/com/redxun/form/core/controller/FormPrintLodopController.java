package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.form.core.entity.FormPrintLodop;
import com.redxun.form.core.service.FormPrintLodopServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/form/core/formPrintLodop")
@ClassDefine(title = "表单套打模板",alias = "formPrintLodopController",path = "/form/core/formPrintLodop",packages = "core",packageName = "表单管理")
@Api(tags = "表单套打模板")
public class FormPrintLodopController extends BaseController<FormPrintLodop> {

    @Autowired
    FormPrintLodopServiceImpl formPrintLodopService;

    @Override
    public BaseService getBaseService() {
        return formPrintLodopService;
    }

    @Override
    public String getComment() {
        return "表单套打模板";
    }

    @MethodDefine(title = "打印单据HTMl", path = "/printHtml", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求参数", varName = "jsonObject")})
    @ApiOperation("打印单据HTMl")
    @PostMapping("/printHtml")
    public JsonResult printHtml(@RequestBody JSONObject jsonObject) throws Exception{
        String pkId = jsonObject.getString("pkId");
        String formData = jsonObject.getString("formData");
        String html=formPrintLodopService.printHtml(pkId,formData);
        return JsonResult.Success().setData(html).setShow(false);
    }

}