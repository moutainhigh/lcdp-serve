package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmInstPermission;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 流程实例权限表数据库访问层
*/
@Mapper
public interface BpmInstPermissionMapper extends BaseDao<BpmInstPermission> {
    /**
     * 根据实例获取数量
     * @param instId
     * @param authId
     * @return
     */
    Integer getByInstId(@Param(value = "instId") String instId, @Param(value = "authId") String authId,@Param(value = "taskId")String taskId);

    /**
     * 根据管理员获取权限数据。
     * @param authId
     * @param defKey
     * @return
     */
    List<BpmInstPermission> getByAdmin(@Param(value = "authId")String authId, @Param(value = "defKey") String defKey);
}
