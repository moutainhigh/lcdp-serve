package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysSeqId;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 系统流水号数据库访问层
*/
@Mapper
public interface SysSeqIdMapper extends BaseDao<SysSeqId> {

}
