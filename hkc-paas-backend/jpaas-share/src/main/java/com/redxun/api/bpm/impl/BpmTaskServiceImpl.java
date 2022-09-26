package com.redxun.api.bpm.impl;

import com.redxun.api.bpm.IBpmTaskService;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.dto.bpm.BpmTaskDto;
import com.redxun.feign.bpm.BpmClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * BPM 任务服务类
 */
@Service
public class BpmTaskServiceImpl implements IBpmTaskService {
    @Resource
    @Lazy
    BpmClient bpmClient;

    /**
     * 获取我的草稿服务
     * @return
     */
    @Override
    public JsonResult getMyAllDraftBpmInst(){
        return bpmClient.getMyAllDraftBpmInst();
    }

    /**
     * 获取我已经审批的流程实例
     * @return
     */
    @Override
    public JsonPageResult getMyAllApproved(){
        return bpmClient.getMyAllApproved();
    }

    /**
     * 获取我已经审批的流程实例数据并且分页返回
     * @return
     */
    @Override
    public JsonPageResult getMyApprovedInsts() {
        return  bpmClient.getMyApprovedInsts(new QueryData());
    }

    /**
     * 获取我已经审批过的流程实例数
     * @return
     */
    @Override
    public JsonResult getMyApprovedInstCount() {
        return bpmClient.getMyApprovedInstCount();
    }

    /**
     * 获取我的待办
     * @param queryData
     * @return
     */
    @Override
    public JsonPageResult myTasks(@RequestBody QueryData queryData){
        return bpmClient.myTasks(queryData);
    }

    /**
     * 获取我的任务数
     * @return
     */
    @Override
    public JsonResult getMyTaskCounts() {
        return bpmClient.getMyTaskCount();
    }

    /**
     * 获取我的某段时间内的待办
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<BpmTaskDto> myTasksToStartBetweenEnd(@RequestParam(value = "startTime") String startTime,
                                                     @RequestParam(value = "endTime") String endTime){
        return bpmClient.myTasksToStartBetweenEnd(startTime,endTime);
    }
}
