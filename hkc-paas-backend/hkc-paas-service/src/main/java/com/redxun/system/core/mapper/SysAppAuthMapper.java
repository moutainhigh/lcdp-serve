package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysAppAuth;
import org.apache.ibatis.annotations.Mapper;

/**
* 应用授权表数据库访问层
*/
@Mapper
public interface SysAppAuthMapper extends BaseDao<SysAppAuth> {

}
