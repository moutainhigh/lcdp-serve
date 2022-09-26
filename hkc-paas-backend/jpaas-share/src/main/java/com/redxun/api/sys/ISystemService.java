package com.redxun.api.sys;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.dto.sys.SysAppDto;

import java.util.List;

/**
 * 系统服务接口类
 */
public interface ISystemService {

    /**
     * 获取所有按钮菜单列表
     * @param menuType
     * @return
     */
    JSONObject getAllButtonsByMenuType(String menuType);


    /**
     * 根据菜单ID集合与菜单类型获取菜单信息
     * @param menuIds
     * @param menuType
     * @return
     */
    List<SysMenuDto> getMenusByIdsAndType(String menuIds,String menuType);

    /**
     * 根据appIds集合获取应用列表
     * @param appIds
     * @return
     */
    List<SysAppDto> getAppsByIds(String appIds);

    /**
     * 根据当前租户ID获取菜单资源。
     * @return
     */
    List<SysMenuDto> getMenusByTenantId(String tenantId);


    /**
     * 根据机构ID获取机构类型菜单ID集合。
     * @param tenantId
     * @return
     */
    List<String> getInstTypeMenusByTenantId( String tenantId);



    /**
     * 根据companyId集合获取公司级别应用列表
     * @param companyId
     * @return
     */
    List<SysAppDto> getCompanyApps(String companyId);

    /**
     * 根据companyId集合获取公司级别菜单列表
     * @param companyId
     * @return
     */
    List<SysMenuDto> getCompanyMenus(String companyId);


}
