package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormPc;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 表单设计数据库访问层
*/
@Mapper
public interface FormPcMapper extends BaseDao<FormPc> {

    /**
     * 判断表单是否存在。
     * @param params
     * @return
     */
    Integer isExist(Map<String,Object> params);


    /**
     * 根据主键获取表单
     * @param id
     * @return
     */
    FormPc getById(String id);

    /**
     * 根据别名获取表单版本。
     * @param alias
     * @return
     */
    List<FormPc> getVersions(String alias);

    /**
    * @Description:  根据实体ID，获取相关的FormPc对象
    * @param entityId form_bo_entity表的主键
    * @Author: Elwin ZHANG  @Date: 2021/8/30 15:43
    **/
    List<FormPc> getByEntityId(@Param("entityId") String entityId);

    void  updNotMain(String alias);

    Integer getMaxVersion(String alias);


}
