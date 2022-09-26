package com.redxun.gateway.feign;

import com.redxun.gateway.SysAppAuthDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description: Feign调用
 * @Author: Elwin ZHANG
 * @Date: 2021/8/23 17:21
 **/
@FeignClient(name = "jpaas-system")
public interface SystemClient {

    /**
     * 通过appId查询授权接口
     * @param appId
     * @return
     */
    @GetMapping("/system/core/sysAppAuth/findListByAppId")
    List<SysAppAuthDto> findListByAppId(@RequestParam(value="appId") String appId);

}
