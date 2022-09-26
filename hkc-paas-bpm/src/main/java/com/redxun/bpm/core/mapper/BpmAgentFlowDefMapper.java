package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmAgentFlowDef;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 代理流程定义数据库访问层
*/
@Mapper
public interface BpmAgentFlowDefMapper extends BaseDao<BpmAgentFlowDef> {

}
