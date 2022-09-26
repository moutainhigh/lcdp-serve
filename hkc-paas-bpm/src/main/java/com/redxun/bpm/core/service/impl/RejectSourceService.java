package com.redxun.bpm.core.service.impl;

import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.event.TaskCompleteApplicationEvent;
import com.redxun.bpm.activiti.eventhandler.EventHandlerExecutor;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.bpm.core.entity.BpmInstStatus;
import com.redxun.bpm.core.entity.BpmRuPath;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.BpmInstServiceImpl;
import com.redxun.bpm.core.service.BpmTaskService;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SpringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 任务回退时节点操作服务类
 */
@Service
public class RejectSourceService {

    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    EventHandlerExecutor eventHandlerExecutor;

    /**
     * 1.将流程现有的任务进行冻结，修改流程任务的状态为LOCKED。
     * 2.修改BPMINST状态为LOCKED.
     * 3.创建一个新的任务指向第一个节点。
     * 4.更新执行路径为驳回。
     * 5.创建一条执行路径。
     * @param preTask
     */
    public void handSource(BpmTask preTask, BpmRuPath targetPath){



        String instId=preTask.getInstId();
        BpmInst bpmInst=bpmInstService.get(instId);

        bpmInst.setLockedBy(targetPath.getAssignee());
        bpmInst.setStatus(BpmInstStatus.LOCKED.name());
        bpmInstService.update(bpmInst);
        /**
         * 更新流程实例状态为锁定。
         */
        bpmTaskService.updTaskStatusByInstId(preTask.getTaskId(),BpmTask.STATUS_LOCKED);

        //执行全局事件
        ProcessConfig processConfig = bpmDefService.getProcessConfig(preTask.getActDefId());

        //执行事件(执行任务开始事件)
        UserTaskConfig completeTaskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(preTask.getActDefId(),preTask.getKey());
        eventHandlerExecutor.executeTask(preTask,completeTaskConfig, EventType.TASK_COMPLETED);
        //执行全局事件
        eventHandlerExecutor.executeGlobalTask( preTask, processConfig, EventType.TASK_COMPLETED );
        TaskCompleteApplicationEvent ev = new TaskCompleteApplicationEvent(preTask, completeTaskConfig,processConfig);
        SpringUtil.publishEvent(ev);
        /**
         * 创建新的任务。
         */
        BpmTask bpmTask= createTask(bpmInst,targetPath,preTask);

        //执行事件(执行任务开始事件)
        UserTaskConfig taskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        eventHandlerExecutor.executeTask(bpmTask,taskConfig, EventType.TASK_CREATED);
        //执行全局事件
        eventHandlerExecutor.executeGlobalTask( bpmTask, processConfig, EventType.TASK_CREATED );
    }

    /**
     * 创建驳回的任务。
     * @param bpmInst
     * @param path
     * @param preTask
     */
    private BpmTask createTask(BpmInst bpmInst, BpmRuPath path, BpmTask preTask){
        BpmTask bpmTask=new BpmTask();
        bpmTask.setStatus(BpmTask.STATUS_UNHANDLE);
        bpmTask.setTaskId(IdGenerator.getIdStr());
        //记录前面的任务（根据这个任务ID激活原来的任务)
        bpmTask.setPreTaskId(preTask.getTaskId());

        bpmTask.setDefId(bpmInst.getDefId());
        bpmTask.setTreeId(bpmInst.getTreeId());
        bpmTask.setActDefId(bpmInst.getActDefId());
        bpmTask.setActInstId(preTask.getActInstId());
        bpmTask.setSubject(bpmInst.getSubject());
        bpmTask.setInstId(bpmInst.getInstId());
        //添加单号单号类型，主键。
        bpmTask.setBillType(bpmInst.getBillType());
        bpmTask.setBillNo(bpmInst.getBillNo());
        bpmTask.setBusKey(bpmInst.getBusKey());

        bpmTask.setAssignee(path.getAssignee());
        bpmTask.setOwner(path.getAssignee());

        bpmTask.setName(path.getNodeName());
        bpmTask.setKey(path.getNodeId());

        bpmTask.setTaskType(BpmTask.TYPE_REJECT_TASK);
        bpmTask.setTenantId(ContextUtil.getCurrentTenantId());
        bpmTaskService.insert(bpmTask);

        return bpmTask;
    }
}
