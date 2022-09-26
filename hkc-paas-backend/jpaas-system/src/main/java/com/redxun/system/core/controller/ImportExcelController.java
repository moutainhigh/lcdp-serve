
package com.redxun.system.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.system.core.entity.ImportExcel;
import com.redxun.system.core.service.ImportExcelLogServiceImpl;
import com.redxun.system.core.service.ImportExcelServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/system/core/importExcel")
@Api(tags = "系统Excel数据导入")
@ClassDefine(title = "Excel导入",alias = "importExcelController",path = "/system/core/importExcel",packages = "core",packageName = "系统管理")
public class ImportExcelController extends BaseController<ImportExcel> {

    @Autowired
    ImportExcelServiceImpl importExcelService;

    @Autowired
    ImportExcelLogServiceImpl importExcelLogService;

    @Override
    public BaseService getBaseService() {
        return importExcelService;
    }

    @Override
    public String getComment() {
        return "系统Excel数据导入";
    }


    @MethodDefine(title = "导入", path = "/doImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @ApiOperation("预览表单数据")
    @PostMapping(value = "/doImport")
    public JsonResult doImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("file");

        Map<String,List<List<String>>> result=new HashMap<>();
        try {
            result = importExcelService.writeWithoutHead(file.getInputStream(),true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return JsonResult.Success().setData(result);
    }


    @MethodDefine(title = "执行导入", path = "/executeImport", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "列表ID", varName = "request")})
    @AuditLog(operation = "执行EXCEL批量导入")
    @ApiOperation("执行EXCEL批量导入")
    @PostMapping("/executeImport")
    public JsonResult executeImport(MultipartHttpServletRequest request) throws Exception {
        MultipartFile file = request.getFile("file");

        Map<String,List<List<String>>> result=new HashMap<>();
        try {
            result = importExcelService.writeWithoutHead(file.getInputStream(),false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String pkId = request.getParameter("pkId");

        ImportExcel importExcel = importExcelService.get(pkId);

        JSONArray gridData = JSON.parseArray(importExcel.getGridData());

        String errorMsg = "";

        for(int i = 0; i < gridData.size();i++) {
            String sheetError = "";
            Map<String, Object> currentSheet = (Map<String, Object>) gridData.get(i);
            String sheetName = (String) currentSheet.get("title");
            List<List<String>> currentExcel = result.get(sheetName);

            sheetError = importExcelService.execute(pkId, currentExcel, currentSheet);
            if(StringUtils.isNotEmpty(sheetError)){
                errorMsg += sheetName + ":" + "\n" + sheetError;
            }
        }

        String detail="执行EXCEL批量导入:" + importExcel.getName() +"("+pkId+")";
        if(StringUtils.isNotEmpty(errorMsg)){
            detail+=",导入错误信息:" + errorMsg;
        }
        LogContext.put(Audit.DETAIL,detail);

        importExcelLogService.handleError(errorMsg,pkId);
        return JsonResult.Success().setMessage("导入完成，详情请看日志");
    }
}

