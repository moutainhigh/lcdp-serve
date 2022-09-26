package com.redxun.portal.feign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

/**
 * 组织架构Feign客户端
 */
@FeignClient(name = "jpaas-user")
public interface OsGroupClient {
    /**
     * 获取组织用户信息
     * @param groupId
     * @return
     */
    @ApiOperation("获取组织用户信息")
    @GetMapping("/user/org/osGroup/getGroupObj")
    JSONObject getGroupObj(@ApiParam @RequestParam("groupId") String groupId);

    /**
     * 获取用户组织信息
     * @param userId
     * @return
     */
    @ApiOperation("获取用户组织信息")
    @GetMapping("/user/org/osGroup/getCurrentProfile")
    Map<String, Set<String>> getCurrentProfile(@ApiParam @RequestParam("userId") String userId);

    /**
     * 获取用户组织信息
     * @return
     */
    @ApiOperation("获取用户组织信息")
    @GetMapping("/user/org/osGroup/getOsUserGroupHanderList")
    JSONArray getOsUserGroupHanderList();
}
