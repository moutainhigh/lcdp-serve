
package com.redxun.user.org.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.StringUtils;
import com.redxun.user.org.entity.OsDdAgent;
import com.redxun.user.org.mapper.OsDdAgentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* [os_dd_agent]业务服务类
*/
@Service
public class OsDdAgentServiceImpl extends SuperServiceImpl<OsDdAgentMapper, OsDdAgent> implements BaseService<OsDdAgent> {

    @Resource
    private OsDdAgentMapper osDdAgentMapper;

    @Override
    public BaseDao<OsDdAgent> getRepository() {
        return osDdAgentMapper;
    }


    /**
     * 将其他的应用更新为非默认
     * @param tenantId
     */
    public void updNoDefault(String tenantId){
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("TENANT_ID_",tenantId);
        wrapper.set("IS_DEFAULT_",0);

        osDdAgentMapper.update(null,wrapper);
    }

    /**
     * 应用ID是否存在
     * @param agent
     * @param tenantId
     * @return
     */
    public boolean isAgentIdExist(OsDdAgent agent,String tenantId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("AGENT_ID_",agent.getAgentId());
        wrapper.eq("TENANT_ID_",tenantId);

        if(StringUtils.isNotEmpty( agent.getId())){
            wrapper.ne("ID_",agent.getId());
        }

        Integer rtn= osDdAgentMapper.selectCount(wrapper);
        return  rtn>0;

    }

    /**
     * appKey是否存在
     * @param agent
     * @return
     */
    public boolean isAgentKeyExist(OsDdAgent agent,String tenantId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("APP_KEY_",agent.getAppKey());
        wrapper.eq("TENANT_ID_",tenantId);

        if(StringUtils.isNotEmpty( agent.getId())){
            wrapper.ne("ID_",agent.getId());
        }

        Integer rtn= osDdAgentMapper.selectCount(wrapper);
        return  rtn>0;

    }

    /**
     * 获取默认的钉钉应用配置。
     * @param tenantId
     * @return
     */
    public OsDdAgent getDefault(String tenantId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("IS_DEFAULT_", 1);
        wrapper.eq("TENANT_ID_",tenantId);
        OsDdAgent agent= osDdAgentMapper.selectOne(wrapper);
        return agent;
    }

}
