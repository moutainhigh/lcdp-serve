package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysKettleLog;
import org.apache.ibatis.annotations.Mapper;

/**
* KETTLE日志数据库访问层
*/
@Mapper
public interface SysKettleLogMapper extends BaseDao<SysKettleLog> {

}
