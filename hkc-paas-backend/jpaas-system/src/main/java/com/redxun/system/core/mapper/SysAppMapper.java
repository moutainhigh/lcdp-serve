package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
* sys_app数据库访问层
*/
@Mapper
public interface SysAppMapper extends BaseDao<SysApp> {

    /**
     * 通过id集合，获取应用数据
     * @param appIds
     * @return
     */
    List<SysApp> getAppsByIds(@Param("appIds") String appIds);

    /**
     * 获取非单页面应用
     * @return
     */
    List<SysApp> getApps(@Param(value = "tenantId") String tenantId ,@Param(value = "appId") String appId);
    /**
     * 获取非单页面应用
     * @return
     */
    List<SysApp> getAllByStatus();

    /**
    * @Description:  通过应用ID查找应用，如果应用ID为空则显示系统内置的应用
    * @param appId 应用ID
    * @return java.util.List<com.redxun.system.core.entity.SysApp>
    * @Author: Elwin ZHANG  @Date: 2021/5/20 16:27
    **/
    List<SysApp> getById(@Param(value = "appId") String appId);

    /**
     * 根据租户ID获取应用。
     * @param tenantId
     * @return
     */
    List<SysApp> getAppByTenant(@Param(value = "tenantId") String tenantId,@Param(value = "deleted") String deleted);

    SysApp getMenusByAppKey(String appKey);

    List<SysApp> getAppsByIdsAndType(@Param(value = "appIds")String appIds, @Param(value = "appType")int appType);

    /**
    *  功能：获取各分类下用户应用的数量
    * @param tenantId 租户
    * @param  authSql 授权SQL条件
    * @author  Elwin ZHANG
    * @date 2022/2/25 17:37
    **/
    List<HashMap> getCountByCategory(@Param(value = "tenantId") String tenantId,@Param(value = "authSql") String authSql);

    /**
     * 获取所有授权应用列表
     *
     * @param tenantId
     * @param companyId
     * @param subCompanyIds
     * @param appIds
     * @return
     */
    List<SysApp> getAllGrantApps(@Param(value = "tenantId") String tenantId,
                                 @Param(value = "companyId") String companyId,
                                 @Param(value = "subCompanyIds") String subCompanyIds,
                                 @Param(value = "appIds") String appIds);
}
