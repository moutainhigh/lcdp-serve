package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysErrorLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 错误日志数据库访问层
*/
@Mapper
public interface SysErrorLogMapper extends BaseDao<SysErrorLog> {

}
