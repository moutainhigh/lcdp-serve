package com.redxun.user.org.mapper;

import com.redxun.user.org.entity.OsPasswordPolicy;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* 密码安全策略管理配置数据库访问层
*/
@Mapper
public interface OsPasswordPolicyMapper extends BaseDao<OsPasswordPolicy> {

}
