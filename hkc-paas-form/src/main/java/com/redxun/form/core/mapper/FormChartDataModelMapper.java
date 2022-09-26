package com.redxun.form.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.form.core.entity.FormChartDataModel;
import org.apache.ibatis.annotations.Mapper;

/**
* 图表数据模型数据库访问层
*/
@Mapper
public interface FormChartDataModelMapper extends BaseDao<FormChartDataModel> {

}
