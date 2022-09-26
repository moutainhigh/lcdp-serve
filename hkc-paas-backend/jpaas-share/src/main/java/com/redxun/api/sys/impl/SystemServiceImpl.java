package com.redxun.api.sys.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.api.sys.ISystemService;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.dto.sys.SysAppDto;
import com.redxun.feign.sys.SystemClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.List;

@Component
@Primary
public class SystemServiceImpl implements ISystemService {

    @Resource
    @Lazy
    SystemClient systemClient;

    @Override
    public JSONObject getAllButtonsByMenuType(String menuType){
        return systemClient.getAllButtonsByMenuType(menuType);
    }

    @Override
    public List<SysMenuDto> getMenusByIdsAndType(String menuIds, String menuType){
        MultiValueMap valueMap=new LinkedMultiValueMap();
        valueMap.add("menuIds",menuIds);
        valueMap.add("menuType",menuType);
        return systemClient.getMenusByIdsAndTypes(valueMap);
    }

    @Override
    public List<SysAppDto> getAppsByIds(String appIds) {
        MultiValueMap valueMap=new LinkedMultiValueMap();
        valueMap.add("appIds",appIds);
        return systemClient.getAppsByIds(valueMap);
    }

    @Override
    public List<SysMenuDto> getMenusByTenantId(String tenantId) {
        return systemClient.getMenusByTenantId(tenantId);
    }

    @Override
    public List<String> getInstTypeMenusByTenantId(String tenantId) {
        return null;
    }

    @Override
    public List<SysAppDto> getCompanyApps(String companyId) {
        return systemClient.getCompanyApps(companyId);
    }

    @Override
    public List<SysMenuDto> getCompanyMenus(String companyId) {
        return systemClient.getCompanyMenus(companyId);
    }


}
