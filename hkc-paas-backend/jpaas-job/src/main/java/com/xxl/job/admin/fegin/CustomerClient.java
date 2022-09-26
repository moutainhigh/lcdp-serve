package com.xxl.job.admin.fegin;

import com.redxun.common.constant.ServiceNameConstants;
import com.xxl.job.core.biz.model.*;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CustomerClient {

    /**
     * 使用post调用微服务接口。
     * @param appName
     * @param url
     * @param params
     * @return
     */
    @RequestLine("POST /executor/run")
    ReturnT<String> run(@RequestBody TriggerParam triggerParam);


    @RequestLine("POST /executor/idleBeat")
    ReturnT<String> idleBeat(@RequestBody IdleBeatParam idleBeatParam);

    @RequestLine("POST /executor/kill")
    ReturnT<String> kill(@RequestBody KillParam killParam);

    @RequestLine("POST /executor/log")
    ReturnT<LogResult> log(@RequestBody LogParam logParam);

}
