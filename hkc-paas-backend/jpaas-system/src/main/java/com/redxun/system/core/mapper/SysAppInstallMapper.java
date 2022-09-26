package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysAppInstall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* sys_app_install 应用安装数据库访问层
*/
@Mapper
public interface SysAppInstallMapper extends BaseDao<SysAppInstall> {


    /**
     * @Description 获取应用市场数据
     * @Author xtk
     * @Date 2021/11/30 11:18
     * @return java.util.List<com.redxun.system.core.entity.SysApp>
     */
    List<SysApp> getAppMarketData(@Param("tenantId") String tenantId);

    /**
     * @Description 获取我的应用数据
     * @Author xtk
     * @Date 2021/12/1 10:29
     * @param tenantId 租户ID
     * @return java.util.List<com.redxun.system.core.entity.SysApp>
     */
    List<SysApp> getMyApp(@Param("tenantId") String tenantId);

    /**
     * @Description 暂停或者启用
     * @Author xtk
     * @Date 2021/12/1 15:26
     * @param sysAppInstall
     */
    void startOrStop(SysAppInstall sysAppInstall);

    /**
     * @Description 卸载应用
     * @Author xtk
     * @Date 2021/12/1 15:26
     * @param sysAppInstall
     */
    void uninstall(SysAppInstall sysAppInstall);

    /**
     * @Description 移除安装应用
     * @Author xtk
     * @Date 2021/12/6 10:02
     * @param appId
     * @param tenantId
     */
    void removeInstallApp(@Param("appId") String appId, @Param("tenantId") String tenantId);

    /**
     * @Description 根据APPID获取安装数据
     * @Author xtk
     * @Date 2021/12/8 16:44
     * @param appId
     * @return java.util.List<com.redxun.system.core.entity.SysApp>
     */
    List<SysAppInstall> listInstallApp(@Param("appId") String appId, @Param("tenantId") String tenantId);

}
