package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormPcHistory;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

/**
* 表单设计历史数据库访问层
*/
@Mapper
public interface FormPcHistoryMapper extends BaseDao<FormPcHistory> {

    FormPcHistory selectByNum(@Param("formPcId") String formPcId, @Param("num")int num);
}
