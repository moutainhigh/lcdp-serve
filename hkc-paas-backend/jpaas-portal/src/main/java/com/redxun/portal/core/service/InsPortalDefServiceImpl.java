package com.redxun.portal.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.portal.core.entity.InsPortalDef;
import com.redxun.portal.core.mapper.InsPortalDefMapper;
import com.redxun.portal.feign.OsGroupClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* [门户定义]业务服务类
*/
@Service
public class InsPortalDefServiceImpl extends SuperServiceImpl<InsPortalDefMapper, InsPortalDef> implements BaseService<InsPortalDef> {

    @Resource
    private InsPortalDefMapper insPortalDefMapper;
    @Autowired
    OsGroupClient osGroupClient;

    @Override
    public BaseDao<InsPortalDef> getRepository() {
        return insPortalDefMapper;
    }

    public List<InsPortalDef> getListByType(String isMobile,String isDefault,String tenantId,String appId){
        return insPortalDefMapper.getListByType(isMobile,isDefault,tenantId,appId);
    }
    /**
     * 根据solId 获取权限。
     * @return
     */
    public List<InsPortalDef> getByOwner(Map<String, Set<String>> profiles){
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("profileMap", profiles);
        return  insPortalDefMapper.getByOwner(params);
    }

    public List<InsPortalDef> listMobilePortals(Map<String, Set<String>> profiles){
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("profileMap", profiles);
        return  insPortalDefMapper.listMobilePortals(params);
    }

    public InsPortalDef getByKey(String portalKey) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("KEY_",portalKey);
        return insPortalDefMapper.selectOne(queryWrapper);
    }

    public boolean isExist(InsPortalDef ent) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("KEY_",ent.getKey());
        if(StringUtils.isNotEmpty( ent.getPortId())){
            queryWrapper.ne("PORT_ID_",ent.getPortId());
        }
        int count=insPortalDefMapper.selectCount(queryWrapper);

        return count>0;
    }

    /**
     * 根据应用ID获取当前人门户
     * @param appId 应用主键
     * @return
     */
    public InsPortalDef getCurUserPortalByAppId(String appId) {
        IUser user = ContextUtil.getCurrentUser();
        Map<String, Set<String>> profiles = osGroupClient.getCurrentProfile(user.getUserId());
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("profileMap", profiles);
        params.put("appId", appId);
        List<InsPortalDef> owerAll = insPortalDefMapper.getByOwner(params);
        if (BeanUtil.isNotEmpty(owerAll)) {
            List<InsPortalDef> sortedEmp = owerAll.stream().sorted(
                    Comparator.comparing(InsPortalDef::getPriority).reversed()).collect(Collectors.toList());
            return sortedEmp.get(0);
        }else {
            List<InsPortalDef> defaultPortals = getListByType(InsPortalDef.IS_NO_MOBILE, InsPortalDef.IS_DEFAULT_,ContextUtil.getCurrentTenantId(),appId);
            //没有权限门户  就取默认门户
            if (BeanUtil.isNotEmpty(defaultPortals)) {
                return defaultPortals.get(0);
            } else {
                return new InsPortalDef();
            }
        }
    }
}
