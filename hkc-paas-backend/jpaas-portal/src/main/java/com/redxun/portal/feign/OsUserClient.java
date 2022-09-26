package com.redxun.portal.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dto.user.OsUserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
