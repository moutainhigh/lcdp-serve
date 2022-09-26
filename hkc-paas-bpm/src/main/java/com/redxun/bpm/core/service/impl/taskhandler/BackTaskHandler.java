package com.redxun.bpm.core.service.impl.taskhandler;

import com.redxun.bpm.activiti.ActInstService;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.mapper.BpmTaskUserMapper;
import com.redxun.bpm.core.service.BpmRuPathServiceImpl;
import com.redxun.bpm.core.service.impl.RejectHandlerContext;
import com.redxun.common.utils.MessageUtil;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SubProcess;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务回退服务处理器
 */
@Component
public class BackTaskHandler  extends BaseTaskHandler {

    @Resource
    BpmRuPathServiceImpl bpmRuPathService;
    @Resource
    ActRepService actRepService;
    @Resource
    RejectHandlerContext rejectHandlerContext;
    @Resource
    ActInstService actInstService;


    @Override
    public boolean canHandler(String checkType) {
        if(TaskOptionType.BACK.name().equals(checkType)){
            return true;
        }
        return false;
    }

    @Override
    public void handTask(BpmTask bpmTask, IExecutionCmd cmd, UserTaskConfig taskConfig) {

        if(BpmTask.TYPE_REJECT_TASK.equals( bpmTask.getTaskType())){
            MessageUtil.triggerException("驳回处理失败!","当前任务是驳回任务不能再次驳回!");
        }
        BpmInst bpmInst=bpmInstService.get(bpmTask.getInstId());
        if(BpmInstStatus.LOCKED.name().equals(bpmInst.getStatus())){
            MessageUtil.triggerException("驳回处理失败!","当前实例已经被驳回,不能再驳回操作!");
        }

        BpmRuPath bpmRuPath=bpmRuPathService.getByExeutionAndNode(bpmTask.getInstId(),bpmTask.getKey(),bpmTask.getExecutionId());
        if(bpmRuPath==null){
            bpmRuPath=bpmRuPathService.getLatestByInstIdNodeId(bpmTask.getInstId(),bpmTask.getKey());
        }
        BpmRuPath parentPath=bpmRuPathService.get(bpmRuPath.getParentId());
        if(parentPath.getParentId().equals("0")){
            MessageUtil.triggerException("审批任务失败!","已经驳回到开始节点,不能再驳回了!");
        }
        //这里主要时解决在驳回中，比如网关，多实例子流程的驳回问题。
        boolean handled= rejectHandlerContext.handle(bpmTask,parentPath);

        if(handled){
            return;
        }

        List<BpmRuPath> pathList=new ArrayList<>();


        //使用流程的方式进行回退。
        String targetNodeId="";
        /*
            处理子流程的情况。
            1.先找到子流程
            2.根据子流程找到结束节点。
            3.获取结束节点的路径。
            4.往回进行查找。
         */
        if("subProcess".equals(parentPath.getNodeType())){
            FlowNode flowNode = actRepService.getFlowNode(parentPath.getActDefId(), parentPath.getNodeId());
            SubProcess subProcess=(SubProcess)flowNode;
            FlowNode endNode= actRepService.getEndNode(subProcess);

            BpmRuPath endPath=bpmRuPathService.getLatestByInstIdNodeId(bpmRuPath.getInstId(),endNode.getId());
            pathList.add(bpmRuPath);
            pathList.add(parentPath);
            parentPath=getTargetPath(endPath,pathList);
        }
        //处理正常的任务
        else{
            pathList.add(bpmRuPath);
            parentPath=getTargetPath(parentPath,pathList);
        }
        targetNodeId=parentPath.getNodeId();

        //当为多实例的情况时，需要删除当前任务的关联任务。
        if(!"normal".equals(bpmRuPath.getMultipleType()) ){
            delMultiTask(bpmTask);
        }

        actInstService.doJumpToTargetNode(bpmTask.getActTaskId(),targetNodeId);
        //驳回时删除当前路径。
        for(BpmRuPath path:pathList){
            bpmRuPathService.delete(path.getPathId());
        }


    }

    /**
     * 获取目标路径。
     * <pre>
     *     沿着当前的路径往回查找。如果意见类型为跳过并且这个节点为起始节点就直接返回这个路径。
     * </pre>
     * @param parentPath
     * @return
     */
    private BpmRuPath getTargetPath(BpmRuPath parentPath,List<BpmRuPath> list){
        while (!"userTask".equals(parentPath.getNodeType()) || ("SKIP".equals( parentPath.getJumpType())) ){
            BpmRuPath curPath=bpmRuPathService.get(parentPath.getParentId());
            if("0".equals(curPath.getParentId())){
                break;
            }
            else {
                list.add(parentPath);
            }
            parentPath=curPath;
        }
        return parentPath;
    }

    /**
     * 删除多实例任务。
     * @param bpmTask
     */
    protected void  delMultiTask(BpmTask bpmTask){
        List<BpmTask> tasks = bpmTaskService.getTasksByActTaskId(bpmTask.getActTaskId());
        for(BpmTask task:tasks){
            bpmTaskService.delByTaskId(task.getTaskId());
        }
    }




}
