package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysJobTask;
import org.apache.ibatis.annotations.Mapper;

/**
* 定时任务定义数据库访问层
*/
@Mapper
public interface SysJobTaskMapper extends BaseDao<SysJobTask> {

}
