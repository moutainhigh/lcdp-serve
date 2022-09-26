package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.ImportExcelBat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* sys_excel_batmanage数据库访问层
*/
@Mapper
public interface ImportExcelBatMapper extends BaseDao<ImportExcelBat> {

    Integer getBatId(@Param("templateId") String templateId);




}
