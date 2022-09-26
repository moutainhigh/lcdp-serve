package com.redxun.bpm.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmInstCc;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* 流程抄送/转发数据库访问层
*/
@Mapper
public interface BpmInstCcMapper extends BaseDao<BpmInstCc> {

    /**
     * 获取我发送的抄送或转办
     * @param page
     * @param params
     * @return
     */
    IPage getMyTurnTo(IPage page, @Param("w") Map<String, Object> params);


    /**
     * 获取我收到的抄送或转办
     * @param page
     * @param params
     * @return
     */
    IPage getMyReceiveTurn(IPage page, @Param("w") Map<String, Object> params);

    /**
     * 根据实例ID获取实例列表。
     * @param instId
     * @return
     */
    List<BpmInstCc> getByInstId(String instId);

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
    void delArchiveByInstId(@Param("instId")String instId,@Param("tableId") int tableId);
}
