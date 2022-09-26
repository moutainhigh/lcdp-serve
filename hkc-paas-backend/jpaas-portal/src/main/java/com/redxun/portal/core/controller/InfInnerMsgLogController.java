package com.redxun.portal.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.portal.core.entity.InfInnerMsgLog;
import com.redxun.portal.core.service.InfInnerMsgLogServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/portal/core/groupMsgLog")
@ClassDefine(title = "内部消息查看记录",alias = "infInnerMsgLogController",path = "/portal/core/groupMsgLog",packages = "core",packageName = "门户管理")
@Api(tags = "内部消息查看记录")
public class InfInnerMsgLogController extends BaseController<InfInnerMsgLog> {

    @Autowired
    InfInnerMsgLogServiceImpl groupMsgLogService;

    @Override
    public BaseService getBaseService() {
        return groupMsgLogService;
    }

    @Override
    public String getComment() {
        return "内部消息查看记录";
    }

}