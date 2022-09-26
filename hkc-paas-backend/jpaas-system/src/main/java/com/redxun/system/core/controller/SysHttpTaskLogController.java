
package com.redxun.system.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.SysHttpTaskLog;
import com.redxun.system.core.service.SysHttpTaskLogServiceImpl;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/system/core/sysHttpTaskLog")
@Api(tags = "接口调用日志表")
@ClassDefine(title = "接口调用日志表",alias = "SysHttpTaskLogController",path = "/system/core/sysHttpTaskLog",packages = "core",packageName = "子系统名称")
@ContextQuerySupport
public class SysHttpTaskLogController extends BaseController<SysHttpTaskLog> {

    @Autowired
    SysHttpTaskLogServiceImpl sysHttpTaskLogService;


    @Override
    public BaseService getBaseService() {
        return sysHttpTaskLogService;
    }

    @Override
    public String getComment() {
        return "接口调用日志表";
    }


    @MethodDefine(title = "清空日志", path = "/clearAll", method = HttpMethodConstants.GET)
    @ApiOperation("清空日志")
    @GetMapping("/clearAll")
    public JsonResult clearAll(){
        sysHttpTaskLogService.clearAll();
        return JsonResult.getSuccessResult("清空日志成功！");
    }

    @PostMapping("/getPageByTaskId")
    public JsonPageResult getPageByTaskId(@RequestParam(value = "taskId")String taskId,@RequestBody QueryData queryData){
        JsonPageResult jsonResult = JsonPageResult.getSuccess("返回数据成功!");
        QueryFilter filter = QueryFilterBuilder.createQueryFilter(queryData);
        filter.addParam("taskId", taskId);
        IPage page = sysHttpTaskLogService.getAllByTaskId(filter);
        jsonResult.setPageData(page);
        return jsonResult;
    }
}

