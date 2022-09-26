
package com.redxun.bpm.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmInstPermission;
import com.redxun.bpm.core.service.BpmInstPermissionServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmInstPermission")
@Api(tags = "流程实例权限表")
@ClassDefine(title = "流程实例权限表",alias = "BpmInstPermissionController",path = "/bpm/core/bpmInstPermission",packages = "core",packageName = "子系统名称")

public class BpmInstPermissionController extends BaseController<BpmInstPermission> {

    @Autowired
    BpmInstPermissionServiceImpl bpmInstPermissionService;

    @Override
    public BaseService getBaseService() {
        return bpmInstPermissionService;
    }

    @Override
    public String getComment() {
        return "流程实例权限表";
    }

}

