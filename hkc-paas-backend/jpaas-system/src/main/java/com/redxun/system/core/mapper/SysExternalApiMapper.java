package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysExternalApi;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 对外API接口管理数据库访问层
*/
@Mapper
public interface SysExternalApiMapper extends BaseDao<SysExternalApi> {

}
