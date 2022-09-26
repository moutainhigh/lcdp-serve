package com.redxun.system.core.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.system.core.entity.SysApp;
import com.redxun.system.core.entity.SysMenu;
import com.redxun.system.core.entity.SysMenuRelease;
import com.redxun.system.core.mapper.SysMenuReleaseMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [发布菜单路径记录表]业务服务类
*/
@Service
public class SysMenuReleaseServiceImpl extends SuperServiceImpl<SysMenuReleaseMapper, SysMenuRelease> implements BaseService<SysMenuRelease> {

    @Resource
    private SysMenuReleaseMapper sysMenuReleaseMapper;

    @Resource
    private SysMenuServiceImpl sysMenuService;
    @Resource
    private SysAppServiceImpl sysAppService;

    @Override
    public BaseDao<SysMenuRelease> getRepository() {
        return sysMenuReleaseMapper;
    }

    public void creatSysMenuReleaseId(SysMenu menu){
        String releaseId =menu.getReleaseId();
        if(StringUtils.isEmpty(releaseId)){
            return;
        }
        SysMenuRelease release = new SysMenuRelease();
        release.setId(IdGenerator.getIdStr());
        release.setReleaseId(releaseId);
        release.setMenuId(menu.getId());
        release.setMenuName(menu.getName());
        release.setReleaseUrl(sysMenuService.getReleaseUrl(menu.getAppId(),menu.getPath()));
        this.insert(release);
    }
}
