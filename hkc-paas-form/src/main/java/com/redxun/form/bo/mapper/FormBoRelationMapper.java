package com.redxun.form.bo.mapper;

import com.redxun.form.bo.entity.FormBoRelation;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

/**
 * 业务实体数据库访问层
 *
 * @author hujun
 */
@Mapper
public interface FormBoRelationMapper extends BaseDao<FormBoRelation> {
    /**
     * 通过业务模型ID，实体名称获取关系
     *逻辑删除
     * @param boDefId
     * @param entName
     * @return
     */
    FormBoRelation getByBoDefIdAndEntName(@Param("boDefId") String boDefId, @Param("entName") String entName,@Param("deleted")String deleted);
}
