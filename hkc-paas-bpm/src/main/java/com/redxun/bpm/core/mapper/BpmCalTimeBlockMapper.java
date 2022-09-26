package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmCalTimeBlock;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 工作时间段设定数据库访问层
*/
@Mapper
public interface BpmCalTimeBlockMapper extends BaseDao<BpmCalTimeBlock> {

}
