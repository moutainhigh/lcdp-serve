package com.redxun.bpm.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 流程任务数据库访问层
*/
@Mapper
public interface BpmTaskMapper extends BaseDao<BpmTask> {
    /**
     * 按流程实例删除任务
     * @param instId
     */
    void deleteByInstId(String instId);


    /**
     * 按ActTaskId删除任务
     * @param actTaskId
     */
    void deleteByActTaskId(String actTaskId);

    /**
     * 按实例 Id获得任务列表
     * @param instId
     * @return
     */
    List<BpmTask> getByInstId(String instId);

    /**
     * 按Act任务Id获取任务
     * @param actTaskId
     * @return
     */
    List<BpmTask> getByActTaskId(String actTaskId);

    /**
     * 更新任务状态
     * @param taskId
     * @param status
     */
    void updateStatus(@Param("taskId") String taskId, @Param("status") String status,@Param("assignee") String assignee);





    /**
     * 获取任务的下一级沟通的任务数
     * @param cmStTaskId
     * @return
     */
    Integer getCountByCmStTaskId(String cmStTaskId);

    /**
     * 查找个人的用户待办任务
     * @param page
     * @param params
     * @param userId
     * @param groupIds
     * @return
     */
    IPage<BpmTask> getByUserId(IPage<BpmTask> page,@Param("w") Map<String, Object> params,
                                 @Param("userId")String userId,@Param("groupIds") List<String> groupIds,@Param("tenantId")String tenantId);
    /**
     * 查找个人的用户待办任务
     * @param page
     * @param params
     * @param userId
     * @param groupIds
     * @Param defKey
     * @return
     */
    IPage<BpmTask> getByUserIdDefKey(IPage<BpmTask> page,@Param("w") Map<String, Object> params,
                               @Param("userId")String userId,@Param("groupIds") List<String> groupIds,@Param("defKey")String defKey);

    /**
     * 查找个人的待办数量
     * @param userId
     * @param groupIds
     * @return
     */
    Integer getCountsByUserId(@Param("userId")String userId,@Param("groupIds") List<String> groupIds,@Param("tenantId")String tenantId);


    List<BpmTask>  getByStartBetweenEnd(@Param("w") Map<String, Object> params,
                                        @Param("groupIds") List<String> groupIds);



    /**
     * 根据流程实例ID和当前用户查询待办任务。
     * @param userId    用户ID
     * @param groupIds  用户组ID
     * @param instId    实例ID
     * @return
     */
    List<BpmTask> getByInstUserId(@Param("userId")String userId,
                                  @Param("groupIds") List<String> groupIds,
                                  @Param("instId")String instId,
                                  @Param("tenantId")String tenantId);

    /**
     * 修改执行人
     * @param receiptUserId
     * @param deliverUserId
     */
    void updateAssignee(@Param("receiptUserId")String receiptUserId, @Param("deliverUserId")String deliverUserId);
}
