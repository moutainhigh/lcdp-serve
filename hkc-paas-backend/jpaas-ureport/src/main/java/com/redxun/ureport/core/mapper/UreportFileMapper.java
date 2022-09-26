package com.redxun.ureport.core.mapper;

import com.redxun.ureport.core.entity.UreportFile;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* ureport_file数据库访问层
*/
@Mapper
public interface UreportFileMapper extends BaseDao<UreportFile> {

}
