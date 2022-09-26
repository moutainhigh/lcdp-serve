package com.redxun.system.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysAppAuthMenu;
import com.redxun.system.core.entity.SysAppInstall;
import com.redxun.system.core.entity.SysMenu;
import com.redxun.system.core.mapper.SysAppInstallMapper;
import com.redxun.system.feign.OsUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * [sys_app_install]应用安装业务服务类
 */
@Slf4j
@Service
public class SysAppInstallServiceImpl extends SuperServiceImpl<SysAppInstallMapper, SysAppInstall> implements BaseService<SysAppInstall> {

    @Resource
    private SysAppInstallMapper sysAppInstallMapper;

    @Autowired
    private SysAppAuthMenuServiceImpl sysAppAuthMenuServiceImpl;

    @Autowired
    private SysMenuServiceImpl sysMenuServiceImpl;

    @Autowired
    private OsUserClient osUserClient;

    @Override
    public BaseDao<SysAppInstall> getRepository() {
        return sysAppInstallMapper;
    }

    public String isExist(SysAppInstall appInstall) {
        StringBuffer buffer = new StringBuffer();
        QueryWrapper queryWrapper = new QueryWrapper();
        // 应用ID
        String appId = appInstall.getAppId();
        queryWrapper.eq("APP_ID_", appId);
        // 用户ID
        String userId = appInstall.getUserId();
        if (StringUtils.isNotEmpty(userId)) {
            queryWrapper.eq("USER_ID_", userId);
        }
        // 租户ID
        String tenantIds = appInstall.getTenantId();
        String appName = appInstall.getAppName();
        if (StringUtils.isNotEmpty(tenantIds)) {
            String[] tenantArray = tenantIds.split(",");
            String instName = appInstall.getInstName();
            String[] instNameArray = null;
            if (StringUtils.isNotEmpty(instName)) {
                instNameArray = instName.split(",");
            }
            for (int i = 0; i < tenantArray.length; i++) {
                queryWrapper.eq("TENANT_ID_", tenantArray[i]);
                int count = sysAppInstallMapper.selectCount(queryWrapper);
                if (count > 0) {
                    if (instNameArray == null) {
                        buffer.append("您已经安装了该【").append(appName).append("】应用；");
                    } else {
                        buffer.append("【").append(instNameArray[i]).append("】租户已经安装了该【").append(appName).append("】应用；");
                    }
                }
            }
        } else {
            int count = sysAppInstallMapper.selectCount(queryWrapper);
            if (count > 0) {
               buffer.append("您已经安装了该【").append(appName).append("】应用；");
            }
        }
        return buffer.toString();
    }

    /**
     * @Description 获取应用市场数据
     * @Author xtk
     * @Date 2021/11/30 11:18
     * @return java.util.List<com.redxun.system.core.entity.SysApp>
     */
    public List<SysApp> getAppMarketData() {
        String tenantId = ContextUtil.getCurrentTenantId();
        return sysAppInstallMapper.getAppMarketData(tenantId);
    }

    /**
     * @Description 获取我的应用数据
     * @Author xtk
     * @Date 2021/12/1 10:29
     * @param tenantId 租户ID
     * @return java.util.List<com.redxun.system.core.entity.SysApp>
     */
    public List<SysApp> getMyApp(String tenantId) {
        return sysAppInstallMapper.getMyApp(tenantId);
    }

    /**
     * @Description 安装
     * @Author xtk
     * @Date 2021/12/3 14:08
     * @param entity
     */
    @Transactional
    public void install(SysAppInstall entity) {
        // 租户ID
        String tenantIds = entity.getTenantId();
        if (StringUtils.isNotEmpty(tenantIds)) {
            String[] tenantIdArray = tenantIds.split(",");
            for (String tenantId : tenantIdArray) {
                entity.setTenantId(tenantId);
                saveInstall(entity);
            }
        } else {
            saveInstall(entity);
        }
    }

    /**
     * @Description 保存安装
     * @Author xtk
     * @Date 2021/12/6 15:31
     * @param entity
     */
    public void saveInstall(SysAppInstall entity) {
        // 生成ID
        String id = IdGenerator.getIdStr();
        entity.setId(id);
        super.save(entity);
        // 应用ID
        String appId = entity.getAppId();
        if (StringUtils.isNotEmpty(appId)) {
            String tenantId = entity.getTenantId();
            // 获取应用菜单
            List<SysMenu> menuList = sysMenuServiceImpl.getMenusByAppId(appId);

            List<SysAppAuthMenu> list = new ArrayList<>(menuList.size());
            for (SysMenu sysMenuDto : menuList) {
                SysAppAuthMenu menu = new SysAppAuthMenu();
                menu.setId(IdGenerator.getIdStr());
                menu.setAppId(appId);
                menu.setTenantId(tenantId);
                menu.setMenuId(sysMenuDto.getId());
                menu.setEnable("Y");
                list.add(menu);
            }
            sysAppAuthMenuServiceImpl.saveBatch(list);
        }
    }

    /**
     * @Description 暂停或者启动
     * @Author xtk
     * @Date 2021/12/1 15:26
     * @param sysAppInstall
     */
    @Transactional
    public void startOrStop(SysAppInstall sysAppInstall) {
        sysAppInstallMapper.startOrStop(sysAppInstall);

        SysAppAuthMenu sysAppAuthMenu = new SysAppAuthMenu();
        sysAppAuthMenu.setEnable(sysAppInstall.getEnable());

        // 更新应用租户用户菜单状态
        QueryWrapper<SysAppAuthMenu> wrapper = new QueryWrapper();
        wrapper.eq("APP_ID_", sysAppInstall.getAppId());
        sysAppAuthMenuServiceImpl.update(sysAppAuthMenu, wrapper);
    }

    /**
     * @Description 卸载应用
     * @Author xtk
     * @Date 2021/12/1 15:26
     * @param sysAppInstall
     */
    @Transactional
    public void uninstall(SysAppInstall sysAppInstall) {
        sysAppInstallMapper.uninstall(sysAppInstall);

        // 删除应用租户用户菜单
        QueryWrapper<SysAppAuthMenu> wrapper = new QueryWrapper();
        wrapper.eq("APP_ID_", sysAppInstall.getAppId());
        sysAppAuthMenuServiceImpl.remove(wrapper);
    }

    /**
     * @Description 移除已安装的应用（应用被删除）
     * @Author xtk
     * @Date 2021/12/6 10:00
     * @param appId
     */
    @Transactional
    public void removeInstallApp(String appId) {
        if (StringUtils.isNotEmpty(appId)) {
            sysAppInstallMapper.removeInstallApp(appId, null);
            // 删除应用租户用户菜单
            QueryWrapper<SysAppAuthMenu> wrapper = new QueryWrapper();
            wrapper.eq("APP_ID_", appId);
            sysAppAuthMenuServiceImpl.remove(wrapper);
        }
    }

    /**
     * @Description 根据租户ID删除应用安装信息
     * @Author xtk
     * @Date 2021/12/9 17:53
     * @param install
     */
    public void deleteByTenantIds(SysAppInstall install) {
        String tenantId = install.getTenantId();
        String appId = install.getAppId();
        if (StringUtils.isNotEmpty(tenantId) && StringUtils.isNotEmpty(appId)) {


            sysAppInstallMapper.removeInstallApp(appId, tenantId);
            // 删除应用租户用户菜单
            QueryWrapper<SysAppAuthMenu> wrapper = new QueryWrapper();
            wrapper.eq("APP_ID_", appId);
            wrapper.eq("TENANT_ID_", tenantId);
            sysAppAuthMenuServiceImpl.remove(wrapper);
        }
    }

}


