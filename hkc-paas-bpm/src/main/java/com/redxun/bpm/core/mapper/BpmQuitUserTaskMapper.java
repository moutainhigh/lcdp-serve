package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmQuitUserTask;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 离职人员流程任务表数据库访问层
*/
@Mapper
public interface BpmQuitUserTaskMapper extends BaseDao<BpmQuitUserTask> {

}
