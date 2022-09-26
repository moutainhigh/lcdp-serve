package com.redxun.system.feign;

import com.redxun.common.base.entity.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 单据自定义查询
 */
@FeignClient(name = "jpaas-form")
public interface FormCustomQueryClient {
    /**
     * 根据key查询自定义SQL对象
     * @param key
     * @return
     */
    @ApiOperation("根据key查询自定义SQL对象")
    @GetMapping("/form/core/formCustomQuery/getByKey")
    JsonResult getByKey(@RequestParam("key") String key);
}
