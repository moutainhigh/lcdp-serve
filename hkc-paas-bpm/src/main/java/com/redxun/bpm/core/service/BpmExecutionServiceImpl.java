package com.redxun.bpm.core.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.event.EventType;
import com.redxun.bpm.activiti.eventhandler.EventHandlerExecutor;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.mapper.BpmExecutionMapper;
import com.redxun.bpm.core.mapper.BpmTaskMapper;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.utils.ContextUtil;
import org.activiti.bpmn.model.FlowNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* [act_ru_execution]业务服务类
*/
@Service
public class BpmExecutionServiceImpl extends SuperServiceImpl<BpmExecutionMapper, BpmExecution> implements BaseService<BpmExecution> {

    @Resource
    private BpmExecutionMapper bpmExecutionMapper;

    @Resource
    private ActTaskServiceImpl  actTaskService;

    @Resource
    private BpmTaskMapper bpmTaskMapper;

    @Resource
    ActRuVariableServiceImpl actRuVariableService;
    @Resource
    EventHandlerExecutor eventHandlerExecutor;
    @Resource
    BpmDefService bpmDefService;

    @Override
    public BaseDao<BpmExecution> getRepository() {
        return bpmExecutionMapper;
    }

    public List<BpmExecution> getByParentId(String parentId){
        return bpmExecutionMapper.getByParentId(parentId);
    }

    /**
     * 删除executions。
     * @param bpmExecutions
     * @param outNodes
     * @param endWay
     */
    public void delExecutions(List<BpmExecution> bpmExecutions, Set<FlowNode> outNodes, String endWay){
        Set<String> nodes= getNodeIds(outNodes);
        for(BpmExecution execution:bpmExecutions){
            String executionId=execution.getId();
            String nodeId=execution.getActId();
            //删除之后的等待节点和正在运行的节点。
            if(nodes.contains(nodeId) || nodeId.equals(endWay)){
                delete(executionId);
            }
        }
    }

    /**
     * 获取节点。
     * @param nodes
     * @return
     */
    private Set<String> getNodeIds(Set<FlowNode> nodes){
        Set<String> set=new HashSet<String>();
        for(FlowNode def:nodes){
            set.add(def.getId());
        }
        return set;
    }


    /**
     *根据流程实例查找EXECTION。
     * @param instId
     * @return
     */
    private List getByInstId(String instId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("PROC_INST_ID_",instId);
        List list = bpmExecutionMapper.selectList(wrapper);
        return list;
    }


    /**
     * 根据实例ID构建树形结构。
     * @param actInstId
     * @return
     */
    private BpmExecution getExecutionByInstId(String actInstId){
        List<BpmExecution> executions= getByInstId( actInstId);

        BpmExecution mainExecution=executions.stream().filter(p->p.getId().equals(p.getProcInstId())).findFirst().get();

        constructExecution(mainExecution,executions);

        return mainExecution;
    }

    /**
     * 修改流程数据，让流程跳转到第一个节点。
     * <pre>
     *     1.构建EXECUTION树。
     *     2.删除EXECUTION数据。
     *     3.更新节点到第一个任务节点。
     * </pre>
     * @param bpmInst
     * @param bpmRuPath
     */
    public void handToStart(BpmInst bpmInst, BpmRuPath bpmRuPath){
        String actInstId=bpmInst.getActInstId();
        BpmExecution execution=getExecutionByInstId(actInstId);

        //删除流程任务。
        actTaskService.delByInstId(actInstId);

        //删除execution。
        delByExecution(execution);

        //更新exeuciton
        BpmExecution newExecution= createExecution(execution,bpmRuPath.getNodeId());

        //创建任务指向第一个节点。
        ActTask actTask=createTask(newExecution, bpmRuPath);
        //创建BPM_TASK
        BpmTask bpmTask= createBpmTask(actTask,bpmInst,bpmRuPath);

        //执行事件(执行任务开始事件)
        UserTaskConfig taskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        eventHandlerExecutor.executeTask(bpmTask,taskConfig, EventType.TASK_CREATED);
        //执行全局事件
        ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmTask.getActDefId());
        eventHandlerExecutor.executeGlobalTask( bpmTask, processConfig, EventType.TASK_CREATED );


    }

    private void delByById(String executeId){
        //删除流程变量
        actRuVariableService.delByExecutionId(executeId);
        //删除任务
        actTaskService.delByExecutionId(executeId);
        //删除ACT_RU_EXECUTION
        bpmExecutionMapper.deleteById(executeId);
    }

    private BpmExecution createExecution(BpmExecution parent,String nodeId){
        BpmExecution bpmExecution=new BpmExecution();
        bpmExecution.setId(IdGenerator.getIdStr());
        bpmExecution.setActId(nodeId);
        bpmExecution.setParentId(parent.getId());
        bpmExecution.setRev(1);
        bpmExecution.setProcInstId(parent.getProcInstId());
        bpmExecution.setRootProcInstId(parent.getRootProcInstId());
        bpmExecution.setProcDefId(parent.getProcDefId());
        bpmExecution.setIsActive((short) 1);
        bpmExecution.setIsConcurrent((short) 0);
        bpmExecution.setIsScope((short) 0);
        bpmExecution.setIsEventScope((short) 0);
        bpmExecution.setIsMiRoot((short) 0);
        bpmExecution.setSuspensionState(1);
        bpmExecution.setStartTime(new Date());
        bpmExecution.setIsCountEnabled((short) 0);
        bpmExecution.setEvtSubscrCount( 0);
        bpmExecution.setTaskCount( 0);
        bpmExecution.setJobCount(0);
        bpmExecution.setTimerJobCount(0);
        bpmExecution.setSuspJobCount(0);
        bpmExecution.setDeadletterJobCount(0);
        bpmExecution.setVarCount(0);
        bpmExecution.setIdLinkCount(0);

        bpmExecutionMapper.insert(bpmExecution);

        return bpmExecution;

    }

    private ActTask createTask(BpmExecution execution,BpmRuPath bpmRuPath){
        ActTask task=new ActTask();
        task.setId(IdGenerator.getIdStr());
        task.setExecutionId(execution.getId());
        task.setProcInstId(execution.getProcInstId());
        task.setRev(1);
        task.setProcDefId(execution.getProcDefId());
        task.setTaskDefKey(execution.getActId());
        task.setCreateTime(new Date());
        task.setSuspensionState(1);
        task.setName(bpmRuPath.getNodeName());
        task.setPriority(50);
        actTaskService.insert(task);

        return task;
    }

    /**
     * 创建BPM_TASK
     * @param actTask
     * @param bpmInst
     * @param bpmRuPath
     */
    private BpmTask  createBpmTask(ActTask actTask,BpmInst bpmInst,BpmRuPath bpmRuPath){

        BpmTask task=new BpmTask();
        task.setTaskId(IdGenerator.getIdStr());
        task.setActTaskId(actTask.getId());
        task.setSubject(bpmInst.getSubject());

        task.setActDefId(actTask.getProcDefId());
        task.setDefId(bpmInst.getDefId());
        task.setName(bpmRuPath.getNodeName());
        task.setInstId(bpmInst.getInstId());
        task.setTreeId(bpmInst.getTreeId());
        task.setKey(actTask.getTaskDefKey());
        task.setActInstId(actTask.getProcInstId());

        //elwin add 2021-4-9
        task.setBillType(bpmInst.getBillType());
        task.setBillNo(bpmInst.getBillNo());
        task.setBusKey(bpmInst.getBusKey());

        task.setOwner(bpmInst.getCreateBy());
        task.setAssignee(bpmInst.getCreateBy());

        task.setTaskType(BpmTask.TYPE_FLOW_TASK);
        task.setStatus(BpmTask.STATUS_UNHANDLE);
        task.setTenantId(ContextUtil.getCurrentTenantId());

        bpmTaskMapper.insert(task);

        return task;
    }
    /**
     * 删除execution。
     * @param parent
     */
    private void delByExecution(BpmExecution parent){
        if(BeanUtil.isEmpty( parent.getChildren())) {
            return;
        }
        for(BpmExecution execution:parent.getChildren()){
            delByExecution(execution);
            if(execution.getLevel()>0){
                String executeId=execution.getId();
                //删除关联数据。
                delByById(executeId);
            }
        }
    }



    private void constructExecution(BpmExecution parent, List<BpmExecution> list){
        String id=parent.getId();
        for(BpmExecution execution:list){
            if(id.equals(execution.getParentId())){
                execution.setLevel(parent.getLevel() +1);

                parent.addChildren(execution);
                //递归构建。
                constructExecution(execution,list);
            }
        }
    }




}
