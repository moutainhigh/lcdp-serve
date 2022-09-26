package com.redxun.form.core.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.form.core.entity.FormEntityDataSetting;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 业务实体数据配置数据库访问层
 */
@Mapper
public interface FormEntityDataSettingMapper extends BaseDao<FormEntityDataSetting> {

    /**
     * 按角色分组查询
     * @param page
     * @param params
     * @return
     */
    IPage queryRole(IPage page,@Param("w") Map<String, Object> params);

    /**
     * 根据角色获取数据
     * @param roleId
     * @return
     */
    FormEntityDataSetting getByRoleId(@Param("roleId") String roleId);
}
