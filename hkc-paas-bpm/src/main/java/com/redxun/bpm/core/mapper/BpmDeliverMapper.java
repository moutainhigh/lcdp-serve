package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmDeliver;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 流程待办任务交接数据库访问层
*/
@Mapper
public interface BpmDeliverMapper extends BaseDao<BpmDeliver> {

}
