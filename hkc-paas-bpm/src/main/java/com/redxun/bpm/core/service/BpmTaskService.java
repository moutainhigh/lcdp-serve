package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.*;
import com.redxun.bpm.activiti.processhandler.ProcessHandlerExecutor;
import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.core.dto.NodeUsersDto;
import com.redxun.bpm.core.dto.TaskAddSign;
import com.redxun.bpm.core.dto.TaskLinkup;
import com.redxun.bpm.core.dto.TaskLinkupReply;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.ext.messagehandler.MessageUtil;
import com.redxun.bpm.core.mapper.BpmTaskMapper;
import com.redxun.bpm.core.mapper.BpmTaskUserMapper;
import com.redxun.bpm.core.service.impl.TaskHandlerContext;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.db.BaseDao;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.constant.ProcessStatus;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.service.impl.SuperServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.form.BpmView;
import com.redxun.dto.form.FormBoEntityDto;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.form.FormClient;
import com.redxun.log.model.Audit;
import com.redxun.log.model.LogContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* [流程任务]业务服务类
*/
@Service
public class BpmTaskService extends SuperServiceImpl<BpmTaskMapper,BpmTask> implements BaseService<BpmTask> {

    @Resource
    private BpmTaskMapper bpmTaskMapper;
    @Resource
    private BpmInstServiceImpl bpmInstService;
    @Resource
    private BpmTaskUserServiceImpl bpmTaskUserService;
    @Resource
    TaskService taskService;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Resource
    BpmRuPathServiceImpl bpmRuPathService;
    @Resource
    ActRepService actRepService;
    @Resource
    BpmInstLogServiceImpl bpmInstLogService;
    @Autowired
    BpmSignDataServiceImpl bpmSignDataService;
    @Resource
    FormDataService formDataService;
    @Resource
    BpmTaskSkipService bpmTaskSkipService;
    @Resource
    ProcessScriptEngine processScriptEngine;
    @Resource
    ProcessHandlerExecutor processHandlerExecutor;
    @Resource
    BpmTaskTransferServiceImpl bpmTaskTransferService;
    @Resource
    IOrgService orgService;
    @Resource
    private BpmTaskUserMapper bpmTaskUserMapper;
    @Resource
    RuntimeService runtimeService;
    @Resource
    TaskExecutorService taskExecutorService;
    @Autowired
    private FormClient formClient;
    @Autowired
    private BpmInstDataServiceImpl bpmInstDataService;
    @Resource
    BpmTemporaryOpinionServiceImpl bpmTemporaryOpinionService;
    @Resource
    BpmTransferServiceImpl bpmTransferService;
    @Resource
    BpmTransferLogServiceImpl bpmTransferLogService;
    @Resource
    BpmInstPermissionServiceImpl bpmInstPermissionService;

    @Override
    public BaseDao<BpmTask> getRepository() {
        return bpmTaskMapper;
    }

    /**
     * 创建Bpm任务
     * @param taskEntity
     * @param taskType 任务类型
     * @param hasExecutor 是否有执行人
     * @return
     */
    public BpmTask createBpmTask(TaskEntity taskEntity, String taskType,boolean hasExecutor){

        BpmDefService bpmDefService= SpringUtil.getBean(BpmDefService.class);
        BpmDef bpmDef=bpmDefService.getSimpleByActDefId(taskEntity.getProcessDefinitionId());

        IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();
        String busKey,instId,subject,billNo,billType;
        if(cmd.getTransientVars().containsKey(BpmConst.BPM_APPROVE_TASK) &&
                taskEntity.getExecutionId().equals(((BpmTask)cmd.getTransientVar(BpmConst.BPM_APPROVE_TASK)).getExecutionId())){
            BpmInst bpmInst=(BpmInst)cmd.getTransientVar(BpmConst.BPM_INST);
            busKey=bpmInst.getBusKey();
            instId=bpmInst.getInstId();
            subject=bpmInst.getSubject();
            billNo=bpmInst.getBillNo();
            billType=bpmInst.getBillType();
        }else {
            busKey=(String)taskEntity.getVariable(BpmInstVars.BUS_KEY.getKey());
            instId=(String)taskEntity.getVariable(BpmInstVars.INST_ID.getKey());
            subject=(String)taskEntity.getVariable(BpmInstVars.PROCESS_SUBJECT.getKey());
            billNo=(String)taskEntity.getVariable(BpmInstVars.BILL_NO.getKey());
            billType=(String)taskEntity.getVariable(BpmInstVars.BILL_TYPE.getKey());
        }

        //创建任务
        BpmTask bpmTask=new BpmTask();

        bpmTask.setTaskId(IdGenerator.getIdStr());
        bpmTask.setInstId(instId);
        bpmTask.setStatus(BpmTask.STATUS_UNHANDLE);
        bpmTask.setBusKey(busKey);
        bpmTask.setBillNo(billNo);
        bpmTask.setBillType(billType);
        bpmTask.setTreeId(bpmDef.getTreeId());
        bpmTask.setActTaskId(taskEntity.getId());
        bpmTask.setDefId(bpmDef.getDefId());
        bpmTask.setActDefId(taskEntity.getProcessDefinitionId());
        bpmTask.setKey(taskEntity.getTaskDefinitionKey());
        bpmTask.setName(taskEntity.getName());
        bpmTask.setSubject(subject);
        if(StringUtils.isNotEmpty(taskType)){
            bpmTask.setTaskType(taskType);
        }else {
            bpmTask.setTaskType(BpmTask.TYPE_FLOW_TASK);
        }
        bpmTask.setActInstId(taskEntity.getProcessInstanceId());
        bpmTask.setExecutionId(taskEntity.getExecutionId());
        //是否有执行人
        String excutor=hasExecutor?"1":"0";
        bpmTask.setExecutor(excutor);
        bpmTask.setTenantId(ContextUtil.getCurrentTenantId());

        insert(bpmTask);

        return  bpmTask;
    }

    /**
     * 移除任务的对应执行人
     * @param taskId
     */
    @GlobalTransactional
    public void doClearTaskUsers(String taskId){
        bpmTaskUserService.deleteByTaskId(taskId);
    }

    /**
     * 产生任务的执行人员
     * @param bpmTask
     * @param userIds
     */
    @Transactional
    public void doAssignTaskUsers(BpmTask bpmTask,String userIds){
        String[] uIds=userIds.split("[,]");
        for(String uId : uIds){
            BpmTaskUser bpmTaskUser=new BpmTaskUser();
            bpmTaskUser.setPartType(BpmTaskUser.PART_TYPE_CANDIDATE);
            bpmTaskUser.setUserId(uId);
            bpmTaskUser.setIsRead(BpmTaskUser.IS_UNREAD);
            bpmTaskUser.setInstId(bpmTask.getInstId());
            bpmTaskUser.setTaskId(bpmTask.getTaskId());
            bpmTaskUser.setUserType(TaskExecutor.TYPE_USER);
            bpmTaskUser.setId(IdGenerator.getIdStr());
            bpmTaskUserService.insert(bpmTaskUser);
        }
    }



    /**
     * 创建任务并授权
     * @param taskEntity
     * @param executors
     * @param instanceType  实例 类型， 单实例=normal,sequence,parallel
     */
    public void createTasksAndAssign(TaskEntity taskEntity,Set<TaskExecutor> executors,String instanceType){
        IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();
        boolean hasExecutor=BeanUtil.isNotEmpty(executors);
        if(TaskInstanceType.sequential.name().equals(instanceType) && hasExecutor){//顺序任务
            //先取第一个
            TaskExecutor executor =executors.iterator().next();
            BpmTask bpmTask= createBpmTask(taskEntity,BpmTask.TYPE_SEQUENTIAL_TASK,true);
            bpmTaskUserService.createUser(bpmTask,executor);

            Map<String,Object> vars=new HashMap<>();
            vars.put(BpmConst.LOOP_COUNTS,executors.size());
            vars.put(BpmConst.LOOP_INDEX,0);
            vars.put(BpmConst.COMPLETE_COUNTS,0);

            String varName=BpmConst.SIGN_EXECUTOR_IDS + taskEntity.getTaskDefinitionKey();
            vars.put(varName,JSONArray.toJSONString(executors));

            //发送消息发送人处理。
            Set<TaskExecutor> taskExecutors=new LinkedHashSet<>();
            taskExecutors.add(executor);

            taskEntity.setVariablesLocal(vars);
            //添加任务的线程变量
            //设置任务执行人。
            bpmTask.setTaskExecutors(taskExecutors);

            cmd.addTask(bpmTask);

        }else if(TaskInstanceType.parallel.name().equals(instanceType) && hasExecutor){//多实例并行流程

            for(TaskExecutor executor:executors){
                BpmTask bpmTask= createBpmTask(taskEntity,BpmTask.TYPE_PARALLEL_TASK,true);
                bpmTaskUserService.createUser(bpmTask,executor);

                Set<TaskExecutor> taskExecutors=new LinkedHashSet<>();
                taskExecutors.add(executor);
                bpmTask.setTaskExecutors(taskExecutors);

                cmd.addTask(bpmTask);
            }

            taskEntity.setVariableLocal(BpmConst.LOOP_COUNTS,executors.size());
            taskEntity.setVariableLocal(BpmConst.LOOP_INDEX,0);

        }else{
           //普通任务
           BpmTask bpmTask= createBpmTask(taskEntity,BpmTask.TYPE_FLOW_TASK,hasExecutor);
           bpmTaskUserService.createUsers(bpmTask,executors);
           //设置执行人
           bpmTask.setTaskExecutors(executors);

           //将任务添加到CMD上下文。
           cmd.addTask(bpmTask);
           cmd.addTransientVar(BpmConst.BPM_APPROVE_TASK,bpmTask);

        }
    }

    public void  createTasksAndAssign(TaskEntity taskEntity,TaskExecutor executor){
        BpmTask bpmTask= createBpmTask(taskEntity,BpmTask.TYPE_FLOW_TASK,true);
        bpmTaskUserService.createUser(bpmTask,executor);

        //将人员数据进行传递。
        IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();

        Set<TaskExecutor> executorSet=new HashSet<>();
        executorSet.add(executor);

        bpmTask.setTaskExecutors(executorSet);

        cmd.addTask(bpmTask);

    }

    /**
     * 递归获取任务。
     * @param taskId
     * @param list
     */
    public void getTaskByTaskId(String taskId,List<BpmTask> list){
        getTaskByTaskId(taskId,"",list);
    }

    /**
     * 递归获取任务。
     * @param taskId
     * @param list
     */
    public void getTaskByTaskId(String taskId,String taskType,List<BpmTask> list){
        QueryWrapper query=new QueryWrapper();
        query.eq("PARENT_ID_",taskId);
        if(StringUtils.isNotEmpty(taskType)){
            query.eq("TASK_TYPE_",taskType);
        }

        List<BpmTask> tasks= bpmTaskMapper.selectList(query);
        if(BeanUtil.isEmpty(tasks)){
            return ;
        }
        list.addAll(tasks);
        for(BpmTask task:tasks){
            getTaskByTaskId(task.getTaskId(),taskType,list);
        }
    }





    /**
     * 根据任务ID删除任务，同时删除任务执行人。
     * @param taskId
     */
    public void  delByTaskId(String taskId){
        bpmTaskUserService.deleteByTaskId(taskId);
        delete(taskId);
    }

    /**
     * 根据流程实例ID查询任务。
     * @param instId
     * @return
     */
    public List<BpmTask> getByInstId(String instId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("INST_ID_",instId);
        return bpmTaskMapper.selectList(queryWrapper);
    }

    /**
    * @Description:  获取某流程实例最新的一条待办任务
    * @param instId 流程实例 ID
    * @Author: Elwin ZHANG  @Date: 2021/12/10 14:12
    **/
    public BpmTask getNewTaskByInstId(String instId ){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("INST_ID_",instId);
        queryWrapper.orderByDesc("CREATE_TIME_");
        List<BpmTask>  list= bpmTaskMapper.selectList(queryWrapper);
        if(list==null || list.size()==0){
            return null;
        }
        return list.get(0);
    }
    /**
     * 根据流程引擎ID查询任务。
     * @param actInstId
     * @return
     */
    public List<BpmTask> getByActInstId(String actInstId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ACT_INST_ID_",actInstId);
        return bpmTaskMapper.selectList(queryWrapper);
    }

    /**
     * 根据流程引擎以及任务ID查询任务。
     * @param actInstId
     * @return
     */
    public List<BpmTask> getByActInstIdAndNodeId(String actInstId,String nodeId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ACT_INST_ID_",actInstId);
        queryWrapper.eq("KEY_",nodeId);
        return bpmTaskMapper.selectList(queryWrapper);
    }

    /**
     * 回复沟通任务
     * <pre>
     *      1.如果沟通任务再有沟通任务，都标记为完成。
     *      2.增加沟通意见。
     * </pre>
     * @param reply
     * @return
     */
    @Transactional
    public JsonResult doCompleteLinkupTask(TaskLinkupReply reply){
        BpmCheckHistory history= bpmCheckHistoryService.getByTaskIdCheckType(reply.getTaskId(),TaskOptionType.REPLY_COMMUNICATE.name());
        if(history!=null){
            return JsonResult.Fail("沟通任务已回复!");
        }
        BpmTask bpmTask=get(reply.getTaskId());

        LogContext.put(Audit.DETAIL,"回复沟通任务:" +bpmTask.getSubject());

        List<BpmTask> taskList=new ArrayList<>();
        getTaskByTaskId(bpmTask.getTaskId(),BpmTask.TYPE_LINKUP_TASK,taskList);

        for(BpmTask task:taskList){
            task.setStatus(BpmTask.STATUS_COMPLETED);
            bpmTaskMapper.updateById(task);
        }
        bpmTask.setStatus(BpmTask.STATUS_COMPLETED);
        bpmTaskMapper.updateById(bpmTask);

        //增加审批记录
        bpmCheckHistoryService.createHistory(bpmTask, TaskOptionType.REPLY_COMMUNICATE.name(),"", reply.getOpinion(),reply.getOpFiles(),"","");

        //发送通知
        if(StringUtils.isNotEmpty(reply.getMsgTypes())){
            Map<String,Object> vars=new HashMap<>();
            vars.put("taskId",bpmTask.getParentId());
            BpmTask parentTask=get(bpmTask.getParentId());
            IUser user=ContextUtil.getCurrentUser();
            OsUserDto sender=orgService.getUserById(user.getUserId());
            Set<TaskExecutor> executors=bpmTaskUserService.getTaskExecutors(parentTask);
            List<TaskExecutor> list=new ArrayList<>();
            list.addAll(executors);
            List<OsUserDto> users= orgService.getUsersByTaskExecutor(list);
            MessageUtil.sendMessage(sender,bpmTask.getSubject(),reply.getMsgTypes(),"replycommu",users,vars);
        }

        return JsonResult.Success("成功进行了沟通回复!");
    }

    /**
     * 撤回沟通任务。
     * <pre>
     *  1.递归获取沟通任务。
     *  2.删除这些沟通任务。
     * </pre>
     * @param taskId
     */
    @Transactional
    public void doRevokeLinkupTask(String taskId,boolean delOpinion){
        List<BpmTask> list=new ArrayList<>();

        BpmTask bpmTask=bpmTaskMapper.selectById(taskId);

        LogContext.put(Audit.DETAIL,"撤回沟通任务，当前任务为:" + bpmTask.getSubject() +",流程实例ID为:" + bpmTask.getInstId());

        getTaskByTaskId(taskId,BpmTask.TYPE_LINKUP_TASK,list);

        for(BpmTask task : list){
            if(delOpinion){
                bpmCheckHistoryService.removeByTaskId(task.getTaskId());
            }
            delByTaskId(task.getTaskId());
            OsUserDto osUserDto = orgService.getUserById(task.getAssignee());
            String userName=osUserDto.getFullName() + "(" + osUserDto.getUserId() + ")";
            bpmInstLogService.addInstLog(bpmTask.getInstId(),"取消沟通："+userName );
        }

    }

    /**
     * 取消任务流转
     * @param taskId
     */
    @Transactional
    public void doCancelTransRoamTask(String taskId){

        List<BpmTask> tasks=getByParentId(taskId);
        for(BpmTask task:tasks) {
            String subTaskId=task.getTaskId();
            BpmTask bpmTask=bpmTaskMapper.selectById(subTaskId);
            doCancelTransRoamTask(subTaskId);
            bpmCheckHistoryService.removeByTaskId(subTaskId);
            delByTaskId(subTaskId);
            OsUserDto osUserDto = orgService.getUserById(bpmTask.getAssignee());
            String userName=osUserDto.getFullName() + "(" + osUserDto.getUserId() + ")";
            bpmInstLogService.addInstLog(bpmTask.getInstId(),"取消流转：" + userName );
        }
        bpmTransferService.delByTaskId(taskId);
        bpmTransferLogService.delByTaskId(taskId);
        bpmCheckHistoryService.removeByTaskId(taskId);
    }

    public List<BpmTask> getByParentId(String taskId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("PARENT_ID_",taskId);
        return bpmTaskMapper.selectList(queryWrapper);
    }

    /**
     * 按用户Id查询
     * @param start
     * @param end
     * @param userId
     * @param groupIds
     * @param tenantId
     * @return
     */
    public List<BpmTask>  getByStartBetweenEnd(String start,String end,String userId,List<String> groupIds,String tenantId){


        Date  startDate= DateUtils.parseDate(start,"yyyy-MM-dd HH:mm:ss");
        Date  endDate= DateUtils.parseDate(end,"yyyy-MM-dd HH:mm:ss");

        Map<String, Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("startTime",startDate);
        params.put("endTime",endDate);
        params.put("tenantId",tenantId);
        return bpmTaskMapper.getByStartBetweenEnd(params,groupIds);
    }

    /**
     * 按用户Id查询
     * @param userId
     * @param groupIds
     * @param filter
     * @return
     */
    public IPage<BpmTask> getByUserId(String userId, List<String> groupIds,String tenantId, QueryFilter filter){
        return bpmTaskMapper.getByUserId(filter.getPage(),filter.getSearchParams(),userId,groupIds,tenantId);
    }

    /**
     * 按用户Id，流程定义ID查询
     * @param userId
     * @param groupIds
     * @param defKey
     * @param filter
     * @return
     */
    public IPage<BpmTask> getByUserIdDefKey(String userId, List<String> groupIds,String defKey, QueryFilter filter){
        return bpmTaskMapper.getByUserIdDefKey(filter.getPage(),filter.getSearchParams(),userId,groupIds,defKey);
    }


    private JsonResult checkTask(BpmTask bpmTask,BpmInst bpmInst){
        JsonResult result=JsonResult.Success();

        if(ProcessStatus.PENDING.name().equals(bpmInst.getStatus())){
            result.setSuccess(false);
            result.setMessage("流程实例已经挂起，不允许进行审批！");
           return result;
        }

        if(BpmTask.STATUS_LOCKED.equals( bpmTask.getStatus())){
            result.setSuccess(false);
            result.setMessage("任务被驳回处于锁定状态，不能进行审批！");
            IUser user= orgService.getUserById(bpmInst.getLockedBy());
            result.setData("请联系【"+user.getFullName()+"】尽快审批!");
            return result;
        }

        //若为沟通任务，则完成沟通任务即可。
        if(BpmTask.TYPE_LINKUP_TASK.equals(bpmTask.getTaskType())){
            result.setSuccess(false);
            result.setMessage("沟通任务不允许调用本接口！");
            return result;
        }

        return result;


    }

    /*
     * 完成任务处理、撤回，回退任务处理入口
     * @param cmd
     * @return
     */
    @GlobalTransactional
    public JsonResult completeTask(ProcessNextCmd cmd)   {
        return completeTask(cmd,false);
    }

    @GlobalTransactional
    public JsonResult completeTask(ProcessNextCmd cmd,boolean skip)   {

        JsonResult result= JsonResult.Success();
        BpmTask bpmTask=get(cmd.getTaskId());
        if(BeanUtil.isEmpty(bpmTask)){
            result= JsonResult.Fail("此任务已经审批!");
            return result;
        }

        JsonResult authResult=bpmInstService.calTaskAuth(bpmTask,ContextUtil.getCurrentUserId());
        if(!skip && !authResult.isSuccess()){
            return authResult;
        }

        String detail="审批任务："+ bpmTask.getSubject() +",意见为:" + cmd.getCheckType();
        LogContext.put(Audit.DETAIL,detail);

        BpmInst bpmInst=bpmInstService.getById(bpmTask.getInstId());
        String status=bpmInst.getStatus();
        if(BpmInstStatus.SUPSPEND.name().equals( status)){
            return JsonResult.Fail("流程已暂停,请联系管理员!");
        }

        result=checkTask(bpmTask,bpmInst);
        if(!result.isSuccess()){
            return result;
        }

        //传入，为后续的任务执行直接获取该定义，而不用进行转换获取。
        cmd.setDefId(bpmTask.getDefId());
        //放置线程变量，在后续可获取到
        cmd.setInstId(bpmTask.getInstId());
        //设置上一节点ID
        cmd.setPreNodeId(bpmTask.getKey());

        //加上审批的流程变更
        ProcessConfig processConfig=bpmDefService.getProcessConfig(bpmTask.getActDefId());
        UserTaskConfig userTaskConfig=(UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        cmd.addTransientVar(BpmConst.PROCESS_CONFIG,processConfig);
        cmd.addTransientVar(BpmConst.USERTASK_CONFIG,userTaskConfig);
        cmd.addTransientVar(BpmConst.BPM_INST,bpmInst);
        cmd.addTransientVar(BpmConst.BPM_APPROVE_TASK,bpmTask);


        //是否允许审批
        JsonResult jsonResult = bpmInstService.getAllowApprove(userTaskConfig,cmd.getFormData(),cmd.getVars());
        if(!jsonResult.isSuccess()){
            return jsonResult;
        }

        //跳过不处理数据。
        if(!TaskOptionType.SKIP.name().equals(cmd.getCheckType()) &&cmd.getSystemHand()){
            //处理表单数据
            formDataService.handFormData(cmd,userTaskConfig.getDataSetting(),"approve");
        }

        //处理流程变量
        bpmInstService.handVars(cmd,bpmInst,processConfig,false);
        //处理任务跳转规则。
        String  targetNodeId= handJumpRules(bpmTask, userTaskConfig);
        if(StringUtils.isNotEmpty(targetNodeId) && StringUtils.isEmpty(cmd.getDestNodeId())){
            cmd.setDestNodeId(targetNodeId);
        }

        //处理fieldJson。
        TaskFieldConfig fieldConfig= processConfig.getTaskFields();
        //处理JSON字段。
        bpmInstService.handInstJson(fieldConfig,bpmInst,cmd);
        //处理任务前置数据
        processHandlerExecutor.handTaskBeforeHandler(userTaskConfig,bpmTask,bpmInst.getBusKey());

        //创建审批历史。
        bpmCheckHistoryService.createHistory(bpmTask, cmd.getCheckType(),cmd.getOpinionName(), cmd.getOpinion(),cmd.getOpFiles(),cmd.getRelInsts(),"");

        //若为回退，包括 上一步、撤回、驳回发起人等
        ITaskHandler taskHandler= TaskHandlerContext.getJumpType(cmd.getCheckType());
        taskHandler.handTask(bpmTask,cmd,userTaskConfig);


        //任务完成处理器。
        processHandlerExecutor.handTaskAfterHandler(userTaskConfig,bpmTask.getKey(),bpmInst.getBusKey());

        //处理任务跳过。
        bpmTaskSkipService.handSkipTask(cmd);

        //加入流程实例权限表
        bpmInstPermissionService.createTaskInfo(bpmInst,cmd);

        //添加审批任务日志
        bpmInstLogService.addTaskLog(bpmTask.getInstId(),bpmTask.getTaskId(),bpmTask.getName(),bpmTask.getKey(),"审批任务");

        //删除暂存的意见
        bpmTemporaryOpinionService.delByTaskId(bpmTask.getTaskId());

        result.setMessage("成功完成处理任务！");

        return result;
    }



    /**
     * 处理跳转规则。
     * @param bpmTask
     * @param taskConfig
     * @return
     */
    private String handJumpRules(BpmTask bpmTask, UserTaskConfig taskConfig ){
        Map<String, Object> vars = runtimeService.getVariables(bpmTask.getActInstId());
        List<JumpRuleConfig> jumpRules = taskConfig.getJumpRules();
        if(BeanUtil.isEmpty(jumpRules)){
            return "";
        }
        Map<String,Object> params= ActivitiUtil.getConextData(vars);
        for(JumpRuleConfig rule:jumpRules){
            String script=rule.getScriptConfig();
            Object obj= processScriptEngine.exeScript(script,params);
            if(obj instanceof  Boolean && (Boolean)obj){
                String nodeId= rule.getDestNodeId();
                return nodeId;
            }
        }
        return "";
    }


    /**
     * 保存表单数据。
     * @param cmd
     */
    @GlobalTransactional
    public void saveData(ProcessNextCmd cmd){
        BpmTask bpmTask=get(cmd.getTaskId());
        UserTaskConfig userTaskConfig=(UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());

        boolean systemHand =cmd.getSystemHand();
        if(systemHand){
            formDataService.handFormData(cmd,userTaskConfig.getDataSetting(),"save");
        }else {
            //获取流程组配置
            ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmTask.getActDefId());
            BpmInst bpmInst =bpmInstService.get(bpmTask.getInstId());
            //在流程启动完成时处理后置处理器。
            processHandlerExecutor.handTaskAfterHandler(userTaskConfig,cmd.getDestNodeId(),bpmInst.getBusKey());
        }
    }

    /**
     * 获取待办用户数
     * @param userId  用户Id
     * @param groupIds 用户所属的用户组Id
     * @param tenantId 机构ID
     * @return
     */
    public Integer getCountsByUserId(String userId,List<String> groupIds,String tenantId){
        return bpmTaskMapper.getCountsByUserId(userId,groupIds,tenantId);
    }

    /**
     * 更新状态
     * @param taskId
     * @param status
     * @param assignee
     */
    public void updateStatus(String taskId,String status,String assignee){
        bpmTaskMapper.updateStatus(taskId,status,assignee);
    }

    /**
     * 按act任务Id删除记录
     * <pre>
     *     根据ACTTASKID删除流程任务和任务的用户。
     * </pre>
     * @param actTaskId
     */
    public void delByActTaskId(String actTaskId){
        //根据actTaskId 获取任务。
        List<BpmTask> list=getByActTaskId(actTaskId);

        if(BeanUtil.isEmpty(list)){
            return;
        }
        //删除关联用户。
        List<String> taskIds=list.stream().map(BpmTask::getTaskId).collect(Collectors.toList());
        QueryWrapper delWrapper=new QueryWrapper();
        delWrapper.in("TASK_ID_",taskIds);
        bpmTaskUserMapper.delete(delWrapper);
        //删除ACT_TASK_ID_关联的任务。
        bpmTaskMapper.deleteByActTaskId(actTaskId);
    }




    /**
     * 增加任务会签数据
     * @param taskAddSign
     */
    @Transactional
    public void addTaskData(TaskAddSign taskAddSign){
        BpmTask bpmTask=getById(taskAddSign.getTaskId());
        String signType=bpmTask.getTaskType();
        /**
         * 判断多实例任务类型。
         */
        boolean isParallel=BpmTask.TYPE_PARALLEL_TASK.equals(signType);
        String[] userIds=taskAddSign.getToUserIds().split("[,]");

        Integer loopCounts=(Integer) taskService.getVariable(bpmTask.getActTaskId(),BpmConst.LOOP_COUNTS);
        if(loopCounts==null){
            loopCounts=0;
        }

        StringBuilder detail=new StringBuilder( "对任务:"+bpmTask.getSubject() +"进行加签,会签任务类型为:"+(isParallel?"并行会签":"串行会签") +"加签人员为:");

        List<OsUserDto> userList = orgService.getUsersByIds(taskAddSign.getToUserIds());
        for(OsUserDto userDto:userList){
            detail.append(userDto.getFullName() +"("+userDto.getUserId()+")");
        }

        String varName=BpmConst.SIGN_EXECUTOR_IDS + bpmTask.getKey();
        String users=(String)taskService.getVariable(bpmTask.getActTaskId(),varName);
        List<TaskExecutor> list=new ArrayList<>();
        if(StringUtils.isNotEmpty(users)){
            list=JSONArray.parseArray(users,TaskExecutor.class);
        }
        for(String userId:userIds){

            if(isParallel) {
                BpmTask newTask = new BpmTask();
                try {
                    BeanUtil.copyNotNullProperties(newTask, bpmTask);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                newTask.setTaskId(IdGenerator.getIdStr());
                newTask.setAssignee(userId);
                newTask.setOwner(userId);
                newTask.setTenantId(ContextUtil.getCurrentTenantId());
                insert(newTask);
            }else{
                OsUserDto userDto=orgService.getUserById(userId);
                list.add(TaskExecutor.getUser(userDto));
            }
        }

        if(!isParallel) {
            taskService.setVariable(bpmTask.getActTaskId(), varName,JSONArray.toJSONString(list));
        }
        //加入会签总数
        taskService.setVariable(bpmTask.getActTaskId(),BpmConst.LOOP_COUNTS,loopCounts+userIds.length);

        LogContext.put(Audit.DETAIL,detail);
    }
    /**
     * 任务沟通
     * <pre>
     *     1.产生沟通任务。
     *     2.给沟通人发送消息。
     *     3.记录沟通信息。
     * </pre>
     * @param taskLinkup
     */
     @Transactional
    public void taskLinkups(TaskLinkup taskLinkup) {
        StringBuilder sb=new StringBuilder();
        BpmTask bpmTask = getById(taskLinkup.getTaskId());
         BpmInst bpmInst = bpmInstService.getById(bpmTask.getInstId());

        sb.append("将任务("+ bpmTask.getSubject() + ")流程实例ID("+bpmTask.getInstId()+")沟通给:" );

        String[] userAccounts=taskLinkup.getToUserAccounts().split("[,]");
        IUser curUser=ContextUtil.getCurrentUser();
        OsUserDto sender=orgService.getUserById(curUser.getUserId());
        List<String> userIds=new ArrayList<>();
         ArrayList <String>contactUsers=new ArrayList(); //记录沟通用户的姓名
        for(String userAccount : userAccounts){
            OsUserDto user = orgService.getUserById(userAccount);
            sb.append(user.getFullName() +"(" + user.getAccount() +")");
            userIds.add(user.getUserId());
            BpmTask newTask=new BpmTask();
            try {
                BeanUtil.copyNotNullProperties(newTask, bpmTask);
            }catch (Exception ex){
                ex.printStackTrace();
            }
            newTask.setStatus(BpmTask.STATUS_UNHANDLE);
            newTask.setTaskId(IdGenerator.getIdStr());
            newTask.setAssignee(user.getUserId());
            newTask.setOwner(user.getUserId());
            newTask.setSubject(bpmTask.getSubject());
            newTask.setTaskType(BpmTask.TYPE_LINKUP_TASK);
            newTask.setTenantId(ContextUtil.getCurrentTenantId());
            newTask.setParentId(bpmTask.getTaskId());
            newTask.setCreateTime(new Date());
            newTask.setUpdateTime(new Date());
            insert(newTask);

            contactUsers.add(user.getFullName() + "(" + user.getUserId() + ")");
            //发送通知消息。
            if(StringUtils.isNotEmpty(taskLinkup.getMsgTypes())){
                Map<String,Object> vars=new HashMap<>();
                vars.put("taskId",newTask.getTaskId());
                vars.put("instId",newTask.getInstId());
                vars.put("opinion",taskLinkup.getOpinion());

                MessageUtil.sendMessage(sender,bpmTask.getSubject(),taskLinkup.getMsgTypes(),
                        "commu",user,vars);
            }
            bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER,user,"", bpmTask.getTaskId());
            //原来沟通历史里写的是旧的ID，没办法删除；改为存新ID
            bpmCheckHistoryService.createLinkupHistory(newTask,ContextUtil.getCurrentUserId(),taskLinkup.getOpinion(),taskLinkup.getOpFiles(),user.getUserId());
        }
         String toUserIds = StringUtils.join(userIds, ",");

        bpmInstLogService.addInstLog(bpmTask.getInstId(),"沟通用户：" + StringUtils.join(contactUsers,",") );
    }


    /**
     * 取得某任务发起的所有沟通任务列表
     * @param taskId
     * @return
     */
    public List<BpmTask>  getCommuByTaskId(String taskId,boolean hasHistory){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("PARENT_ID_",taskId);
        wrapper.eq("TASK_TYPE_",BpmTask.TYPE_LINKUP_TASK);
        List<BpmTask> list = bpmTaskMapper.selectList(wrapper);
        if(BeanUtil.isEmpty(list)){
            return list;
        }
        if(hasHistory){
            List<String> taskIds=list.stream().filter(p->p.getStatus().equals(BpmTask.STATUS_COMPLETED))
                    .map(p->p.getTaskId()).collect(Collectors.toList());
            if(BeanUtil.isEmpty(taskIds)){
                return list;
            }
            List<BpmCheckHistory> historyList= bpmCheckHistoryService.getByTaskIds(taskIds);
            Map<String,BpmCheckHistory> taskMap=historyList.stream().filter(item->TaskOptionType.REPLY_COMMUNICATE.name().equals(item.getCheckStatus())).collect(Collectors.toMap(p->p.getTaskId(), p -> p));
            for(BpmTask task:list){
                if(taskMap.containsKey(task.getTaskId())){
                    task.setBpmCheckHistory(taskMap.get(task.getTaskId()));
                }
            }
        }

        return  list;
    }
    /**
    * @Description:  流转任务执行后，增加返回记录ID
    * @param task 原任务
     *@param  userId 转给用户
    **/
    public BpmTask createTransRoamTask(BpmTask task,String userId){
        //创建任务
        BpmTask bpmTask=new BpmTask();
        BeanUtil.copyProperties(bpmTask,task);
        bpmTask.setTaskId(IdGenerator.getIdStr());
        bpmTask.setStatus(BpmTask.STATUS_UNHANDLE);
        bpmTask.setTaskType(BpmTask.TYPE_TRANSFER_TASK);
        bpmTask.setAssignee(userId);
        bpmTask.setParentId(task.getTaskId());
        bpmTask.setCreateTime(new Date());
        bpmTask.setTenantId(ContextUtil.getCurrentTenantId());
        insert(bpmTask);
        createBpmTransferLog(userId,task.getTaskId(),bpmTask.getTaskId());
        return  bpmTask;
    }

    @Transactional
    public JsonResult  doTransRoamTask(JSONObject json){

        StringBuilder sb=new StringBuilder();

        sb.append("将任务：");

        JsonResult result=JsonResult.Success("任务被成功流转!");

        String opinion=json.getString("opinion");
        JSONObject toUser = json.getJSONObject("toUser");
        String msgType=json.getString("msgType");
        String taskId=json.getString("taskId");
        String transferType=json.getString("transferType");
        String approveType=json.getString("approveType");
        Integer completeType=json.getInteger("completeType");
        String completeJudgeType=json.getString("completeJudgeType");
        Integer completeSetting=json.getInteger("completeSetting");

        String userIdAry=toUser.getString("value");
        IUser curUser=ContextUtil.getCurrentUser();
        BpmTask bpmTask=bpmTaskMapper.selectById(taskId);
        BpmInst bpmInst=bpmInstService.getById(bpmTask.getInstId());

        sb.append(bpmTask.getSubject());

        sb.append("进行流转，流程实例ID为:" + bpmTask.getInstId());

        if(StringUtils.isEmpty(userIdAry)){
            return JsonResult.Fail("流转人员为空！");
        }
        String[] userIds=userIdAry.split(",");
        if(ArrayUtils.contains(userIds,curUser.getUserId())){
            return JsonResult.Fail("不能流转给自己！");
        }
        ArrayList <String>transUsers=new ArrayList(); //记录流转用户的姓名
        if("parallel".equals(approveType)) {
            //并行
            for (int i = 0; i < userIds.length; i++) {
                //任务流转
                String userId = userIds[i];
                BpmTask newTask=createTransRoamTask(bpmTask, userId);
                //发送消息。
                Map<String, Object> vars = new HashMap<>();
                vars.put("taskId", newTask.getTaskId());
                OsUserDto receiver = orgService.getUserById(userId);
                vars.put("opinion", opinion);
                vars.put("instId",bpmTask.getInstId());
                OsUserDto sender = orgService.getUserById(curUser.getUserId());
                //逐个写流程审批历史
                writeCheckHistory(newTask,opinion);
                transUsers.add(receiver.getFullName() + "(" + receiver.getUserId() + ")");
                //发送消息
                MessageUtil.sendMessage(sender, bpmTask.getSubject(), msgType, "transferRoam", receiver, vars);
                //增加权限。
                bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER, bpmTask.getTaskId(), receiver);
            }
        }else{
            //串行
            BpmTask newTask=createTransRoamTask(bpmTask, userIds[0]);
            //发送消息。
            Map<String, Object> vars = new HashMap<>();
            vars.put("taskId", newTask.getTaskId());
            OsUserDto receiver = orgService.getUserById(userIds[0]);
            vars.put("opinion", opinion);
            vars.put("instId",bpmTask.getInstId());
            OsUserDto sender = orgService.getUserById(curUser.getUserId());
            //逐个写流程审批历史
            writeCheckHistory(newTask,opinion);
            transUsers.add(receiver.getFullName() + "(" + receiver.getUserId() + ")");
            //发送消息
            MessageUtil.sendMessage(sender, bpmTask.getSubject(), msgType, "transferRoam", receiver, vars);
            //增加权限。
            bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER, bpmTask.getTaskId(), receiver);
        }
        bpmTransferLogService.updateStatusByTransTaskId(taskId,opinion,BpmTask.STATUS_HANDLE);

        //添加流转信息。
        BpmTransfer transfer=new BpmTransfer();
        transfer.setTaskId(taskId);
        transfer.setInstId(bpmTask.getInstId());
        transfer.setCount(userIds.length);
        transfer.setApproveType(approveType);
        transfer.setTransferType(transferType);
        transfer.setCompleteType(completeType);
        transfer.setCompleteJudgeType(completeJudgeType);
        transfer.setNoticeType(msgType);
        transfer.setCompleteSetting(completeSetting);
        transfer.setCompleteCount(0);
        transfer.setTaskUserIdx(0);
        transfer.setTaskUserId(userIdAry);
        bpmTransferService.insert(transfer);
        //记录日志
        LogContext.put(Audit.DETAIL,sb.toString());
        bpmInstLogService.addInstLog(bpmTask.getInstId(),"流转任务："+StringUtils.join(transUsers,",") );
        return result;
    }
    /**
    * @Description:  写流转历史；原来是多人共用一条历史，会导致无法删除
    * @param bpmTask 流转任务
    * @Author: Elwin ZHANG  @Date: 2021/12/6 15:53
    **/
    private  void  writeCheckHistory(BpmTask bpmTask,String opinion){
        //产生流转审批历史
        BpmCheckHistory bpmCheckHistory=new BpmCheckHistory();
        bpmCheckHistory.setActDefId(bpmTask.getActDefId());
        bpmCheckHistory.setInstId(bpmTask.getInstId());
        bpmCheckHistory.setTreeId(bpmTask.getTreeId());
        bpmCheckHistory.setSubject(bpmTask.getSubject());
        bpmCheckHistory.setNodeId(bpmTask.getKey());
        bpmCheckHistory.setNodeName(bpmTask.getName());
        bpmCheckHistory.setTaskId(bpmTask.getTaskId());
        bpmCheckHistory.setJumpType(TaskOptionType.ROAM_TRANSFER.name());
        bpmCheckHistory.setCheckStatus(TaskOptionType.ROAM_TRANSFER.name());
        bpmCheckHistory.setRemark(opinion);
        bpmCheckHistory.setCompleteTime(new Date());
        IUser curUser=ContextUtil.getCurrentUser();
        bpmCheckHistory.setOwnerId(curUser.getUserId());
        bpmCheckHistory.setHandlerId(curUser.getUserId());
        bpmCheckHistory.setHandleDepId(curUser.getDeptId());
        bpmCheckHistory.setLinkUpUserIds(bpmTask.getAssignee());
        bpmCheckHistoryService.insert(bpmCheckHistory);
    }

    private void createBpmTransferLog(String userId,String taskId,String transTaskId) {
        BpmTransferLog log = new BpmTransferLog();
        log.setAssignee(userId);
        log.setTaskId(taskId);
        log.setTransTaskId(transTaskId);
        log.setStatus(BpmTask.STATUS_UNHANDLE);
        bpmTransferLogService.insert(log);
    }

    @Transactional
    public JsonResult  doTransTask(JSONObject json){

        StringBuilder sb=new StringBuilder();

        JsonResult result=JsonResult.Success("任务被成功转办!");

        String opinion=json.getString("opinion");
        JSONObject toUser = json.getJSONObject("toUser");
        String msgType=json.getString("msgType");
        String taskId=json.getString("taskId");

        String userId=toUser.getString("value");
        IUser curUser=ContextUtil.getCurrentUser();

        //任务转移
        BpmTask bpmTask=bpmTaskMapper.selectById(taskId);
        BpmInst bpmInst=bpmInstService.getById(bpmTask.getInstId());

        sb.append("将任务:" + bpmTask.getSubject() +",转交给:" + toUser.get("label") +"办理!") ;

        if(!curUser.getUserId().equals( bpmTask.getAssignee())){
            result.setSuccess(false);
            result.setMessage("你不是任务处理人不能进行转办操作!");
            return result;
        }
        bpmTask.setAssignee(userId);
        bpmTaskMapper.updateById(bpmTask);

        //产生转办审批历史
        BpmCheckHistory bpmCheckHistory=new BpmCheckHistory();
        bpmCheckHistory.setActDefId(bpmTask.getActDefId());
        bpmCheckHistory.setInstId(bpmTask.getInstId());
        bpmCheckHistory.setTreeId(bpmTask.getTreeId());
        bpmCheckHistory.setSubject(bpmTask.getSubject());
        bpmCheckHistory.setNodeId(bpmTask.getKey());
        bpmCheckHistory.setNodeName(bpmTask.getName());
        bpmCheckHistory.setTaskId(bpmTask.getTaskId());
        bpmCheckHistory.setJumpType(TaskOptionType.TRANSFER.name());
        bpmCheckHistory.setCheckStatus(TaskOptionType.TRANSFER.name());
        bpmCheckHistory.setRemark(opinion);
        bpmCheckHistory.setCompleteTime(new Date());
        bpmCheckHistory.setOwnerId(curUser.getUserId());
        bpmCheckHistory.setHandlerId(userId);
        bpmCheckHistoryService.insert(bpmCheckHistory);

        //添加转移信息。
        BpmTaskTransfer transfer=new BpmTaskTransfer();
        transfer.setId(IdGenerator.getIdStr());
        transfer.setTaskId(taskId);
        transfer.setType(BpmTaskTransfer.TYPE_TRANSFER);
        transfer.setOwnerId(ContextUtil.getCurrentUserId());
        transfer.setToUserId(userId);
        transfer.setSubject(bpmTask.getSubject());
        transfer.setInstId(bpmTask.getInstId());
        transfer.setTreeId(bpmTask.getTreeId());
        bpmTaskTransferService.insert(transfer);

        //发送消息。
        Map<String,Object> vars=new HashMap<>();
        vars.put("taskId",bpmTask.getTaskId());
        vars.put("instId",bpmTask.getInstId());
        OsUserDto receiver=orgService.getUserById(userId);
        vars.put("opinion",opinion);
        OsUserDto sender=orgService.getUserById(curUser.getUserId());

        MessageUtil.sendMessage(sender,bpmTask.getSubject(),msgType,"transfer",receiver,vars);
        //增加权限。
        bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER, bpmTask.getTaskId(), receiver);
        return result;
    }

    /**
     * 根据actTaskId 获取任务。
     * @param actTaskId
     * @return
     */
    private List<BpmTask> getByActTaskId(String actTaskId){
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("ACT_TASK_ID_",actTaskId);
        List<BpmTask> list= bpmTaskMapper.selectList(wrapper);
        return list;
    }


    /**
     * 取得当前任务的处理用户
     * @param taskId
     * @return
     */
    public Set<TaskExecutor> getTaskExecutors(String taskId){
        BpmTask bpmTask=get(taskId);

        FlowNode flowNode =actRepService.getFlowNode(bpmTask.getActDefId(),bpmTask.getKey());
        UserTaskConfig nodeConfig = (UserTaskConfig)bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        Set<TaskExecutor> userIds=new HashSet<TaskExecutor>();

        //多实例任务会签
        if(flowNode!=null && StringUtils.isNotEmpty(nodeConfig.getMultipleType()) && "NONE".equals(nodeConfig.getMultipleType())){
            String signUserIds=(String)runtimeService.getVariable(bpmTask.getActInstId(), BpmConst.SIGN_EXECUTOR_IDS+bpmTask.getKey());
            if(BeanUtil.isNotEmpty(signUserIds)) {
                List<TaskExecutor> list = JSONArray.parseArray(signUserIds,TaskExecutor.class);
                userIds.addAll(list);
            }
        }else{
            //从执行用户获取
            if(StringUtils.isNotEmpty(bpmTask.getAssignee())){
                OsUserDto user=orgService.getUserById(bpmTask.getAssignee());
                userIds.add(TaskExecutor.getUser(user));
            }else{
                //取得候选用户
                List<BpmTaskUser> bpmTaskUsers = bpmTaskUserService.getByTaskId(taskId);
                for (BpmTaskUser bpmTaskUser : bpmTaskUsers) {
                    if (StringUtils.isNotEmpty(bpmTaskUser.getGroupId())) {
                        OsGroupDto group =orgService.getGroupById(bpmTaskUser.getGroupId());
                        userIds.add(TaskExecutor.getGroup(group));
                    } else if (StringUtils.isNotEmpty(bpmTaskUser.getUserId())) {
                        OsUserDto user=orgService.getUserById(bpmTaskUser.getUserId());
                        userIds.add(TaskExecutor.getUser(user));
                    }
                }
            }
        }
        return userIds;
    }

    public BpmTask getActTaskId(String taskId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ACT_TASK_ID_",taskId);
        return bpmTaskMapper.selectOne(queryWrapper);
    }

    /**
     * 获取当前节点后续节点的执行人员
     * @return
     */
    public List<NodeUsersDto> getTaskFlowNodesExecutors(BpmTask bpmTask, Map<String,Object> vars){
        List<NodeUsersDto> nodeUsersDtoList=new ArrayList<>();
        if(BpmTask.TYPE_TRANSFER_TASK.equals(bpmTask.getTaskType())){
            BpmTask parentTask = getById(bpmTask.getParentId());
            //流转任务执行人员
            BpmTransfer bpmTransfer=bpmTransferService.getByTaskId(parentTask.getTaskId());
            if("sequential".equals(bpmTransfer.getApproveType())){
                //串行
                String[] taskUserIds=bpmTransfer.getTaskUserId().split(",");
                Integer taskUserIdx=bpmTransfer.getTaskUserIdx();
                if(taskUserIdx+1>=taskUserIds.length){
                    nodeUsersDtoList=getBpmTransferUser(bpmTransfer,bpmTask,parentTask,vars);
                }else{
                    String userId=taskUserIds[taskUserIdx+1];
                    FlowNode flowNode=actRepService.getFlowNode(parentTask.getActDefId(),parentTask.getKey());
                    NodeUsersDto dto = new NodeUsersDto();
                    dto.setFlowNode(flowNode);
                    Set<TaskExecutor> executors=new HashSet<>();
                    executors.add(TaskExecutor.getUser(orgService.getUserById(userId)));
                    dto.setExecutors(executors);
                    nodeUsersDtoList.add(dto);
                }
            }else if("parallel".equals(bpmTransfer.getApproveType())){
                //并行
                nodeUsersDtoList=getBpmTransferUser(bpmTransfer,bpmTask,parentTask,vars);
            }
        }else {
            // 取得流程定义
            Set<FlowNode> flowNodes = getOutNodes(bpmTask);
            //节点ID
            flowNodes=handNodes(flowNodes,bpmTask.getActDefId(),bpmTask.getKey(),vars);

            //根据节点获取人员。
            nodeUsersDtoList = getNodeUsers(bpmTask.getActDefId(), flowNodes, vars);
            parseUserFilter(nodeUsersDtoList,bpmTask.getActDefId());
        }
        return nodeUsersDtoList;
    }

    /**
     * 如果节点之后配置了条件。
     * @param flowNodes     后续节点集合。
     * @param actDefId      流程定义ID
     * @param nodeId        节点ID
     * @param vars          变量Map
     * @return
     */
    private Set<FlowNode> handNodes(Set<FlowNode> flowNodes,String actDefId, String nodeId,Map<String,Object> vars ){
        UserTaskConfig userTaskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(actDefId,nodeId);
        String nextHandMode = userTaskConfig.getNextHandMode();
        //没有配置或者是手工选择则直接返回
        if(StringUtils.isEmpty(nextHandMode) || UserTaskConfig.NEXT_MODE_MANUAL.equals(nextHandMode)){
            return flowNodes;
        }
        //后续节点如果只有一个的情况直接返回。
        List<NodeOutcomeConfig> outs = userTaskConfig.getOuts();
        if(outs.size()==1){
            return flowNodes;
        }

        Map<String,Object> contextData=new HashMap<>();
        contextData.put("vars",vars);

        ProcessNextCmd cmd= (ProcessNextCmd) ProcessHandleUtil.getProcessCmd();
        if(cmd!=null && BeanUtil.isNotEmpty(cmd.getBoDataMap())){
            //设置cmd。
            contextData.put("cmd",cmd);

            JSONObject json=cmd.getBoDataMap();
            if(json!=null){
                Set<Map.Entry<String, Object>> ents = json.entrySet();
                for(Map.Entry<String, Object> ent : ents){
                    contextData.put(ent.getKey(),ent.getValue());
                }
            }
        }

        String outNodeId="";

        for(NodeOutcomeConfig outConfig:outs){
            String script= outConfig.getCondition();
            Boolean rtn=(Boolean) processScriptEngine.exeScript(script,contextData);
            if(rtn){
                outNodeId=outConfig.getTargetNodeId();
                break;
            }
        }

        Set<FlowNode> outnodes=new LinkedHashSet<>();

        if(StringUtils.isNotEmpty(outNodeId)){
            for(Iterator<FlowNode> it=flowNodes.iterator();it.hasNext();){
                FlowNode node=it.next();
                if(node.getId().equals(outNodeId)){
                    outnodes.add(node);
                    break;
                }
            }
        }
        else{
            for(Iterator<FlowNode> it=flowNodes.iterator();it.hasNext();){
                FlowNode node=it.next();
                outnodes.add(node);
                break;
            }
        }
        return outnodes;
    }

    private void parseUserFilter(Collection<NodeUsersDto> nodeUsersDtoList,String actDefId){
        if(BeanUtil.isNotEmpty(nodeUsersDtoList)) {
            for (NodeUsersDto nodeUsersDto : nodeUsersDtoList) {
                if ("UserTask".equals(nodeUsersDto.getNodeType())) {
                    UserTaskConfig config = (UserTaskConfig) bpmDefService.getNodeConfig(actDefId, nodeUsersDto.getNodeId());
                    JSONObject userFilter=config.getUserFilter();
                    if(userFilter!=null) {
                        nodeUsersDto.setUserFilter(userFilter.getJSONObject("user"));
                        nodeUsersDto.setGroupFilter(userFilter.getJSONObject("group"));
                    }
                }
                parseUserFilter(nodeUsersDto.getOutcomeNodeUsers(),actDefId);
            }
        }
    }

    private List<NodeUsersDto> getBpmTransferUser(BpmTransfer bpmTransfer,BpmTask bpmTask,BpmTask parentTask,Map<String,Object> vars){
        List<NodeUsersDto> nodeUsersDtoList=new ArrayList<>();
        if("waitAllVoted".equals(bpmTransfer.getTransferType())){
            //返回
            FlowNode flowNode=actRepService.getFlowNode(parentTask.getActDefId(),parentTask.getKey());
            NodeUsersDto dto = new NodeUsersDto();
            dto.setFlowNode(flowNode);
            if(BpmTask.TYPE_TRANSFER_TASK.equals(parentTask.getTaskType())){
                Set<TaskExecutor> executors=bpmTaskUserService.getTaskExecutors(parentTask);
                dto.setExecutors(executors);
            }else {
                getUserDtoByNode(dto, parentTask.getActDefId(), vars);
            }
            nodeUsersDtoList.add(dto);
        }else if("jumpNext".equals(bpmTransfer.getTransferType())){
            //审批下一步
            // 取得流程定义
            Set<FlowNode> flowNodes = getOutNodes(bpmTask);
            //根据节点获取人员。
            nodeUsersDtoList = getNodeUsers(bpmTask.getActDefId(), flowNodes, vars);
        }
        return nodeUsersDtoList;
    }

    /**
     * 根据任务获取后续节点。
     * @param bpmTask
     * @return
     */
    private Set<FlowNode> getOutNodes(BpmTask bpmTask){
        // 取得流程定义
        Map<String,FlowNode> actProcessDef = actRepService.getFlowNodes(bpmTask.getActDefId());
        String nodeId = null;
        // 查找是否为原路返回的模式，即当前任务是否由回退处理的
        BpmRuPath ruPath = bpmRuPathService.getFarestPath(bpmTask.getInstId(), bpmTask.getKey());
        if (ruPath != null && BpmRuPath.SOURCE.equals(ruPath.getNextJumpType())) {
            BpmRuPath toNodePath = bpmRuPathService.get(ruPath.getParentId());
            if (toNodePath != null) {
                nodeId = toNodePath.getNodeId();
            }
        }
        Set<FlowNode> flowNodes=new LinkedHashSet<>();
        if(StringUtils.isNotEmpty(nodeId)){
            flowNodes.add(actProcessDef.get(nodeId));
        }else {
            flowNodes = actRepService.getOutcomeNodes(bpmTask.getActDefId(), bpmTask.getKey());
        }
        return flowNodes;
    }


    public   List<NodeUsersDto> getNodeUsers(String actDefId,Set<FlowNode> flowNodes,Map<String,Object> vars){
        List<NodeUsersDto> nodeUsersDtoList = new ArrayList<>();
        for (FlowNode node : flowNodes) {
            NodeUsersDto dto = new NodeUsersDto();
            dto.setFlowNode(node);
            getUserDtoByNode(dto,actDefId,vars);
            nodeUsersDtoList.add(dto);
        }
        return nodeUsersDtoList;
    }

    /**
     * 获取节点人员。
     * <pre>
     *     1.如果是任务节点则获取人员。
     *     2.如果是子流程节点则获取第一个任务节点的人员
     *     3.如果是网关节点则递归。
     * </pre>
     * @param dto
     * @param actDefId
     * @param vars
     */
    private void getUserDtoByNode(NodeUsersDto dto,String actDefId,Map<String,Object> vars){
        FlowNode node=dto.getFlowNode();
        String clsName=node.getClass().getName();

        if (clsName.indexOf("UserTask") != -1) {//任务节点
            Set<TaskExecutor> executors=getExecutors(actDefId,node.getId(),vars);
            dto.setExecutors(executors);
            return;
        }
        else if(clsName.indexOf("SubProcess")!=-1){
            //子流程计算第一个节点的人员，并且修改节点为第一个任务节点。
            SubProcess process= (SubProcess) node;
            FlowNode startNode=actRepService.getInitNode(process);
            UserTask userTask = (UserTask) startNode.getOutgoingFlows().get(0).getTargetFlowElement();

            NodeUsersDto subDto=new NodeUsersDto();
            subDto.setFlowNode(userTask);
            Set<TaskExecutor> executors=getExecutors(actDefId,userTask.getId(),vars);

            subDto.setExecutors(executors);
            dto.addNodeUserDto(subDto);
        }
        else if(clsName.indexOf("Gateway") != -1 && dto.getLevel()<=1){//接收任务
            List<SequenceFlow> outgoingFlows = node.getOutgoingFlows();
            for(SequenceFlow flow:outgoingFlows){
                FlowNode subNode = (FlowNode) flow.getTargetFlowElement();
                NodeUsersDto subDto=new NodeUsersDto();
                subDto.setFlowNode(subNode);
                subDto.setLevel(dto.getLevel()+1);
                getUserDtoByNode(subDto,actDefId,vars);
                dto.addNodeUserDto(subDto);
            }
        }
    }
    /**
     * 取得执行人员。
     * @param actDefId
     * @param nodeId
     * @param vars
     * @return
     */
    public Set<TaskExecutor> getExecutors(String actDefId,String nodeId,Map<String,Object> vars){
        Set<TaskExecutor> executors = new LinkedHashSet<>();
        Set<TaskExecutor> nodeVarTaskExecutors = taskExecutorService.getExecutorFromVars(nodeId, vars);
        if (BeanUtil.isNotEmpty(nodeVarTaskExecutors)) {
            executors.addAll(nodeVarTaskExecutors);
            return executors;
        }
        List<BpmTask> bpmTasks = getByInstId((String) vars.get("instId"));
        if (bpmTasks != null) {
            for (BpmTask bpmTask : bpmTasks) {
                if (bpmTask.getKey().equals(nodeId)) {
                    executors.addAll(bpmTaskUserService.getTaskExecutors(bpmTask));
                }
            }
        }

        UserTaskConfig config = (UserTaskConfig) bpmDefService.getNodeConfig(actDefId, nodeId);
        executors.addAll(taskExecutorService.getTaskExecutors(config.getUserConfigs(), vars));

        return executors;
    }

    /**
     * 根据流程配置与用户节点配置获取单据的单据与数据
     * @param instId
     * @param processConfig
     * @param userTaskConfig
     * @param isMobile
     * @return
     */
    public JsonResult getFormData(String instId,ProcessConfig processConfig,UserTaskConfig userTaskConfig,String isMobile){
        FormConfig formConfig= getForms(processConfig,userTaskConfig);
        DataSetting dataSetting=processConfig.getDataSetting();
        if(BeanUtil.isNotEmpty(userTaskConfig)){
            dataSetting=userTaskConfig.getDataSetting();
        }
        BpmInst bpmInst=null;
        if(StringUtils.isNotEmpty(instId)){
            bpmInst=bpmInstService.get(instId);
        }

        JsonResult result= formDataService.getByInstId(dataSetting, processConfig.getBoDefs().getValue(), formConfig,bpmInst,userTaskConfig.getKey(),isMobile,false,"");
        return result;
    }

    /**
     * 判断是的自定义表单
     * @param processConfig
     * @param userTaskConfig
     * @return
     */
    public String handFormConfig(ProcessConfig processConfig,UserTaskConfig userTaskConfig){
        //是否自定义表单
        String formType="online";
        FormConfig formConfig= getForms(processConfig,userTaskConfig);
        if(BeanUtil.isEmpty(formConfig) ){
            return formType;
        }
        List<Form> formpc =formConfig.getFormpc();
        if(BeanUtil.isEmpty(formpc) ){
            return formType;
        }
        Form form =formpc.get(0);
        if(BpmTask.FORM_TYPE_SEL_DEV.equals(form.getType())){
            return BpmTask.FORM_TYPE_SEL_DEV;
        }
        return formType;
    }

    /**
     * 从配置获取表单别名
     * 节点审批，权限有限取当前节点，没有则走全局
     * @param processConfig
     * @param userTaskConfig
     * @return
     */
    private FormConfig getForms(ProcessConfig processConfig,UserTaskConfig userTaskConfig){
        FormConfig config =null;
        if (BeanUtil.isNotEmpty(userTaskConfig) ) {
            config = userTaskConfig.getForm();
            if (BeanUtil.isNotEmpty(config) && BeanUtil.isNotEmpty(config.getFormpc())) {
                FormConfig returnConfig = null;
                try {
                    returnConfig = (FormConfig) FileUtil.cloneObject(config);
                } catch (Exception ex) {
                    log.error("BpmTaskController.FormConfig is error ---:" + ExceptionUtil.getExceptionMessage(ex));
                }
                List<Form> formpcs = returnConfig.getFormpc();
                List<Form> newFormPcs = new ArrayList<>();
                for (Form form : formpcs) {
                    String permission = form.getPermission();
                    Form newFormpc = null;
                    if (StringUtils.isEmpty(permission) || "{}".endsWith(permission)) {
                        newFormpc = getFormByGlobalForm(processConfig, form.getAlias());
                    }
                    if (BeanUtil.isNotEmpty(newFormpc)) {
                        newFormPcs.add(newFormpc);
                        continue;
                    }
                    newFormPcs.add(form);
                }
                returnConfig.setFormpc(newFormPcs);
                return returnConfig;
            }
        }
        if (BeanUtil.isNotEmpty(processConfig)) {
            config = processConfig.getGlobalForm();
            if (BeanUtil.isNotEmpty(config) && BeanUtil.isNotEmpty(config.getFormpc())) {
                return config;
            }
        }
        return null;
    }

    /**
     * 获取全局表单
     * @param processConfig
     * @param alias
     * @return
     */
    private Form getFormByGlobalForm(ProcessConfig processConfig, String alias){
        Form newFormpc =null;
        if (BeanUtil.isEmpty(processConfig) ){
            return newFormpc;
        }
        FormConfig globalForm= processConfig.getGlobalForm();
        if (BeanUtil.isEmpty(globalForm)){
            return newFormpc;
        }
        List<Form> formpcs = globalForm.getFormpc();
        if (BeanUtil.isEmpty(formpcs)){
            return newFormpc;
        }
        for (Form form:formpcs) {
            if (alias.equals(form.getAlias())){
                Form returmForm=null;
                try{
                    returmForm= (Form) FileUtil.cloneObject(form);
                }
                catch (Exception ex){
                    log.error("BpmTaskController.getFormByGlobalForm is error ---:"+ExceptionUtil.getExceptionMessage(ex));
                }
                returmForm.setReadOnly(true);
                return returmForm;
            }
        }
        return newFormpc;
    }

    /**
     * 当前处理人是否有任务的处理权限
     * @param bpmTask
     * @param taskUsers
     * @return
     */
    public boolean hasHandlerRight(BpmTask bpmTask, List<BpmTaskUser> taskUsers){
        String curUserId=ContextUtil.getCurrentUserId();
        IUser user=ContextUtil.getCurrentUser();
        if(curUserId.equals(user.getUserId())){
            return  true;
        }
        List<String> roles=user.getRoles();
        if(BeanUtil.isEmpty(roles)){
            return false;
        }

        for(BpmTaskUser bpmTaskUser:taskUsers){
            String userType= bpmTaskUser.getUserType();
            if(TaskExecutor.TYPE_GROUP.equals(userType)){
                if(  user.getRoles().contains(bpmTaskUser.getGroupId())){
                    return true;
                }
            }
        }

        return false;

    }




    /**
     * 处理表单意见。
     * @param processConfig
     * @param userTaskConfig
     * @param formData
     */
    public void handOpinion(ProcessConfig processConfig,UserTaskConfig userTaskConfig,JsonResult formData){
        //获取意见配置
        DataSetting dataSetting = userTaskConfig.getDataSetting();
        List<OpinionSetting> opinionSetting =dataSetting.getOpinionSetting();
        //节点配置为空时，取全局配置
        if(BeanUtil.isEmpty(opinionSetting)){
            opinionSetting = processConfig.getDataSetting().getOpinionSetting();
        }
        List<BpmView> dataList= (List<BpmView>) formData.getData();
        if(BeanUtil.isEmpty(opinionSetting)) {
            return;
        }
        for (BpmView bpmView : dataList) {
            for (OpinionSetting setting : opinionSetting) {
                FormBoEntityDto boEntityDto = formClient.getBoFields(setting.getAlias());
                if(bpmView.getBoAlias().equals(boEntityDto.getAlias())){
                    bpmView.setOpinionSetting(setting.getAttrData());
                }
            }
        }

    }

    /**
     * 当前流程实例是否可以进行作废
     * @param bpmInst
     * @return
     */
    public boolean canDiscard(BpmInst bpmInst){
        boolean isShowDiscardBtn=false;
        if(!BpmInstStatus.RUNNING.name().equals(bpmInst.getStatus())) {
            return false;
        }
        Map<String,FlowNode> flowNodes=actRepService.getFlowNodes(bpmInst.getActDefId());
        List<BpmTask> bpmTasks=getByActInstId(bpmInst.getActInstId());
        String curUserId=ContextUtil.getCurrentUserId();
        for(BpmTask t:bpmTasks){
            for(FlowNode node:flowNodes.values()){
                //当前节点处于经办人环节，并且处理人为经办人，即允许作废单据
                if(node.getId().equals(t.getKey()) && curUserId.equals(t.getAssignee())){
                    isShowDiscardBtn=true;
                    break;
                }
            }
        }
        return isShowDiscardBtn;
    }

    /**
     * 根据ACT_TASK_ID_ 获取任务列表。
     * <pre>
     *     这里主要需要解决的时查询会签任务需要使用到。
     * </pre>
     * @param taskId
     * @return
     */
    public List<BpmTask> getTasksByActTaskId(String taskId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ACT_TASK_ID_",taskId);
        return bpmTaskMapper.selectList(queryWrapper);
    }

    /**
     * 更新流程的实例任务为锁定。
     * @param taskId
     */
    public void updTaskStatusByInstId(String taskId,String status ){
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("TASK_ID_",taskId);
        wrapper.set("STATUS_",status);
        bpmTaskMapper.update(null,wrapper);
    }

    /**
     * 根据实例ID删除任务。
     * @param instId
     */
    public void deleteByInstId(String instId){
        bpmTaskMapper.deleteByInstId(instId);
    }

    /**
     * 根据instId获取单据的单据与数据
     * @param instId
     * @param initPermission 是否使用权限
     * @return
     */
    public JsonResult getFormData(String instId,Boolean initPermission){
        List<BpmInstData> bpmInstDatas = bpmInstDataService.getByInstId(instId);
        BpmInstData bpmInstData = bpmInstDatas.get(0);
        JsonResult  result=formClient.getByAlias(bpmInstData.getBodefAlias(),bpmInstData.getPk(),initPermission);
        return result;
    }


    /**
     * 处理完成任务报错。
     * @param ex
     */
    public void handException(Exception ex){
        String title="审批任务失败!";
        String message=ExceptionUtil.getExceptionMessage(ex);
        //网关出错
        if(message.indexOf("No outgoing sequence")!=-1){
            title="网关没有符合条件的路径，请检查网关条件配置!";
        }
        String tmp= com.redxun.common.utils.MessageUtil.getTitle();
        if(StringUtils.isNotEmpty(tmp)) {
            title=tmp;
        }
        com.redxun.common.utils.MessageUtil.triggerException(title,message);

    }


    /**
     * 根据个人获取任务列表。
     * @param jPaasUser
     * @param instId
     * @return
     */
    public List<BpmTask> getByInstUserId(JPaasUser jPaasUser, String instId){
        return bpmTaskMapper.getByInstUserId(jPaasUser.getUserId(),jPaasUser.getRoles(),instId,jPaasUser.getTenantId());
    }


    /**
     * 修改执行人
     * @param receiptUserId
     * @param deliverUserId
     */
    public void updateAssignee(String receiptUserId, String deliverUserId) {
        bpmTaskMapper.updateAssignee(receiptUserId, deliverUserId);
    }
}


