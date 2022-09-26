package com.redxun.form.core.mapper;

import com.redxun.form.core.entity.FormEntityDataSettingDic;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务实体数据配置字典数据库访问层
 */
@Mapper
public interface FormEntityDataSettingDicMapper extends BaseDao<FormEntityDataSettingDic> {

    /**
     * 通过角色id类型id获取字典数据
     * @param roleId
     * @param dataTypeId
     * @return
     */
    List<FormEntityDataSettingDic> getByRoleIdDataTypeId(@Param("roleId") String roleId,@Param("dataTypeId") String dataTypeId);
}
