package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmSignData;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 任务会签数据数据库访问层
*/
@Mapper
public interface BpmSignDataMapper extends BaseDao<BpmSignData> {

    /**
     * 通过流程实例与任务节点Id获取流程会签数据
     * @param actInstId
     * @param nodeId
     * @return
     */
    List<BpmSignData> getByActInstIdNodeId(@Param("actInstId") String actInstId, @Param("nodeId") String nodeId);

    /**
     * 获取某流程某任务的总投票数
     * @param actInstId
     * @param nodeId
     * @return
     */
    Integer getCountsByActInstIdNodeId(@Param("actInstId") String actInstId, @Param("nodeId") String nodeId);

    /**
     * 获取某流程某任务的总投票数
     * @param actInstId
     * @param nodeId
     * @param voteStatus
     * @return
     */
    Integer getCountsByActInstIdNodeIdVoteStatus(@Param("actInstId") String actInstId, @Param("nodeId") String nodeId,@Param("voteStatus") String voteStatus);

    /**
     * 按流程实例删除数据
     * @param actInstId
     */
    void deleteByActInstId(String actInstId);


    /**
     * 修改用户ID
     * @param receiptUserId
     * @param deliverUserId
     */
    void updateUserId(@Param("receiptUserId")String receiptUserId, @Param("deliverUserId")String deliverUserId);
}
