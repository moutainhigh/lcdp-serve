package com.redxun.bpm.listener.taskcreate;

import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.event.TaskAssignApplicationEvent;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.cache.CacheUtil;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

/**
 * 功能: 任务分配人员后监听器。
 * <pre>
 *     1.先计算是否有代理。
 *          如果有代理，则处理代理，处理完成后则返回。
 *     2.处理交接，如果配置了交接，则处理交接人。
 *
 *     3.如果没有交接配置，则处理一下人员是否离职。
 * </pre>
 * @author ray
 * @date 2022/7/12 14:24
 */
@Service
public class TaskAssignListener implements ApplicationListener<TaskAssignApplicationEvent> {

    /**
     * dealQuitUser缓存区域
     */
    private static final String REGION_DEAL_QUIT_USER = "dealQuitUser";

    @Autowired(required = false)
    IBpmAgentService agentService;

    @Autowired
    BpmTaskTransferServiceImpl bpmTaskTransferService;
    @Autowired
    BpmDeliverServiceImpl bpmDeliverService;
    @Autowired
    IOrgService orgService;

    @Autowired
    BpmDefService bpmDefService;
    @Autowired
    BpmQuitUserTaskServiceImpl bpmQuitUserTaskService;


    @Override
    public void onApplicationEvent(TaskAssignApplicationEvent event) {
        BpmTask task=(BpmTask) event.getSource();

        //如果设置了代理，先处理代理。
        boolean handAgent=handAgent(task);
        if(handAgent){
            return;
        }
        //是否设置了交接
        handDeliver(event);

        //处理离职的情况
        handQuitUser(event);
    }

    /**
     * 处理离职
     * @param event
     */
    private void  handQuitUser(TaskAssignApplicationEvent event){
        BpmTask bpmTask=(BpmTask) event.getSource();
        String assignee = bpmTask.getAssignee();
        if(StringUtils.isNotEmpty(assignee)){
            handQuitUser(bpmTask,assignee);
        }
        else{
            BpmTaskUser bpmTaskUser=event.getBpmTaskUser();
            if(bpmTaskUser==null || bpmTaskUser.getUserId()==null){
                return ;
            }
            handQuitUser(bpmTask,bpmTaskUser.getUserId());
        }
    }

    /**
     * 处理交接
     * @param event
     * @return
     */
    private boolean handDeliver(TaskAssignApplicationEvent event){
        BpmTask bpmTask=(BpmTask) event.getSource();

        String assignee = bpmTask.getAssignee();
        if(StringUtils.isNotEmpty(assignee)){
            BpmDeliver bpmDeliver = bpmDeliverService.getByDeliverUserId(assignee);
            //是否有交接人，直接设置交接人
            if(BeanUtil.isNotEmpty(bpmDeliver)){
                bpmTask.setAssignee(bpmDeliver.getReceiptUserId());
                return  true;
            }

        }
        else{
            BpmTaskUser bpmTaskUser=event.getBpmTaskUser();
            if(bpmTaskUser==null){
                return false;
            }
            BpmDeliver bpmDeliver = bpmDeliverService.getByDeliverUserId(bpmTaskUser.getUserId());
            //获取到交接配置
            if(BeanUtil.isNotEmpty(bpmDeliver)){
                bpmTaskUser.setUserId(bpmDeliver.getReceiptUserId());
                return true;
            }
        }
        return false;

    }

    /**
     * 处理代理。
     * @param bpmTask
     * @return
     */
    private  boolean handAgent(BpmTask bpmTask){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        if( !cmd.getCheckType().equals(TaskOptionType.AGREE.name()) &&
                !cmd.getCheckType().equals(TaskOptionType.REFUSE.name()) &&
                !cmd.getCheckType().equals(TaskOptionType.SKIP.name()) ){
            return false;
        }

        JsonResult<String> result = agentService.getAgentUser(bpmTask.getAssignee(), bpmTask.getDefId());
        if(!result.isSuccess()) {
            return false;
        }
        //设置任务处理人。
        bpmTask.setAssignee(result.getData());
        //代理任务加到下文。
        cmd.addTransientVar(BpmConst.BPM_AGENT_TASK,true);
        //添加任务转换。
        addTrans(bpmTask);

        return true;
    }

    /**
     * 添加任务转交。
     * @param task
     */
    private void addTrans(BpmTask task){
        BpmTaskTransfer taskTransfer=new BpmTaskTransfer();
        taskTransfer.setId(IdGenerator.getIdStr());
        taskTransfer.setInstId(task.getInstId());
        taskTransfer.setOwnerId(task.getOwner());
        taskTransfer.setToUserId(task.getAssignee());
        taskTransfer.setType(BpmTaskTransfer.TYPE_AGENT);
        taskTransfer.setSubject(task.getSubject());
        taskTransfer.setTreeId(task.getTreeId());
        taskTransfer.setTaskId(task.getTaskId());

        bpmTaskTransferService.insert(taskTransfer);
    }

    /**
     * 获取流程定义名称。
     * @param defId
     * @return
     */
    private String getBpmDefName(String defId){
        String defName =(String) CacheUtil.get(REGION_DEAL_QUIT_USER,defId);
        if(StringUtils.isEmpty(defName)){
            defName = bpmDefService.get(defId).getName();
            CacheUtil.set(REGION_DEAL_QUIT_USER, defId, defName);
        }
        return defName;
    }

    /**
     * 处理离职。
     * @param bpmTask
     * @param userId
     */
    private void handQuitUser(BpmTask bpmTask,String userId){
        OsUserDto user = orgService.getUserById(userId);
        if(StringUtils.isEmpty(user.getStatus()) || !"0".equals(user.getStatus())){
            return;
        }
        BpmQuitUserTask bpmQuitUserTask = new BpmQuitUserTask();
        bpmQuitUserTask.setQuitUserId(user.getUserId());
        bpmQuitUserTask.setQuitUserNo(user.getAccount());
        bpmQuitUserTask.setQuitUserName(user.getFullName());
        bpmQuitUserTask.setDefId(bpmTask.getDefId());

        //先从缓存取
        String defName =getBpmDefName(bpmTask.getDefId());
        bpmQuitUserTask.setDefName(defName);
        bpmQuitUserTask.setNodeId(bpmTask.getKey());
        bpmQuitUserTask.setNodeName(bpmTask.getName());
        bpmQuitUserTaskService.dealQuitUser(bpmQuitUserTask);
    }

}
