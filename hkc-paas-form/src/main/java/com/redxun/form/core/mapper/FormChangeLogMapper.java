package com.redxun.form.core.mapper;

import com.alibaba.fastjson.JSONObject;
import com.redxun.form.core.entity.FormChangeLog;
import com.redxun.form.core.entity.FormExecuteLog;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 表单变更记录数据库访问层
*/
@Mapper
public interface FormChangeLogMapper extends BaseDao<FormChangeLog> {

    Integer getMaxSn();

    List<FormChangeLog> queryChangeLog(@Param("dsAlias") String dsAlias, @Param("queryParams") JSONObject queryParams);

    List<FormExecuteLog> getExecutedLog(String dsAlias);

    List<FormChangeLog> getIgnoreFormChangeLog(String dsAlias);
}
