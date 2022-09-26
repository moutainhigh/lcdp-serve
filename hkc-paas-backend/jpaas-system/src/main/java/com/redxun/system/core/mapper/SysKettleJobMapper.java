package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysKettleJob;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* KETTLE任务调度数据库访问层
*/
@Mapper
public interface SysKettleJobMapper extends BaseDao<SysKettleJob> {

}
