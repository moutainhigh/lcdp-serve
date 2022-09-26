package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSONArray;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.ActInstService;
import com.redxun.bpm.activiti.config.SignConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.event.TaskAssignApplicationEvent;
import com.redxun.bpm.activiti.event.TaskCompleteApplicationEvent;
import com.redxun.bpm.activiti.eventhandler.EventHandlerExecutor;
import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.mapper.BpmTaskMapper;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import lombok.Getter;
import lombok.Setter;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 处理多实例任务签批的情况。
 */
@Service
public class BpmMultiTaskService {

    @Resource
    private BpmSignDataServiceImpl bpmSignDataService;
    @Resource
    private  TaskService taskService;
    @Resource
    private  BpmTaskMapper bpmTaskMapper;
    @Resource
    private BpmTaskUserServiceImpl bpmTaskUserService;
    @Resource
    private  BpmTaskService bpmTaskService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private ProcessScriptEngine processScriptEngine;
    @Resource
    private BpmTransferServiceImpl bpmTransferService;
    @Resource
    private BpmTransferLogServiceImpl bpmTransferLogService;
    @Resource
    private BpmDefService bpmDefService;
    @Resource
    private BpmInstServiceImpl bpmInstService;
    @Resource
    private BpmRuPathServiceImpl bpmRuPathService;
    @Resource
    EventHandlerExecutor eventHandlerExecutor;
    @Resource
    BpmInstCcServiceImpl bpmInstCcService;

    @Resource
    ActInstService actInstService;
    @Resource
    IOrgService orgService;
    /**
     * 处理流转任务
     * @param bpmTask
     * @param cmd
     */
    public void handTransTask(BpmTask bpmTask,ProcessNextCmd cmd){
        BpmTask parentTask = bpmTaskService.getById(bpmTask.getParentId());
        BpmTransfer bpmTransfer=bpmTransferService.getByTaskId(parentTask.getTaskId());
        bpmTaskService.delByTaskId(bpmTask.getTaskId());
        bpmTransfer.setCompleteCount(bpmTransfer.getCompleteCount()+1);
        if("sequential".equals(bpmTransfer.getApproveType())){
            //串行
            String[] taskUserIds=bpmTransfer.getTaskUserId().split(",");
            Integer taskUserIdx=bpmTransfer.getTaskUserIdx();
            if(taskUserIdx+1<taskUserIds.length){
                bpmTransfer.setTaskUserIdx(taskUserIdx+1);
                BpmTask newTask=bpmTaskService.createTransRoamTask(parentTask,taskUserIds[taskUserIdx+1]);
                //发送消息通知
                sendTaskMessage(newTask,cmd,"approve");
            }else{
                handTransferTask(bpmTransfer,bpmTask,parentTask,cmd);
            }
        }else{
            handTransferTask(bpmTransfer,bpmTask,parentTask,cmd);
        }
        bpmTransferLogService.updateStatusByTransTaskId(bpmTask.getTaskId(),cmd.getOpinion(),BpmTask.STATUS_COMPLETED);
        bpmTransferService.update(bpmTransfer);
        //处理抄送
        transerReturnInstCc(bpmTask,cmd);
    }
    /**
    * @Description:  给任务的执行人发消息通知
    * @param task 任务对象
     *  @param  cmd 流程下一步执行命令对象
     *  @param  template 消息模板
    * @Author: Elwin ZHANG  @Date: 2021/12/10 16:09
    **/
    public void sendTaskMessage(BpmTask task,ProcessNextCmd cmd,String template){
        String msgType=cmd.getMsgTypes();
        if(StringUtils.isEmpty(msgType)) {
            return;
        }
        Map<String, Object> vars = new HashMap<>();
        OsUserDto receiver = orgService.getUserById(task.getAssignee());
        vars.put("opinion", cmd.getOpinion());
        vars.put("taskId", task.getTaskId());
        vars.put("instId", task.getInstId());
        vars.put("actDefId",task.getActDefId());
        vars.put("nodeId",task.getKey());
        OsUserDto sender = orgService.getUserById(ContextUtil.getCurrentUserId());
        com.redxun.bpm.core.ext.messagehandler.MessageUtil.sendMessage(sender, task.getSubject(), msgType, template, receiver, vars);
    }

    private void handTransferTask(BpmTransfer bpmTransfer,BpmTask bpmTask,BpmTask parentTask,ProcessNextCmd cmd){
        boolean flag=handVoteResult(bpmTransfer);
        if("waitAllVoted".equals(bpmTransfer.getTransferType())){
            //返回
            if(flag){
                //通知流转人
                sendTaskMessage(parentTask,cmd,"approve");
                //处理抄送
                transerReturnInstCc(bpmTask,cmd);

                bpmTransferService.delete(bpmTransfer.getId());
                bpmTransferLogService.delByTaskId(parentTask.getTaskId());
            }

        }else if("jumpNext".equals(bpmTransfer.getTransferType())) {
            //审批下一步
            if(flag){
                BpmInst bpmInst=bpmInstService.get(bpmTask.getInstId());
                //如果实例是锁定状态，改成运行状态。
                if(BpmInstStatus.LOCKED.name().equals( bpmInst.getStatus())){
                    bpmInst.setStatus(BpmInstStatus.RUNNING.name());
                    bpmInstService.update(bpmInst);
                }
                if(BpmTask.TYPE_PARALLEL_TASK.equals(parentTask.getTaskType())
                        || BpmTask.TYPE_SEQUENTIAL_TASK.equals(parentTask.getTaskType())){
                    UserTaskConfig userTaskConfig=(UserTaskConfig)bpmDefService.getNodeConfig(parentTask.getActDefId(),parentTask.getKey());
                    handMultiTask(parentTask,userTaskConfig,cmd);
                }
                else if(BpmTask.TYPE_TRANSFER_TASK.equals(parentTask.getTaskType())){
                    handTransTask(parentTask,cmd);
                }
                else{
                    taskService.complete(parentTask.getActTaskId(),cmd.getVars());
                    bpmTransferLogService.delByTaskId(parentTask.getTaskId());
                }
                bpmRuPathService.updRuPath(parentTask);
            }
        }
    }

    /**
    * @Description: 被流转人在处理任务返回时的抄送处理，
     * 因为没有触发任务完成事件是不会CompleteTaskHandler监听到的，所以要手动调用下
    * @Author: Elwin ZHANG  @Date: 2021/12/6 11:27
    **/
    private void transerReturnInstCc(BpmTask bpmTask,ProcessNextCmd cmd){
        BpmInstCc instCc=actInstService.buildBpmInstCc(bpmTask);
        //处理上下文的抄送。
        boolean handContextCopy=actInstService.handContextCopy(cmd,instCc);
        if(handContextCopy){
            bpmInstCcService.insert(instCc);
        }
    }
    private boolean handVoteResult(BpmTransfer bpmTransfer){
        if(bpmTransfer.getCompleteType()==0){
            //默认
            if(bpmTransfer.getCompleteCount()>=bpmTransfer.getCount()){
                return true;
            }
        }else if(bpmTransfer.getCompleteType()==1){
            //高级配置
            if("parallel".equals(bpmTransfer.getApproveType())) {
                //并行
                if("voteCount".equals(bpmTransfer.getCompleteJudgeType())){
                    if(bpmTransfer.getCompleteCount()>=bpmTransfer.getCompleteSetting()){
                        return true;
                    }
                }else if("votePercent".equals(bpmTransfer.getCompleteJudgeType())){
                    if((bpmTransfer.getCompleteCount()*100)/bpmTransfer.getCount()>=bpmTransfer.getCompleteSetting()){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * 处理多实例任务。
     * <pre>
     *     1.根据规则对会签进行处理。
     *     2.当会签完成时，调用流程引擎驱动流程往下执行。
     * </pre>
     * @param bpmTask
     * @param userTaskConfig
     * @param cmd
     */
    public void handMultiTask(BpmTask bpmTask, UserTaskConfig userTaskConfig,ProcessNextCmd cmd){
        SignConfig signConfig=userTaskConfig.getSignConfig();
        String signType=bpmTask.getTaskType();
        String actTaskId=bpmTask.getActTaskId();

        /**
         * 判断多实例任务类型。
         */
        boolean isParallel=BpmTask.TYPE_PARALLEL_TASK.equals(signType);
        /**
         * 获取会签任务的总数。
         */
        Integer loopCounts=(Integer)taskService.getVariable(actTaskId, BpmConst.LOOP_COUNTS);
        /**
         * 获取会签的顺序。
         */
        Integer loopIndex=(Integer)taskService.getVariable(actTaskId,BpmConst.LOOP_INDEX);

        Integer nextLoopIndex=loopIndex+1;

        //设置新的流程变量
        taskService.setVariable(actTaskId, BpmConst.LOOP_INDEX,nextLoopIndex);
        //处理会签。
        handSign(bpmTask,cmd);
        //投票结果。
        VoteResult voteResult= getVoteResult( signConfig, bpmTask.getActInstId(), bpmTask.getKey(), loopCounts,nextLoopIndex);
        //执行会签任务完成事件
        eventHandlerExecutor.executeTask(bpmTask,userTaskConfig, EventType.TASK_COMPLETED);
        TaskCompleteApplicationEvent ev = new TaskCompleteApplicationEvent(bpmTask, userTaskConfig);
        SpringUtil.publishEvent(ev);
        //会签完成
        if(voteResult.getCompleted()){
            //如果直接通过的情况,或者投票由脚本直接决定的情况。
            if(signConfig.getFinishOption().equalsIgnoreCase(SignConfig.HANDLE_TYPE_DIRECT)
                    || SignConfig.COMPLETE_CUSTOM.equalsIgnoreCase( signConfig.getCompleteType())){
                completeTask(cmd,voteResult,bpmTask,signConfig);
            }
            else {
                //等待所有的完成。
                if(nextLoopIndex.equals(loopCounts)){
                   completeTask(cmd,voteResult,bpmTask,signConfig);
                }
                else{
                    //串签
                    if(!isParallel){
                        //创建下一个任务
                        BpmTask newTask=createTask(bpmTask, nextLoopIndex,userTaskConfig);
                        //发送任务待办消息
                        sendTaskMessage(newTask,cmd,"approve");
                    }
                }
            }
        }
        else{
            //串签
            if(!isParallel){
                //创建下一个任务
                BpmTask newTask=createTask(bpmTask, nextLoopIndex,userTaskConfig);
                //发送任务待办消息
                sendTaskMessage(newTask,cmd,"approve");
            }
        }
        //处理抄送
        transerReturnInstCc(bpmTask,cmd);
    }

    /**
     * 完成会签任务。
     * @param cmd
     * @param voteResult
     * @param bpmTask
     * @param signConfig
     */
    private void completeTask(IExecutionCmd cmd,VoteResult voteResult,BpmTask bpmTask,SignConfig signConfig){
        //标记会签会签结果
        cmd.addTransientVar(SignConfig.SIGN_RESULT,voteResult.getResult());


        //删除会签数据。
        bpmSignDataService.deleteByNodeId(bpmTask.getActInstId(),bpmTask.getKey());


        List<String> varList=new ArrayList<>();
        varList.add(BpmConst.LOOP_COUNTS);
        varList.add(BpmConst.COMPLETE_COUNTS);
        varList.add(BpmConst.LOOP_INDEX);
        varList.add("sigExecutorIds_" + bpmTask.getKey());


        taskService.removeVariables(bpmTask.getActTaskId(),varList);

        //完成任务
        taskService.complete(bpmTask.getActTaskId(),cmd.getVars());
    }


    /**
     * 处理串行会签。
     * <pre>
     *     1.产生新的任务。
     *     2.分配任务。
     * </pre>
     * @param bpmTask
     * @param loopIndex
     * @param userTaskConfig
     */
    private BpmTask createTask(BpmTask bpmTask,Integer loopIndex,UserTaskConfig userTaskConfig){
        BpmTask newTask=bpmTask;
        newTask.setTaskId(IdGenerator.getIdStr());
        newTask.setTenantId(ContextUtil.getCurrentTenantId());
        String varName=BpmConst.SIGN_EXECUTOR_IDS + bpmTask.getKey();
        String users=  (String)taskService.getVariable(bpmTask.getActTaskId(),varName);
        if(StringUtils.isNotEmpty(users)) {
            List<TaskExecutor> list = JSONArray.parseArray(users,TaskExecutor.class);
            TaskExecutor executor = list.get(loopIndex);
            bpmTaskService.insert(newTask);
            bpmTaskUserService.createUser(newTask, executor);
        }
        //执行会签任务创建事件
        eventHandlerExecutor.executeTask(newTask,userTaskConfig, EventType.TASK_CREATED);
        return newTask;
    }

    /**
     * 处理签批
     * <pre>
     *     1.插入会签意见数据。
     *     2.删除当前的会签任务。
     *     3.插入签批的意见。
     * </pre>
     * @param bpmTask
     * @param cmd
     */
    private void handSign(BpmTask bpmTask, IExecutionCmd cmd){
        //生成会签结果
        BpmSignData bpmSignData=new BpmSignData(bpmTask, ContextUtil.getCurrentUserId(),cmd.getCheckType());
        bpmSignData.setDataId(IdGenerator.getIdStr());
        bpmSignDataService.insert(bpmSignData);
        //删除任务
        bpmTaskMapper.deleteById(bpmTask.getTaskId());

    }

    /**
     * 获取投票结果。
     * @param signConfig
     * @param actInstId
     * @param nodeId
     * @param instanceOfNumbers
     * @param approveTimes
     * @return
     */
    private VoteResult getVoteResult(SignConfig signConfig,String actInstId,String nodeId,int instanceOfNumbers,int approveTimes){

        VoteResult voteResult=null;
        if(SignConfig.COMPLETE_TICKETS.equalsIgnoreCase( signConfig.getCompleteType())){
            TicketCount ticketCount= getTicketCount( actInstId, nodeId);
            voteResult= getVoteResult( signConfig,   instanceOfNumbers,  ticketCount);
        }
        else{
            //如果审批次数等于投票次数,表示投票完成。


            String condition=signConfig.getFinishCondition();
            if(StringUtils.isEmpty(condition)){
                MessageUtil.triggerException("审批任务失败!","没有配置自定义完成条件!");
            }
            TicketCount ticketCount= getTicketCount( actInstId, nodeId);

            //获取流程变量。
            Map<String, Object> vars = runtimeService.getVariables(actInstId);
            Map<String,Object> contextVars= ActivitiUtil.getConextData(vars);

            //投票结果
            contextVars.put("ticketCount",ticketCount);
            //总的实例数
            contextVars.put("instanceOfNumbers",instanceOfNumbers);
            //审批次数
            contextVars.put("approveTimes",approveTimes);

            Object rtn = processScriptEngine.exeScript(condition,contextVars);
            if(!(rtn instanceof  VoteResult)){
                MessageUtil.triggerException("审批任务失败!","自定义脚本的返回类型为VoteResult:\n"+condition);
            }

            VoteResult vResult=(VoteResult)rtn;
            //当投票完成，但是脚本返回没完成的时候，这个时候使用最后一个人的意见进行返回。
            if((instanceOfNumbers==approveTimes) && !vResult.getCompleted()){
                IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();
                boolean result=TaskOptionType.AGREE.name().equals(cmd.getCheckType()) ;
                return  new VoteResult(true,result);
            }

            voteResult=(VoteResult)rtn;
        }


        return  voteResult;
    }


    /**
     * 计算投票结果
     * @param signConfig            会签配置
     * @param instanceOfNumbers     总的实例数
     * @param ticketCount           票数

     * @return
     */
    private VoteResult getVoteResult(SignConfig signConfig, int instanceOfNumbers, TicketCount ticketCount){


        VoteResult rtn=new VoteResult();

        if(ticketCount.getTotalCount()==instanceOfNumbers){
            rtn.setCompleted(true);
        }


        //按投票通过数进行计算
        if(SignConfig.VOTE_TYPE_PASS.equals(signConfig.getVoteOption())){
            rtn.setResult(false);
            //计算是否通过
            //按投票数进行统计
            if(SignConfig.CAL_TYPE_NUMS.equals(signConfig.getCalOption())){
                //代表通过
                if(ticketCount.getPassCount()>=signConfig.getVoteCount()){
                    rtn.setCompleted(true);
                    rtn.setResult(true);
                }
            }else{//按百分比进行计算
                int resultPercent=new Double(ticketCount.getPassCount()*100/instanceOfNumbers).intValue();
                //代表通过
                if(resultPercent>=signConfig.getVoteCount()){
                    rtn.setCompleted(true);
                    rtn.setResult(true);
                }
            }
        }else{//按投票反对数进行计算
            //计算是否通过
            rtn.setResult(true);
            //按投票数进行统计
            if(SignConfig.CAL_TYPE_NUMS.equals(signConfig.getCalOption())){
                //代表通过
                if(ticketCount.getRefuseCount()>=signConfig.getVoteCount()){
                    rtn.setCompleted(true);
                    rtn.setResult(false);
                }
            }else{//按百分比进行计算
                int resultPercent=new Double(ticketCount.getRefuseCount()*100/instanceOfNumbers).intValue();
                //代表通过
                if(resultPercent>=signConfig.getVoteCount()){
                    rtn.setCompleted(true);
                    rtn.setResult(false);
                }
            }
        }
        return rtn;
    }

    /**
     * 根据投票获取投票数量
     * @param actInstId
     * @param nodeId
     * @return
     */
    private TicketCount getTicketCount(String actInstId,String nodeId){
        //获得会签的数据
        List<BpmSignData> bpmSignDatas=bpmSignDataService.getByActInstIdNodeId(actInstId, nodeId);
        //通过票数
        int passCount=0;
        //反对票数
        int refuseCount=0;

        for(BpmSignData data:bpmSignDatas){
            int calCount=1;
            //弃权不作票数统计
            if(TaskOptionType.ABSTAIN.name().equals(data.getVoteStatus())){
                continue;
            }
            //TODO 特权处理先放过。
            //统计同意票数
            if(TaskOptionType.AGREE.name().equals(data.getVoteStatus())){
                passCount+=calCount;
            }
            if(TaskOptionType.REFUSE.name().equals(data.getVoteStatus())){//统计反对票数
                refuseCount+=calCount;
            }
        }

        TicketCount ticketCount=new TicketCount();
        ticketCount.setPassCount(passCount);
        ticketCount.setRefuseCount(refuseCount);
        ticketCount.setTotalCount(bpmSignDatas.size());

        return ticketCount;
    }


}


/**
 * 投票计票类。
 */
@Getter
@Setter
class TicketCount{

    public TicketCount(){
    }

    public TicketCount(int passCount,int refuseCount){
        this.passCount=passCount;
        this.refuseCount=refuseCount;
    }

    //通过票数
    int passCount=0;
    //反对票数
    int refuseCount=0;
    //总投票数
    int totalCount=0;

}



