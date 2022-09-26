package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysKettleDef;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* KETTLE定义数据库访问层
*/
@Mapper
public interface SysKettleDefMapper extends BaseDao<SysKettleDef> {

}
