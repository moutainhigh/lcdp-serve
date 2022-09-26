package com.redxun.system.feign;

import com.redxun.common.dto.SysMenuDto;
import com.redxun.dto.user.OsInstDto;
import com.redxun.dto.user.OsUserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hujun
 */
@FeignClient(name = "jpaas-user")
public interface OsUserClient {
    /**
     * 通过ID获取用户信息
     *
     * @param userId
     * @return
     */
    @ApiOperation("通过ID获取用户信息")
    @GetMapping("/user/org/osUser/getById")
    OsUserDto getById(@ApiParam @RequestParam("userId") String userId);

    /**
     * 通过机构ID查找机构信息
     * @param instId
     * @return
     */
    @ApiOperation("通过机构ID查找机构信息")
    @GetMapping("/user/org/osInst/getById")
    OsInstDto getByInstId(@ApiParam @RequestParam("instId") String instId);

    /**
     * 根据机构类型ID查询菜单列表
     * @param instTypeId
     * @return
     */
    @ApiOperation(value = "根据机构类型ID查询菜单列表")
    @GetMapping("/user/org/osInstTypeMenu/findSysMenuDtoByInstTypeId")
    List<SysMenuDto> findSysMenuDtoByInstTypeId(@RequestParam("instTypeId") String instTypeId);



    /**
     * 根据菜单ids查询用户组菜单列表
     * @param menuIds
     * @return
     */
    @ApiOperation(value = "根据菜单ids查询用户组菜单列表")
    @GetMapping("/user/org/osGroupMenu/findGroupMenuDtoByMenuIds")
    Map<String,Set<String>> findGroupMenuDtoByMenuIds(@RequestParam("menuIds") String menuIds);

    /**
     * 查询机构列表
     * @return
     */
    @ApiOperation(value = "查询机构列表")
    @GetMapping("/user/org/osInst/listInst")
    List<OsInstDto> ListInst(@RequestParam("instIds") String instIds);

    /**
     * 更新菜单权限
     * @return
     */
    @ApiOperation(value = "更新菜单权限")
    @PostMapping("/user/org/osInstTypeMenu/updateMenuControl")
    void updateMenuControl(@RequestBody SysMenuDto sysMenuDto);

    /**
     * 更新菜单权限
     * @return
     */
    @ApiOperation(value = "删除菜单权限")
    @PostMapping("/user/org/osInstTypeMenu/delMenuControl")
    void delMenuControl(@RequestBody List<String> list);

}
