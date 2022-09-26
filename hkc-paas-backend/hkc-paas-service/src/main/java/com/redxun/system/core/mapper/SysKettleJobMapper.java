package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysKettleJob;
import org.apache.ibatis.annotations.Mapper;

/**
* KETTLE任务调度数据库访问层
*/
@Mapper
public interface SysKettleJobMapper extends BaseDao<SysKettleJob> {

}
