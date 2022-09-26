package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmTaskUser;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 任务处理相关人数据库访问层
*/
@Mapper
public interface BpmTaskUserMapper extends BaseDao<BpmTaskUser> {
    /**
     * 按任务删除数据
     * @param taskId
     */
    void deleteByTaskId(String taskId);

    /**
     * 根据任务ID获取所有流程待办事项
     * @param taskId
     * @return
     */
    List<BpmTaskUser> getByTaskId(String taskId);

    /**
     * 根据流程实例ID删除任务。
     * @param instId
     */
    void deleteByInstId(String instId);

    /**
     * 修改执行人
     * @param receiptUserId
     * @param deliverUserId
     */
    void updateUserId(@Param("receiptUserId")String receiptUserId, @Param("deliverUserId")String deliverUserId);
}
