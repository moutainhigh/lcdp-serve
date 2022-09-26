package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.BpmTaskTransfer;
import com.redxun.bpm.core.mapper.BpmTaskTransferMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.db.PageHelper;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.utils.ContextUtil;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
* [流程任务转移记录]业务服务类
*/
@Service
public class BpmTaskTransferServiceImpl extends SuperServiceImpl<BpmTaskTransferMapper, BpmTaskTransfer> implements BaseService<BpmTaskTransfer> {

    @Resource
    private BpmTaskTransferMapper bpmTaskTransferMapper;
    @Resource
    private BpmTaskService bpmTaskService;

    @Override
    public BaseDao<BpmTaskTransfer> getRepository() {
        return bpmTaskTransferMapper;
    }


    /**
     * 根据任务ID删除。
     * @param taskId
     */
    public void delByTaskId(String taskId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("TASK_ID_" ,taskId);

        bpmTaskTransferMapper.delete(wrapper);
    }


    /**
     * 获取我接收的任务。
     * @param queryFilter
     * @return
     */
    public IPage getMyReceiveTasks(QueryFilter queryFilter){


        Map<String,Object> params= PageHelper.constructParams(queryFilter);

        return bpmTaskTransferMapper.getMyReceiveTask(queryFilter.getPage(),params);
    }

    /**
     * 获取我转出去的任务
     * @param queryFilter
     * @return
     */
    public IPage getMyTransOutTask(QueryFilter queryFilter){
        Map<String,Object> params= PageHelper.constructParams(queryFilter);
        return bpmTaskTransferMapper.getMyTransOutTask(queryFilter.getPage(),params);
    }


    /**
     * 取回审批任务。
     * @param taskId
     * @param options
     * @param messageType
     * @return
     */
    public JsonResult doRetrieveTask(String taskId,String options,String messageType){

        String curUserId= ContextUtil.getCurrentUserId();
        //将任务执行人修改成自己。
        BpmTask bpmTask= bpmTaskService.get(taskId);
        if(bpmTask!=null) {
            bpmTask.setAssignee(curUserId);
            bpmTaskService.update(bpmTask);

            String detail = "将任务:" + bpmTask.getSubject() + ",收回!";
            LogContext.put(Audit.DETAIL, detail);
        }else{
            return JsonResult.Success("当前事项已经没有待办任务，不能取回！");
        }
        //获取我的转办。
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("TASK_ID_",taskId);
        wrapper.eq("OWNER_ID_",curUserId);
        BpmTaskTransfer bpmTaskTransfer=bpmTaskTransferMapper.selectOne(wrapper);

        //删除转办历史
        QueryWrapper delWrapper=new QueryWrapper();
        delWrapper.eq("TASK_ID_",taskId);
        delWrapper.ge("CREATE_TIME_",bpmTaskTransfer.getCreateTime());
        bpmTaskTransferMapper.delete(delWrapper);


        return JsonResult.Success("取回任务成功!");

    }
}
