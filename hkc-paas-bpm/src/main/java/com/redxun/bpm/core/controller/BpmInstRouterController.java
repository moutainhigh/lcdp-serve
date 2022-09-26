package com.redxun.bpm.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmInstRouter;
import com.redxun.bpm.core.service.BpmInstRouterServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmInstRouter")
@ClassDefine(title = "流程实例路由",alias = "bpmInstRouterController",path = "/bpm/core/bpmInstRouter",packages = "core",packageName = "流程管理")
@Api(tags = "流程实例路由")
public class BpmInstRouterController extends BaseController<BpmInstRouter> {

    @Autowired
    BpmInstRouterServiceImpl bpmInstRouterService;

    @Override
    public BaseService getBaseService() {
        return bpmInstRouterService;
    }

    @Override
    public String getComment() {
        return "流程实例路由";
    }

}