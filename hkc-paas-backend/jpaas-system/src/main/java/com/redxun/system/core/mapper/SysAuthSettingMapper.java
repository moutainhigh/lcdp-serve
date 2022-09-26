package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysAuthSetting;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 权限配置表数据库访问层
*/
@Mapper
public interface SysAuthSettingMapper extends BaseDao<SysAuthSetting> {

}
