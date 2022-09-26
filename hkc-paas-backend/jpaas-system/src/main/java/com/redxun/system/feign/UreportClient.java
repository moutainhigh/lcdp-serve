package com.redxun.system.feign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description: 通过Feign调用报表中的服务
 * @Author: Elwin ZHANG
 * @Date: 2021/6/28 11:50
 **/
@FeignClient(name = "jpaas-ureport")
public interface UreportClient {
    /**
     * @Description: 删除本数据库中应用相关的数据
     * @param appId 应用ID
     **/
    @PostMapping( "/ureport2/core/appdata/delete")
    void delete(@RequestParam("appId") String appId);

    /**
     * @Description:  查询本数据库中应用相关的数据
     * @param appId 应用ID
     **/
    @GetMapping( "/ureport2/core/appdata/query")
    Map<String,String> query(@RequestParam("appId") String appId);

    /**
     * @Description: 导入本数据库中应用相关的数据
     * @param data 应用相关数据
     **/
    @PostMapping( value = "/ureport2/core/appdata/install",consumes = "application/json;charset=UTF-8")
    JsonResult install( @RequestBody String data);

    /**
    * @Description: 应用导入前数据校验
    * @param array 应用相关表记录数组
    **/
    @PostMapping( value = "/ureport2/core/appdata/importCheck",consumes = "application/json;charset=UTF-8")
    JsonResult importCheck(@ApiParam @RequestBody Object[] array);
}
