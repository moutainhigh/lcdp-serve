package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormEntRelation;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 数据关联删除约束数据库访问层
*/
@Mapper
public interface FormEntRelationMapper extends BaseDao<FormEntRelation> {

}
