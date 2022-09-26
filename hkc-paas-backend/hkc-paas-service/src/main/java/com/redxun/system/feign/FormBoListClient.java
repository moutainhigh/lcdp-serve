package com.redxun.system.feign;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hujun
 */
@FeignClient(name = "jpaas-form")
public interface FormBoListClient {
    /**
     * 获取PC列表按钮
     *
     * @param alias
     * @return
     */
    @ApiOperation("获取PC列表按钮")
    @PostMapping("/form/core/formBoList/getPcListBtns")
    JSONArray getPcListBtns(@ApiParam @RequestParam("alias") String alias);
}
