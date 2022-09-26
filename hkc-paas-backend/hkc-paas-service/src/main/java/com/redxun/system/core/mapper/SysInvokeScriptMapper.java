package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysInvokeScript;
import org.apache.ibatis.annotations.Mapper;

/**
* 脚本调用数据库访问层
*/
@Mapper
public interface SysInvokeScriptMapper extends BaseDao<SysInvokeScript> {

}
