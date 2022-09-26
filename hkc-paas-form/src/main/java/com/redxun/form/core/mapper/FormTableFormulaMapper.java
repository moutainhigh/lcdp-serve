package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormTableFormula;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 表间公式数据库访问层
*/
@Mapper
public interface FormTableFormulaMapper extends BaseDao<FormTableFormula> {

}
