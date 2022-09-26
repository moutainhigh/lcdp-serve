package com.redxun.feign.common;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient("customer-service")
public interface CustomerClient {

    /**
     * 使用post调用微服务接口。
     * @param appName
     * @param url
     * @param params
     * @return
     */
    @PostMapping("//{appName}/{url}")
    Object executePostApi(@PathVariable("appName") String appName,
                          @PathVariable("url") String url,
                          @RequestBody Object params);


    /**
     * 使用get方法调用微服务接口。
     * @param appName
     * @param url
     * @param params
     * @return
     */
    @GetMapping("//{appName}/{url}")
    Object executeGetApi(@PathVariable("appName") String appName,
                         @PathVariable("url") String url,
                         @SpringQueryMap Object params);
}
