package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormExcelGenTask;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* Excel生成任务数据库访问层
*/
@Mapper
public interface FormExcelGenTaskMapper extends BaseDao<FormExcelGenTask> {

}
