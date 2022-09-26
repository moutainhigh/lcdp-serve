package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormQueryStrategy;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
 * 查询策略表数据库访问层
 */
@Mapper
public interface FormQueryStrategyMapper extends BaseDao<FormQueryStrategy> {

}
