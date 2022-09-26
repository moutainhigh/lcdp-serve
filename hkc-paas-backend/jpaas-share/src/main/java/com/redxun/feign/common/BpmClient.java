package com.redxun.feign.common;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * 用于执行BPM实例各代理方法的客户端
 */
@FeignClient(name = "jpaas-bpm")
public interface BpmClient {
    /**
     * 执行POST方法
     * @param url
     * @param params
     * @return
     */
    @PostMapping("{url}")
    Object executePostApi(@PathVariable("url") String url, @RequestBody Object params);

    /**
     * 执行GET方法
     * @param url
     * @param params
     * @return
     */
    @GetMapping("{url}")
    Object executeGetApi(@PathVariable("url") String url, @SpringQueryMap Object params);
}

