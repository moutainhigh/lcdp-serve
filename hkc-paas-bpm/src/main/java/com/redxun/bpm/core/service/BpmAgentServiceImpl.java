package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmAgent;
import com.redxun.bpm.core.entity.BpmAgentFlowDef;
import com.redxun.bpm.core.mapper.BpmAgentFlowDefMapper;
import com.redxun.bpm.core.mapper.BpmAgentMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.DbLogicDelete;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
* [代理配置]业务服务类
*/
@Service
public class BpmAgentServiceImpl extends SuperServiceImpl<BpmAgentMapper, BpmAgent> implements BaseService<BpmAgent>,IBpmAgentService {

    @Resource
    private BpmAgentMapper bpmAgentMapper;
    @Resource
    private BpmAgentFlowDefMapper bpmAgentFlowDefMapper;

    @Override
    public BaseDao<BpmAgent> getRepository() {
        return bpmAgentMapper;
    }

    @Override
    public int insert(BpmAgent entity) {
        entity.setId(IdGenerator.getIdStr());

        int rtn=bpmAgentMapper.insert(entity);

        if( BpmAgent.TYPE_FLOWDEF.equals( entity.getType())){
            List<BpmAgentFlowDef> bpmAgentFlowDefs = entity.getBpmAgentFlowDefs();
            for(BpmAgentFlowDef flowDef:bpmAgentFlowDefs){
                flowDef.setId(IdGenerator.getIdStr());
                flowDef.setAgentId(entity.getId());
                bpmAgentFlowDefMapper.insert(flowDef);
            }
        }
        return rtn;
    }

    @Override
    public int update(BpmAgent entity) {
        int rtn= bpmAgentMapper.updateById(entity);

        if( BpmAgent.TYPE_FLOWDEF.equals( entity.getType())){
            QueryWrapper<BpmAgentFlowDef> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("AGENT_ID_",entity.getId());
            bpmAgentFlowDefMapper.delete(queryWrapper);

            List<BpmAgentFlowDef> bpmAgentFlowDefs = entity.getBpmAgentFlowDefs();
            for(BpmAgentFlowDef flowDef:bpmAgentFlowDefs){
                flowDef.setId(IdGenerator.getIdStr());
                flowDef.setAgentId(entity.getId());
                bpmAgentFlowDefMapper.insert(flowDef);
            }
        }
        return rtn;
    }

    @Override
    public BpmAgent get(Serializable id) {
        BpmAgent bpmAgent=bpmAgentMapper.selectById(id);

        QueryWrapper<BpmAgentFlowDef> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("AGENT_ID_",id);
        List<BpmAgentFlowDef> bpmAgentFlowDefs = bpmAgentFlowDefMapper.selectList(queryWrapper);
        bpmAgent.setBpmAgentFlowDefs(bpmAgentFlowDefs);


        return bpmAgent;
    }

    @Override
    public void delete(Collection<Serializable> entities) {
        for(Serializable id:entities){
            bpmAgentMapper.deleteById(id);

            QueryWrapper<BpmAgentFlowDef> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("AGENT_ID_",id);
            bpmAgentFlowDefMapper.delete(queryWrapper);
        }
    }

    @Override
    public JsonResult<String> getAgentUser(String userId, String actDefId) {
        JsonResult<String> result=JsonResult.Success();
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("OWNER_ID_",userId);
        wrapper.eq("STATUS_",1);
        wrapper.le("START_TIME_", new Date() );
        wrapper.gt("END_TIME_", new Date() );
        //逻辑删除
        if (DbLogicDelete.getLogicDelete()) {
            wrapper.gt("DELETED_", "0");
        }
        List<BpmAgent> list = bpmAgentMapper.selectList(wrapper);
        for(BpmAgent agent:list){
            if(BpmAgent.TYPE_ALL.equals(agent.getType())){
                result.setData(agent.getToUser());
                return result;
            }
            else{
                QueryWrapper defWrapper= new QueryWrapper<>();
                defWrapper.eq("AGENT_ID_",agent.getId());
                defWrapper.eq("DEF_ID_",actDefId);

                Integer amount = bpmAgentFlowDefMapper.selectCount(defWrapper);
                if(amount>0){
                    result.setData(agent.getToUser());
                    return result;
                }
            }
        }
        return result.setSuccess(false);
    }
}
