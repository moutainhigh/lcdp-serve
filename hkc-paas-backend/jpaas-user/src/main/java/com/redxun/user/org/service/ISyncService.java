package com.redxun.user.org.service;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsUser;

import java.util.List;

/**
 * 同步用户组织接口
 */
public interface ISyncService {

    /**
     * 获取用户
     * @param tenantId
     * @param groups
     * @return
     */
     JsonResult<List<OsUser>> getUsers(String tenantId, List<OsGroup> groups);

    /**
     * 获取部门
     * @param tenantId
     * @return
     */
    JsonResult<List<OsGroup>> getDepartMent(String tenantId);

}
