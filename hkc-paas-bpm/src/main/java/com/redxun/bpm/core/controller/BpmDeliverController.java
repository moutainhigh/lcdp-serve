
package com.redxun.bpm.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmDeliver;
import com.redxun.bpm.core.service.BpmDeliverServiceImpl;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.log.annotation.AuditLog;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;

@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmDeliver")
@Api(tags = "流程待办任务交接")
@ClassDefine(title = "流程待办任务交接",alias = "BpmDeliverController",path = "/bpm/core/bpmDeliver",packages = "core",packageName = "子系统名称")

public class BpmDeliverController extends BaseController<BpmDeliver> {

    @Autowired
    BpmDeliverServiceImpl bpmDeliverService;


    @Override
    public BaseService getBaseService() {
        return bpmDeliverService;
    }

    @Override
    public String getComment() {
        return "流程待办任务交接";
    }

    @Override
    @PostMapping("/save")
    @ApiOperation(value = "保存交接信息")
    @AuditLog(operation = "保存交接信息")
    public JsonResult save(@ApiParam @RequestBody BpmDeliver entity, BindingResult validResult){

        return bpmDeliverService.dealDeliver(entity);

    }

    @PostMapping("/getByDeliverUserId")
    @ApiOperation(value = "根据移交人ID获取交接信息")
    @AuditLog(operation = "根据移交人ID获取交接信息")
    public BpmDeliver getByDeliverUserId(@RequestParam("deliverUserId") String deliverUserId){
        return bpmDeliverService.getByDeliverUserId(deliverUserId);
    }


}

