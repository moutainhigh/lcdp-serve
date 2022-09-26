package com.redxun.api.sys;

import com.redxun.api.sys.factory.RemoteMenuFallbackFactory;
import com.redxun.common.constant.ServiceNameConstants;
import com.redxun.common.dto.AuthDto;
import com.redxun.common.dto.SysMenuDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 菜单 Feign服务层
 * 
 * @author yjy
 * @date 2019-11-02
 */
@FeignClient(name = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteMenuFallbackFactory.class)
public interface RemoteMenuService
{

    @GetMapping("system/core/sysMenu/selectMenusByMenuIds")
    List<AuthDto> selectMenusByMenuIds( @RequestParam("menuIds") String menuIds);



    @GetMapping("system/core/sysMenu/getMenusByType")
    List<SysMenuDto> getMenusByType( @RequestParam("menuType") String menuType);
}
