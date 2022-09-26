package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmTaskUser;
import com.redxun.bpm.core.service.BpmTaskUserServiceImpl;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.log.annotation.AuditLog;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmTaskUser")
@ClassDefine(title = "任务处理相关人",alias = "bpmTaskUserController",path = "/bpm/core/bpmTaskUser",packages = "core",packageName = "流程管理")
@Api(tags = "任务处理相关人")
public class BpmTaskUserController extends BaseController<BpmTaskUser> {

    @Autowired
    BpmTaskUserServiceImpl bpmTaskUserService;

    @Override
    public BaseService getBaseService() {
        return bpmTaskUserService;
    }

    @Override
    public String getComment() {
        return "任务处理相关人";
    }

    @MethodDefine(title = "设置执行人", path = "/setAssignee", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "jsonObject")})
    @ApiOperation("设置执行人")
    @AuditLog(operation = "设置执行人")
    @PostMapping("setAssignee")
    public JsonResult setAssignee(@RequestBody JSONObject jsonObject) {
        JsonResult result=JsonResult.Success();
        BpmTask bpmTask=jsonObject.getObject("bpmTask",BpmTask.class);
        List<String> userIds = jsonObject.getObject("userIds", ArrayList.class);
        bpmTaskUserService.setAssignee(bpmTask,userIds);
        result.setShow(false);

        String detail="设置任务:("+bpmTask.getSubject()+")执行人";
        LogContext.put(Audit.DETAIL,detail);

        return result;
    }

}
