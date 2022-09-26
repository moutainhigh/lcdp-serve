package com.redxun.feign;

import com.alibaba.fastjson.JSONArray;
import com.redxun.dto.user.OsGroupDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hujun
 */
@FeignClient(name = "jpaas-user")
public interface OsGroupClient {
    /**
     * 获取用户主部门信息
     * @param userId
     * @param tenantId
     * @return
     */
    @ApiOperation("获取用户主部门信息")
    @GetMapping("/user/org/osGroup/getMainDeps")
    OsGroupDto getMainDeps(@ApiParam @RequestParam("userId")String userId, @ApiParam @RequestParam("tenantId")String tenantId);

    /**
     * 通过ID获取用户组信息
     * @param groupId
     * @return
     */
    @ApiOperation("通过ID获取用户组信息")
    @GetMapping("/user/org/osGroup/getById")
    OsGroupDto getById(@ApiParam @RequestParam("groupId")String groupId);

    /**
     * 获取用户组直属下级用户组ID集合
     * @param groupId
     * @return
     */
    @ApiOperation("获取用户组直属下级用户组ID集合")
    @GetMapping("/user/org/osGroup/getDdownDeps")
    List<String> getDdownDeps(@ApiParam @RequestParam("groupId")String groupId);

    /**
     * 获取用户组所有下级用户组ID集合
     * @param groupId
     * @return
     */
    @ApiOperation("获取用户组所有下级用户组ID集合")
    @GetMapping("/user/org/osGroup/getDownDeps")
    List<String> getDownDeps(@ApiParam @RequestParam("groupId")String groupId);

    /**
     * 获取权限设置策略列表
     * @return
     */
    @ApiOperation("获取权限设置策略列表")
    @GetMapping("/user/org/osGroup/getOsUserGroupHanderList")
    JSONArray getOsUserGroupHanderList();

    /**
     * 获取用户组织信息
     * @param userId
     * @return
     */
    @ApiOperation("获取用户组织信息")
    @GetMapping("/user/org/osGroup/getCurrentProfile")
    Map<String, Set<String>> getCurrentProfile(@ApiParam @RequestParam("userId") String userId);
}
