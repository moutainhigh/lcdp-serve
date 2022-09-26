package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysAppActionLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 应用操作日志数据库访问层
*/
@Mapper
public interface SysAppActionLogMapper extends BaseDao<SysAppActionLog> {


}
