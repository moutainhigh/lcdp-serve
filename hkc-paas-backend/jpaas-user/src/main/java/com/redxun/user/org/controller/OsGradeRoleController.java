package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.user.org.entity.OsGradeRole;
import com.redxun.user.org.service.OsGradeRoleServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分级管理员映射的角色Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osGradeRole")
@ClassDefine(title = "分级管理员角色",alias = "osGradeRoleController",path = "/user/org/osGradeRole",packages = "org",packageName = "组织架构")
@Api(tags = "分级管理员角色")
public class OsGradeRoleController extends BaseController<OsGradeRole> {

    @Autowired
    OsGradeRoleServiceImpl osGradeRoleService;

    @Override
    public BaseService getBaseService() {
        return osGradeRoleService;
    }

    @Override
    public String getComment() {
        return "分级管理员角色";
    }

    @MethodDefine(title = "根据用户获取角色列表", path = "/getGroupByUserId", method = HttpMethodConstants.GET)
    @ApiOperation("根据用户获取角色列表")
    @GetMapping("/getGroupByUserId")
    public List<OsGradeRole> getGroupByUserId(@ApiParam @RequestParam(value = "userId") String userId,
                                              @ApiParam @RequestParam(value = "tenantId",required = false) String tenantId) {
        List<OsGradeRole> groupIdList = osGradeRoleService.getGroupByUserId(userId,tenantId);
        return groupIdList;
    }

}
