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
* [????????????]???????????????
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
     * ??????Bpm??????
     * @param taskEntity
     * @param taskType ????????????
     * @param hasExecutor ??????????????????
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

        //????????????
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
        //??????????????????
        String excutor=hasExecutor?"1":"0";
        bpmTask.setExecutor(excutor);
        bpmTask.setTenantId(ContextUtil.getCurrentTenantId());

        insert(bpmTask);

        return  bpmTask;
    }

    /**
     * ??????????????????????????????
     * @param taskId
     */
    @GlobalTransactional
    public void doClearTaskUsers(String taskId){
        bpmTaskUserService.deleteByTaskId(taskId);
    }

    /**
     * ???????????????????????????
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
     * ?????????????????????
     * @param taskEntity
     * @param executors
     * @param instanceType  ?????? ????????? ?????????=normal,sequence,parallel
     */
    public void createTasksAndAssign(TaskEntity taskEntity,Set<TaskExecutor> executors,String instanceType){
        IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();
        boolean hasExecutor=BeanUtil.isNotEmpty(executors);
        if(TaskInstanceType.sequential.name().equals(instanceType) && hasExecutor){//????????????
            //???????????????
            TaskExecutor executor =executors.iterator().next();
            BpmTask bpmTask= createBpmTask(taskEntity,BpmTask.TYPE_SEQUENTIAL_TASK,true);
            bpmTaskUserService.createUser(bpmTask,executor);

            Map<String,Object> vars=new HashMap<>();
            vars.put(BpmConst.LOOP_COUNTS,executors.size());
            vars.put(BpmConst.LOOP_INDEX,0);
            vars.put(BpmConst.COMPLETE_COUNTS,0);

            String varName=BpmConst.SIGN_EXECUTOR_IDS + taskEntity.getTaskDefinitionKey();
            vars.put(varName,JSONArray.toJSONString(executors));

            //??????????????????????????????
            Set<TaskExecutor> taskExecutors=new LinkedHashSet<>();
            taskExecutors.add(executor);

            taskEntity.setVariablesLocal(vars);
            //???????????????????????????
            //????????????????????????
            bpmTask.setTaskExecutors(taskExecutors);

            cmd.addTask(bpmTask);

        }else if(TaskInstanceType.parallel.name().equals(instanceType) && hasExecutor){//?????????????????????

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
           //????????????
           BpmTask bpmTask= createBpmTask(taskEntity,BpmTask.TYPE_FLOW_TASK,hasExecutor);
           bpmTaskUserService.createUsers(bpmTask,executors);
           //???????????????
           bpmTask.setTaskExecutors(executors);

           //??????????????????CMD????????????
           cmd.addTask(bpmTask);
           cmd.addTransientVar(BpmConst.BPM_APPROVE_TASK,bpmTask);

        }
    }

    public void  createTasksAndAssign(TaskEntity taskEntity,TaskExecutor executor){
        BpmTask bpmTask= createBpmTask(taskEntity,BpmTask.TYPE_FLOW_TASK,true);
        bpmTaskUserService.createUser(bpmTask,executor);

        //??????????????????????????????
        IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();

        Set<TaskExecutor> executorSet=new HashSet<>();
        executorSet.add(executor);

        bpmTask.setTaskExecutors(executorSet);

        cmd.addTask(bpmTask);

    }

    /**
     * ?????????????????????
     * @param taskId
     * @param list
     */
    public void getTaskByTaskId(String taskId,List<BpmTask> list){
        getTaskByTaskId(taskId,"",list);
    }

    /**
     * ?????????????????????
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
     * ????????????ID?????????????????????????????????????????????
     * @param taskId
     */
    public void  delByTaskId(String taskId){
        bpmTaskUserService.deleteByTaskId(taskId);
        delete(taskId);
    }

    /**
     * ??????????????????ID???????????????
     * @param instId
     * @return
     */
    public List<BpmTask> getByInstId(String instId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("INST_ID_",instId);
        return bpmTaskMapper.selectList(queryWrapper);
    }

    /**
    * @Description:  ????????????????????????????????????????????????
    * @param instId ???????????? ID
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
     * ??????????????????ID???????????????
     * @param actInstId
     * @return
     */
    public List<BpmTask> getByActInstId(String actInstId){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("ACT_INST_ID_",actInstId);
        return bpmTaskMapper.selectList(queryWrapper);
    }

    /**
     * ??????????????????????????????ID???????????????
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
     * ??????????????????
     * <pre>
     *      1.????????????????????????????????????????????????????????????
     *      2.?????????????????????
     * </pre>
     * @param reply
     * @return
     */
    @Transactional
    public JsonResult doCompleteLinkupTask(TaskLinkupReply reply){
        BpmCheckHistory history= bpmCheckHistoryService.getByTaskIdCheckType(reply.getTaskId(),TaskOptionType.REPLY_COMMUNICATE.name());
        if(history!=null){
            return JsonResult.Fail("?????????????????????!");
        }
        BpmTask bpmTask=get(reply.getTaskId());

        LogContext.put(Audit.DETAIL,"??????????????????:" +bpmTask.getSubject());

        List<BpmTask> taskList=new ArrayList<>();
        getTaskByTaskId(bpmTask.getTaskId(),BpmTask.TYPE_LINKUP_TASK,taskList);

        for(BpmTask task:taskList){
            task.setStatus(BpmTask.STATUS_COMPLETED);
            bpmTaskMapper.updateById(task);
        }
        bpmTask.setStatus(BpmTask.STATUS_COMPLETED);
        bpmTaskMapper.updateById(bpmTask);

        //??????????????????
        bpmCheckHistoryService.createHistory(bpmTask, TaskOptionType.REPLY_COMMUNICATE.name(),"", reply.getOpinion(),reply.getOpFiles(),"","");

        //????????????
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

        return JsonResult.Success("???????????????????????????!");
    }

    /**
     * ?????????????????????
     * <pre>
     *  1.???????????????????????????
     *  2.???????????????????????????
     * </pre>
     * @param taskId
     */
    @Transactional
    public void doRevokeLinkupTask(String taskId,boolean delOpinion){
        List<BpmTask> list=new ArrayList<>();

        BpmTask bpmTask=bpmTaskMapper.selectById(taskId);

        LogContext.put(Audit.DETAIL,"????????????????????????????????????:" + bpmTask.getSubject() +",????????????ID???:" + bpmTask.getInstId());

        getTaskByTaskId(taskId,BpmTask.TYPE_LINKUP_TASK,list);

        for(BpmTask task : list){
            if(delOpinion){
                bpmCheckHistoryService.removeByTaskId(task.getTaskId());
            }
            delByTaskId(task.getTaskId());
            OsUserDto osUserDto = orgService.getUserById(task.getAssignee());
            String userName=osUserDto.getFullName() + "(" + osUserDto.getUserId() + ")";
            bpmInstLogService.addInstLog(bpmTask.getInstId(),"???????????????"+userName );
        }

    }

    /**
     * ??????????????????
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
            bpmInstLogService.addInstLog(bpmTask.getInstId(),"???????????????" + userName );
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
     * ?????????Id??????
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
     * ?????????Id??????
     * @param userId
     * @param groupIds
     * @param filter
     * @return
     */
    public IPage<BpmTask> getByUserId(String userId, List<String> groupIds,String tenantId, QueryFilter filter){
        return bpmTaskMapper.getByUserId(filter.getPage(),filter.getSearchParams(),userId,groupIds,tenantId);
    }

    /**
     * ?????????Id???????????????ID??????
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
            result.setMessage("???????????????????????????????????????????????????");
           return result;
        }

        if(BpmTask.STATUS_LOCKED.equals( bpmTask.getStatus())){
            result.setSuccess(false);
            result.setMessage("?????????????????????????????????????????????????????????");
            IUser user= orgService.getUserById(bpmInst.getLockedBy());
            result.setData("????????????"+user.getFullName()+"???????????????!");
            return result;
        }

        //???????????????????????????????????????????????????
        if(BpmTask.TYPE_LINKUP_TASK.equals(bpmTask.getTaskType())){
            result.setSuccess(false);
            result.setMessage("???????????????????????????????????????");
            return result;
        }

        return result;


    }

    /*
     * ??????????????????????????????????????????????????????
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
            result= JsonResult.Fail("?????????????????????!");
            return result;
        }

        JsonResult authResult=bpmInstService.calTaskAuth(bpmTask,ContextUtil.getCurrentUserId());
        if(!skip && !authResult.isSuccess()){
            return authResult;
        }

        String detail="???????????????"+ bpmTask.getSubject() +",?????????:" + cmd.getCheckType();
        LogContext.put(Audit.DETAIL,detail);

        BpmInst bpmInst=bpmInstService.getById(bpmTask.getInstId());
        String status=bpmInst.getStatus();
        if(BpmInstStatus.SUPSPEND.name().equals( status)){
            return JsonResult.Fail("???????????????,??????????????????!");
        }

        result=checkTask(bpmTask,bpmInst);
        if(!result.isSuccess()){
            return result;
        }

        //???????????????????????????????????????????????????????????????????????????????????????
        cmd.setDefId(bpmTask.getDefId());
        //??????????????????????????????????????????
        cmd.setInstId(bpmTask.getInstId());
        //??????????????????ID
        cmd.setPreNodeId(bpmTask.getKey());

        //???????????????????????????
        ProcessConfig processConfig=bpmDefService.getProcessConfig(bpmTask.getActDefId());
        UserTaskConfig userTaskConfig=(UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        cmd.addTransientVar(BpmConst.PROCESS_CONFIG,processConfig);
        cmd.addTransientVar(BpmConst.USERTASK_CONFIG,userTaskConfig);
        cmd.addTransientVar(BpmConst.BPM_INST,bpmInst);
        cmd.addTransientVar(BpmConst.BPM_APPROVE_TASK,bpmTask);


        //??????????????????
        JsonResult jsonResult = bpmInstService.getAllowApprove(userTaskConfig,cmd.getFormData(),cmd.getVars());
        if(!jsonResult.isSuccess()){
            return jsonResult;
        }

        //????????????????????????
        if(!TaskOptionType.SKIP.name().equals(cmd.getCheckType()) &&cmd.getSystemHand()){
            //??????????????????
            formDataService.handFormData(cmd,userTaskConfig.getDataSetting(),"approve");
        }

        //??????????????????
        bpmInstService.handVars(cmd,bpmInst,processConfig,false);
        //???????????????????????????
        String  targetNodeId= handJumpRules(bpmTask, userTaskConfig);
        if(StringUtils.isNotEmpty(targetNodeId) && StringUtils.isEmpty(cmd.getDestNodeId())){
            cmd.setDestNodeId(targetNodeId);
        }

        //??????fieldJson???
        TaskFieldConfig fieldConfig= processConfig.getTaskFields();
        //??????JSON?????????
        bpmInstService.handInstJson(fieldConfig,bpmInst,cmd);
        //????????????????????????
        processHandlerExecutor.handTaskBeforeHandler(userTaskConfig,bpmTask,bpmInst.getBusKey());

        //?????????????????????
        bpmCheckHistoryService.createHistory(bpmTask, cmd.getCheckType(),cmd.getOpinionName(), cmd.getOpinion(),cmd.getOpFiles(),cmd.getRelInsts(),"");

        //????????????????????? ???????????????????????????????????????
        ITaskHandler taskHandler= TaskHandlerContext.getJumpType(cmd.getCheckType());
        taskHandler.handTask(bpmTask,cmd,userTaskConfig);


        //????????????????????????
        processHandlerExecutor.handTaskAfterHandler(userTaskConfig,bpmTask.getKey(),bpmInst.getBusKey());

        //?????????????????????
        bpmTaskSkipService.handSkipTask(cmd);

        //???????????????????????????
        bpmInstPermissionService.createTaskInfo(bpmInst,cmd);

        //????????????????????????
        bpmInstLogService.addTaskLog(bpmTask.getInstId(),bpmTask.getTaskId(),bpmTask.getName(),bpmTask.getKey(),"????????????");

        //?????????????????????
        bpmTemporaryOpinionService.delByTaskId(bpmTask.getTaskId());

        result.setMessage("???????????????????????????");

        return result;
    }



    /**
     * ?????????????????????
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
     * ?????????????????????
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
            //?????????????????????
            ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmTask.getActDefId());
            BpmInst bpmInst =bpmInstService.get(bpmTask.getInstId());
            //????????????????????????????????????????????????
            processHandlerExecutor.handTaskAfterHandler(userTaskConfig,cmd.getDestNodeId(),bpmInst.getBusKey());
        }
    }

    /**
     * ?????????????????????
     * @param userId  ??????Id
     * @param groupIds ????????????????????????Id
     * @param tenantId ??????ID
     * @return
     */
    public Integer getCountsByUserId(String userId,List<String> groupIds,String tenantId){
        return bpmTaskMapper.getCountsByUserId(userId,groupIds,tenantId);
    }

    /**
     * ????????????
     * @param taskId
     * @param status
     * @param assignee
     */
    public void updateStatus(String taskId,String status,String assignee){
        bpmTaskMapper.updateStatus(taskId,status,assignee);
    }

    /**
     * ???act??????Id????????????
     * <pre>
     *     ??????ACTTASKID???????????????????????????????????????
     * </pre>
     * @param actTaskId
     */
    public void delByActTaskId(String actTaskId){
        //??????actTaskId ???????????????
        List<BpmTask> list=getByActTaskId(actTaskId);

        if(BeanUtil.isEmpty(list)){
            return;
        }
        //?????????????????????
        List<String> taskIds=list.stream().map(BpmTask::getTaskId).collect(Collectors.toList());
        QueryWrapper delWrapper=new QueryWrapper();
        delWrapper.in("TASK_ID_",taskIds);
        bpmTaskUserMapper.delete(delWrapper);
        //??????ACT_TASK_ID_??????????????????
        bpmTaskMapper.deleteByActTaskId(actTaskId);
    }




    /**
     * ????????????????????????
     * @param taskAddSign
     */
    @Transactional
    public void addTaskData(TaskAddSign taskAddSign){
        BpmTask bpmTask=getById(taskAddSign.getTaskId());
        String signType=bpmTask.getTaskType();
        /**
         * ??????????????????????????????
         */
        boolean isParallel=BpmTask.TYPE_PARALLEL_TASK.equals(signType);
        String[] userIds=taskAddSign.getToUserIds().split("[,]");

        Integer loopCounts=(Integer) taskService.getVariable(bpmTask.getActTaskId(),BpmConst.LOOP_COUNTS);
        if(loopCounts==null){
            loopCounts=0;
        }

        StringBuilder detail=new StringBuilder( "?????????:"+bpmTask.getSubject() +"????????????,?????????????????????:"+(isParallel?"????????????":"????????????") +"???????????????:");

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
        //??????????????????
        taskService.setVariable(bpmTask.getActTaskId(),BpmConst.LOOP_COUNTS,loopCounts+userIds.length);

        LogContext.put(Audit.DETAIL,detail);
    }
    /**
     * ????????????
     * <pre>
     *     1.?????????????????????
     *     2.???????????????????????????
     *     3.?????????????????????
     * </pre>
     * @param taskLinkup
     */
     @Transactional
    public void taskLinkups(TaskLinkup taskLinkup) {
        StringBuilder sb=new StringBuilder();
        BpmTask bpmTask = getById(taskLinkup.getTaskId());
         BpmInst bpmInst = bpmInstService.getById(bpmTask.getInstId());

        sb.append("?????????("+ bpmTask.getSubject() + ")????????????ID("+bpmTask.getInstId()+")?????????:" );

        String[] userAccounts=taskLinkup.getToUserAccounts().split("[,]");
        IUser curUser=ContextUtil.getCurrentUser();
        OsUserDto sender=orgService.getUserById(curUser.getUserId());
        List<String> userIds=new ArrayList<>();
         ArrayList <String>contactUsers=new ArrayList(); //???????????????????????????
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
            //?????????????????????
            if(StringUtils.isNotEmpty(taskLinkup.getMsgTypes())){
                Map<String,Object> vars=new HashMap<>();
                vars.put("taskId",newTask.getTaskId());
                vars.put("instId",newTask.getInstId());
                vars.put("opinion",taskLinkup.getOpinion());

                MessageUtil.sendMessage(sender,bpmTask.getSubject(),taskLinkup.getMsgTypes(),
                        "commu",user,vars);
            }
            bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER,user,"", bpmTask.getTaskId());
            //????????????????????????????????????ID?????????????????????????????????ID
            bpmCheckHistoryService.createLinkupHistory(newTask,ContextUtil.getCurrentUserId(),taskLinkup.getOpinion(),taskLinkup.getOpFiles(),user.getUserId());
        }
         String toUserIds = StringUtils.join(userIds, ",");

        bpmInstLogService.addInstLog(bpmTask.getInstId(),"???????????????" + StringUtils.join(contactUsers,",") );
    }


    /**
     * ????????????????????????????????????????????????
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
    * @Description:  ??????????????????????????????????????????ID
    * @param task ?????????
     *@param  userId ????????????
    **/
    public BpmTask createTransRoamTask(BpmTask task,String userId){
        //????????????
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

        sb.append("????????????");

        JsonResult result=JsonResult.Success("?????????????????????!");

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

        sb.append("???????????????????????????ID???:" + bpmTask.getInstId());

        if(StringUtils.isEmpty(userIdAry)){
            return JsonResult.Fail("?????????????????????");
        }
        String[] userIds=userIdAry.split(",");
        if(ArrayUtils.contains(userIds,curUser.getUserId())){
            return JsonResult.Fail("????????????????????????");
        }
        ArrayList <String>transUsers=new ArrayList(); //???????????????????????????
        if("parallel".equals(approveType)) {
            //??????
            for (int i = 0; i < userIds.length; i++) {
                //????????????
                String userId = userIds[i];
                BpmTask newTask=createTransRoamTask(bpmTask, userId);
                //???????????????
                Map<String, Object> vars = new HashMap<>();
                vars.put("taskId", newTask.getTaskId());
                OsUserDto receiver = orgService.getUserById(userId);
                vars.put("opinion", opinion);
                vars.put("instId",bpmTask.getInstId());
                OsUserDto sender = orgService.getUserById(curUser.getUserId());
                //???????????????????????????
                writeCheckHistory(newTask,opinion);
                transUsers.add(receiver.getFullName() + "(" + receiver.getUserId() + ")");
                //????????????
                MessageUtil.sendMessage(sender, bpmTask.getSubject(), msgType, "transferRoam", receiver, vars);
                //???????????????
                bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER, bpmTask.getTaskId(), receiver);
            }
        }else{
            //??????
            BpmTask newTask=createTransRoamTask(bpmTask, userIds[0]);
            //???????????????
            Map<String, Object> vars = new HashMap<>();
            vars.put("taskId", newTask.getTaskId());
            OsUserDto receiver = orgService.getUserById(userIds[0]);
            vars.put("opinion", opinion);
            vars.put("instId",bpmTask.getInstId());
            OsUserDto sender = orgService.getUserById(curUser.getUserId());
            //???????????????????????????
            writeCheckHistory(newTask,opinion);
            transUsers.add(receiver.getFullName() + "(" + receiver.getUserId() + ")");
            //????????????
            MessageUtil.sendMessage(sender, bpmTask.getSubject(), msgType, "transferRoam", receiver, vars);
            //???????????????
            bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER, bpmTask.getTaskId(), receiver);
        }
        bpmTransferLogService.updateStatusByTransTaskId(taskId,opinion,BpmTask.STATUS_HANDLE);

        //?????????????????????
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
        //????????????
        LogContext.put(Audit.DETAIL,sb.toString());
        bpmInstLogService.addInstLog(bpmTask.getInstId(),"???????????????"+StringUtils.join(transUsers,",") );
        return result;
    }
    /**
    * @Description:  ???????????????????????????????????????????????????????????????????????????
    * @param bpmTask ????????????
    * @Author: Elwin ZHANG  @Date: 2021/12/6 15:53
    **/
    private  void  writeCheckHistory(BpmTask bpmTask,String opinion){
        //????????????????????????
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

        JsonResult result=JsonResult.Success("?????????????????????!");

        String opinion=json.getString("opinion");
        JSONObject toUser = json.getJSONObject("toUser");
        String msgType=json.getString("msgType");
        String taskId=json.getString("taskId");

        String userId=toUser.getString("value");
        IUser curUser=ContextUtil.getCurrentUser();

        //????????????
        BpmTask bpmTask=bpmTaskMapper.selectById(taskId);
        BpmInst bpmInst=bpmInstService.getById(bpmTask.getInstId());

        sb.append("?????????:" + bpmTask.getSubject() +",?????????:" + toUser.get("label") +"??????!") ;

        if(!curUser.getUserId().equals( bpmTask.getAssignee())){
            result.setSuccess(false);
            result.setMessage("????????????????????????????????????????????????!");
            return result;
        }
        bpmTask.setAssignee(userId);
        bpmTaskMapper.updateById(bpmTask);

        //????????????????????????
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

        //?????????????????????
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

        //???????????????
        Map<String,Object> vars=new HashMap<>();
        vars.put("taskId",bpmTask.getTaskId());
        vars.put("instId",bpmTask.getInstId());
        OsUserDto receiver=orgService.getUserById(userId);
        vars.put("opinion",opinion);
        OsUserDto sender=orgService.getUserById(curUser.getUserId());

        MessageUtil.sendMessage(sender,bpmTask.getSubject(),msgType,"transfer",receiver,vars);
        //???????????????
        bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER, bpmTask.getTaskId(), receiver);
        return result;
    }

    /**
     * ??????actTaskId ???????????????
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
     * ?????????????????????????????????
     * @param taskId
     * @return
     */
    public Set<TaskExecutor> getTaskExecutors(String taskId){
        BpmTask bpmTask=get(taskId);

        FlowNode flowNode =actRepService.getFlowNode(bpmTask.getActDefId(),bpmTask.getKey());
        UserTaskConfig nodeConfig = (UserTaskConfig)bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        Set<TaskExecutor> userIds=new HashSet<TaskExecutor>();

        //?????????????????????
        if(flowNode!=null && StringUtils.isNotEmpty(nodeConfig.getMultipleType()) && "NONE".equals(nodeConfig.getMultipleType())){
            String signUserIds=(String)runtimeService.getVariable(bpmTask.getActInstId(), BpmConst.SIGN_EXECUTOR_IDS+bpmTask.getKey());
            if(BeanUtil.isNotEmpty(signUserIds)) {
                List<TaskExecutor> list = JSONArray.parseArray(signUserIds,TaskExecutor.class);
                userIds.addAll(list);
            }
        }else{
            //?????????????????????
            if(StringUtils.isNotEmpty(bpmTask.getAssignee())){
                OsUserDto user=orgService.getUserById(bpmTask.getAssignee());
                userIds.add(TaskExecutor.getUser(user));
            }else{
                //??????????????????
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
     * ?????????????????????????????????????????????
     * @return
     */
    public List<NodeUsersDto> getTaskFlowNodesExecutors(BpmTask bpmTask, Map<String,Object> vars){
        List<NodeUsersDto> nodeUsersDtoList=new ArrayList<>();
        if(BpmTask.TYPE_TRANSFER_TASK.equals(bpmTask.getTaskType())){
            BpmTask parentTask = getById(bpmTask.getParentId());
            //????????????????????????
            BpmTransfer bpmTransfer=bpmTransferService.getByTaskId(parentTask.getTaskId());
            if("sequential".equals(bpmTransfer.getApproveType())){
                //??????
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
                //??????
                nodeUsersDtoList=getBpmTransferUser(bpmTransfer,bpmTask,parentTask,vars);
            }
        }else {
            // ??????????????????
            Set<FlowNode> flowNodes = getOutNodes(bpmTask);
            //??????ID
            flowNodes=handNodes(flowNodes,bpmTask.getActDefId(),bpmTask.getKey(),vars);

            //???????????????????????????
            nodeUsersDtoList = getNodeUsers(bpmTask.getActDefId(), flowNodes, vars);
            parseUserFilter(nodeUsersDtoList,bpmTask.getActDefId());
        }
        return nodeUsersDtoList;
    }

    /**
     * ????????????????????????????????????
     * @param flowNodes     ?????????????????????
     * @param actDefId      ????????????ID
     * @param nodeId        ??????ID
     * @param vars          ??????Map
     * @return
     */
    private Set<FlowNode> handNodes(Set<FlowNode> flowNodes,String actDefId, String nodeId,Map<String,Object> vars ){
        UserTaskConfig userTaskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(actDefId,nodeId);
        String nextHandMode = userTaskConfig.getNextHandMode();
        //????????????????????????????????????????????????
        if(StringUtils.isEmpty(nextHandMode) || UserTaskConfig.NEXT_MODE_MANUAL.equals(nextHandMode)){
            return flowNodes;
        }
        //??????????????????????????????????????????????????????
        List<NodeOutcomeConfig> outs = userTaskConfig.getOuts();
        if(outs.size()==1){
            return flowNodes;
        }

        Map<String,Object> contextData=new HashMap<>();
        contextData.put("vars",vars);

        ProcessNextCmd cmd= (ProcessNextCmd) ProcessHandleUtil.getProcessCmd();
        if(cmd!=null && BeanUtil.isNotEmpty(cmd.getBoDataMap())){
            //??????cmd???
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
            //??????
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
            //???????????????
            // ??????????????????
            Set<FlowNode> flowNodes = getOutNodes(bpmTask);
            //???????????????????????????
            nodeUsersDtoList = getNodeUsers(bpmTask.getActDefId(), flowNodes, vars);
        }
        return nodeUsersDtoList;
    }

    /**
     * ?????????????????????????????????
     * @param bpmTask
     * @return
     */
    private Set<FlowNode> getOutNodes(BpmTask bpmTask){
        // ??????????????????
        Map<String,FlowNode> actProcessDef = actRepService.getFlowNodes(bpmTask.getActDefId());
        String nodeId = null;
        // ??????????????????????????????????????????????????????????????????????????????
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
     * ?????????????????????
     * <pre>
     *     1.???????????????????????????????????????
     *     2.???????????????????????????????????????????????????????????????
     *     3.?????????????????????????????????
     * </pre>
     * @param dto
     * @param actDefId
     * @param vars
     */
    private void getUserDtoByNode(NodeUsersDto dto,String actDefId,Map<String,Object> vars){
        FlowNode node=dto.getFlowNode();
        String clsName=node.getClass().getName();

        if (clsName.indexOf("UserTask") != -1) {//????????????
            Set<TaskExecutor> executors=getExecutors(actDefId,node.getId(),vars);
            dto.setExecutors(executors);
            return;
        }
        else if(clsName.indexOf("SubProcess")!=-1){
            //???????????????????????????????????????????????????????????????????????????????????????
            SubProcess process= (SubProcess) node;
            FlowNode startNode=actRepService.getInitNode(process);
            UserTask userTask = (UserTask) startNode.getOutgoingFlows().get(0).getTargetFlowElement();

            NodeUsersDto subDto=new NodeUsersDto();
            subDto.setFlowNode(userTask);
            Set<TaskExecutor> executors=getExecutors(actDefId,userTask.getId(),vars);

            subDto.setExecutors(executors);
            dto.addNodeUserDto(subDto);
        }
        else if(clsName.indexOf("Gateway") != -1 && dto.getLevel()<=1){//????????????
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
     * ?????????????????????
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
     * ?????????????????????????????????????????????????????????????????????
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
     * ???????????????????????????
     * @param processConfig
     * @param userTaskConfig
     * @return
     */
    public String handFormConfig(ProcessConfig processConfig,UserTaskConfig userTaskConfig){
        //?????????????????????
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
     * ???????????????????????????
     * ???????????????????????????????????????????????????????????????
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
     * ??????????????????
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
     * ?????????????????????????????????????????????
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
     * ?????????????????????
     * @param processConfig
     * @param userTaskConfig
     * @param formData
     */
    public void handOpinion(ProcessConfig processConfig,UserTaskConfig userTaskConfig,JsonResult formData){
        //??????????????????
        DataSetting dataSetting = userTaskConfig.getDataSetting();
        List<OpinionSetting> opinionSetting =dataSetting.getOpinionSetting();
        //???????????????????????????????????????
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
     * ??????????????????????????????????????????
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
                //???????????????????????????????????????????????????????????????????????????????????????
                if(node.getId().equals(t.getKey()) && curUserId.equals(t.getAssignee())){
                    isShowDiscardBtn=true;
                    break;
                }
            }
        }
        return isShowDiscardBtn;
    }

    /**
     * ??????ACT_TASK_ID_ ?????????????????????
     * <pre>
     *     ??????????????????????????????????????????????????????????????????
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
     * ???????????????????????????????????????
     * @param taskId
     */
    public void updTaskStatusByInstId(String taskId,String status ){
        UpdateWrapper wrapper=new UpdateWrapper();
        wrapper.eq("TASK_ID_",taskId);
        wrapper.set("STATUS_",status);
        bpmTaskMapper.update(null,wrapper);
    }

    /**
     * ????????????ID???????????????
     * @param instId
     */
    public void deleteByInstId(String instId){
        bpmTaskMapper.deleteByInstId(instId);
    }

    /**
     * ??????instId??????????????????????????????
     * @param instId
     * @param initPermission ??????????????????
     * @return
     */
    public JsonResult getFormData(String instId,Boolean initPermission){
        List<BpmInstData> bpmInstDatas = bpmInstDataService.getByInstId(instId);
        BpmInstData bpmInstData = bpmInstDatas.get(0);
        JsonResult  result=formClient.getByAlias(bpmInstData.getBodefAlias(),bpmInstData.getPk(),initPermission);
        return result;
    }


    /**
     * ???????????????????????????
     * @param ex
     */
    public void handException(Exception ex){
        String title="??????????????????!";
        String message=ExceptionUtil.getExceptionMessage(ex);
        //????????????
        if(message.indexOf("No outgoing sequence")!=-1){
            title="???????????????????????????????????????????????????????????????!";
        }
        String tmp= com.redxun.common.utils.MessageUtil.getTitle();
        if(StringUtils.isNotEmpty(tmp)) {
            title=tmp;
        }
        com.redxun.common.utils.MessageUtil.triggerException(title,message);

    }


    /**
     * ?????????????????????????????????
     * @param jPaasUser
     * @param instId
     * @return
     */
    public List<BpmTask> getByInstUserId(JPaasUser jPaasUser, String instId){
        return bpmTaskMapper.getByInstUserId(jPaasUser.getUserId(),jPaasUser.getRoles(),instId,jPaasUser.getTenantId());
    }


    /**
     * ???????????????
     * @param receiptUserId
     * @param deliverUserId
     */
    public void updateAssignee(String receiptUserId, String deliverUserId) {
        bpmTaskMapper.updateAssignee(receiptUserId, deliverUserId);
    }
}


