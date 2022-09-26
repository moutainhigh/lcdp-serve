package com.redxun.bpm.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmArchiveLog;
import com.redxun.bpm.core.service.BpmArchiveLogServiceImpl;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmArchiveLog")
@ClassDefine(title = "流程归档日志",alias = "bpmArchiveLogController",path = "/bpm/core/bpmArchiveLog",packages = "core",packageName = "流程管理")
@Api(tags = "流程归档日志")
public class BpmArchiveLogController extends BaseController<BpmArchiveLog> {

    @Autowired
    BpmArchiveLogServiceImpl bpmArchiveLogService;

    @Override
    public BaseService getBaseService() {
        return bpmArchiveLogService;
    }

    @Override
    public String getComment() {
        return "流程归档日志";
    }


    @ApiOperation(value="新建流程归档日志信息")
    @PostMapping("/save")
    @AuditLog(operation = "流程归档")
    @Override
    public JsonResult save(@ApiParam @RequestBody BpmArchiveLog bpmArchiveLog, BindingResult validResult) throws Exception{
        JsonResult jsonResult=new JsonResult();
        Integer count= bpmArchiveLogService.getFinishTimes(bpmArchiveLog.getArchiveDate());
        if(count>0){
            jsonResult.setMessage("指定日期已经完成了归档!");
            return jsonResult.setSuccess(false);
        }
        archive(bpmArchiveLog);
        return jsonResult.setSuccess(true).setMessage("成功添加归档任务到后台执行!");
    }

    //归档
    private  void archive(BpmArchiveLog bpmArchiveLog){
        IUser user=ContextUtil.getCurrentUser();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bpmArchiveLogService.archive(bpmArchiveLog.getArchiveDate(),bpmArchiveLog.getMemo(),user);
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(runnable, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void handleFilter(QueryFilter filter) {

    }

    @MethodDefine(title = "获取归档记录", path = "/getBpmArchiveLogs", method = HttpMethodConstants.GET)
    @ApiOperation(value="获取归档记录")
    @GetMapping("/getBpmArchiveLogs")
    public List<BpmArchiveLog> getBpmArchiveLogs(){
        List<BpmArchiveLog> bpmArchiveLogs= bpmArchiveLogService.getBpmArchiveLogs();
        return  bpmArchiveLogs;
    }
}
