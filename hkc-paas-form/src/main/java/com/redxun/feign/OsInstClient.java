package com.redxun.feign;

import com.redxun.dto.user.OsInstDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author hujun
 */
@FeignClient(name = "jpaas-user")
public interface OsInstClient {
    /**
     * 通过ID获取机构信息
     * @param instId
     * @return
     */
    @ApiOperation("通过ID获取机构信息")
    @GetMapping("/user/org/osInst/getById")
    OsInstDto getById(@ApiParam @RequestParam("instId")String instId);

    /**
     * 获取机构直属下级机构ID集合
     * @param instId
     * @return
     */
    @ApiOperation("获取机构直属下级机构ID集合")
    @GetMapping("/user/org/osInst/getDdownTenantIds")
    List<String> getDdownTenantIds(@ApiParam @RequestParam("instId")String instId);

    /**
     * 获取机构所有下级机构ID集合
     * @param instId
     * @return
     */
    @ApiOperation("获取机构所有下级机构ID集合")
    @GetMapping("/user/org/osInst/getDownTenantIds")
    List<String> getDownTenantIds(@ApiParam @RequestParam("instId")String instId);

}
