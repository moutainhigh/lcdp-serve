package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysTransferLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
 * 权限转移日志表数据库访问层
 */
@Mapper
public interface SysTransferLogMapper extends BaseDao<SysTransferLog> {

}
