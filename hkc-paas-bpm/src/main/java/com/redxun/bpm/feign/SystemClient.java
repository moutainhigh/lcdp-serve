package com.redxun.bpm.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hujun
 */
@FeignClient(name = "jpaas-system")
public interface SystemClient {

    /**
     * 根据表单别名和节点权限获取表单相关数据。
     *
     * @param jsonObject
     * @return
     */
    @GetMapping("/system/core/sysWebReqDef/start")
    JsonResult start(@RequestBody JSONObject jsonObject);



    /**
     * 执行第三方接口
     * @param apiId
     * @param params
     * @return
     */
    @PostMapping("/system/core/sysInterfaceApi/executeApi")
    JsonResult executeApi(@ApiParam @RequestParam("apiId") String apiId, @ApiParam @RequestBody JSONObject params);
}
