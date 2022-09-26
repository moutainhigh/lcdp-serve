package com.redxun.feign;

import com.redxun.common.dto.SysMenuDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "jpaas-system")
public interface SysMenuClient {
    @PostMapping("/system/core/sysMenu/getMenuMenuIds")
    List<SysMenuDto> getMenuMenuIds(@RequestParam("menuIds") String menuIds);

    @PostMapping("/system/core/sysMenu/delSysMenus")
    void delSysMenus(@RequestParam("ids") String ids);
}
