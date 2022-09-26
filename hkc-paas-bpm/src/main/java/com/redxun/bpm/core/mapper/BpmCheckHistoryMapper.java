package com.redxun.bpm.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmCheckHistory;
import com.redxun.bpm.core.entity.BpmInstData;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* 流程审批流转记录数据库访问层
*/
@Mapper
public interface BpmCheckHistoryMapper extends BaseDao<BpmCheckHistory> {
    /**
     * 按流程实例Id返回审批历史
     *
     * @param instId
     * @return
     */
    List<BpmCheckHistory> getByInstId(String instId);

    /**
     * 按流程实例Id删除数据
     *
     * @param instId
     * @return
     */
    void delByInstId(@Param("instId") String instId);

    /**
     * 按流程实例Id返回审批历史，并且按时间倒序
     *
     * @param instId
     * @return
     */
    List<BpmCheckHistory> getByInstIdDesc(String instId);

    /**
     * 获取我审批的所有数据
     * @param params
     * @return
     */
    List<BpmCheckHistory> getMyApproved(@Param("w") Map<String, Object> params);


    /**
     * 获取我审批的
     *
     * @param page
     * @param params
     * @return
     */
    IPage<T> getMyApproved(IPage<T> page, @Param("w") Map<String, Object> params);


    /**
     * 根据instId查询OpinionName不为空的审批意见
     *
     * @param instId
     * @return
     */
    List<BpmCheckHistory> getOpinionNameNotEmpty(@Param("instId") String instId);

    /**
     * 获取某个节点的所有审批历史，并按时间倒序
     *
     * @param instId
     * @param nodeId
     * @return
     */
    List<BpmCheckHistory> getByInstIdNodeId(@Param("instId") String instId, @Param("nodeId") String nodeId);

    /**
     * 获取某个节点的最新一条审批历史的数据
     * @param instId
     * @param nodeId
     * @return
     */
    BpmCheckHistory getLatestByInstIdNodeId(@Param("instId") String instId, @Param("nodeId") String nodeId);

    /**
     * 备份数据
     * @param archiveDate
     * @param tableId
     */
    void archiveByArchiveDate(@Param("archiveDate") Date archiveDate,@Param("tableId") Integer tableId,@Param("tenantId") String tenantId);

    /**
     * 删除已备份的数据
     * @param archiveDate
     * @param batSize
     * @param dbType
     * @return
     */
    int removeByArchiveDate(@Param("archiveDate")Date archiveDate, @Param("batSize")Integer batSize,@Param("dbType")String dbType);


    /**
     * 根据instId获取备份数据
     * @param instId
     * @param tableId
     * @return
     */
    List<BpmCheckHistory> getByArchiveLog(@Param("instId")String instId, @Param("tableId")Integer tableId);

    /**
     * 根据instId获取意见不为空的备份数据
     * @param instId
     * @param tableId
     * @return
     */
    List<BpmCheckHistory> getArchiveByOpinionNameNotEmpty(@Param("instId")String instId, @Param("tableId")Integer tableId);

    /**
     * 删除备份的数据
     * @param instId
     */
    void delArchiveByInstId(@Param("instId")String instId,@Param("tableId") int tableId);

    /**
     * 获取我的已办任务数
     * @param userId
     * @param tenantId
     * @return
     */
    Integer getMyApprovedCount(@Param("userId") String userId,@Param("tenantId")String tenantId);
}

