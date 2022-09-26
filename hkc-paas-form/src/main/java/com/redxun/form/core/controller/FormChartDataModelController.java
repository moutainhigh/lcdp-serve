
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.form.core.entity.FormChartDataModel;
import com.redxun.form.core.service.FormChartDataModelServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/form/core/chartDataModel")
@Api(tags = "图表数据模型")
@ClassDefine(title = "图表数据模型", alias = "ChartDataModelController", path = "/form/core/chartDataModel", packages = "core")
public class FormChartDataModelController extends BaseController<FormChartDataModel> {

    @Autowired
    FormChartDataModelServiceImpl formChartDataModelService;

    @Override
    public BaseService getBaseService() {
        return formChartDataModelService;
    }

    @Override
    public String getComment() {
        return "图表数据模型";
    }

    @MethodDefine(title = "根据sql获取数据", path = "/getData", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "config", varName = "config")})
    @ApiOperation("获取数据")
    @PostMapping("/getData")
    public JSONObject getData(@RequestBody JSONObject config) {
        return formChartDataModelService.getData(config);
    }

}

