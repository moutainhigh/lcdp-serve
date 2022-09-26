package com.redxun.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "jpaas-system")
public interface PublicClient {
    /**
     * 获取流水号数据
     * @param alias
     * @return
     */
    @ApiOperation("获取流水号数据")
    @GetMapping("/system/core/sysSeqId/genSeqNo")
    String genSeqNo (@RequestParam(value = "alias") String alias);

}
