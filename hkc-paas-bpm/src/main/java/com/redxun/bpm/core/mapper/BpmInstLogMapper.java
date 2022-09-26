package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmInstLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* 流程活动日志数据库访问层
*/
@Mapper
public interface BpmInstLogMapper extends BaseDao<BpmInstLog> {
    /**
     * 流程某流程实例下的活动日志
     * @param instId 流程实例Id
     */
    void delByInstId(String instId);

    /**
     * 获得某流程活动列表
     * @param instId 流程实例Id
     * @return
     */
    List<BpmInstLog> getByInstId(String instId);

    /**
     * 备份数据
     * @param archiveDate
     * @param tableId
     */
    void archiveByArchiveDate(@Param("archiveDate") Date archiveDate, @Param("tableId") Integer tableId,@Param("tenantId") String tenantId);

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
    void delArchiveByInstId(@Param("instId")String instId,@Param("tableId") int tableId);
}
