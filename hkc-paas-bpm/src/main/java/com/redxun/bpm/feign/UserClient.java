package com.redxun.bpm.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "jpaas-user")
public interface UserClient {

    /**
     * 根据用户id和属性名获取属性值
     * @param json
     * @return
     */
    @PostMapping("/user/org/osUser/getUserProperty")
    JsonResult getUserProperty(@RequestBody JSONObject json);

    /**
     * 根据组织id和属性名获取属性值
     * @param json
     * @return
     */
    @PostMapping("/user/org/osGroup/getGroupProperty")
    JsonResult getGroupProperty(@RequestBody JSONObject json);
}
