package com.redxun.system.core.mapper;

import com.redxun.system.core.entity.SysWebReqDef;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
 * WEB请求调用定义数据库访问层
 */
@Mapper
public interface SysWebReqDefMapper extends BaseDao<SysWebReqDef> {

}
