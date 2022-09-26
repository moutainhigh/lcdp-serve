package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormPermission;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 表单权限配置数据库访问层
*/
@Mapper
public interface FormPermissionMapper extends BaseDao<FormPermission> {

}
