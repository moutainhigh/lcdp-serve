package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysOffice;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

/**
* SYS_OFFICE数据库访问层
*/
@Mapper
public interface SysOfficeMapper extends BaseDao<SysOffice> {

}
