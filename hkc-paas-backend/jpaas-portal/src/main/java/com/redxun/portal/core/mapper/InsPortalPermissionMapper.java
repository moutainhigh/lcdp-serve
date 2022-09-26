package com.redxun.portal.core.mapper;

import com.redxun.portal.core.entity.InsPortalPermission;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 布局权限设置数据库访问层
*/
@Mapper
public interface InsPortalPermissionMapper extends BaseDao<InsPortalPermission> {
    List<InsPortalPermission> getListByLayoutId(@Param("layoutId") String layoutId);
}
