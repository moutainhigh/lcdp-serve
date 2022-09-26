package com.redxun.bpm.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmAgentFlowDef;
import com.redxun.bpm.core.service.BpmAgentFlowDefServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmAgentFlowDef")
@ClassDefine(title = "代理流程定义",alias = "bpmAgentFlowDefController",path = "/bpm/core/bpmAgentFlowDef",packages = "core",packageName = "流程管理")
@Api(tags = "代理流程定义")
public class BpmAgentFlowDefController extends BaseController<BpmAgentFlowDef> {

    @Autowired
    BpmAgentFlowDefServiceImpl bpmAgentFlowDefService;

    @Override
    public BaseService getBaseService() {
        return bpmAgentFlowDefService;
    }

    @Override
    public String getComment() {
        return "代理流程定义";
    }

}