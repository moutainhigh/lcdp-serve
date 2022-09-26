
package com.redxun.bpm.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.bpm.core.entity.BpmTransfer;
import com.redxun.bpm.core.service.BpmTransferServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.StringUtils;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmTransfer")
@Api(tags = "流程流转表")
public class BpmTransferController extends BaseController<BpmTransfer> {

    @Autowired
    BpmTransferServiceImpl bpmTransferService;


    @Override
    public BaseService getBaseService() {
    return bpmTransferService;
    }

    @Override
    public String getComment() {
    return "流程流转表";
    }

}

