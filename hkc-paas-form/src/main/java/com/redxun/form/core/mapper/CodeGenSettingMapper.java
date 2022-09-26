package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.CodeGenSetting;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 自定义列表管理数据库访问层
*/
@Mapper
public interface CodeGenSettingMapper extends BaseDao<CodeGenSetting> {

}
