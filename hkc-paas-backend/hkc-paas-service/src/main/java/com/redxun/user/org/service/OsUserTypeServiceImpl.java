package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.user.org.entity.OsUserType;
import com.redxun.user.org.mapper.OsUserTypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
* [用户类型]业务服务类
*/
@Service
public class OsUserTypeServiceImpl extends SuperServiceImpl<OsUserTypeMapper, OsUserType> implements BaseService<OsUserType> {

    @Resource
    private OsUserTypeMapper osUserTypeMapper;
    @Resource
    private OsGroupServiceImpl osGroupServiceImpl;

    @Override
    public BaseDao<OsUserType> getRepository() {
        return osUserTypeMapper;
    }

    public OsUserType getByCode(String code,String tenantId){
        return osUserTypeMapper.getByCode(code,tenantId);
    }

    @Override
    public void delete(Serializable id) {
        OsUserType osUserType=get(id);
        if(osUserType!=null && StringUtils.isNotEmpty(osUserType.getGroupId())){
            osGroupServiceImpl.delete(osUserType.getGroupId());
        }
        osUserTypeMapper.deleteById(id);
    }

    /**
     *  功能：按名称查找用户类型
     * @param name 名称
     * @param tenantId 租户ID
     * @return com.redxun.user.org.entity.OsUserType
     * @author  Elwin ZHANG
     * @date 2022/3/10 16:04
     **/
    public OsUserType getByName(String name,String tenantId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("NAME_",name.trim());
        if(StringUtils.isNotEmpty(tenantId)){
            wrapper.eq("TENANT_ID_",tenantId);
        }
        List<OsUserType> list=osUserTypeMapper.selectList(wrapper);
        if(list==null || list.size()==0){
            return null;
        }
        return list.get(0);
    }


}
