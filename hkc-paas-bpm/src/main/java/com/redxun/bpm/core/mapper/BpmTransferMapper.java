package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmTransfer;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 流程流转表数据库访问层
*/
@Mapper
public interface BpmTransferMapper extends BaseDao<BpmTransfer> {

}
