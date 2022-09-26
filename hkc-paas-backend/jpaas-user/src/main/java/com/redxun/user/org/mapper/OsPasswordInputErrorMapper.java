package com.redxun.user.org.mapper;

import com.redxun.user.org.entity.OsPasswordInputError;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 用户密码输入错误记录表数据库访问层
*/
@Mapper
public interface OsPasswordInputErrorMapper extends BaseDao<OsPasswordInputError> {

}
