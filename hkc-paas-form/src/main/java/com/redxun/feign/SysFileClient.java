package com.redxun.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.dto.sys.SysFileDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "jpaas-system")
public interface SysFileClient {
    /**
     * 通过主键获取
     *
     * @param fileId
     * @return
     */
    @ApiOperation("通过主键获取")
    @GetMapping("/system/core/sysFile/getByFileId")
    SysFileDto getByFileId(@ApiParam @RequestParam("fileId") String fileId);


    /**
     * 生成Excel文件
     *  excelConfig
     * {
     *     fileName:"文件名",
     *     heads:"列头",
     *     data:"数据",
     * }
     * @return
     */
    @ApiOperation("生成Excel文件")
    @PostMapping("/system/core/sysFile/genExcel")
    void genExcel(@RequestBody JSONObject excelConfig);


    /**
     * 根据文件ID删除文件
     * @param fileIds
     * @return
     */
    @ApiOperation("根据文件ID删除文件")
    @GetMapping("/system/core/sysFile/delByFileIds")
    void delByFileId(@ApiParam @RequestParam("fileIds") List<String> fileIds);
}
