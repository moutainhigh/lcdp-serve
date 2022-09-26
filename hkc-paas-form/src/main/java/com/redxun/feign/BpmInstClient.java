package com.redxun.feign;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.bpm.BpmInstDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "jpaas-bpm")
public interface BpmInstClient {
    @ApiOperation("通过实例ID获取实例信息")
    @GetMapping("/bpm/core/bpmInst/getById")
    BpmInstDto getById(@ApiParam @RequestParam("instId")String instId);

    @ApiOperation("通过实例ID获取实例信息")
    @GetMapping("/bpm/core/bpmInst/getVariables")
    Map<String,Object>  getVariables(@ApiParam @RequestParam("actInstId")String actInstId);

    @ApiOperation("根据INST_ID_获取变量数据")
    @GetMapping("/bpm/core/bpmInst/getVariablesByInstId")
    Map<String,Object>  getVariablesByInstId(@ApiParam @RequestParam("instId")String instId);

    @ApiOperation("根据PK和流程定义ID查询流程状态")
    @GetMapping("/bpm/core/bpmInst/getStatusByBusKey")
    JsonResult getStatusByBusKey(@ApiParam @RequestParam(value="defIds") String defIds,
                           @ApiParam @RequestParam(value="pk")String pk);
    /**
     * 根据流程实例ID获取任务ID
     * @return
     */
    @PostMapping(value="/bpm/core/bpmInst/getTaskIds")
    List getTaskIds(@ApiParam @RequestParam(value="instIds") String  instIds) ;

    /**
     * 根据流程实例ID获取任务ID
     * @return
     */
    @GetMapping(value="/bpm/core/bpmInst/getInstDetailForInterpose")
    JSONObject getInstDetailForInterpose(@ApiParam @RequestParam(value="instId") String  instId,@ApiParam @RequestParam(value="isMobile") String  isMobile) ;

    /**
     * 获取审批历史
     * @return
     */
    @GetMapping(value="/bpm/core/bpmCheckHistory/getCheckHistorys")
    List getCheckHistorys(@ApiParam @RequestParam(value="instId") String  instId) ;

}
