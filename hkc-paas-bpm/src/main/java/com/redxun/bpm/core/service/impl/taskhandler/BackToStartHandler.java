package com.redxun.bpm.core.service.impl.taskhandler;

import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.ActInstService;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.event.TaskCompleteApplicationEvent;
import com.redxun.bpm.activiti.eventhandler.EventHandlerExecutor;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.mapper.BpmTaskUserMapper;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.core.service.impl.RejectSourceService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.user.OsUserDto;
import org.activiti.bpmn.model.FlowNode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class BackToStartHandler extends BaseTaskHandler {

    @Resource
    ActRepService actRepService;
    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    BpmRuPathServiceImpl bpmRuPathService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    BpmTaskUserMapper bpmTaskUserMapper;
    @Resource
    BpmExecutionServiceImpl bpmExecutionService;
    @Resource
    RejectSourceService rejectSourceService;
    @Resource
    IOrgService orgService;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    EventHandlerExecutor eventHandlerExecutor;
    @Resource
    BpmInstCcServiceImpl bpmInstCcService;

    @Resource
    ActInstService actInstService;

    @Override
    public boolean canHandler( String checkType) {
        if(TaskOptionType.BACK_TO_STARTOR.name().equals(checkType)){
            return true;
        }
        return false;
    }

    @Override
    public void handTask(BpmTask bpmTask, IExecutionCmd cmd, UserTaskConfig taskConfig) {

        if(BpmTask.TYPE_REJECT_TASK.equals( bpmTask.getTaskType())){
            MessageUtil.triggerException("驳回发起人失败!","当前任务是驳回任务不能再次驳回!");
        }
        BpmInst bpmInst=bpmInstService.get(bpmTask.getInstId());
        //如果流程实例处在驳回状态给出驳回提示。
        if(BpmInstStatus.LOCKED.name().equals(bpmInst.getStatus())){
            IUser user=orgService.getUserById(bpmInst.getLockedBy()) ;
            MessageUtil.triggerException("驳回发起人失败!","当前实例正处在驳回状态,驳回人为["+user.getFullName()+"]!");
        }

        String actDefId=bpmTask.getActDefId();
        /**
         * 创建新的任务。
         */
        FlowNode flowNode= actRepService.getFirstUserTaskNode(actDefId);

        String backNodeId=flowNode.getId();

        ProcessNextCmd nextCmd=(ProcessNextCmd)cmd;

        if(backNodeId.equals(bpmTask.getKey())){
            MessageUtil.triggerException ("驳回到发起人失败!","目前已经是开始节点不能驳回!");
        }
        String nextJumpType=((ProcessNextCmd) cmd).getNextJumpType();

        /**
         * 按照流程图执行
         */
        if(ProcessNextCmd.NEXT_JUMP_OPTION_NORMAL.equals(nextJumpType)){
            handNormal(nextCmd.getTaskId());
        }
        //原路返回处理
        else {
            BpmRuPath ruPath= bpmRuPathService.getLatestByInstIdNodeId(bpmTask.getInstId(),backNodeId);
            rejectSourceService.handSource(bpmTask,ruPath);
        }
        //通知发起人
        String msgType=cmd.getMsgTypes();
        if(StringUtils.isNotEmpty(msgType)) {
            Map<String, Object> vars = new HashMap<>();
            BpmTask newTask=bpmTaskService.getNewTaskByInstId(bpmTask.getInstId());
            OsUserDto receiver = orgService.getUserById(newTask.getAssignee());
            vars.put("opinion", cmd.getOpinion());
            vars.put("taskId", newTask.getTaskId());
            vars.put("instId", newTask.getInstId());
            vars.put("actDefId",newTask.getActDefId());
            vars.put("nodeId",newTask.getKey());
            OsUserDto sender = orgService.getUserById(ContextUtil.getCurrentUserId());
            com.redxun.bpm.core.ext.messagehandler.MessageUtil.sendMessage(sender, newTask.getSubject(), msgType, "backToStart", receiver, vars);
        }
        //处理抄送
        transerReturnInstCc(bpmTask,nextCmd);
    }
    /**
     * @Description: 抄送处理，
     * @Author: Elwin ZHANG  @Date: 2021/12/10 11:27
     **/
    private void transerReturnInstCc(BpmTask bpmTask,ProcessNextCmd cmd){
        BpmInstCc instCc=actInstService.buildBpmInstCc(bpmTask);
        //处理上下文的抄送。
        boolean handContextCopy=actInstService.handContextCopy(cmd,instCc);
        if(handContextCopy){
            bpmInstCcService.insert(instCc);
        }
    }
    /**
     * 处理按照流程图驳回。
     * <pre>
     *     处理方式。
     *     1.清理流程执行路径。
     *     2.清理流程引擎的执行数据。
     *     3.将流程执行节点修改到第一个节点。
     *     4.再第一个节点创建流程任务 (BPM_TASK,ACT_RU_TASK)
     * </pre>
     * @param taskId
     */
    private void handNormal(String taskId){
        BpmTask bpmTask=bpmTaskService.get(taskId);

        //执行事件(执行任务完成事件)
        UserTaskConfig taskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        eventHandlerExecutor.executeTask(bpmTask,taskConfig, EventType.TASK_COMPLETED);
        //执行全局事件
        ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmTask.getActDefId());
        eventHandlerExecutor.executeGlobalTask( bpmTask, processConfig, EventType.TASK_COMPLETED );
        TaskCompleteApplicationEvent ev = new TaskCompleteApplicationEvent(bpmTask, taskConfig,processConfig);
        SpringUtil.publishEvent(ev);

        String instId=bpmTask.getInstId();
        BpmInst inst=bpmInstService.get(bpmTask);

        List<BpmRuPath> bpmRuPaths= bpmRuPathService.getByInstId(instId);

        //删除RU_PATH
        bpmRuPathService.removeRuPath(bpmRuPaths);
        //获取第一个审批路径。
        BpmRuPath path= bpmRuPathService.getFirstPath(bpmRuPaths);
        //删除BPM_TASK_USER
        bpmTaskUserMapper.deleteByInstId(instId);

        //删除BPM_TASK
        bpmTaskService.deleteByInstId(instId);
        //删除 ACT_RU_EXECUTION ,并进行更改
        bpmExecutionService.handToStart(inst,path);

    }





}
