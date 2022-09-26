package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmCalSetting;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 日历设定数据库访问层
*/
@Mapper
public interface BpmCalSettingMapper extends BaseDao<BpmCalSetting> {

}
