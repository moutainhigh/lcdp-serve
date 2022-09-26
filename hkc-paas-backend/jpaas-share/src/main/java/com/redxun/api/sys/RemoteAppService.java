package com.redxun.api.sys;

import com.redxun.api.sys.factory.RemoteAppFallbackFactory;
import com.redxun.common.constant.ServiceNameConstants;
import com.redxun.dto.sys.SysAppDto;
import io.swagger.annotations.ApiParam;
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
@FeignClient(name = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteAppFallbackFactory.class)
public interface RemoteAppService
{


    @GetMapping("system/core/sysApp/getAllApps")
    List<SysAppDto> getAllApps();


    @GetMapping("system/core/sysApp/getAppsByIds")
    List<SysAppDto> getAppsByIds(@ApiParam @RequestParam("appIds") String appIds);




}
