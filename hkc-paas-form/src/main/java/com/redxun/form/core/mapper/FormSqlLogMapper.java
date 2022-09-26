package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormSqlLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* SQL执行日志数据库访问层
*/
@Mapper
public interface FormSqlLogMapper extends BaseDao<FormSqlLog> {

}
