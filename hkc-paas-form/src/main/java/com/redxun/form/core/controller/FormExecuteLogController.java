
package com.redxun.form.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.form.core.entity.FormExecuteLog;
import com.redxun.form.core.service.FormExecuteLogServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/form/core/formExecuteLog")
@Api(tags = "租户数据源执行记录")
@ClassDefine(title = "租户数据源执行记录", alias = "FormExecuteLogController", path = "/form/core/formExecuteLog", packages = "core", packageName = "子系统名称")

public class FormExecuteLogController extends BaseController<FormExecuteLog> {

    @Autowired
    FormExecuteLogServiceImpl formExecuteLogService;


    @Override
    public BaseService getBaseService() {
        return formExecuteLogService;
    }

    @Override
    public String getComment() {
        return "租户数据源执行记录";
    }

    @MethodDefine(title = "根据条件查询业务数据记录", path = "/getExecuteLog", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询条件", varName = "queryData")})
    @ApiOperation(value="根据条件查询业务数据记录", notes="根据条件查询业务数据记录")
    @PostMapping(value="/getExecuteLog")
    public JsonPageResult getExecuteLog(@RequestBody QueryData queryData) throws Exception {
        JsonPageResult jsonResult = JsonPageResult.getSuccess("返回数据成功!");
        QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
        handleFilter(filter);
        IPage page = formExecuteLogService.getExecuteLog(filter);
        handlePage(page);
        jsonResult.setPageData(page);
        return jsonResult;
    }

    @MethodDefine(title = "根据批次获取数据记录", path = "/getByBatch", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "批次", varName = "batch")})
    @ApiOperation(value="根据批次获取数据记录", notes="根据批次获取数据记录")
    @GetMapping(value="/getByBatch")
    public JsonResult getByBatch(@RequestParam(value="batch") String batch){
        List<FormExecuteLog> executeLogs = formExecuteLogService.getByBatch(batch);
        return new JsonResult(true,executeLogs,"返回数据成功").setShow(false);
    }



}

