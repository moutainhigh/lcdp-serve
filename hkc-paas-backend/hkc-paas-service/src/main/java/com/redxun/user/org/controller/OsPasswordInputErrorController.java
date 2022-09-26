
package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.user.org.entity.OsPasswordInputError;
import com.redxun.user.org.service.OsPasswordInputErrorServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user/org/osPasswordInputError")
@Api(tags = "用户密码输入错误记录表")
@ClassDefine(title = "用户密码输入错误记录表",alias = "OsPasswordInputErrorController",path = "/user/org/osPasswordInputError",packages = "org",packageName = "子系统名称")

public class OsPasswordInputErrorController extends BaseController<OsPasswordInputError> {

    @Autowired
    OsPasswordInputErrorServiceImpl osPasswordInputErrorService;


    @Override
    public BaseService getBaseService() {
    return osPasswordInputErrorService;
    }

    @Override
    public String getComment() {
    return "用户密码输入错误记录表";
    }

    @MethodDefine(title = "密码多次错误输入导致账号被锁定", path = "/isLockedByPasswordInputError", method = HttpMethodConstants.GET)
    @GetMapping("/isLockedByPasswordInputError")
    @ApiOperation(value = "密码多次错误输入导致账号被锁定")
    public Boolean isLockedByPasswordInputError(@RequestParam(name = "username") String username, @RequestParam("tenantId") String tenantId) {
        return osPasswordInputErrorService.isLocked(username, tenantId);
    }

    @MethodDefine(title = "密码错误输入次数递增", path = "/saveInputErrorIncrement", method = HttpMethodConstants.GET)
    @GetMapping("/saveInputErrorIncrement")
    @ApiOperation(value = "密码错误输入次数递增")
    public void saveInputErrorIncrement(@RequestParam(name = "username") String username, @RequestParam("tenantId") String tenantId) {
        osPasswordInputErrorService.saveInputErrorIncrement(username, tenantId);
    }

}

