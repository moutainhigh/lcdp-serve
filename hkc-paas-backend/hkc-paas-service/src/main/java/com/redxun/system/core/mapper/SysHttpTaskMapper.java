package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysHttpTask;
import org.apache.ibatis.annotations.Mapper;

/**
* 接口调用表数据库访问层
*/
@Mapper
public interface SysHttpTaskMapper extends BaseDao<SysHttpTask> {

    void clearAll();
}
