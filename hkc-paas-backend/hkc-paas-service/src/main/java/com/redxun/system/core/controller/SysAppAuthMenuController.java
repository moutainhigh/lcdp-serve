package com.redxun.system.core.controller;

import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.DbLogicDelete;
import com.redxun.dto.user.OsInstDto;
import com.redxun.system.core.entity.SysAppAuthMenu;
import com.redxun.system.core.service.SysAppAuthMenuServiceImpl;
import com.redxun.util.QueryFilterUtil;
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

import java.util.Collections;
import java.util.List;

/**
 * @Description 应用授权菜单管理
 * @Author xtk
 * @Date 2021/12/3 11:06
 */
@Slf4j
@RestController
@RequestMapping("/system/core/app/auth/menu")
@ClassDefine(title = "应用授权菜单",alias = "sysAppAuthMenuController",path = "/system/core/app/auth/menu",packages = "core",packageName = "应用管理")
@Api(tags = "应用授权菜单")
public class SysAppAuthMenuController extends BaseController<SysAppAuthMenu> {

    @Autowired
    SysAppAuthMenuServiceImpl sysAppAuthMenuService;

    @Override
    public BaseService<SysAppAuthMenu> getBaseService() {
        return sysAppAuthMenuService;
    }

    @Override
    public String getComment() {
        return "应用租户用户菜单";
    }

    @Override
    protected void handleFilter(QueryFilter filter) {
        String tenantId= ContextUtil.getCurrentTenantId();
        Boolean isRootTenant= OsInstDto.ROOT_INST.equals(tenantId);

        QueryFilterUtil.setQueryFilterByTreeId(filter,"CATEGORY_ID_","APP","read");

        //非根租户
        if(!isRootTenant){
            super.handleFilter(filter);
        }else if (DbLogicDelete.getLogicDelete()) {
            //逻辑删除
            filter.addQueryParam("Q_DELETED__S_EQ","0");
        }
    }

    /**
     * @Description 根据租户ID或者用户ID获取应用安装菜单
     * @Author xtk
     * @Date 2021/12/3 15:45
     * @param tenantId 租户ID
     * @param userId   用户ID
     * @return java.util.List<java.lang.String>
     */
    @MethodDefine(title = "根据租户ID或者用户ID获取应用安装菜单", path = "/getTenantOrUserAppInstallMenu", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "租户ID", varName = "tenantId"),@ParamDefine(title = "用户ID", varName = "userId")})
    @ApiOperation("根据租户ID合作用户ID获取应用安装菜单")
    @GetMapping("/getTenantOrUserAppInstallMenu")
    public List<SysMenuDto> getTenantOrUserAppInstallMenu(@ApiParam @RequestParam("tenantId") String tenantId, @ApiParam @RequestParam("userId") String userId){
        if (StringUtils.isEmpty(tenantId) && StringUtils.isEmpty(userId)) {
            return Collections.emptyList();
        }
        List<SysMenuDto> menuList = sysAppAuthMenuService.getTenantOrUserAppInstallMenu(tenantId, userId);
        return menuList;
    }

}
