
package com.redxun.system.core.controller;

import com.redxun.common.base.db.BaseService;
import com.redxun.system.core.entity.SysAppActionLog;
import com.redxun.system.core.service.SysAppActionLogServiceImpl;
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
@RequestMapping("/system/core/sysAppActionLog")
@Api(tags = "应用操作日志")
@ClassDefine(title = "应用操作日志",alias = "SysAppActionLogController",path = "/system/core/sysAppActionLog",packages = "core",packageName = "子系统名称")

public class SysAppActionLogController extends BaseController<SysAppActionLog> {

@Autowired
SysAppActionLogServiceImpl sysAppActionLogService;


@Override
public BaseService getBaseService() {
return sysAppActionLogService;
}

@Override
public String getComment() {
return "应用操作日志";
}

}

