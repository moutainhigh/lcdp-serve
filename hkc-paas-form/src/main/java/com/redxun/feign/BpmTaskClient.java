package com.redxun.feign;

import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.dto.bpm.BpmTaskDto;
import com.redxun.dto.bpm.TaskExecutor;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@FeignClient(name = "jpaas-bpm")
public interface BpmTaskClient {
    @ApiOperation("通过InstId获取待办信息")
    @GetMapping("/bpm/core/bpmTask/getByInstId")
    List<BpmTaskDto> getByInstId(@ApiParam @RequestParam("instId")String instId);

    @ApiOperation("通过ActInstId获取待办信息")
    @GetMapping("/bpm/core/bpmTask/getByActInstId")
    List<BpmTaskDto> getByActInstId(@ApiParam @RequestParam("actInstId")String actInstId);
    @ApiOperation("通过taskId获取审批人信息")
    @GetMapping("/bpm/core/bpmTask/getTaskExecutors")
    Set<TaskExecutor> getTaskExecutors(@ApiParam @RequestParam("taskId")String taskId);
    @ApiOperation("按条件查询对应流程定义的所有的个人待办")
    @PostMapping("/bpm/core/bpmTask/myTasksByDefKey")
    JsonPageResult myTasksByDefKey(@ApiParam @RequestParam("defKey")String defKey, @ApiParam @RequestBody QueryData queryData);
}
