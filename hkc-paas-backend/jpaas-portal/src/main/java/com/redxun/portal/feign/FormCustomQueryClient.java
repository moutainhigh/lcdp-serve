package com.redxun.portal.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 单据自定义查询客户端
 * @author hujun
 */
@FeignClient(name = "jpaas-form")
public interface FormCustomQueryClient {
    /**
     * 根据别名查询自定义SQL数据
     * @param alias
     * @return
     */
    @ApiOperation("根据别名查询自定义SQL数据")
    @PostMapping("/form/core/formCustomQuery/queryForJson_{alias}")
    JsonResult queryForJsonByAlias(@ApiParam @PathVariable("alias") String alias);
}
