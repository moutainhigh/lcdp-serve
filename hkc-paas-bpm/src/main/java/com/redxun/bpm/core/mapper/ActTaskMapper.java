package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.ActTask;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* act_ru_task数据库访问层
*/
@Mapper
public interface ActTaskMapper extends BaseDao<ActTask> {

}
