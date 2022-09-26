package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormRegLib;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 正则表达式数据库访问层
*/
@Mapper
public interface FormRegLibMapper extends BaseDao<FormRegLib> {

}
