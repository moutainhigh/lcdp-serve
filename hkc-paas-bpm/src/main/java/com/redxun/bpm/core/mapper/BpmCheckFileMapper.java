package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmCheckFile;
import com.redxun.bpm.core.entity.BpmCheckHistory;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* 审批意见附件数据库访问层
*/
@Mapper
public interface BpmCheckFileMapper extends BaseDao<BpmCheckFile> {


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
     * 根据hisId获取意见不为空的备份数据
     * @param hisId
     * @param tableId
     * @return
     */
    List<BpmCheckFile> getByArchiveLog(@Param("hisId")String hisId, @Param("tableId")Integer tableId);

    /**
     * 删除备份的数据
     * @param instId
     */
    void delArchiveByInstId(@Param("instId") String instId,@Param("tableId") Integer tableId);
}
