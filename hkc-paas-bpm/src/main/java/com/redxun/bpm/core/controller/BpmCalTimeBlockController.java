package com.redxun.bpm.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmCalTimeBlock;
import com.redxun.bpm.core.service.BpmCalTimeBlockServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmCalTimeBlock")
@ClassDefine(title = "工作时间段设定",alias = "bpmCalTimeBlockController",path = "/bpm/core/bpmCalTimeBlock",packages = "core",packageName = "流程管理")
@Api(tags = "工作时间段设定")
public class BpmCalTimeBlockController extends BaseController<BpmCalTimeBlock> {

    @Autowired
    BpmCalTimeBlockServiceImpl bpmCalTimeBlockService;

    @Override
    public BaseService getBaseService() {
        return bpmCalTimeBlockService;
    }

    @Override
    public String getComment() {
        return "工作时间段设定";
    }

}