package com.redxun.system.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.dto.SysMenuDto;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.system.core.entity.SysAppAuthMenu;
import com.redxun.system.core.entity.SysAppInstall;
import com.redxun.system.core.mapper.SysAppAuthMenuMapper;
import com.redxun.system.core.mapper.SysAppInstallMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * [sys_app_auth_menu]应用授权菜单业务服务类
 */
@Slf4j
@Service
public class SysAppAuthMenuServiceImpl extends SuperServiceImpl<SysAppAuthMenuMapper, SysAppAuthMenu> implements BaseService<SysAppAuthMenu> {

    @Resource
    private SysAppAuthMenuMapper sysAppAuthMenuMapper;

    @Resource
    private SysAppInstallMapper sysAppInstallMapper;

    @Override
    public BaseDao<SysAppAuthMenu> getRepository() {
        return sysAppAuthMenuMapper;
    }

    public boolean isExist(SysAppAuthMenu sysAppAuthMenu) {
        QueryWrapper queryWrapper = new QueryWrapper();
        // 应用ID
        String appId = sysAppAuthMenu.getAppId();
        queryWrapper.eq("APP_ID_", appId);
        // 租户ID
        String tenantId = sysAppAuthMenu.getTenantId();
        if (StringUtils.isNotEmpty(tenantId)) {
            queryWrapper.eq("TENANT_ID_", tenantId);
        }
        // 用户ID
        String userId = sysAppAuthMenu.getUserId();
        if (StringUtils.isNotEmpty(userId)) {
            queryWrapper.eq("USER_ID_", userId);
        }
        // 菜单ID
        String menuId = sysAppAuthMenu.getMenuId();
        if (StringUtils.isNotEmpty(menuId)) {
            queryWrapper.eq("MENU_ID_", menuId);
        }
        int count = sysAppAuthMenuMapper.selectCount(queryWrapper);

        return count > 0;
    }

    /**
     * @Description 根据租户ID或用户ID查询应用安装菜单
     * @Author xtk
     * @Date 2021/12/3 15:48
     * @param tenantId
     * @param userId
     * @return java.util.List<java.lang.String>
     */
    public List<SysMenuDto> getTenantOrUserAppInstallMenu(String tenantId, String userId) {
        return sysAppAuthMenuMapper.getTenantOrUserAppInstallMenu(tenantId, userId);
    }

    /**
     * @Description 保存应用授权菜单
     * @Author xtk
     * @Date 2021/12/8 16:42
     * @param appId
     * @param menuId
     */
    public void saveAppAuthMenu(String appId, String menuId) {
        if (StringUtils.isNotEmpty(appId) && StringUtils.isNotEmpty(menuId)) {
            List<SysAppInstall> sysAppInstalls = sysAppInstallMapper.listInstallApp(appId, null);
            if (sysAppInstalls != null && sysAppInstalls.size() > 0) {
                List<SysAppAuthMenu> menuList = new ArrayList<>(sysAppInstalls.size());
                for (SysAppInstall sysAppInstall : sysAppInstalls) {
                    SysAppAuthMenu menu = new SysAppAuthMenu();
                    menu.setId(IdGenerator.getIdStr());
                    menu.setAppId(appId);
                    menu.setTenantId(sysAppInstall.getTenantId());
                    menu.setMenuId(menuId);
                    menu.setEnable(sysAppInstall.getEnable());
                    menuList.add(menu);
                }
                this.saveBatch(menuList);
            }
        }
    }

    /**
     * @Description 根据菜单ID删除授权应用菜单信息
     * @Author xtk
     * @Date 2021/12/8 17:22
     * @param menuId
     */
    public void deleteAppAuthMenuByMenuId(String menuId) {
        if (StringUtils.isNotEmpty(menuId)) {
            sysAppAuthMenuMapper.deleteAppAuthMenuByMenuId(menuId);
        }
    }

}


