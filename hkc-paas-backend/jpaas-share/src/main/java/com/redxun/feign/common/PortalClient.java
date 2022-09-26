package com.redxun.feign.common;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * jpaas-portal的通用请求封装
 */
@FeignClient(name = "jpaas-portal")
public interface PortalClient {
    /**
     * 发起POST请求
     * @param url
     * @param params
     * @return
     */
    @PostMapping("{url}")
    Object executePostApi(@PathVariable("url") String url, @RequestBody Object params);

    /**
     * 发起GET请求
     * @param url
     * @param params
     * @return
     */
    @GetMapping("{url}")
    Object executeGetApi(@PathVariable("url") String url, @SpringQueryMap Object params);
}
