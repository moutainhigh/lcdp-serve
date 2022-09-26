package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysExternalApi;
import org.apache.ibatis.annotations.Mapper;

/**
* 对外API接口管理数据库访问层
*/
@Mapper
public interface SysExternalApiMapper extends BaseDao<SysExternalApi> {

}
