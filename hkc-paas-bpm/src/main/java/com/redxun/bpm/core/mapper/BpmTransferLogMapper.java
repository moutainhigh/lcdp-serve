package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmTransferLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 流转任务日志表数据库访问层
*/
@Mapper
public interface BpmTransferLogMapper extends BaseDao<BpmTransferLog> {

}
