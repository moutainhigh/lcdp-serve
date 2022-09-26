package com.redxun.user.org.service;

import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.utils.ContextUtil;
import com.redxun.user.org.entity.OsPropertiesDef;
import com.redxun.user.org.mapper.OsPropertiesDefMapper;
import com.redxun.user.org.mapper.OsPropertiesValMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [自定义属性配置]业务服务类
*/
@Service
public class OsPropertiesDefServiceImpl extends SuperServiceImpl<OsPropertiesDefMapper, OsPropertiesDef> implements BaseService<OsPropertiesDef> {

    @Resource
    private OsPropertiesDefMapper osPropertiesDefMapper;
    @Resource
    private OsPropertiesValMapper osPropertiesValMapper;

    @Override
    public BaseDao<OsPropertiesDef> getRepository() {
        return osPropertiesDefMapper;
    }

    public void delByGroupId(String[] groupIds) {
        for (String groupId : groupIds) {
            //删除属性
            osPropertiesDefMapper.deleteByGroupId(groupId);
            //删除属性值
            osPropertiesValMapper.deleteByGroupId(groupId);
        }
    }

    public List<OsPropertiesDef> getPropertiesByDimId(String dimId) {
        return osPropertiesDefMapper.getPropertiesByDimId(dimId, ContextUtil.getCurrentTenantId());
    }
}
