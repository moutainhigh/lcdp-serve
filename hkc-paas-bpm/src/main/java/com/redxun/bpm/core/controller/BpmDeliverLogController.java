
package com.redxun.bpm.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmDeliverLog;
import com.redxun.bpm.core.service.BpmDeliverLogServiceImpl;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.utils.ContextUtil;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;

@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmDeliverLog")
@Api(tags = "流程待办任务交接日志")
@ClassDefine(title = "流程待办任务交接日志",alias = "BpmDeliverLogController",path = "/bpm/core/bpmDeliverLog",packages = "core",packageName = "子系统名称")

public class BpmDeliverLogController extends BaseController<BpmDeliverLog> {

    @Autowired
    BpmDeliverLogServiceImpl bpmDeliverLogService;


    @Override
    public BaseService getBaseService() {
    return bpmDeliverLogService;
    }

    @Override
    public String getComment() {
    return "流程待办任务交接日志";
    }


}

