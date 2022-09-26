package com.redxun.portal.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * BPM 任务调用Feign客户端
 */
@FeignClient(name = "jpaas-bpm")
public interface BpmTaskClient {
    @ApiOperation("获取组织用户信息")
    @GetMapping("/bpm/core/bpmTask/getDataListByUserId")
    JsonResult<List<JSONObject>> getDataListByUserId(@ApiParam @RequestParam("userId") String userId);

    @GetMapping("/bpm/core/bpmCheckHistory/getMyApprovedTaskCount")
    JsonResult getMyApprovedTaskCount();
}
