package com.redxun.feign.bpm;

import com.alibaba.fastjson.JSONObject;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.dto.bpm.BpmCheckHistoryDto;
import com.redxun.dto.bpm.BpmTaskDto;
import com.redxun.feign.bpm.entity.BpmInst;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * BPM的客户端
 */
@FeignClient(name = "jpaas-bpm")
public interface BpmClient {
    /**
     * 查询我的所有已办
     * @return
     * @throws Exception
     */
    @GetMapping("/bpm/core/bpmInst/getMyAllDraftBpmInst")
    JsonResult getMyAllDraftBpmInst();

    /**
     * 查询我的所有已办历史
     * @return
     * @throws Exception
     */
    @GetMapping("/bpm/core/bpmCheckHistory/getMyAllApproved")
    JsonPageResult getMyAllApproved();

    /**
     * 查询我的已经办流程历史
     * @param queryData
     * @return
     */
    @PostMapping("/bpm/core/bpmInst/getMyApprovedInsts")
    JsonPageResult getMyApprovedInsts(@RequestBody QueryData queryData);


    /**
     * 查询我的已办流程实例数
     * @return
     */
    @GetMapping("/bpm/core/bpmInst/getMyApprovedInstCount")
    JsonResult getMyApprovedInstCount();

    /**
     * 按条件查询所有的个人待办
     * @return
     * @throws Exception
     */
    @PostMapping("/bpm/core/bpmTask/myTasks")
    JsonPageResult myTasks(@RequestBody QueryData queryData);

    /**
     * 查询我的待办数
     * @return
     * @throws Exception
     */
    @GetMapping("/bpm/core/bpmTask/getMyTaskCount")
    JsonResult getMyTaskCount();

    @GetMapping("/bpm/core/bpmTask/myTasksToStartBetweenEnd")
    List<BpmTaskDto> myTasksToStartBetweenEnd(@RequestParam(value = "startTime") String startTime,
                                              @RequestParam(value = "endTime") String endTime);


    /**
     * 保存草稿。
     * @param startCmd
     * @return
     */
    @PostMapping("/bpm/core/bpmInst/saveDraft")
    JsonResult saveDraft(@ApiParam @RequestBody JSONObject startCmd);


    /**
     * 启动流程。
     * @param startCmd
     * @return
     */
    @PostMapping("/bpm/core/bpmInst/startProcess")
    JsonResult startProcess(@ApiParam @RequestBody JSONObject startCmd);

    /**
     * 让流程继续往下执行。
     * @param executionId
     * @param jumpType
     * @param opinion
     */
    @GetMapping("/bpm/core/bpmInst/trigger")
    void trigger(@RequestParam(value="executionId") String executionId,
                 @RequestParam(value="jumpType")String jumpType,
                 @RequestParam(value="opinion")String opinion);

    /**
     * 根据instId查询OpinionName不为空的审批意见
     * @param instId
     * @return
     */
    @GetMapping("/bpm/core/bpmCheckHistory/getOpinionNameNotEmpty")
    List<BpmCheckHistoryDto> getOpinionNameNotEmpty(@RequestParam("instId") String instId);
}

