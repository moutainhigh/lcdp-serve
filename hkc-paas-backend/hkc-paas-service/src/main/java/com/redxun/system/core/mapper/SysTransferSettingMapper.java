package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysTransferSetting;
import org.apache.ibatis.annotations.Mapper;

/**
 * 权限转移设置表数据库访问层
 */
@Mapper
public interface SysTransferSettingMapper extends BaseDao<SysTransferSetting> {

}
