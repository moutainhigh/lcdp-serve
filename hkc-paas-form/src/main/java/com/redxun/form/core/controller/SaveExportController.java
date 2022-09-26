
package com.redxun.form.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.form.core.entity.SaveExport;
import com.redxun.form.core.service.SaveExportServiceImpl;
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

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/form/core/saveExport")
@ClassDefine(title = "单据导出EXCEL列表配置",alias = "saveExportController",path = "/form/core/saveExport",packages = "core",packageName = "表单管理")
@Api(tags = "单据导出EXCEL列表配置")
public class SaveExportController extends BaseController<SaveExport> {

    @Autowired
    SaveExportServiceImpl saveExportService;


    @Override
    public BaseService getBaseService() {
        return saveExportService;
    }

    @Override
    public String getComment() {
        return "单据导出EXCEL列表配置";
    }



    @MethodDefine(title = "保存单据列表配置", path = "/saveConfigJson", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "配置信息", varName = "json")})
    @ApiOperation(value = "保存导出EXCEL列表配置")
    @AuditLog(operation = "保存导出EXCEL列表配置")
    @PostMapping("/saveConfigJson")
    public JsonResult saveConfigJson(@RequestBody JSONObject json) throws Exception {

        JsonResult result= saveExportService.addList(json);

        return result;
    }

    @MethodDefine(
            title = "根据列表名称查询数据",
            path = "/getByList",
            method = HttpMethodConstants.GET,
            params = {@ParamDefine(
                    title = "列表名称",
                    varName = "dataList"
            )}
    )
    @ApiOperation(
            value = "查找列表数据",
            notes = "根据列表名称查询数据。"
    )
    @GetMapping({"/getByList"})
    public JsonResult getByList(@RequestParam("dataList") String dataList) throws Exception {
        String userId = ContextUtil.getCurrentUserId();
        JsonResult result = JsonResult.Success();
        List data = saveExportService.getByList(dataList, userId);
        result.setData(data);
        return result;
    }


    @MethodDefine(
            title = "根据列表名称查询我创建的私人列表数据",
            path = "/getPrivateByList",
            method = HttpMethodConstants.GET,
            params = {@ParamDefine(
                    title = "列表名称",
                    varName = "dataList"
            )}
    )
    @ApiOperation(
            value = "根据列表名称查询我创建的私人列表数据",
            notes = "根据列表名称查询我创建的私人列表数据。"
    )
    @GetMapping({"/getPrivateByList"})
    public JsonResult getPrivateByList(@RequestParam("dataList") String dataList) throws Exception {
        String userId = ContextUtil.getCurrentUserId();
        List data = saveExportService.getByListPrivate(dataList, userId);
        JsonResult result = JsonResult.Success();
        result.setData(data);
        result.setShow(false);
        return result;
    }

    @MethodDefine(
            title = "根据列表名称查询公共导出列表",
            path = "/getByListPublic",
            method = HttpMethodConstants.GET,
            params = {@ParamDefine(
                    title = "列表名称",
                    varName = "dataList"
            )}
    )
    @ApiOperation(
            value = "根据列表名称查询公共导出列表",
            notes = "根据列表名称查询公共导出列表。"
    )
    @GetMapping({"/getByListPublic"})
    public JsonResult getByListPublic(@RequestParam("dataList") String dataList)  {
        List data = saveExportService.getByListPublic(dataList);
        JsonResult result = JsonResult.Success();
        result.setData(data);
        result.setShow(false);
        return result;
    }




    @MethodDefine(
            title = "根据名称导出配置",
            path = "/getByName",
            method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "配置名称",varName = "name"),
                    @ParamDefine(title = "列表名称",varName = "dataList")
            }
    )
    @ApiOperation(
            value = "导出配置数据",
            notes = "根据名称导出配置。"
    )

    @GetMapping({"/getByName"})
    public JsonResult getByName(@RequestParam("name") String name,@RequestParam("dataList") String dataList) throws Exception {
        SaveExport saveExport = saveExportService.getByName(name, dataList);
        JsonPageResult result = new JsonPageResult();
        result.setData(saveExport);
        result.setShow(false);
        return result;
    }



}

