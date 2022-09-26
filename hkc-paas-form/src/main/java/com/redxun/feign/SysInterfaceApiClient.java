package com.redxun.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.sys.SysInterfaceApiDto;
import com.redxun.dto.sys.SysWebReqDefDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "jpaas-system")
public interface SysInterfaceApiClient {
    /**
     * 通过接口ID获取
     *
     * @param apiId
     * @return
     */
    @ApiOperation("通过接口ID获取")
    @GetMapping("/system/core/sysInterfaceApi/getById")
    SysInterfaceApiDto getById(@ApiParam @RequestParam("apiId") String apiId);

    @ApiOperation("执行接口")
    @PostMapping("/system/core/sysInterfaceApi/executeApi")
    JsonResult executeApi(@ApiParam @RequestParam("apiId") String apiId,@ApiParam @RequestBody JSONObject params);
}
