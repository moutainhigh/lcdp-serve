package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmArchiveLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

import java.util.Date;
import java.util.List;

/**
* 流程归档日志数据库访问层
*/
@Mapper
public interface BpmArchiveLogMapper extends BaseDao<BpmArchiveLog> {

    /**
     * 根据归档时间获取是否已有归档记录
     * @param archiveDate
     * @return
     */
    Integer getFinishTimes(Date archiveDate);

    /**
     * 获取tableId
     * @param status
     * @return
     */
    Integer getMaxTableId(String status);

    /**
     * 获取归档记录
     * @return
     */
    List<BpmArchiveLog> getBpmArchiveLogs();
}
