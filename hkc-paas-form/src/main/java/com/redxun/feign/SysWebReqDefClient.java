package com.redxun.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.sys.SysWebReqDefDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "jpaas-system")
public interface SysWebReqDefClient {
    /**
     * 通过别名获取
     *
     * @param alias
     * @return
     */
    @ApiOperation("通过别名获取")
    @GetMapping("/system/core/sysWebReqDef/getByAlias")
    SysWebReqDefDto getByAlias(@ApiParam @RequestParam("alias") String alias);

    /**
     * 执行脚本
     *
     * @param jsonObject
     * @return
     */
    @ApiOperation("执行脚本")
    @GetMapping("/system/core/sysWebReqDef/executeScript")
    JsonResult executeScript(@ApiParam @RequestBody JSONObject jsonObject);
}
