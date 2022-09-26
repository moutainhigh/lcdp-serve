
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.system.core.entity.ImportExcelLog;
import com.redxun.system.core.service.ImportExcelLogServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/core/importExcelLog")
@Api(tags = "Excel导入日志")
@ClassDefine(title = "Excel导入日志",alias = "importExcelController",path = "/system/core/importExcelLog",packages = "core",packageName = "系统管理")
public class ImportExcelLogController extends BaseController<ImportExcelLog> {

    @Autowired
    ImportExcelLogServiceImpl importExcelLogService;


    @Override
    public BaseService getBaseService() {
        return importExcelLogService;
    }

    @Override
    public String getComment() {
        return "Excel导入日志";
    }

    @MethodDefine(
            title = "根据主键查询记录详细信息",
            path = "/getByTemplateId",
            method = HttpMethodConstants.GET,
            params = {@ParamDefine(
                    title = "模板ID",
                    varName = "pkId"
            )}
    )
    @ApiOperation(
            value = "查看单条记录信息",
            notes = "根据模板查询记录详细信息"
    )
    @GetMapping("/getByTemplateId")
    public JsonResult getByTemplateId(@RequestParam("pkId") String pkId) {
        List<ImportExcelLog> list = importExcelLogService.getByTemplate(pkId);
        return JsonResult.Success().setData(list);
    }
    @MethodDefine(
            title = "根据主键删除记录详细信息",
            path = "/delByTemplateId",
            method = HttpMethodConstants.GET,
            params = {@ParamDefine(
                    title = "模板ID",
                    varName = "pkId"
            )}
    )
    @ApiOperation(
            value = "删除记录信息",
            notes = "根据主键删除记录详细信息"
    )
    @GetMapping("/delByTemplateId")
    public JsonResult delByTemplateId(@RequestParam("pkId") String pkId) {
        importExcelLogService.delByTemplate(pkId);
        return JsonResult.Success("清空日志成功！");
    }

}

