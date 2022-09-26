package com.redxun.form.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.form.core.entity.GridReportDesign;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* demo数据库访问层
*/
@Mapper
@Repository
public interface GridReportDesignMapper extends BaseDao<GridReportDesign> {
}
