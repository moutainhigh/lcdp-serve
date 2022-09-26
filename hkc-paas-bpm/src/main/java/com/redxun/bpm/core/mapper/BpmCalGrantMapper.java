package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmCalGrant;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 日历分配数据库访问层
*/
@Mapper
public interface BpmCalGrantMapper extends BaseDao<BpmCalGrant> {

}
