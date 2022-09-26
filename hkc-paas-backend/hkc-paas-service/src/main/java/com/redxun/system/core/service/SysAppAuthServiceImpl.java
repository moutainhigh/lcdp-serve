package com.redxun.system.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.system.core.entity.SysAppAuth;
import com.redxun.system.core.mapper.SysAppAuthMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* [应用授权表]业务服务类
*/
@Service
public class SysAppAuthServiceImpl extends SuperServiceImpl<SysAppAuthMapper, SysAppAuth> implements BaseService<SysAppAuth> {

    @Resource
    private SysAppAuthMapper sysAppAuthMapper;

    @Override
    public BaseDao<SysAppAuth> getRepository() {
        return sysAppAuthMapper;
    }

    public List<SysAppAuth> findListByAppId(String appId) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("APP_ID_",appId);
        return sysAppAuthMapper.selectList(queryWrapper);
    }

    public void removeByAppId(String appId) {
        Map m = Maps.newHashMap();
        m.put("APP_ID_", appId);
        removeByMap(m);
    }
}
