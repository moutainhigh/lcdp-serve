
package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.user.org.entity.OsFsAgent;
import com.redxun.user.org.mapper.OsFsAgentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [飞书应用表]业务服务类
*/
@Service
public class OsFsAgentServiceImpl extends SuperServiceImpl<OsFsAgentMapper, OsFsAgent> implements BaseService<OsFsAgent> {

    @Resource
    private OsFsAgentMapper osFsAgentMapper;

    @Override
    public BaseDao<OsFsAgent> getRepository() {
        return osFsAgentMapper;
    }

    /**
     * 更新为非默认默认应用
     * @param tenantId
     */
    public void updateNotDefault(String tenantId){
        UpdateWrapper updateWrapper=new UpdateWrapper();
        updateWrapper.eq("TENANT_ID_",tenantId);
        updateWrapper.set("IS_DEFAULT_",0);
        osFsAgentMapper.update(null,updateWrapper);
    }
    /**
     * 获取租户默认的应用。
     * @param tenantId
     * @return
     */
    public OsFsAgent getDefaultAgent(String tenantId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("TENANT_ID_",tenantId);
        queryWrapper.eq("IS_DEFAULT_",1);
        List<OsFsAgent> defaultList= osFsAgentMapper.selectList(queryWrapper);
        if(BeanUtil.isNotEmpty(defaultList)){
            return defaultList.get(0);
        }
        return null;
    }

}
