package com.redxun.feign.org;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.user.OsGroupMenuDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户组菜单客户端
 */
@FeignClient(name = "jpaas-user")
public interface OsGroupMenuClient {

    @ApiOperation("根据分类KEY获取数据字典数据")
    @GetMapping("/user/org/osGroupMenu/getResourceByGrade")
    JsonResult<List<OsGroupMenuDto>> getResourceByGrade(@RequestParam(value = "userId") String userId,
                                                        @RequestParam(value = "tenantId") String tenantId);

    @ApiOperation("根据应用ID以及用户组ID获取授权的菜单id列表数据")
    @GetMapping("/user/org/osGroupMenu/getResourceByAppIdAndGroupId")
    List<String> getResourceByAppIdAndGroupId(@RequestParam(value = "appId") String appId,
                                               @RequestParam(value = "groupId") String groupId);

    @ApiOperation("根据用户组ID获取授权的菜单id列表数据")
    @GetMapping("/user/org/osGroupMenu/getResourceByGroupId")
    List<String> getResourceByGroupId(@RequestParam(value = "groupId") String groupId);

    @ApiOperation("根据多个用户组ID获取授权的菜单列表数据")
    @GetMapping("/user/org/osGroupMenu/getResourceByGroupIdList")
    List<String> getResourceByGroupIdList(@RequestParam(value = "groupIds") String groupId);


    @ApiOperation("根据多个用户组ID获取授权的菜单列表数据")
    @GetMapping("/user/org/osGroupMenu/getAppIdGroupIdList")
    List<String> getAppIdGroupIdList(@RequestParam(value = "groupIds") String groupId);

}
