package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmAgent;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 代理配置数据库访问层
*/
@Mapper
public interface BpmAgentMapper extends BaseDao<BpmAgent> {

}
