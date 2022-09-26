package com.redxun.feign.org;

import com.redxun.dto.user.OsGradeRoleDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "jpaas-user")
public interface OsGradeRoleClient {
    @ApiOperation("根据管理员获取角色列表")
    @GetMapping("/user/org/osGradeRole/getGroupByUserId")
    List<OsGradeRoleDto> getGroupByUserId(@RequestParam(value = "userId") String userId,@RequestParam(required = false,value = "tenantId") String tenantId);
}
