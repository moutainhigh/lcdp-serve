package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysKettleLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* KETTLE日志数据库访问层
*/
@Mapper
public interface SysKettleLogMapper extends BaseDao<SysKettleLog> {

}
