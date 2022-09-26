
package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.system.core.entity.SysKettleLog;
import com.redxun.system.core.service.SysKettleLogServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/system/core/sysKettleLog")
@Api(tags = "KETTLE日志")
@ClassDefine(title = "KETTLE日志",alias = "SysKettleLogController",path = "/system/core/sysKettleLog",packages = "core",packageName = "子系统名称")
public class SysKettleLogController extends BaseController<SysKettleLog> {

@Autowired
SysKettleLogServiceImpl sysKettleLogService;


@Override
public BaseService getBaseService() {
return sysKettleLogService;
}

@Override
public String getComment() {
return "KETTLE日志";
}

}

