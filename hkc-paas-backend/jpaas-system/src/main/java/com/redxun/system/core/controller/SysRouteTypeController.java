package com.redxun.system.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.SysRouteType;
import com.redxun.system.core.service.SysRouteTypeServiceImpl;
import com.redxun.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/system/core/sysRouteType")
@ClassDefine(title = "路由类型",alias = "sysRouteTypeController",path = "/system/core/sysRouteType",packages = "core",packageName = "系统管理")
@Api(tags = "路由类型")
public class SysRouteTypeController extends BaseController<SysRouteType> {

    @Autowired
    SysRouteTypeServiceImpl sysRouteTypeService;

    @Override
    public BaseService getBaseService() {
        return sysRouteTypeService;
    }

    @Override
    public String getComment() {
        return "路由类型";
    }


    @MethodDefine(title = "获取路由类型", path = "/getList", method = HttpMethodConstants.GET)
    @ApiOperation(value = "获取路由类型")
    @GetMapping("/getList")
    public List<SysRouteType> getList() throws Exception {
        String tenantId= ContextUtil.getCurrentTenantId();

        QueryWrapper wrapper=new QueryWrapper();

        wrapper.in("TENANT_ID_",new String[]{tenantId,"0"});

        List<SysRouteType> list= sysRouteTypeService.getRepository().selectList(wrapper);
        return  list;
    }


}
