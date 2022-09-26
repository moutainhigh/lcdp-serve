package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysJob;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 系统定时任务数据库访问层
*/
@Mapper
public interface SysJobMapper extends BaseDao<SysJob> {

}
