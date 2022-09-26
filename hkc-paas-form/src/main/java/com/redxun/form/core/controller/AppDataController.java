package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.form.core.service.impl.AppDataServiceImpl;
import com.redxun.log.annotation.AuditLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * @Description: 与应用相关的数据处理
 * @Author: Elwin ZHANG
 * @Date: 2021/6/25 15:58
 **/
@Slf4j
@RestController
@RequestMapping("/form/core/appdata")
@ClassDefine(title = "应用数据处理", alias = "AppDataController", path = "/form/core/appdata", packages = "core", packageName = "表单管理")
@Api(tags = "应用数据处理")
public class AppDataController {
    @Autowired
    AppDataServiceImpl appDataService;

    @MethodDefine(title = "删除本数据库中应用相关的数据", path = "/delete", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "应用ID", varName = "appId")})
    @ApiOperation("删除本数据库中应用相关的数据")
    @AuditLog(operation = "删除应用")
    @PostMapping("/delete")
    public void delete(@ApiParam @RequestParam(value = "appId") String appId) {
        appDataService.delete(appId);
    }

    @MethodDefine(title = "查询本数据库中应用相关的数据", path = "/query", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "应用ID", varName = "appId"),@ParamDefine(title = "是否导出自定义实体数据", varName = "exportCustomTables")})
    @ApiOperation("查询本数据库中应用相关的数据")
    @GetMapping("/query")
    public Map<String,String> query(@ApiParam @RequestParam(value = "appId") String appId ,
                                    @ApiParam @RequestParam(value = "exportCustomTables") boolean exportCustomTables) {
        return appDataService.query(appId,exportCustomTables);
    }

    @MethodDefine(title = "导入本数据库中应用相关的数据", path = "/install", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "应用相关数据", varName = "data")})
    @ApiOperation("导入本数据库中应用相关的数据")
    @PostMapping("/install")
    @AuditLog(operation = "导入应用")
    public JsonResult install(@ApiParam @RequestBody  String data){
        try {
            return appDataService.install(data);
        }catch (Exception e){
            return JsonResult.Fail("导入应用表单数据失败：" + e.getMessage());
        }
    }

    @MethodDefine(title = "导入前校验本数据库中应用相关的数据", path = "/importCheck", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "表数据组", varName = "array")})
    @ApiOperation("导入前校验本数据库中应用相关的数据")
    @PostMapping("/importCheck")
    public JsonResult importCheck(@ApiParam @RequestBody Object[] array){
        return appDataService.checkTables(array);
    }
}
