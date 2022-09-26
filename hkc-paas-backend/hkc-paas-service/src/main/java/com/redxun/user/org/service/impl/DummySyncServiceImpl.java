package com.redxun.user.org.service.impl;

import com.redxun.common.base.entity.JsonResult;
import com.redxun.user.org.entity.OsGroup;
import com.redxun.user.org.entity.OsUser;
import com.redxun.user.org.service.ISyncService;

import java.util.List;

/**
 *
 */
public class DummySyncServiceImpl implements ISyncService {

    @Override
    public JsonResult<List<OsUser>> getUsers(String tenantId, List<OsGroup> groups) {
        return null;
    }

    @Override
    public JsonResult<List<OsGroup>> getDepartMent(String tenantId) {
        return null;
    }
}
