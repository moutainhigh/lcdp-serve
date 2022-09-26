package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormSolution;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 表单方案数据库访问层
*/
@Mapper
public interface FormSolutionMapper extends BaseDao<FormSolution> {

    FormSolution getByFormAlias(String formAlias);
}
