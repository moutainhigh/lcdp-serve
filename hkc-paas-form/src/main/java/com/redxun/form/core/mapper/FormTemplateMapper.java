package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormTemplate;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 表单模版数据库访问层
*/
@Mapper
public interface FormTemplateMapper extends BaseDao<FormTemplate> {

    /**
     * 根据别名与类型获取表单模板
     * @param alias
     * @param type
     * @return
     */
    String getByAliasAndType(@Param("alias")String alias,@Param("type") String type);

    /**
     * 根据类型与类别获取表单模板
     * @param type
     * @param category
     * @return
     */
    List<FormTemplate> getByTypeAndCategory(@Param("type")String type,@Param("category") String category);


    /**
     * 根据参数获取代码生成模板
     * @param genMode
     * @param fileName
     * @param mainSubType
     * @return
     */
    List<FormTemplate> getCodeGenByParams(@Param("genMode")String genMode,@Param("fileName") String fileName);

}
