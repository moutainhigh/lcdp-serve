package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.ActInstService;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.eventhandler.EventHandlerExecutor;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.mapper.BpmTaskUserMapper;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.BpmConst;
import org.activiti.bpmn.model.FlowNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 功能: 撤回流程处理
 *
 * @author ray
 * @date 2022/5/30 22:01
 */
@Service
public class BpmTakeBackService {

    @Resource
    BpmRuPathServiceImpl bpmRuPathService;
    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    ActInstService actInstService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Resource
    ActRepService actRepService;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    EventHandlerExecutor eventHandlerExecutor;
    @Resource
    BpmTaskUserMapper bpmTaskUserMapper;
    @Resource
    BpmExecutionServiceImpl bpmExecutionService;
    @Resource
    FormDataService formDataService;



    /**
     * 是否可以收回到发起人。
     * @param inst
     * @return
     */
    public JsonResult canToStart(BpmInst inst){

        String userId=ContextUtil.getCurrentUserId();
        String instId=inst.getInstId();

        if(!inst.getCreateBy().equals(userId)){
            return JsonResult.Fail("流程发起人才能进行撤回!");
        }

        //判断是否可以撤回，如果任务已经在发起节点则不能再撤回。
        FlowNode firstNode = actRepService.getFirstUserTaskNode(inst.getActDefId());
        List<BpmTask> tasks = bpmTaskService.getByInstId(instId);
        for (BpmTask task:tasks){
            if(task.getKey().equals(firstNode.getId())){
                return JsonResult.Fail("已经撤回到发起节点不能在撤销!");
            }
        }
        return JsonResult.Success();
    }


    /**
     * 撤销到发起人。
     * @param instId
     * @param opinion
     * @return
     */
    @Transactional
    public JsonResult revokeToStart(String instId,String opinion){
        List<BpmTask> tasks = bpmTaskService.getByInstId(instId);
        BpmInst inst=bpmInstService.get(instId);
        JsonResult result=canToStart(inst);
        if(!result.isSuccess()){
            return result;
        }

        //获取表单数据
        JSONObject jsonObject= formDataService.getDataByInstId(instId);

        ProcessNextCmd nextCmd=new ProcessNextCmd();

        nextCmd.setBoDataMap(jsonObject);
        nextCmd.setCheckType(TaskOptionType.INVOKE_TO_STARTOR.name());
        ProcessHandleUtil.setProcessCmd(nextCmd);

        for(BpmTask bpmTask:tasks){
            //执行事件(执行任务完成事件)
            UserTaskConfig taskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
            eventHandlerExecutor.executeTask(bpmTask,taskConfig, EventType.TASK_COMPLETED);
            //执行全局事件
            ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmTask.getActDefId());
            eventHandlerExecutor.executeGlobalTask( bpmTask, processConfig, EventType.TASK_COMPLETED );
        }
        List<BpmRuPath> bpmRuPaths= bpmRuPathService.getByInstId(instId);

        //获取第一个审批路径。
        BpmRuPath path= bpmRuPathService.getFirstPath(bpmRuPaths);
        //删除RU_PATH
        bpmRuPathService.removeRuPath(bpmRuPaths);
        //删除BPM_TASK_USER
        bpmTaskUserMapper.deleteByInstId(instId);
        //删除BPM_TASK
        bpmTaskService.deleteByInstId(instId);
        //删除 ACT_RU_EXECUTION ,并进行更改
        bpmExecutionService.handToStart(inst,path);
        // 撤销到发起人。
        bpmCheckHistoryService.createHistory(inst,path.getNodeId(),path.getNodeName(),TaskOptionType.INVOKE_TO_STARTOR.name(),"",opinion);

        inst.setStatus(TaskOptionType.INVOKE_TO_STARTOR.name());
        //更新流程实例。
        bpmInstService.update(inst);

        ProcessHandleUtil.clearProcessCmd();

        return JsonResult.Success("撤回流程成功!");
    }





    /**
     * 是否可以收回。
     * @param bpmInst
     * @return
     */
    public JsonResult canTakeBack(BpmInst bpmInst){
        String userId= ContextUtil.getCurrentUserId();
        String instId=bpmInst.getInstId();
        JsonResult result=JsonResult.Success();

        BpmRuPath bpmRuPath= bpmRuPathService.getMyLatestHandle(instId,userId);
        if(bpmRuPath==null){
            result=JsonResult.Fail("找不到当期人执行路径,不可收回!");
            return result;
        }

        List<BpmRuPath> nextPaths = bpmRuPathService.getNextPath(bpmRuPath);
        if(BeanUtil.isEmpty(nextPaths)){
            result=JsonResult.Fail("下一步任务已审批!");
            return result;
        }

        if(nextPaths.size()>1){
            result=JsonResult.Fail("后续有多个任务,不能撤回!");
            return result;
        }

        BpmTakeBack takeBack=new BpmTakeBack();
        takeBack.setBpmInst(bpmInst);
        takeBack.setCurPath(bpmRuPath);
        takeBack.setNextPaths(nextPaths);
        result.setData(takeBack);
        return  result;
    }


    /**
     * 撤回任务。
     * <pre>
     *     1.判断是否可以撤回。
     * </pre>
     * @param instId    流程实例ID
     * @param opinion   追回意见
     * @return
     */
    public JsonResult  takeBack(String instId,String opinion){
        BpmInst bpmInst=bpmInstService.get(instId);
        //判断是否可以驳回。
        JsonResult  result= canTakeBack(bpmInst);

        if(!result.isSuccess()){
            return  result;
        }

        BpmTakeBack takeBack=(BpmTakeBack)result.getData();

        BpmRuPath ruPath= takeBack.getNextPaths().get(0);

        List<BpmTask> tasks = bpmTaskService.getByInstId(instId);
        if(BeanUtil.isEmpty(tasks)){
            return JsonResult.Fail("流程任务已审批完成");
        }
        BpmTask bpmTask=null;
        for(BpmTask task:tasks){
            if(task.getKey().equals(ruPath.getNodeId()) && BpmTask.TYPE_FLOW_TASK.equals( task.getTaskType())){
                bpmTask=task;
            }
        }
        //获取表单数据
        JSONObject boDataMap= formDataService.getDataByInstId(instId);

        //构建CMD上下文。
        ProcessNextCmd nextCmd=new ProcessNextCmd();
        nextCmd.setCheckType(TaskOptionType.RECOVER.name());
        nextCmd.addTransientVar(BpmConst.BPM_APPROVE_TASK,bpmTask);
        nextCmd.addTransientVar(BpmConst.BPM_INST,bpmInst);
        nextCmd.setBoDataMap(boDataMap);
        ProcessHandleUtil.setProcessCmd(nextCmd);

        String targetNode=takeBack.getCurPath().getNodeId();
        String targetNodeName=takeBack.getCurPath().getNodeName();
        //跳转回来。
        actInstService.doJumpToTargetNode(bpmTask.getActTaskId(),targetNode);

        //添加撤销历史。
        bpmCheckHistoryService.createHistory(bpmInst,targetNode,targetNodeName, TaskOptionType.RECOVER.name(),"",opinion);

        ProcessHandleUtil.clearProcessCmd();

        return JsonResult.Success("撤回成功!");
    }


    /**
     * 判断是否可以撤回。
     * @param instId    实例ID
     * @param from      撤回方式（mystart,mydone)
     * @return
     */
    public JsonResult canRevoke(String instId,String from){
        if(!BpmTakeBack.BACK_TYPE_START.equals(from) && !BpmTakeBack.BACK_TYPE_DONE.equals(from)){
            return JsonResult.Fail();
        }
        JsonResult result=JsonResult.Success();
        BpmInst inst=bpmInstService.get(instId);
        if(BpmTakeBack.BACK_TYPE_START.equals(from)){
            result=canToStart(inst);
        }
        else if(BpmTakeBack.BACK_TYPE_DONE.equals(from)){
            result=canTakeBack(inst);
        }
        return result;
    }


    /**
     * 撤回任务。
     * <pre>
     *  撤回分为两种情况。
     *  1. 撤回到发起人
     *  2. 撤回到上一步
     * </pre>
     *
     *
     * @param instId
     */
    @Transactional
    public JsonResult revoke(String instId,String from, String opinion){
        JsonResult result=JsonResult.Success("撤回任务成功!");
        if(BpmTakeBack.BACK_TYPE_START.equals(from)){
            result= revokeToStart(instId,opinion);
        }
        else if(BpmTakeBack.BACK_TYPE_DONE.equals(from)){
            result= takeBack(instId,opinion);
        }
        else{
            result= JsonResult.Fail("传入撤回类型不对!");
        }
        return  result;

    }


}
