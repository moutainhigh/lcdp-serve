package com.redxun.feign;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "jpaas-bpm")
public interface BpmDefClient {

    /**
     * 通过流程定义ID获取绑定的业务模型
     * @param defKey
     * @return
     */
    @ApiOperation("通过流程定义KEY获取绑定的业务模型")
    @GetMapping("/bpm/core/bpmDef/getBoDefsByDefKey")
    String getBoDefsByDefKey(@ApiParam @RequestParam("defKey") String defKey);
}
