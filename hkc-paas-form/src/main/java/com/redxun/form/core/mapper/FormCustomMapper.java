package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormCustom;
import com.redxun.form.core.entity.FormPc;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 表单定制数据库访问层
*/
@Mapper
public interface FormCustomMapper extends BaseDao<FormCustom> {


}
