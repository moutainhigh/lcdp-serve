package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* 流程实例运行路线数据库访问层
*/
@Mapper
public interface BpmRuPathMapper extends BaseDao<BpmRuPath> {
    /**
     * 按流程实例获取执行列表
     * @param instId
     * @return
     */
    List<BpmRuPath> getByInstId(String instId);

    /**
     * 获取流程实例上某一节点的所有的跳转的数据
     * @param instId
     * @param nodeId
     * @return
     */
    List<BpmRuPath> getByInstIdNodeId(@Param("instId") String instId,@Param("nodeId") String nodeId);

    /**
     * 按流程实例Id删除数据
     * @param instId
     */
    void delByInstId (@Param("instId") String instId);



    /**
     * 通过executionId获取所有跳转的数据
     * @param executionId
     * @return
     */
    List<BpmRuPath> getByExecutionId(@Param("executionId")String executionId);
    /**
     * 获取流程实例的最大层级数
     * @param instId
     * @return
     */
    Integer getMaxLevelByInst(String instId);


    Integer getMaxLevelByInstIdNodeId(@Param("instId") String instId,@Param("nodeId") String nodeId);

    BpmRuPath getByInstIdLevel(@Param("instId") String instId, @Param("level") Integer level);

    List<BpmRuPath> getByInstIdNodeIdLevel(@Param("instId") String instId,@Param("nodeId") String nodeId, @Param("level") Integer level);

    /**
     * 获取从某个节点开始最早的审批记录。
     * @param instId
     * @param nodeId
     * @return
     */
    List<BpmRuPath> getEarliestByInstId(@Param("instId") String instId,@Param("nodeId") String nodeId);

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
     * 删除备份的数据
     * @param instId
     */
    void delArchiveByInstId(@Param("instId") String instId,@Param("tableId") Integer tableId);
}
