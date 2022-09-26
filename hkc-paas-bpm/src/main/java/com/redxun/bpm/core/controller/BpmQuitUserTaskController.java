
package com.redxun.bpm.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmQuitUserTask;
import com.redxun.bpm.core.service.BpmQuitUserTaskServiceImpl;
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
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmQuitUserTask")
@Api(tags = "离职人员流程任务表")
@ClassDefine(title = "离职人员流程任务表",alias = "BpmQuitUserTaskController",path = "/bpm/core/bpmQuitUserTask",packages = "core",packageName = "子系统名称")

public class BpmQuitUserTaskController extends BaseController<BpmQuitUserTask> {

@Autowired
BpmQuitUserTaskServiceImpl bpmQuitUserTaskService;


@Override
public BaseService getBaseService() {
return bpmQuitUserTaskService;
}

@Override
public String getComment() {
return "离职人员流程任务表";
}

}

