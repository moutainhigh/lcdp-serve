package com.redxun.feign;

import com.redxun.dto.user.OsUserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hujun
 */
@FeignClient(name = "jpaas-user")
public interface OsUserClient {
    /**
     * 通过ID获取用户信息
     * @param userId
     * @return
     */
    @ApiOperation("通过ID获取用户信息")
    @GetMapping("/user/org/osUser/getById")
    OsUserDto getById(@ApiParam @RequestParam("userId")String userId);

    /**
     * 通过ID获取用户信息
     * @param groupId
     * @return
     */
    @ApiOperation("通过ID获取用户信息")
    @GetMapping("/user/org/osUser/getByGroupId")
    List<OsUserDto> getByGroupId(@ApiParam @RequestParam("groupId")String groupId);

    /**
     * 获取当前用户所在上下级的人员路径
     * @param userId
     * @return
     */
    @ApiOperation("获得当前用户所在上下级的人员路径")
    @GetMapping("/user/org/osUser/getUserUpLowPath")
    String getUserUpLowPath(@ApiParam @RequestParam("userId")String userId);

    /**
     * 获取用户直属上级用户ID集合
     * @param upLowPath
     * @return
     */
    @ApiOperation("获取用户直属上级用户ID集合")
    @GetMapping("/user/org/osUser/getDupUserIds")
    List<String> getDupUserIds(@ApiParam @RequestParam("upLowPath")String upLowPath);

    /**
     * 获取用户直属下级用户ID集合
     * @param userId
     * @return
     */
    @ApiOperation("获取用户直属下级用户ID集合")
    @GetMapping("/user/org/osUser/getDdownUserIds")
    List<String> getDdownUserIds(@ApiParam @RequestParam("userId")String userId);

    /**
     * 获取用户所有下级用户ID集合
     * @param upLowPath
     * @return
     */
    @ApiOperation("获取用户所有下级用户ID集合")
    @GetMapping("/user/org/osUser/getDownUserIds")
    List<String> getDownUserIds(@ApiParam @RequestParam("upLowPath")String upLowPath);

    /**
     * 根据用户组ids获取用户组名称
     * @param groupIds
     * @return
     */
    @ApiOperation("根据用户组ids获取用户组名称")
    @GetMapping("/user/org/osGroup/getGoupNames")
    List<String> getGoupNames(@ApiParam @RequestParam("groupIds")List<String> groupIds);

}
