package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmInstMsg;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* 流程沟通留言板数据库访问层
*/
@Mapper
public interface BpmInstMsgMapper extends BaseDao<BpmInstMsg> {
    /**
     * 获得流程实例
     * @param instId
     * @return
     */
    List<BpmInstMsg> getByInstId(String instId);

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
    void delArchiveByInstId(@Param("instId") String instId,@Param("tableId") Integer tableId);
}
