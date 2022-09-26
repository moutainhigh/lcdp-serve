package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysOfficeVer;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* SYS_OFFICE_VER数据库访问层
*/
@Mapper
public interface SysOfficeVerMapper extends BaseDao<SysOfficeVer> {
    List<SysOfficeVer> getByOfficeId(@Param("officeId")String officeId);
    Integer getVersionByOfficeId(@Param("officeId")String officeId);
}
