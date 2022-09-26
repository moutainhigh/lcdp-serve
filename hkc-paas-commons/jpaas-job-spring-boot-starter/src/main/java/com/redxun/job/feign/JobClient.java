package com.redxun.job.feign;

import com.redxun.common.constant.ServiceNameConstants;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户的Feign客户端
 */
@FeignClient(name = ServiceNameConstants.JOB_SERVICE)
public interface JobClient {
    /**
     * 注册到调度中心
     * @return
     */
    @PostMapping(value = "job/rest/registry", params = "data")
    ReturnT<String> registry(@RequestBody String data);


    @PostMapping(value = "job/rest/callback", params = "data")
    ReturnT<String> callback(@RequestBody String data);

    @PostMapping(value = "job/rest/registryRemove", params = "data")
    ReturnT<String> registryRemove(@RequestBody String data);

}
