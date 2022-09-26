package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysFile;
import org.apache.ibatis.annotations.Mapper;

/**
* 系统附件数据库访问层
*/
@Mapper
public interface SysFileMapper extends BaseDao<SysFile> {

}
