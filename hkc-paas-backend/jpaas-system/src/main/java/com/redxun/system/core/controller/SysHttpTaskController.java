
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.SysHttpTask;
import com.redxun.system.core.service.SysHttpTaskLogServiceImpl;
import com.redxun.system.core.service.SysHttpTaskServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/core/sysHttpTask")
@Api(tags = "接口调用表")
@ClassDefine(title = "接口调用表",alias = "SysHttpTaskController",path = "/system/core/sysHttpTask",packages = "core",packageName = "子系统名称")
@ContextQuerySupport(company = ContextQuerySupport.CURRENT)
public class SysHttpTaskController extends BaseController<SysHttpTask> {

    @Autowired
    SysHttpTaskServiceImpl sysHttpTaskService;
    @Resource
    SysHttpTaskLogServiceImpl sysHttpTaskLogService;


    @Override
    public BaseService getBaseService() {
        return sysHttpTaskService;
    }

    @Override
    public String getComment() {
        return "接口调用表";
    }


    @Override
    protected JsonResult beforeRemove(List<String> list) {
        for(String taskId:list) {
            sysHttpTaskLogService.deleteByTaskId(taskId);
        }
        return super.beforeRemove(list);
    }

    @MethodDefine(title = "清空任务", path = "/clearAll", method = HttpMethodConstants.GET)
    @ApiOperation("清空任务")
    @GetMapping("/clearAll")
    public JsonResult clearAll(){
        sysHttpTaskService.clearAll();
        return JsonResult.getSuccessResult("清空任务成功！");
    }

    @MethodDefine(title = "重推任务", path = "/invokeTask", method = HttpMethodConstants.GET)
    @ApiOperation("重推任务")
    @GetMapping("/invokeTask")
    public JsonResult invokeTask(@RequestParam(value = "id")String id){
        JsonResult result=JsonResult.Fail("重推失败！");
        if(StringUtils.isEmpty(id)){
            return result;
        }
        try {
            SysHttpTask task = sysHttpTaskService.getById(id);
            sysHttpTaskService.handJob(task);
            result.setSuccess(true).setMessage("重推成功！");
        }catch (Exception e) {
        }
        return result;
    }
}

