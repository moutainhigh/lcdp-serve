package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.system.core.entity.SysMenuRelease;
import com.redxun.system.core.service.SysMenuReleaseServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/system/core/sysMenuRelease")
@ClassDefine(title = "发布菜单路径记录表",alias = "sysMenuReleaseController",path = "/system/core/sysMenuRelease",packages = "core",packageName = "系统管理")
@Api(tags = "发布菜单路径记录表")
public class SysMenuReleaseController extends BaseController<SysMenuRelease> {

    @Autowired
    SysMenuReleaseServiceImpl sysMenuReleaseService;

    @Override
    public BaseService getBaseService() {
        return sysMenuReleaseService;
    }

    @Override
    public String getComment() {
        return "发布菜单路径记录表";
    }

}