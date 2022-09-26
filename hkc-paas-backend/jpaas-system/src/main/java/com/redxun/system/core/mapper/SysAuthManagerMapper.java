package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysAuthManager;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 应用接口授权管理表数据库访问层
*/
@Mapper
public interface SysAuthManagerMapper extends BaseDao<SysAuthManager> {

}
