package com.redxun.form.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.form.core.entity.FormExecuteLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 租户数据源执行记录数据库访问层
*/
@Mapper
public interface FormExecuteLogMapper extends BaseDao<FormExecuteLog> {

    void delIgnoreFormChangeLog(String changeLogId,String dsAlias);

    IPage getExecuteLog(IPage<FormExecuteLog> page, @Param("w") Map<String, Object> params);

    List<FormExecuteLog> getByBatch(String batch);
}
