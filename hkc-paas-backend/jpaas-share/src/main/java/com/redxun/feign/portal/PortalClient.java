package com.redxun.feign.portal;


import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Portal Feign客户端
 */
@FeignClient(name = "jpaas-portal")
public interface PortalClient {

    /**
     * 发送内部消息
     * @param jsonObject
     * @return
     */
    @PostMapping("/portal/core/infInnerMsg/sendSystemMsg")
    JsonResult sendSystemMsg(@RequestBody JSONObject jsonObject);


}
