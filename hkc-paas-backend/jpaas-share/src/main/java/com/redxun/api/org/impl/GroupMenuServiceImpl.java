package com.redxun.api.org.impl;


import com.redxun.api.org.IGroupMenuService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.dto.user.OsGroupMenuDto;
import com.redxun.feign.org.OsGroupMenuClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户组菜单服务类
 * 实现用户组与菜单的映射关联处理
 */
@Component
public class GroupMenuServiceImpl implements IGroupMenuService {

    @Autowired
    @Lazy
    OsGroupMenuClient osGroupMenuClient;

    /**
     * 获取租户下的用户授权菜单
     * @param userId
     * @param tenantId
     * @return
     */
    @Override
    public JsonResult<List<OsGroupMenuDto>> getResourceByGrade(String userId, String tenantId) {
        return osGroupMenuClient.getResourceByGrade(userId,tenantId);
    }
}
