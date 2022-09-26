package com.redxun.api.org;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.user.OsGroupMenuDto;

import java.util.List;


public interface IGroupMenuService {

    /**
     * 根据用户ID和租户ID获取菜单资源。
     * @param userId
     * @param tenantId
     * @return
     */
    JsonResult<List<OsGroupMenuDto>> getResourceByGrade(String userId, String tenantId);
}
