package com.redxun.feign.common;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * jpaas-form的通用请求封装
 */
@FeignClient(name = "jpaas-form")
public interface FormClient {
    /**
     * 执行POST请求
     * @param url  restful URL
     * @param params 参数
     * @return
     */
    @PostMapping("{url}")
    Object executePostApi(@PathVariable("url") String url, @RequestBody Object params);

    /**
     * 执行GET请求
     * @param url
     * @param params
     * @return
     */
    @GetMapping("{url}")
    Object executeGetApi(@PathVariable("url") String url, @SpringQueryMap Object params);
}

