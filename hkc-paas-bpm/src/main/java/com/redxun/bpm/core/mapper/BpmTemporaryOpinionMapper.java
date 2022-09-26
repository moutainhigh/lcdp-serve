package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmTemporaryOpinion;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

/**
* 流程意见暂存数据库访问层
*/
@Mapper
public interface BpmTemporaryOpinionMapper extends BaseDao<BpmTemporaryOpinion> {
    BpmTemporaryOpinion getByTaskIdAndUserId(@Param("taskId") String taskId,@Param("userId") String userId);
}
