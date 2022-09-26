package com.redxun.bpm.activiti;

import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.cmd.DeleteTaskCmd;
import com.redxun.bpm.activiti.cmd.JumpToTargetCmd;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.event.TaskCompleteApplicationEvent;
import com.redxun.bpm.activiti.eventhandler.EventHandlerExecutor;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.ext.messagehandler.MessageUtil;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.user.OsUserDto;
import org.activiti.engine.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程实例服务类
 */
@Service
public class ActInstService {

    @Resource
    ManagementService managementService;

    @Resource
    RuntimeService runtimeService;
    @Resource
    BpmRuPathServiceImpl bpmRuPathService;
    @Resource
    EventHandlerExecutor eventHandlerExecutor;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Resource
    IOrgService orgService;
    @Resource
    BpmInstCpServiceImpl bpmInstCpService;
    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    BpmInstCcServiceImpl bpmInstCcService;

    @Resource
    BpmInstRouterServiceImpl bpmInstRouterService;

    @Resource
    BpmTaskService bpmTaskService;


    /**
     * 让流程继续往下执行。
     * @param executionId
     * @param jumpType
     * @param opinion
     */
    public void trigger(String executionId,String jumpType,String opinion){
        ProcessNextCmd cmd=new ProcessNextCmd();
        BpmRuPath bpmRuPath=bpmRuPathService.getLatestByExecutionId(executionId);
        if(bpmRuPath!=null) {
            cmd.setPreNodeId(bpmRuPath.getNodeId());
        }
        cmd.setCheckType(jumpType);
        cmd.setOpinion(opinion);
        ProcessHandleUtil.setProcessCmd(cmd);
        runtimeService.trigger(executionId);
    }


    /**
     * 跳转到目标节点。
     * @param actTaskId
     * @param targetNodeId
     */
    public void doJumpToTargetNode(String actTaskId,String targetNodeId){
        AbstractExecutionCmd iExecutionCmd= (AbstractExecutionCmd)ProcessHandleUtil.getProcessCmd();
        BpmTask bpmTask=(BpmTask) iExecutionCmd.getTransientVar(BpmConst.BPM_APPROVE_TASK);
        ProcessConfig processConfig= (ProcessConfig) iExecutionCmd.getTransientVar(BpmConst.PROCESS_CONFIG);
        if(BeanUtil.isEmpty(processConfig)){
            processConfig = bpmDefService.getProcessConfig(bpmTask.getActDefId());
        }
        UserTaskConfig userTaskConfig=(UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        //执行事件。
        eventHandlerExecutor.executeTask(bpmTask,userTaskConfig, EventType.TASK_COMPLETED);
        eventHandlerExecutor.executeGlobalTask(bpmTask,processConfig,EventType.TASK_COMPLETED);
        TaskCompleteApplicationEvent ev = new TaskCompleteApplicationEvent(bpmTask, userTaskConfig,processConfig);
        SpringUtil.publishEvent(ev);

        String executionEntityId = managementService.executeCommand(new DeleteTaskCmd(actTaskId));
        //流程执行到来源节点
        managementService.executeCommand(new JumpToTargetCmd(targetNodeId, executionEntityId));

        BpmInstCc instCc=buildBpmInstCc(bpmTask);
        //处理上下文的抄送。
        boolean handContextCopy=handContextCopy(iExecutionCmd,instCc);
        if(handContextCopy){
            bpmInstCcService.insert(instCc);
        }

        /**
         * 只有跳过的时候才增加跳过的历史。
         */
        if(TaskOptionType.SKIP.name().equals( iExecutionCmd.getCheckType())){
            IUser iUser = ContextUtil.getCurrentUser();
            bpmCheckHistoryService.createHistory(bpmTask, TaskOptionType.SKIP.name(),"","",iUser.getUserId(),iUser.getDeptId(),iExecutionCmd.getOpFiles(),iExecutionCmd.getRelInsts(),"");
        }

    }

    /**
     * 构建抄送对象。
     * @param bpmTask
     * @return
     */
    public BpmInstCc buildBpmInstCc(BpmTask bpmTask){
        IUser currentUser= ContextUtil.getCurrentUser();
        BpmInstCc instCc=new BpmInstCc();
        instCc.setId(IdGenerator.getIdStr());
        instCc.setCcType(BpmInstCc.CC_TYPE_COPY);
        instCc.setDefId(bpmTask.getDefId());
        instCc.setInstId(bpmTask.getInstId());
        instCc.setTreeId(bpmTask.getTreeId());
        instCc.setFromUser(currentUser.getFullName());
        instCc.setFromUserId(currentUser.getUserId());
        instCc.setSubject(bpmTask.getSubject());
        instCc.setNodeId(bpmTask.getKey());
        instCc.setNodeName(bpmTask.getName());

        return  instCc;
    }

    /**
     * 处理Context 复制
     * @param cmd
     * @param instCc
     * @return
     */
    public boolean handContextCopy(IExecutionCmd cmd,BpmInstCc instCc){
        if(StringUtils.isEmpty(cmd.getCopyUserAccounts()) ) {
            return false;
        }
        String[] userAccounts=cmd.getCopyUserAccounts().split("[,]");
        List<OsUserDto> userList=new ArrayList<>();
        for (String userAccount : userAccounts) {
            OsUserDto user = orgService.getByAccount(userAccount);
            userList.add(user);
        }

        addBpmInstReceivers(instCc,userList);
        //发送抄送消息
        sendMessage(instCc,cmd.getMsgTypes(),userList);

        return  true;
    }

    /**
     * 发送消息
     * @param bpmInstCc
     * @param informTypes
     * @param users
     */
    protected void sendMessage(BpmInstCc bpmInstCc, String informTypes ,List<OsUserDto> users){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        IUser user= ContextUtil.getCurrentUser();

        OsUserDto userDto=orgService.getUserById(user.getUserId());

        Map<String,Object> vars=new HashMap<>();

        if(cmd.getBoDataMap()!=null) {
            vars.putAll(cmd.getBoDataMap());
        }
        vars.put("instId",bpmInstCc.getInstId());
        BpmInst bpmInst = bpmInstService.get(bpmInstCc.getInstId());
        if(bpmInst==null){
            bpmInst=(BpmInst) cmd.getTransientVar(BpmConst.BPM_INST);
        }
        vars.put("actDefId",bpmInst.getActDefId());

        MessageUtil.sendMessage(userDto,bpmInstCc.getSubject(),informTypes,"copy",users,vars);

    }


    /**
     * 添加抄送人员。
     * @param instCc
     * @param users
     */
    protected void addBpmInstReceivers(BpmInstCc instCc,List<OsUserDto> users){
        for(OsUserDto user:users){
            if(BeanUtil.isNotEmpty(user)){
                BpmInstCp bpmInstCp=new BpmInstCp();
                bpmInstCp.setId(IdGenerator.getIdStr());
                bpmInstCp.setCcId(instCc.getId());
                bpmInstCp.setInstId(instCc.getInstId());
                bpmInstCp.setUserId(user.getUserId());
                bpmInstCp.setIsRead(MBoolean.NO.name());
                bpmInstCpService.insert(bpmInstCp);
            }
        }
    }



    /**
     * 通过流程实例ID获得流程的跳转线ID列表
     * @param instId                程实例ID
     * @param flowsSkipIdlist       流程跳过的跳转线ID列表
     * @param flowsAgreeIdlist      流程通过的跳转线ID列表
     * @param flowsRefuseIdlist      流程不通过的跳转线ID列表
     * @param flowsBackIdlist      流程驳回的跳转线ID列表
     * @param flowsHandleIdlist     流程当前正在运行的的跳转线ID列表
     */
    public void getFlowsByInstId(String instId,List<String> flowsSkipIdlist,List<String> flowsAgreeIdlist
            , List<String> flowsRefuseIdlist,List<String> flowsBackIdlist, List<String> flowsHandleIdlist){

        BpmInst bpmInst;
        BpmInstRouter bpmInstRouter = bpmInstRouterService.get(instId);
        if(BeanUtil.isNotEmpty(bpmInstRouter)){
            bpmInst=bpmInstService.getByArchiveLog(instId,bpmInstRouter.getTableId());
        }else {
            bpmInst=bpmInstService.get(instId);
        }
        List<BpmCheckHistory> historyList = bpmCheckHistoryService.getByInstId(bpmInst.getInstId());
        List<BpmTask> tasks=bpmTaskService.getByInstId(bpmInst.getInstId());
        for(BpmTask task:tasks){
            //加上当前正在运行的
            BpmCheckHistory hisRun=new BpmCheckHistory();
            hisRun.setNodeId(task.getKey());
            hisRun.setCheckStatus(task.getStatus());
            hisRun.setCreateTime(task.getCreateTime());
            historyList.add(hisRun);
        }

        Map<String, List<BpmCheckHistory>> map = historyList.stream().collect(Collectors.groupingBy(BpmCheckHistory::getNodeId));
        for (Map.Entry<String, List<BpmCheckHistory>> bpmCheckHistorys : map.entrySet()) {
            List<BpmCheckHistory> bpmCheckHistoryList = bpmCheckHistorys.getValue();
            BpmCheckHistory history = bpmCheckHistoryList.stream().max(Comparator.comparing(BpmCheckHistory::getCreateTime)).get();
            if(history.getCheckStatus().equals("SKIP")){
                flowsSkipIdlist.add(history.getNodeId());
            }else if(history.getCheckStatus().equals("AGREE")){
                flowsAgreeIdlist.add(history.getNodeId());
            }else if(history.getCheckStatus().equals("REFUSE")){
                flowsRefuseIdlist.add(history.getNodeId());
            }else if(history.getCheckStatus().equals("BACK")){
                flowsBackIdlist.add(history.getNodeId());
            }else if(history.getCheckStatus().equals("HANDLE")){
                flowsHandleIdlist.add(history.getNodeId());
            }else{
                flowsAgreeIdlist.add(history.getNodeId());
            }
        }


    }


}
