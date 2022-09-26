package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormDownloadRecord;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;

/**
* Excel下载记录(异步)数据库访问层
*/
@Mapper
public interface FormDownloadRecordMapper extends BaseDao<FormDownloadRecord> {

}
