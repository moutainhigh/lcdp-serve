package com.redxun.form.bo.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.form.bo.entity.FormBoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* 业务实体数据库访问层
*/
@Mapper
public interface FormBoEntityMapper extends BaseDao<FormBoEntity> {

    /**
     * 逻辑删除
     * 根据boDefId 获取关联实体数据。
     * @param boDefId
     * @return
     */
    List<FormBoEntity> getByDefId(String boDefId,String deleted);

    /**
     * 根据boDefId 获取主实体数据
     * @param boDefId
     * @return
     */
    FormBoEntity getMainEntByDefId(String boDefId);

    /**
     * 根据BO别名获取主实体对象。
     * @param boAlias
     * @return
     */
    FormBoEntity getMainEntByBoAlias(String boAlias);
}
