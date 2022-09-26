package com.redxun.user.org.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsPasswordInputError;
import org.apache.ibatis.annotations.Mapper;

/**
* 用户密码输入错误记录表数据库访问层
*/
@Mapper
public interface OsPasswordInputErrorMapper extends BaseDao<OsPasswordInputError> {

}
