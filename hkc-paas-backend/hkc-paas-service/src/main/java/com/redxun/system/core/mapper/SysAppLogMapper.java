package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysAppLog;
import org.apache.ibatis.annotations.Mapper;

/**
* 应用授权接口日志表数据库访问层
*/
@Mapper
public interface SysAppLogMapper extends BaseDao<SysAppLog> {

}
