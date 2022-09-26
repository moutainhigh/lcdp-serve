package com.redxun.portal.core.mapper;

import com.redxun.portal.core.entity.InsPortalDef;
import org.apache.ibatis.annotations.Mapper;
import com.redxun.common.base.db.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* 门户定义数据库访问层
*/
@Mapper
public interface InsPortalDefMapper extends BaseDao<InsPortalDef> {

    List<InsPortalDef> getListByType(@Param("isMobile") String isMobile,
                                     @Param("isDefault") String isDefault,
                                     @Param("tenantId") String tenantId,
                                     @Param("appId") String  appId);

    /**
     * 根据solId 获取权限。
     * @return
     */
    List<InsPortalDef> getByOwner(@Param("params") Map<String,Object> params);

    /**
     * 取权限移动门户。
     * @return
     */
    List<InsPortalDef> listMobilePortals(@Param("params") Map<String,Object> params);


}
