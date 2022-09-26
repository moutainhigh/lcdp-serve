package com.redxun.portal.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.portal.core.entity.InsPortalPermission;
import com.redxun.portal.core.mapper.InsPortalPermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [布局权限设置]业务服务类
*/
@Service
public class InsPortalPermissionServiceImpl extends SuperServiceImpl<InsPortalPermissionMapper, InsPortalPermission> implements BaseService<InsPortalPermission> {

    @Resource
    private InsPortalPermissionMapper insPortalPermissionMapper;

    @Override
    public BaseDao<InsPortalPermission> getRepository() {
        return insPortalPermissionMapper;
    }

    public List<InsPortalPermission> getListByLayoutId(String layoutId){
        return insPortalPermissionMapper.getListByLayoutId(layoutId);
    }


    public void delByLayoutId(String layoutId){
        QueryWrapper queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("LAYOUT_ID_",layoutId);
        insPortalPermissionMapper.delete(queryWrapper);
    }
}
