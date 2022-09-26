package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormDataSourceDef;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 数据源定义管理数据库访问层
*/
@Mapper
public interface FormDataSourceDefMapper extends BaseDao<FormDataSourceDef> {

}
