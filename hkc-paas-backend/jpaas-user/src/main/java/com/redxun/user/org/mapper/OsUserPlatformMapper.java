package com.redxun.user.org.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.user.org.entity.OsUserPlatform;
import org.apache.ibatis.annotations.Mapper;

/**
* 第三方平台登陆绑定数据库访问层
*/
@Mapper
public interface OsUserPlatformMapper extends BaseDao<OsUserPlatform> {

}
