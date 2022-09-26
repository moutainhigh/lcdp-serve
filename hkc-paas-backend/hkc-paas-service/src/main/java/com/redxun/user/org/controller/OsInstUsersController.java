package com.redxun.user.org.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.user.org.entity.OsInstUsers;
import com.redxun.user.org.service.OsInstUsersServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机构用户Controller
 */
@Slf4j
@RestController
@RequestMapping("/user/org/osInstUsers")
@ClassDefine(title = "租户关联用户表",alias = "osInstUsersController",path = "/user/org/osInstUsers",packages = "org",packageName = "租户管理")
@Api(tags = "租户关联用户表")
public class OsInstUsersController extends BaseController<OsInstUsers> {

    @Autowired
    OsInstUsersServiceImpl osInstUsersService;

    @Override
    public BaseService getBaseService() {
        return osInstUsersService;
    }

    @Override
    public String getComment() {
        return "租户关联用户表";
    }

}