package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysMenuRelease;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 发布菜单路径记录表数据库访问层
*/
@Mapper
public interface SysMenuReleaseMapper extends BaseDao<SysMenuRelease> {

}
