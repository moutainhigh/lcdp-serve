package com.redxun.system.core.mapper;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.system.core.entity.SysAppAuthMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* sys_app_auth_menu 应用授权菜单数据库访问层
*/
@Mapper
public interface SysAppAuthMenuMapper extends BaseDao<SysAppAuthMenu> {

    /**
     * @Description 更新状态
     * @Author xtk
     * @Date 2021/12/3 11:23
     * @param sysAppAuthMenu
     */
    void updateStatus(SysAppAuthMenu sysAppAuthMenu);

    /**
     * @Description 根据租户ID或用户ID查询应用安装菜单
     * @Author xtk
     * @Date 2021/12/3 15:48
     * @param tenantId
     * @param userId
     * @return java.util.List<java.lang.String>
     */
    List<SysMenuDto> getTenantOrUserAppInstallMenu(@Param("tenantId") String tenantId, @Param("userId") String userId);


    /**
     * @Description 根据菜单ID删除授权应用菜单信息
     * @Author xtk
     * @Date 2021/12/8 17:22
     * @param menuId
     */
    void deleteAppAuthMenuByMenuId(@Param("menuId") String menuId);
}
