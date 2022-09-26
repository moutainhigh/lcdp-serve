package com.redxun.bpm.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.Date;
import java.util.Map;

/**
* 流程实例数据库访问层
 * @author  csx
*/
@Mapper
public interface BpmInstMapper extends BaseDao<BpmInst> {
    /**
     * 根据实例 Id更新其状态
     * @param instId
     * @param status 状态值来自 @BpmInstStatus
     */
    void updateStatusByInstId(@Param("instId") String instId, @Param("status") String status);

    /**
     * 按Act实例Id获取流程实例
     * @param actInstId
     * @return
     */
    BpmInst getByActInstId(String actInstId);

    /**
     * 获取个人用户启动的实例
     * @param params
     * @return
     */
   IPage<BpmInst> getByUserId(IPage page, @Param("w") Map<String, Object> params);


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
     * @return
     */
    int removeByArchiveDate(@Param("archiveDate")Date archiveDate, @Param("batSize")Integer batSize,@Param("dbType")String dbType);

    /**
     * 分页查询备份的流程实例
     * @param page
     * @param tableId
     * @return
     */
    IPage<BpmInst> queryByArchiveLog(IPage<BpmInst> page, @Param("w") Map<String, Object> params,@Param("tableId") String tableId);

    /**
     * 根据instId查询备份数据
     * @param instId
     * @return
     */
    BpmInst getByArchiveLog(@Param("instId")String instId,@Param("tableId") int tableId);


    /**
     * 删除备份的数据
     * @param instId
     */
    void delArchiveByInstId(@Param("instId")String instId,@Param("tableId") int tableId);

    /**
     * 获取我的已办流程
     * @param page
     * @param params
     * @return
     */
    IPage<BpmInst> getMyApproved(IPage<T> page, @Param("w") Map<String, Object> params);

    /**
     * 获取我的已办流程数
     * @param userId
     * @param tenantId
     * @return
     */
    Integer getMyApprovedCount(@Param("userId") String userId,@Param("tenantId") String tenantId);

}
