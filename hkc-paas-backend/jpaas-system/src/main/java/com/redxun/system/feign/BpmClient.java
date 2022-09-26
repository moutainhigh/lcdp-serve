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

@FeignClient(name = "jpaas-bpm")
public interface BpmClient {
    /**
     * @Description: 删除本数据库中应用相关的数据
     * @param appId 应用ID
     **/
    @PostMapping( "/bpm/core/appdata/delete")
    void delete(@RequestParam("appId") String appId);

    /**
     * @Description:  查询本数据库中应用相关的数据
     * @param appId 应用ID
     **/
    @GetMapping( "/bpm/core/appdata/query")
    Map<String,String> query(@RequestParam("appId") String appId);

    /**
     * @Description: 导入本数据库中应用相关的数据
     * @param data 应用相关数据
     **/
    @PostMapping( value = "/bpm/core/appdata/install",consumes = "application/json;charset=UTF-8")
    JsonResult install( @RequestBody String data);

    /**
     * @Description: 应用导入前数据校验
     * @param array 应用相关表记录数组
     **/
    @PostMapping( value = "/bpm/core/appdata/importCheck",consumes = "application/json;charset=UTF-8",produces = {"application/json;charset=UTF-8"})
    JsonResult importCheck(@RequestBody  Object[]  array);

    /**
     * 统一消息改造---szw
     * @Description: 获取单推数据
     * @param list 单推ID列表
     **/
    @PostMapping( value = "/bpm/core/mobiletag/getBycidList")
    JSONObject getBycidList(@RequestBody JSONArray list);
}
