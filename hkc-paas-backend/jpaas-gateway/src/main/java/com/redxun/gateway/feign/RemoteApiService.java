package com.redxun.gateway.feign;

import com.redxun.common.constant.ServiceNameConstants;
import com.redxun.common.dto.AuthDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 菜单 Feign服务层
 * 
 * @author yjy
 * @date 2019-11-02
 */
@FeignClient(name = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteApiService
{
    @ApiOperation("获取所有的授权接口（接口授权合并到菜单授权）")
    @GetMapping("/system/core/sysMenu/getUrlGroupIdMap")
    Map<String, Set<String>> getUrlGroupIdMap();
}
