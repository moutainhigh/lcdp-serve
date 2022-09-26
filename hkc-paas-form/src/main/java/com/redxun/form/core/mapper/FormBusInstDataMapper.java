package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormBusInstData;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 表单业务数据数据库访问层
*/
@Mapper
public interface FormBusInstDataMapper extends BaseDao<FormBusInstData> {

    List<FormBusInstData> getDataByMainPk(@Param("busSolId")String busSolId, @Param("mainPk")String mainPk);
}
