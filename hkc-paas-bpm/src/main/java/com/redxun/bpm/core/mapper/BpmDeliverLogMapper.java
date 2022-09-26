package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmDeliverLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 流程待办任务交接日志数据库访问层
*/
@Mapper
public interface BpmDeliverLogMapper extends BaseDao<BpmDeliverLog> {

}
