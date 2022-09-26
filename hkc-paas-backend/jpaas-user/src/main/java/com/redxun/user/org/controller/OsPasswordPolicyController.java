
package com.redxun.user.org.controller;

import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.user.org.entity.OsPasswordPolicy;
import com.redxun.user.org.service.OsPasswordPolicyServiceImpl;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.redxun.common.annotation.ClassDefine;

@Slf4j
@RestController
@RequestMapping("/user/org/osPasswordPolicy")
@Api(tags = "密码安全策略管理配置")
@ClassDefine(title = "密码安全策略管理配置",alias = "osPasswordPolicyController",path = "/user/org/osPasswordPolicy",packages = "org",packageName = "组织架构")
public class OsPasswordPolicyController extends BaseController<OsPasswordPolicy> {

    @Autowired
    OsPasswordPolicyServiceImpl osPasswordPolicyService;


    @Override
    public BaseService getBaseService() {
    return osPasswordPolicyService;
    }

    @Override
    public String getComment() {
    return "密码安全策略管理配置";
    }


    @MethodDefine(title = "获取数据", path = "/getConfig", method = HttpMethodConstants.GET)
    @GetMapping("/getConfig")
    @ApiOperation(value = "获取数据")
    public JsonResult getData() {
        return osPasswordPolicyService.getConfig(ContextUtil.getCurrentTenantId());
    }

    @MethodDefine(title = "保存或者修改配置", path = "/saveOrUpdateConfig", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "配置用数据", varName = "passwordPolicyConfigure")})
    @PostMapping("/saveOrUpdateConfig")
    @ApiOperation(value = "保存或者修改配置")
    public JsonResult saveOrUpdate(@RequestBody OsPasswordPolicy passwordPolicy) {
        return osPasswordPolicyService.saveOrUpdateConfig(passwordPolicy);
    }

    @MethodDefine(title = "密码过期导致账号被锁定", path = "/isLockedByPasswordExpire", method = HttpMethodConstants.GET)
    @GetMapping("/isLockedByPasswordExpire")
    @ApiOperation(value = "密码过期导致账号被锁定")
    public Boolean isLockedByPasswordExpire(@RequestParam("username")String username, @RequestParam("tenantId") String tenantId) {
        return osPasswordPolicyService.isLockedByPasswordExpire(username, tenantId);
    }


    @MethodDefine(title = "获取用户是否首次登录", path = "/getIsFirstLogin", method = HttpMethodConstants.GET)
    @GetMapping("/getIsFirstLogin")
    @ApiOperation(value = "获取用户是否首次登录")
    public String getIsFirstLogin(@RequestParam("isFirstLogin")String isFirstLogin, @RequestParam("tenantId") String tenantId) {
        return osPasswordPolicyService.getIsFirstLogin(isFirstLogin, tenantId);
    }


}

