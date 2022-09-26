package com.redxun.feign;

import com.redxun.dto.sys.SysDicDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "jpaas-system")
public interface SysDicClient {
    /**
     * 根据分类ID与项值查询数据字典
     * @param treeId
     * @param dicValue
     * @return
     */
    @ApiOperation("根据分类ID与项值查询数据字典")
    @GetMapping("/system/core/sysDic/getDicListByTreeIdDicValues")
    List<SysDicDto> getDicListByTreeIdDicValues(@ApiParam @RequestParam(value = "treeId") String treeId,
                                                @ApiParam @RequestParam(value = "dicValue") String dicValue);

}
