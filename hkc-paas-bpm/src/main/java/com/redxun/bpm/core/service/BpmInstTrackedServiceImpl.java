
package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.bpm.core.entity.BpmInstTracked;
import com.redxun.bpm.core.mapper.BpmInstTrackedMapper;
import com.redxun.common.tool.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

/**
* [流程实例跟踪]业务服务类
*/
@Service
public class BpmInstTrackedServiceImpl extends SuperServiceImpl<BpmInstTrackedMapper, BpmInstTracked> implements BaseService<BpmInstTracked> {

    @Resource
    private BpmInstTrackedMapper bpmInstTrackedMapper;
    @Resource
    private BpmTaskService bpmTaskService;

    @Override
    public BaseDao<BpmInstTracked> getRepository() {
        return bpmInstTrackedMapper;
    }

    /**
     * 添加关注。
     * @param instId
     * @param userId
     * @return
     */
    public JsonResult doTracked(String instId,String userId){
        JsonResult result=getTracked(instId,userId);
        if(result.isSuccess()){;
            removeTracked(instId,userId);
            JsonResult rtn=  JsonResult.Success("取消跟踪成功");
            rtn.setData("0");
            return rtn;
        }
        else{
            addTracked(instId,userId);
            JsonResult rtn=  JsonResult.Success("添加跟踪成功");
            rtn.setData("1");
            return rtn;
        }
    }

    /**
     * 添加跟踪。
     * @param instId
     * @param userId
     */
    public void addTracked(String instId,String userId){
        BpmInstTracked tracked=new BpmInstTracked();
        tracked.setId(IdGenerator.getIdStr());
        tracked.setInstId(instId);
        tracked.setCreateBy(userId);
        Set<String> nodeIdAry=new HashSet<>();
        List<BpmTask> list=bpmTaskService.getByInstId(instId);
        for(BpmTask bpmTask:list){
            nodeIdAry.add(bpmTask.getName());
        }
        tracked.setCurNode(StringUtils.join(nodeIdAry));
        bpmInstTrackedMapper.insert(tracked);
    }

    /**
     * 删除某人的跟踪
     * @param instId
     * @param userId
     */
    public void removeTracked(String instId,String userId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("INST_ID_",instId);
        wrapper.eq("CREATE_BY_",userId);
        bpmInstTrackedMapper.delete(wrapper);
    }

    /**
     * 根据流程实例删除跟踪。
     * @param instId
     */
    public void removeTracked(String instId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("INST_ID_",instId);
        bpmInstTrackedMapper.delete(wrapper);
    }

    /**
     * 根据流程实例获取跟踪情况。
     * @param instId
     * @return
     */
    public List<BpmInstTracked> getByInstId(String instId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("INST_ID_",instId);
        List list = bpmInstTrackedMapper.selectList(wrapper);
        return list;
    }

    /**
     * 获取某人的跟踪。
     * @param instId
     * @param userId
     * @return
     */
    public JsonResult getTracked(String instId,String userId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("INST_ID_",instId);
        wrapper.eq("CREATE_BY_",userId);
        Integer count = bpmInstTrackedMapper.selectCount(wrapper);
        JsonResult result=count>0? JsonResult.Success("已经跟踪!"):JsonResult.Fail("还未跟踪!");
        return  result;
    }

    /**
     * 分页查询跟踪。
     * @param queryFilter
     * @return
     */
    public IPage getMyTracked(QueryFilter queryFilter){
        Map<String,Object> params= PageHelper.constructParams(queryFilter);
        return bpmInstTrackedMapper.getMyTracked(queryFilter.getPage(),params);
    }

}
