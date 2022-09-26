package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSONArray;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.TaskInstanceType;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import org.activiti.engine.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能: 处理流程干预
 * @author ray
 * @date 2022/6/11 15:51
 */
@Service
public class BpmInterposeSeviceImpl {

    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    TaskService taskService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    BpmTaskUserServiceImpl bpmTaskUserService;
    @Resource
    IOrgService orgService;

    /**
     * 干预正在执行节点的执行人。
     * @param actInstId
     * @param nodeId
     * @param userIds
     * @return
     */
    @Transactional
    public JsonResult updateRunningNodeUser(String actInstId, String nodeId, String userIds){
        BpmInst bpmInst=bpmInstService.getByActInstId(actInstId);
        String actDefId=bpmInst.getActDefId();
        UserTaskConfig taskConfig=(UserTaskConfig) bpmDefService.getNodeConfig(actDefId,nodeId);

        LogContext.put(Audit.DETAIL,"干预" +bpmInst.getSubject() +"节点"+taskConfig.getName()+"执行人员");

        String multiType=taskConfig.getMultipleType();

        if(TaskInstanceType.parallel.name().equals(multiType)){
            return handParallel(bpmInst,nodeId,userIds);
        }
        else if(TaskInstanceType.sequential.name().equals(multiType)){
            return handSequential(bpmInst,nodeId,userIds);
        }
        //普通任务
        return handNormal(bpmInst,nodeId,userIds);
    }


    /**
     * 处理普通任务
     * @param bpmInst
     * @param nodeId
     * @param userIds
     * @return
     */
    private JsonResult handNormal(BpmInst bpmInst,String nodeId,String userIds){
        List<BpmTask> bpmTasks=bpmTaskService.getByActInstIdAndNodeId(bpmInst.getActInstId(),nodeId);
        if(BeanUtil.isEmpty(bpmInst)){
            return new JsonResult(false,"当前节点无任务！");
        }
        String[] userIdArr=userIds.split("[,]");
        List<TaskExecutor> executors=new ArrayList<>();
        //普通任务
        for(BpmTask bpmTask:bpmTasks){
            //单个用户，则设置为只读更新状态
            if(userIdArr.length==1){
                bpmTask.setOwner(userIdArr[0]);
                bpmTask.setAssignee(userIdArr[0]);
                bpmTaskService.doClearTaskUsers(bpmTask.getTaskId());
                bpmTaskService.update(bpmTask);
                continue;
            }

            bpmTask.setOwner(null);
            bpmTask.setAssignee(null);
            bpmTaskService.update(bpmTask);
            bpmTaskService.doClearTaskUsers(bpmTask.getTaskId());

            for(String userId:userIdArr) {
                executors.add(new TaskExecutor(TaskExecutor.TYPE_USER, userId));
            }
            bpmTaskUserService.createUsers(bpmTask, executors);
        }
        return new JsonResult(true,"成功设置运行的节点用户！");
    }


    /**
     * 并行会签。
     * <pre>
     * 1.先获取人员总数量。
     * 2.如果现有的任务不包含选进来的人，那么需要删除任务，总数减1
     * 3.如果选择的任务的任务执行人，不再选择的人中，表示为新增，总数量加1，添加一个任务
     * 4.记录总数量
     * </pre>
     * @param bpmInst
     * @param nodeId
     * @param userIds
     * @return
     */
    private JsonResult handParallel(BpmInst bpmInst,String nodeId,String userIds){
        List<BpmTask> bpmTasks=bpmTaskService.getByActInstIdAndNodeId(bpmInst.getActInstId(),nodeId);
        if(BeanUtil.isEmpty(bpmInst)){
            return new JsonResult(false,"当前节点无任务！");
        }
        BpmTask paralletTask=bpmTasks.get(0);
        String actTaskId = paralletTask.getActTaskId();
        //1.获取会签的总数量。
        Integer loopCounts= (Integer) taskService.getVariable(actTaskId, BpmConst.LOOP_COUNTS);
        //2.删除任务
        for(BpmTask bpmTask:bpmTasks){
            if(!isContain(bpmTask.getAssignee(),userIds)){
                bpmTaskService.delByTaskId(bpmTask.getTaskId());
                loopCounts--;
                continue;
            }
        }
        String[] userIdArr=userIds.split("[,]");
        //3.新增任务
        for(String userId:userIdArr) {
            boolean isContains = false;
            for(BpmTask bpmTask:bpmTasks){
                if(userId.equals(bpmTask.getAssignee())){
                    isContains = true;
                    break;
                }
            }
            if(!isContains){
                paralletTask.setTaskId(IdGenerator.getIdStr());
                paralletTask.setAssignee(userId);
                paralletTask.setOwner(userId);
                bpmTaskService.insert(paralletTask);
                loopCounts++;
            }
        }
        //4.记录总数量
        taskService.setVariable(actTaskId,BpmConst.LOOP_COUNTS,loopCounts);
        return new JsonResult(true,"成功设置运行的节点用户！");
    }

    /**
     * 串行会签
     * <pre>
     *  1.获取总数
     *  2.获取会签人员(从变量中获取)
     * </pre>
     * @param bpmInst
     * @param nodeId
     * @param userIds
     * @return
     */
    private JsonResult handSequential(BpmInst bpmInst,String nodeId,String userIds){
        List<BpmTask> bpmTasks=bpmTaskService.getByActInstIdAndNodeId(bpmInst.getActInstId(),nodeId);
        if(BeanUtil.isEmpty(bpmTasks)){
            return new JsonResult(false,"当前节点任务已完成！");
        }

        if(BeanUtil.isEmpty(userIds)){
            return new JsonResult(false,"请选择用户！");
        }
        String[] aryUserId=userIds.split("[,]");

        BpmTask bpmTask=bpmTasks.get(0);
        String actTaskId=bpmTask.getActTaskId();

        String varName=BpmConst.SIGN_EXECUTOR_IDS+bpmTask.getKey();
        //1.从变量中,获取会签人员
        String signUserIds=(String)taskService.getVariable(actTaskId, varName);
        //2.获取保存的会签人员
        List<TaskExecutor> list = JSONArray.parseArray(signUserIds,TaskExecutor.class);
        //3.获取处理到第几个人
        Integer loopIndex= (Integer) taskService.getVariable(actTaskId,BpmConst.LOOP_INDEX);
        //4.计算已经完成审批的人

        List<TaskExecutor> completedExecutors=getCompleteExecutor(list,loopIndex);
        //5.计算人员总数
        Integer loopCounts=completedExecutors.size() + aryUserId.length;
        //6.取得执行人
        List<OsUserDto> users = orgService.getUsersByIds(userIds);
        //7.所有的执行人
        List<TaskExecutor> executors=new ArrayList<>();
        executors.addAll(completedExecutors);

        for(OsUserDto dto:users){
            executors.add(TaskExecutor.getUser(dto));
        }

        //8.更新执行人
        OsUserDto dto=users.get(0);
        if(!dto.getUserId().equals(bpmTask.getAssignee())){
            bpmTask.setAssignee(dto.getUserId());
            bpmTask.setOwner(dto.getUserId());
            bpmTaskService.update(bpmTask);
        }
        //保存执行用户。
        String varUsers= JSONArray.toJSONString(executors);
        taskService.setVariable(actTaskId,varName,varUsers);
        //设置总数
        taskService.setVariable(actTaskId,BpmConst.LOOP_COUNTS,loopCounts);

        return new JsonResult(true,"成功设置运行的节点用户！");
    }

    private boolean isContain(String userId,String userIds){
        if (StringUtils.isEmpty(userId)){
            return  false;
        }
        if(userIds.contains(userId)){
            return true;
        }
        return false;
    }


    /**
     * 取得完成的用户
     * @param list
     * @param loopIndex
     * @return
     */
    private List<TaskExecutor> getCompleteExecutor(List<TaskExecutor> list ,int loopIndex){
        List<TaskExecutor> rtnList=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            if(i<loopIndex){
                rtnList.add(list.get(i));
            }
        }
        return rtnList;
    }

}
