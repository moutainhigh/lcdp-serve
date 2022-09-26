package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.core.entity.BpmSignData;
import com.redxun.bpm.core.mapper.BpmSignDataMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* [任务会签数据]业务服务类
*/
@Service
public class BpmSignDataServiceImpl extends SuperServiceImpl<BpmSignDataMapper, BpmSignData> implements BaseService<BpmSignData> {

    @Resource
    private BpmSignDataMapper bpmSignDataMapper;

    @Override
    public BaseDao<BpmSignData> getRepository() {
        return bpmSignDataMapper;
    }

    public List<BpmSignData> getByActInstIdNodeId(String actInstId,String nodeId){
        return bpmSignDataMapper.getByActInstIdNodeId(actInstId,nodeId);
    }

    /**
     * 获取某流程某任务的总投票数
     * @param actInstId
     * @param nodeId
     * @return
     */
    public Integer getCountsByActInstIdNodeId( String actInstId, String nodeId){
        return bpmSignDataMapper.getCountsByActInstIdNodeId(actInstId,nodeId);
    }

    /**
     * 获取某流程某任务的总投票数
     * @param actInstId
     * @param nodeId
     * @param voteStatus
     * @return
     */
    public Integer getCountsByActInstIdNodeIdVoteStatus(String actInstId,String nodeId,String voteStatus){
        Integer rtn= bpmSignDataMapper.getCountsByActInstIdNodeIdVoteStatus(actInstId,nodeId,voteStatus);
        return  rtn;
    }

    /**
     * 按流程实例删除数据
     * @param actInstId
     */
    void deleteByActInstId(String actInstId){
        bpmSignDataMapper.deleteByActInstId(actInstId);
    }

    /**
     * 根据节点删除
     * @param actInstId
     * @param nodeId
     */
    void deleteByNodeId(String actInstId,String nodeId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("act_inst_id_",actInstId);
        wrapper.eq("NODE_ID_",nodeId);
        bpmSignDataMapper.delete(wrapper);
    }


    /**
     * 修改用户ID
     * @param receiptUserId
     * @param deliverUserId
     */
    public void updateUserId(String receiptUserId, String deliverUserId) {
        bpmSignDataMapper.updateUserId(receiptUserId, deliverUserId);
    }

}
