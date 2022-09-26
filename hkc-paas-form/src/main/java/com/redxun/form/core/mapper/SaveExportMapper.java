package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.SaveExport;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* form_save_export数据库访问层
*/
@Mapper
public interface SaveExportMapper extends BaseDao<SaveExport> {

    SaveExport getByName(@Param("name") String name, @Param("dataList")String dataList);

    /**
     * 根据列表名获取公开的和我创建的列表。
     * @param dataList
     * @param userId
     * @return
     */
    List<SaveExport> getByDataList(@Param("dataList")String dataList,@Param("userId")String userId);


    int removeConfig(@Param("name") String name, @Param("dataList")String dataList);





}
