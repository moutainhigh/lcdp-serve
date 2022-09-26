package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmInstRouter;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
* 流程实例路由数据库访问层
*/
@Mapper
public interface BpmInstRouterMapper extends BaseDao<BpmInstRouter> {

    /**
     * 备份数据
     * @param archiveDate
     * @param tableId
     */
    void archiveByArchiveDate(@Param("archiveDate") Date archiveDate,
                              @Param("tableId") Integer tableId,
                              @Param("tenantId")String tenantId);
}
