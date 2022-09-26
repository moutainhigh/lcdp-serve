package com.redxun.api.bpm;

import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.dto.bpm.BpmTaskDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 日历时间计算接口。
 *
 */
public interface IBpmTaskService {

    /**
     *  查询我创建的草稿
     * @return
     * @throws Exception
     */
    JsonResult getMyAllDraftBpmInst();

    /**
     *  查询我的所有已办历史
     * @return
     * @throws Exception
     */
    JsonPageResult getMyAllApproved();

    /**
     * 查看我的已办流程实例
     * @return
     */
    JsonPageResult getMyApprovedInsts();


    /**
     * 查看我的已办流程实例数
     * @return
     */
    JsonResult getMyApprovedInstCount();


    /**
     * 按条件查询所有的个人待办
     * @return
     * @throws Exception
     */
    JsonPageResult myTasks(@RequestBody QueryData queryData);

    /**
     * 查询我的待办数
     * @return
     */
    JsonResult getMyTaskCounts();


    /**
     * 按条件查询所有的个人待办
     * @return
     * @throws Exception
     */
    List<BpmTaskDto> myTasksToStartBetweenEnd(@RequestParam(value = "startTime") String startTime,
                                              @RequestParam(value = "endTime") String endTime);
}
