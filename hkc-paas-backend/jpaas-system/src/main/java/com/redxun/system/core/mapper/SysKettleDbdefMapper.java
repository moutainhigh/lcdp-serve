package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysKettleDbdef;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

import java.util.List;

/**
* KETTLE资源库定义数据库访问层
*/
@Mapper
public interface SysKettleDbdefMapper extends BaseDao<SysKettleDbdef> {

    List getAllByStatus(String status);
}
