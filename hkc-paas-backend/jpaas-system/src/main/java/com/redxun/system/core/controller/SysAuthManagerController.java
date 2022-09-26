package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.redis.util.AppTokenUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.system.core.entity.SysAuthManager;
import com.redxun.system.core.service.SysAuthManagerService;
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
@RequestMapping("/system/core/sysAuthManager")
@ClassDefine(title = "接口客户端管理",alias = "sysAuthManagerController",path = "/system/core/sysAuthManager",packages = "core",packageName = "系统管理")
@Api(tags = "接口客户端管理")
public class SysAuthManagerController extends BaseController<SysAuthManager> {

    @Autowired
    SysAuthManagerService sysAuthManagerService;


    @Override
    public BaseService getBaseService() {
        return sysAuthManagerService;
    }

    @Override
    public String getComment() {
        return "应用接口授权管理表";
    }

    @MethodDefine(title = "获取token", path = "/genSecret", method = HttpMethodConstants.GET)
    @ApiOperation("获取token")
    @GetMapping("/genSecret")
    public String genSecret() {
        String str = AppTokenUtil.genSecret();
        return str;
    }

    @MethodDefine(title = "获取所有应用接口授权", path = "/getAllList", method = HttpMethodConstants.POST)
    @ApiOperation("获取所有应用接口授权")
    @PostMapping("/getAllList")
    public JsonPageResult getAllList(){
        List<SysAuthManager> data = sysAuthManagerService.getAll();
        JsonPageResult result=new JsonPageResult();
        result.setData(data);
        result.setShow(false);
        return result;
    }


    @Override
    protected JsonResult beforeSave(SysAuthManager ent) {
        if(StringUtils.isNotEmpty( ent.getId())){
            sysAuthManagerService.cleanCache(ent.getId());
        }
        return super.beforeSave(ent);
    }


}
