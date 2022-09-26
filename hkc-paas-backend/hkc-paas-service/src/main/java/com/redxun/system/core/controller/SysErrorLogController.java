
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.base.db.BaseService;
import com.redxun.system.core.entity.SysErrorLog;
import com.redxun.system.core.service.SysErrorLogServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/system/core/sysErrorLog")
@Api(tags = "错误日志")
@ClassDefine(title = "错误日志",alias = "SysErrorLogController",path = "/system/core/sysErrorLog",packages = "core",packageName = "子系统名称")
@ContextQuerySupport(company = ContextQuerySupport.NONE)
public class SysErrorLogController extends BaseController<SysErrorLog> {

    @Autowired
    SysErrorLogServiceImpl sysErrorLogService;


    @Override
    public BaseService getBaseService() {
    return sysErrorLogService;
    }

    @Override
    public String getComment() {
    return "错误日志";
    }

}

