package com.redxun.bpm.core.mapper;

import com.redxun.bpm.core.entity.BpmExecution;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

import java.util.List;

/**
* act_ru_execution数据库访问层
*/
@Mapper
public interface BpmExecutionMapper extends BaseDao<BpmExecution> {
    /**
     * 按父ID获取流程执行类
     * @param parentId
     * @return
     */
    List<BpmExecution> getByParentId(String parentId);
}
