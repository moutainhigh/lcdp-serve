package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
* 系统日志数据库访问层
*/
@Mapper
public interface SysLogMapper extends BaseDao<SysLog> {

}
