package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.dto.*;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.ContextQuerySupport;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.db.BaseService;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.base.search.SortParam;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.tool.constant.CommonConstant;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.dto.bpm.BpmTaskDto;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.form.BpmView;
import com.redxun.feign.sys.SystemClient;
import com.redxun.log.annotation.AuditLog;
import com.redxun.util.QueryFilterUtil;
import com.redxun.web.controller.BaseController;
import com.redxun.web.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmTask")
@ClassDefine(title = "流程任务",alias = "bpmTaskController",path = "/bpm/core/bpmTask",packages = "core",packageName = "流程管理")
@Api(tags = "流程任务")
public class BpmTaskController extends BaseController<BpmTask> {

    @Autowired
    BpmTaskService bpmTaskService;

    @Autowired
    BpmDefService bpmDefService;

    @Autowired
    BpmInstServiceImpl bpmInstService;

    @Autowired
    ActRepService actRepService;

    @Resource
    RuntimeService runtimeService;

    @Resource
    BpmTaskUserServiceImpl bpmTaskUserService;

    @Resource
    BpmRuPathServiceImpl bpmRuPathService;

    @Resource
    BpmInstLogServiceImpl bpmInstLogService;

    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;

    @Resource
    TaskExecutorService taskExecutorService;

    @Resource
    MessageService messageService;

    @Resource
    BpmTransferServiceImpl bpmTransferService;
    @Resource
    BpmTransferLogServiceImpl bpmTransferLogService;
    @Resource
    BpmInstTrackedServiceImpl bpmInstTrackedService;

    @Override
    public BaseService getBaseService() {
        return bpmTaskService;
    }

    @Override
    public String getComment() {
        return "流程任务";
    }

    @MethodDefine(title = "获取任务审批人信息", path = "/getTaskExecutors", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "任务ID", varName = "taskId")})
    @ApiOperation("获取任务审批人信息")
    @GetMapping("getTaskExecutors")
    public Set<TaskExecutor> getTaskExecutors(@RequestParam(value = "taskId") String taskId){
        BpmTask bpmTask = bpmTaskService.get(taskId);
        Map<String,Object> vars=runtimeService.getVariables(bpmTask.getActInstId());
        UserTaskConfig config = (UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(), bpmTask.getKey());
        return taskExecutorService.getTaskExecutors(config.getUserConfigs(), vars);
    }

    @MethodDefine(title = "获取任务回退节点列表", path = "/getBackNodes/*", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "任务ID", varName = "taskId")})
    @ApiOperation("获取任务回退节点列表")
    @GetMapping("getBackNodes/{taskId}")
    public List<BpmRuPath> getBackNodes(@PathVariable(value="taskId")String taskId){
        BpmTask bpmTask = bpmTaskService.get(taskId);
        String instId = bpmTask.getInstId();
        String nodeId = bpmTask.getKey();
        List<BpmRuPath> list=bpmRuPathService.getBackNodes(instId,nodeId);
        return list;
    }

    /**
     * 获得所有查询数据列表，不传入条件时，则返回所有的记录
     * @return
     * @throws Exception
     */
    @ApiOperation(value="按条件查询所有记录", notes="根据条件查询所有记录")
    @PostMapping(value="/query")
    @Override
    public JsonPageResult query(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult result=JsonPageResult.getSuccess("");
        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        handleFilter(filter);
        IPage<BpmTask> page= bpmTaskService.query(filter);
        result.setPageData(page);
        List<BpmTask> list=page.getRecords();
        for(BpmTask task:list){
            Set<TaskExecutor> executors=bpmTaskUserService.getTaskExecutors(task);
            task.setTaskExecutors(executors);
        }
        return result;
    }

    /**
     * 按条件查询所有的个人待办
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "按条件查询所有的个人待办", path = "/myTasksToStartBetweenEnd", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "开始时间", varName = "startTime"),@ParamDefine(title = "结束时间", varName = "endTime")})
    @ApiOperation(value="按条件查询所有的个人待办", notes="按条件查询所有的个人待办")
    @GetMapping(value="/myTasksToStartBetweenEnd")
    public List<BpmTask> myTasksToStartBetweenEnd(@RequestParam(value = "startTime") String startTime,
                                                   @RequestParam(value = "endTime") String endTime) throws Exception{
        JsonResult result=new JsonResult();

        IUser curUser=ContextUtil.getCurrentUser();
        String userId=curUser.getUserId();
        List<String> groupIds=curUser.getRoles();



        List<BpmTask> list = bpmTaskService.getByStartBetweenEnd(startTime,endTime,userId,groupIds,ContextUtil.getCurrentTenantId());
        return list;
    }

    /**
     * 按条件查询对应流程定义的所有的个人待办
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "按条件查询对应流程定义的所有的个人待办", path = "/myTasksByDefKey", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程定义KEY", varName = "defKey"),@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="按条件查询对应流程定义的所有的个人待办", notes="按条件查询对应流程定义的所有的个人待办")
    @PostMapping(value="/myTasksByDefKey")
    public JsonPageResult myTasksByDefKey(@RequestParam(value = "defKey")String defKey,@RequestBody QueryData queryData) throws Exception{
        JsonPageResult result=JsonPageResult.getSuccess("");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        filter.addSortParam(new SortParam("create_time_","desc"));
        IUser curUser=ContextUtil.getCurrentUser();
        String userId=curUser.getUserId();
        List<String> groupIds=curUser.getRoles();

        IPage<BpmTask> page= bpmTaskService.getByUserIdDefKey(userId,groupIds,defKey,filter);
        result.setPageData(page);
        List<BpmTask> list=page.getRecords();
        //在任务中显示任务执行人与候选用户
        for(BpmTask task:list){
            Set<TaskExecutor> executors=bpmTaskUserService.getTaskExecutors(task);
            task.setTaskExecutors(executors);
        }
        return result;
    }

    /**
     * 按条件查询所有的个人待办
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "按条件查询所有的个人待办", path = "/myTasks", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "查询数据", varName = "queryData")})
    @ApiOperation(value="按条件查询所有的个人待办", notes="按条件查询所有的个人待办")
    @PostMapping(value="/myTasks")
    public JsonPageResult myTasks(@RequestBody QueryData queryData) throws Exception{
        JsonPageResult result=JsonPageResult.getSuccess("");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        filter.addSortParam(new SortParam("create_time_","desc"));
        IUser curUser=ContextUtil.getCurrentUser();
        String userId=curUser.getUserId();
        List<String> groupIds=curUser.getRoles();
        /**
         * 增加租户过滤
         */
        WebUtil.handFilter(filter, ContextQuerySupport.CURRENT,ContextQuerySupport.NONE);

        IPage<BpmTask> page= bpmTaskService.getByUserId(userId,groupIds,ContextUtil.getCurrentTenantId(),filter);
        result.setPageData(page);
        List<BpmTask> list=page.getRecords();
        List<String> instIds=new ArrayList<>();
        //在任务中显示任务执行人与候选用户
        for(BpmTask task:list){
            Set<TaskExecutor> executors=bpmTaskUserService.getTaskExecutors(task);
            instIds.add( task.getInstId());
            task.setTaskExecutors(executors);
        }

        if(BeanUtil.isNotEmpty(instIds)){
            Map<String, String> fieldJsonMap = bpmInstService.getFieldJsonByInstIds(instIds);
            for(BpmTask task:list){
                String fieldJson=fieldJsonMap.get(task.getInstId());
                if(StringUtils.isNotEmpty(fieldJson)){
                    task.setFormJson(fieldJson);
                }
            }
        }



        return result;
    }

    /**
     * 查询我的待办数
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "查询我的待办数", path = "/getMyTaskCount", method = HttpMethodConstants.GET)
    @ApiOperation(value="查询我的待办数")
    @GetMapping(value="/getMyTaskCount")
    public JsonResult getMyTaskCount(){
        IUser curUser=ContextUtil.getCurrentUser();
        Integer counts=bpmTaskService.getCountsByUserId(curUser.getUserId(),curUser.getRoles(),ContextUtil.getCurrentTenantId());
        return new JsonResult(true,counts,"");
    }



    /**
     *根据栏目Id获取数据
     */
    @MethodDefine(title = "根据栏目Id获取数据", path = "/getDataListByUserId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "用户ID", varName = "userId")})
    @ApiOperation(value = "根据栏目Id获取数据")
    @GetMapping("/getDataListByUserId")
    public JsonResult getDataListByUserId(@RequestParam(value="userId") String userId) {
        List<BpmTask> list = bpmTaskService.getAll();
        return JsonResult.Success().setData(list);
    }


    @Override
    protected void handleFilter(QueryFilter filter) {
        //增加分类过滤
        QueryFilterUtil.setQueryFilterByTreeId(filter,"TREE_ID_","v.TREE_ID_","BPM","task.read");
        filter.addSortParam(new SortParam("t.create_time_","desc"));

        filter.addParam(CommonConstant.TENANT_PREFIX,"t.");
        filter.addParam(CommonConstant.COMPANY_PREFIX,"t.");
        filter.addParam(CommonConstant.DELETED_PREFIX,"t.");
        super.handleFilter(filter);
    }

    /**
     * 获取任务所有明细
     * @param taskId
     * @return
     */
    @MethodDefine(title = "获取任务所有明细", path = "/getTaskInfo", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "任务ID", varName = "taskId"),@ParamDefine(title = "审批类型", varName = "checkType"),@ParamDefine(title = "是否手机端", varName = "isMobile")})
    @ApiOperation("获取任务所有明细")
    @GetMapping("getTaskInfo")
    public JsonResult getTaskInfo(@RequestParam(value = "taskId") String taskId,
                                  @RequestParam(value = "checkType", required = false)String checkType,
                                  @RequestParam(value ="isMobile", required = false) String isMobile){

        BpmTaskDetail detail=new BpmTaskDetail();
        BpmTask bpmTask=bpmTaskService.get(taskId);
        //任务已经完成或删除
        if(bpmTask==null){
            JsonResult result=JsonResult.Fail("流程任务已经被处理完成!").setShow(false);
            //如果流程已经被审批，则返回流程实例 ID
            BpmCheckHistory history=bpmCheckHistoryService.getByTaskId(taskId);
            if(history!=null){
                result.setData(history.getInstId());
            }
            return result;
        }
        detail.setBpmTask(bpmTask);
        //转办任务
        if(BpmTask.TYPE_TRANSFER_TASK.equals(bpmTask.getTaskType())){
            detail.setIsTransferRoam(true);
        }
        BpmTransfer bpmTransfer=bpmTransferService.getByTaskId(taskId);
        if(bpmTransfer!=null){
            detail.setIsCanRoamTransfer(true);
        }

        IUser curUser=ContextUtil.getCurrentUser();
        //判断是否能够转办。
        if(curUser.getUserId().equals(bpmTask.getAssignee())){
            detail.setCanTransfer(true);
        }
        //获取任务配置，流程配置
        UserTaskConfig userTaskConfig=(UserTaskConfig)bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        ProcessConfig processConfig=(ProcessConfig) bpmDefService.getProcessConfig(bpmTask.getActDefId());

        if(StringUtils.isNotEmpty(bpmTask.getDefId())){
            detail.setTaskConfig(userTaskConfig);
        }
        if(StringUtils.isNotEmpty(bpmTask.getInstId())){
            BpmInst bpmInst=bpmInstService.get(bpmTask.getInstId());
            detail.setBpmInst(bpmInst);
        }
        if(StringUtils.isNotEmpty(bpmTask.getDefId())){
            detail.setProcessConfig(processConfig);
        }

        //获取流程任务变量
        Map<String,Object> vars=null;
        try{
            vars= runtimeService.getVariables(bpmTask.getActInstId());
        }catch (Exception ex){
            vars=new HashMap<>();
            log.error(ex.getMessage());
        }
        //是否允许回退
        boolean canReject=bpmRuPathService.canReject(bpmTask.getActDefId(),bpmTask.getKey());
        if(canReject){
            detail.setIsCanBack(true);
        }

        //是否允许加签，//TODO需要进行权限控制
        if(BpmTask.TYPE_SEQUENTIAL_TASK.equals(bpmTask.getTaskType())
                || BpmTask.TYPE_PARALLEL_TASK.equals(bpmTask.getTaskType())){
            if(userTaskConfig.getSwitchOptions().contains("allowAddSign")) {
                detail.setCanAddSign(true);
            }
        }

        //是否允许作废
        if(BeanUtil.isNotEmpty(detail.getBpmInst())){
            boolean isDiscard=bpmTaskService.canDiscard(detail.getBpmInst());
            detail.setIsShowDiscardBtn(isDiscard);
        }

        //查看是否生成了沟通任务，如果有沟通任务，就可以查看沟通情况。
        List<BpmTask> cmBpmTasks=bpmTaskService.getCommuByTaskId(bpmTask.getTaskId(),false);
        if(cmBpmTasks.size()>0){
            detail.setCommunicated(true);
        }
        //是否能够回复要看当前任务的状态，而不是看上面是否能查询出记录
        detail.setCanReply(false);
        if(BpmTask.TYPE_LINKUP_TASK .equals(bpmTask.getTaskType()) &&
                !BpmTask.STATUS_COMPLETED.equals(bpmTask.getStatus())        ){
            detail.setCanReply(true);
        }
        //审批意见
        List<BpmCheckHistory> opinionHistoryList= bpmCheckHistoryService.getOpinionNameNotEmpty(bpmTask.getInstId());
        detail.setBpmCheckHistories(opinionHistoryList);

        //判断是否自定义表单
        String formType=bpmTaskService.handFormConfig(processConfig,userTaskConfig);
        ProcessNextCmd cmd= new ProcessNextCmd();
        if(StringUtils.isEmpty(checkType)){
            checkType="AGREE";
        }
        cmd.setCheckType(checkType);
        ProcessHandleUtil.setProcessCmd(cmd);
        if(!BpmTask.FORM_TYPE_SEL_DEV.equals(formType)){
            /**
             * 获取表单数据。
             */
            JsonResult formData= bpmTaskService.getFormData(bpmTask.getInstId(),processConfig,userTaskConfig,isMobile);

            if(!formData.isSuccess()){
                formData.setShow(false);
                return  formData;
            }

            //处理表单意见。
            bpmTaskService.handOpinion(processConfig,userTaskConfig,formData);

            detail.setFormData(formData);

            JSONObject formDataJson=new JSONObject();
            for(BpmView bpmView:(List<BpmView>)formData.getData()){
                formDataJson.put(bpmView.getBoAlias(),bpmView.getData());
            }
            /**
             * 是否允许审批
             */
            JsonResult allowApproveResult=bpmInstService.getAllowApprove(userTaskConfig,formDataJson,vars);
            detail.setAllowApprove(allowApproveResult);
            //取得后续节点的人员映射
            cmd.setBoDataMap(formDataJson);
            cmd.setFormJson(formDataJson.toJSONString());
        }
        List<NodeUsersDto>  nodeUsersDtos= bpmTaskService.getTaskFlowNodesExecutors(bpmTask,vars);
        detail.setNodeExecutors(nodeUsersDtos);

        //沟通任务不能审批
        if(BpmTask.TYPE_LINKUP_TASK.equals(bpmTask.getTaskType())){
            detail.setCanCheck(false);
        }
        //获取是否有候选人。
        List<BpmTaskUser> taskUsers=bpmTaskUserService.getByTaskId(taskId);
        if(taskUsers.size()>0){
            bpmTask.setHasCandicate(true);
        }

        //如果未读，如果有权限的人 则更新为正在处理。
        if(BpmTask.STATUS_UNHANDLE.equals(bpmTask.getStatus())){
            boolean hasHandlerRight=bpmTaskService.hasHandlerRight(bpmTask,taskUsers);
            if(hasHandlerRight){
                bpmTask.setStatus(BpmTask.STATUS_HANDLE);
                bpmTaskService.update(bpmTask);
            }
        }
        /**
         * 是否tracked.
         */
        JsonResult tabResult= bpmInstTrackedService.getTracked(bpmTask.getInstId(),curUser.getUserId());
        detail.setTracked(tabResult.isSuccess()?"1":"0");

        JsonResult result=JsonResult.Success();
        result.setData(detail);

        return result;
    }

    /**
     * 任务审批/回退
     * @param cmd
     * @return
     */
    @MethodDefine(title = "任务审批/回退", path = "/completeTask", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "审批参数", varName = "cmd")})
    @ApiOperation("任务审批/回退")
    @AuditLog(operation = "任务审批/回退")
    @PostMapping("completeTask")
    public JsonResult completeTask(@ApiParam @RequestBody ProcessNextCmd cmd) {
        try{
            ProcessHandleUtil.clearProcessCmd();
            ProcessHandleUtil.setProcessCmd(cmd);
            //构建一个线程变量
            JsonResult result= bpmTaskService.completeTask(cmd);

            //发送任务消息通知
            messageService.sendMsg();

            return result;
        }
        catch (Exception ex){
            bpmTaskService.handException(ex);
            return null;
        }


    }


    /**
     * 参数格式:
     * {
     *      taskIds:"",
     *      checkType:"",
     *      opinion:""
     * }
     * @param json
     * @return
     */
    @MethodDefine(title = "批量任务审批", path = "/batCompleteTask", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "审批参数", varName = "json")})
    @ApiOperation("批量任务审批")
    @PostMapping("batCompleteTask")
    @AuditLog(operation = "任务批量审批")
    public JsonResult batCompleteTask(@ApiParam @RequestBody JSONObject json) {

        String taskIds=json.getString("taskIds");
        String checkType=json.getString("checkType");
        String opinion=json.getString("opinion");

        if(StringUtils.isEmpty(taskIds)){
            return JsonResult.Fail("任务ID为空!");
        }
        String[] aryTask=taskIds.split(",");
        for(String taskId:aryTask){

            ProcessNextCmd cmd=new ProcessNextCmd();
            cmd.setTaskId(taskId);
            cmd.setOpinion(opinion);
            cmd.setCheckType(checkType);

            ProcessHandleUtil.clearProcessCmd();
            ProcessHandleUtil.setProcessCmd(cmd);

            JsonResult result= bpmTaskService.completeTask(cmd);
            //发送任务消息通知
            messageService.sendMsg();
        }

        return JsonResult.Success("批量审批成功!");

    }

    /**
     * 取回任务
     * @param instId 实例Id
     * @param nodeId 节点Id
     * @return
     */
    @MethodDefine(title = "取回任务", path = "/takeBackTask", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "实例Id", varName = "instId"),@ParamDefine(title = "节点Id", varName = "nodeId")})
    @ApiOperation("取回任务")
    @PostMapping("takeBackTask")
    public JsonResult takeBackTask(@RequestBody JSONObject params){
        String instId=params.getString("instId");
        String nodeId=params.getString("backNodeId");
        String nextJumpType=params.getString("nextJumpType");
        String opinion=params.getString("opinion");
        //获取流程实例对应的待办列表
        List<BpmTask> bpmTasks=bpmTaskService.getByInstId(instId);
        if(bpmTasks==null || bpmTasks.size()==0){
            return new JsonResult(true,"当前事项已经没有待办任务，不能取回！");
        }

        //取回当前有actTaskId的任务
        String taskId=null;
        for (BpmTask task:bpmTasks){
            if(StringUtils.isNotEmpty(task.getActTaskId())){
                taskId=task.getTaskId();
            }
        }
        if(taskId==null){
            return new JsonResult(true,"当前事项已经没有待办任务，不能取回！");
        }

        //获取单据的数据 TODO
        ProcessNextCmd cmd=new ProcessNextCmd();
        cmd.setTaskId(taskId);
        cmd.setCheckType(TaskOptionType.RECOVER.name());
        cmd.setNextJumpType(nextJumpType);
        cmd.setOpinion(opinion);
        cmd.setDestNodeId(nodeId);
        return completeTask(cmd);

    }

    /**
     * 回复任务沟通
     * @param reply
     * @return
     */
    @ApiOperation("回复任务沟通")
    @AuditLog(operation = "回复任务沟通")
    @PostMapping("replyLinkupTask")
    public JsonResult replyLinkupTask(@ApiParam @RequestBody TaskLinkupReply reply){
        JsonResult result= bpmTaskService.doCompleteLinkupTask(reply);
        return result;
    }


    /**
     * 保存单据的数据
     * @param cmd
     * @return
     */
    @ApiOperation("保存单据的数据")
    @PostMapping("saveFormData")
    public JsonResult saveFormData(@ApiParam @RequestBody ProcessNextCmd cmd){
        JsonResult result=new JsonResult(true,"成功保存单据数据！");
        ProcessHandleUtil.clearProcessCmd();
        ProcessHandleUtil.setProcessCmd(cmd);
        try{
            bpmTaskService.saveData(cmd);
        }
        catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
            result=JsonResult.getFailResult("保存表单数据失败");
        }
        return result;
    }

    /**
     * 更新任务状态
     * @param taskId
     * @return
     */
    @MethodDefine(title = "更新状态", path = "/updateLocked", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "任务ID", varName = "taskId")})
    @ApiOperation("更新状态")
    @PostMapping("updateLocked")
    public JsonResult updateLocked(@ApiParam @RequestParam("taskId") String taskId){
        JsonResult result= JsonResult.Success();
        String msg="";
        String currentUserId=ContextUtil.getCurrentUserId();
        BpmTask bpmTask=bpmTaskService.get(taskId);
        List<BpmTaskUser> taskUsers= bpmTaskUserService.getByTaskId(taskId);
        if(taskUsers.size()>0){
            bpmTask.setStatus(BpmTask.STATUS_HANDLE);
            if(BeanUtil.isEmpty( bpmTask.getAssignee())){
                msg="对流程任务（" +  bpmTask.getName()+"）进行了锁定操作！";
                bpmTask.setAssignee(currentUserId);
            }
            else{
                msg="对流程任务（" +  bpmTask.getName()+"）进行了释放操作！";
                bpmTask.setAssignee(null);
            }
            bpmTaskService.update(bpmTask);
            bpmInstLogService.addTaskLog(bpmTask.getInstId(),bpmTask.getTaskId(),
                    bpmTask.getName(),bpmTask.getKey(),msg);
        }
        else{
            msg="非候选任务不需要操作！";
        }
        result.setMessage(msg);
        return result;
    }

    @MethodDefine(title = "根据流程实例Id获取任务列表", path = "/getByInstId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程实例Id", varName = "instId")})
    @ApiOperation("根据流程实例Id获取任务列表")
    @GetMapping("/getByInstId")
    public List<BpmTaskDto> getByInstId(@ApiParam @RequestParam(value = "instId") String instId) {
        List<BpmTask> bpmTasks = bpmTaskService.getByInstId(instId);
        List<BpmTaskDto> bpmTaskDtos = new ArrayList<>();
        if(BeanUtil.isNotEmpty(bpmTasks)) {
            for(BpmTask bpmTask:bpmTasks){
                BpmTaskDto bpmTaskDto = new BpmTaskDto();
                BeanUtil.copyProperties(bpmTaskDto,bpmTask);
                bpmTaskDtos.add(bpmTaskDto);
            }
            BeanUtil.copyProperties(bpmTaskDtos, bpmTasks);
        }
        return bpmTaskDtos;
    }

    @MethodDefine(title = "根据流程实例ActId获取任务列表", path = "/getByActInstId", method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "流程实例Id", varName = "actInstId")})
    @ApiOperation("根据流程实例ActId获取任务列表")
    @GetMapping("/getByActInstId")
    public List<BpmTaskDto> getByActInstId(@ApiParam @RequestParam(value = "actInstId") String actInstId) {
        List<BpmTask> bpmTasks = bpmTaskService.getByActInstId(actInstId);
        List<BpmTaskDto> bpmTaskDtos = new ArrayList<>();
        if(BeanUtil.isNotEmpty(bpmTasks)) {
            for(BpmTask bpmTask:bpmTasks){
                BpmTaskDto bpmTaskDto = new BpmTaskDto();
                BeanUtil.copyProperties(bpmTaskDto,bpmTask);
                bpmTaskDtos.add(bpmTaskDto);
            }
            BeanUtil.copyProperties(bpmTaskDtos, bpmTasks);
        }
        return bpmTaskDtos;
    }

    @MethodDefine(title = "获取任务后续节点及其执行人员", path = "/getTaskFlowNodesExecutors", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "任务参数", varName = "taskExeParam")})
    @ApiOperation("获取任务后续节点及其执行人员")
    @PostMapping("getTaskFlowNodesExecutors")
    public List<NodeUsersDto> getTaskFlowNodesExecutors(@RequestBody TaskExeParam taskExeParam){
        try{
            BpmTask bpmTask=bpmTaskService.get(taskExeParam.getTaskId());
            Map<String,Object> vars=runtimeService.getVariables(bpmTask.getActInstId());

            ProcessNextCmd cmd = new ProcessNextCmd();
            cmd.setCheckType(taskExeParam.getCheckType());
            //获取将表单数据转成bo别名和数据的映射。
            JSONObject boDataMap = bpmDefService.getBoDataMap(taskExeParam.getFormData());
            cmd.setBoDataMap(boDataMap);
            cmd.setFormJson(taskExeParam.getFormData().toJSONString());
            ProcessHandleUtil.setProcessCmd(cmd);

            List<NodeUsersDto> nodeExecutors = bpmTaskService.getTaskFlowNodesExecutors(bpmTask, vars);
            return nodeExecutors;
        }
        finally {
            ProcessHandleUtil.clearProcessCmd();
        }

    }


    /**
     * 添加会签用户
     * @param taskAddSign
     * @return
     */
    @MethodDefine(title = "任务添加会签用户", path = "/addSignUser", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "会签参数", varName = "taskAddSign")})
    @ApiOperation("任务添加会签用户")
    @AuditLog(operation = "任务添加会签用户")
    @PostMapping("addSignUser")
    public JsonResult addSignUser(@RequestBody TaskAddSign taskAddSign){
        bpmTaskService.addTaskData(taskAddSign);
        return new JsonResult(true,"成功添加会签用户！");
    }

    @MethodDefine(title = "任务沟通", path = "/linkup", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "沟通参数", varName = "taskLinkup")})
    @ApiOperation("任务沟通")
    @AuditLog(operation = "任务沟通")
    @PostMapping("linkup")
    public JsonResult linkup(@RequestBody TaskLinkup taskLinkup) throws Exception {
        try{
            bpmTaskService.taskLinkups(taskLinkup);
            JsonResult jsonResult=new JsonResult(true,"成功沟通！");
            return jsonResult;
        }
        catch (Exception ex){
            MessageUtil.triggerException("发送沟通失败!",ExceptionUtil.getExceptionMessage(ex));
            return null;
        }

    }

    /**
     * 流程干预跳转
     * @param taskNodeJump
     * @return
     * @throws Exception
     */
    @MethodDefine(title = "任务干预跳转", path = "/jumpToNode", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "跳转参数", varName = "taskNodeJump")})
    @ApiOperation("任务干预跳转")
    @AuditLog(operation = "任务干预跳转")
    @PostMapping("jumpToNode")
    public JsonResult jumpToNode(@RequestBody TaskNodeJump taskNodeJump) throws Exception {
        try{
            BpmTask bpmTask=bpmTaskService.get(taskNodeJump.getTaskId());

            if(bpmTask==null){
              List<BpmTask> bpmTasks = bpmTaskService.getByActInstId(taskNodeJump.getActInstId());
              if(bpmTasks.size()>0){
                  bpmTask=bpmTasks.get(0);
              }
            }

            if(bpmTask==null){
                return new JsonResult(false,"当前任务已经完成,不允许干预跳转");
            }

            ProcessNextCmd cmd=new ProcessNextCmd();
            cmd.setTaskId(bpmTask.getTaskId());
            cmd.setCheckType(TaskOptionType.INTERPOSE.name());
            cmd.setDestNodeId(taskNodeJump.getDestNodeId());
            //设置目标节点的人员
            cmd.setNodeUserIds(taskNodeJump.getDestNodeId(),taskNodeJump.getToUserIds());

            cmd.setOpinion(taskNodeJump.getOpinion());
            return completeTask(cmd);
        } catch (Exception ex){
            MessageUtil.triggerException("任务干预跳转失败!",ExceptionUtil.getExceptionMessage(ex));
            return null;
        }
    }


    @MethodDefine(title = "撤销沟通任务", path = "/revokeCmTask", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "任务ID", varName = "taskId"),@ParamDefine(title = "是否删除意见", varName = "delOpinion")})
    @ApiOperation("撤销沟通任务")
    @AuditLog(operation = "撤销沟通任务")
    @PostMapping("revokeCmTask")
    public JsonResult revokeCmTask(@RequestParam(value = "taskId") String taskId,
                                   @RequestParam(value = "delOpinion") boolean delOpinion ){
        try{
            bpmTaskService.doRevokeLinkupTask(taskId,delOpinion);
            return new JsonResult(true,"成功撤回！");
        }
        catch (Exception ex){
            MessageUtil.triggerException("撤销沟通任务失败！!",ExceptionUtil.getExceptionMessage(ex));
            return null;
        }
    }

    @MethodDefine(title = "取消任务流转",path = "/cancelTransRoamTask",method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "任务ID",varName = "taskId")})
    @ApiOperation("取消任务流转")
    @AuditLog(operation = "取消任务流转")
    @GetMapping("cancelTransRoamTask")
    public JsonResult cancelTransRoamTask(@RequestParam(value = "taskId")String taskId){
        try{
            bpmTaskService.doCancelTransRoamTask(taskId);
            return new JsonResult(true,"成功取消任务流转！");
        }
        catch (Exception ex){
            MessageUtil.triggerException("取消任务流转失败!",ExceptionUtil.getExceptionMessage(ex));
            return null;
        }
    }

    @MethodDefine(title = "任务流转", path = "/transRoamTask", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "json")})
    @ApiOperation("任务流转")
    @AuditLog(operation = "任务流转")
    @PostMapping("transRoamTask")
    public JsonResult transRoamTask(@RequestBody  JSONObject json){
        JsonResult rtn= bpmTaskService.doTransRoamTask(json);
        return rtn;
    }

    @MethodDefine(title = "任务转办", path = "/transTask", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "请求数据", varName = "json")})
    @ApiOperation("任务转办")
    @AuditLog(operation = "任务转办")
    @PostMapping("transTask")
    public JsonResult transTask(@RequestBody  JSONObject json){
        JsonResult rtn= bpmTaskService.doTransTask(json);
        return rtn;
    }

    @MethodDefine(title = "获取沟通任务", path = "/getCommuTasks", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "任务ID", varName = "taskId")})
    @ApiOperation("获取沟通任务")
    @PostMapping("getCommuTasks")
    public JsonResult getCommuTasks(@ApiParam @RequestParam(value = "taskId") String taskId) {
        JsonResult result=JsonResult.Success();
        try{
            List<BpmTask> tasks= bpmTaskService.getCommuByTaskId(taskId,true);
            result.setData(tasks);
        }
        catch (Exception ex){
            MessageUtil.triggerException("获取沟通任务失败!",ExceptionUtil.getExceptionMessage(ex));
        }
        return result;
    }

    @MethodDefine(title = "获取流转任务日志", path = "/getRoamTasks", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "任务ID", varName = "taskId")})
    @ApiOperation("获取流转任务日志")
    @PostMapping("getRoamTasks")
    public JsonResult getRoamTasks(@ApiParam @RequestParam(value = "taskId") String taskId) {
        JsonResult result=JsonResult.Success();
        try{
            List<BpmTransferLog> tasks= bpmTransferLogService.getByTaskId(taskId);
            result.setData(tasks);
        }
        catch (Exception ex){
            MessageUtil.triggerException("获取流转任务日志失败!",ExceptionUtil.getExceptionMessage(ex));
        }
        return result;
    }

    @MethodDefine(title = "判断是否可以沟通", path = "/canReplyCommu", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "任务ID", varName = "taskId")})
    @ApiOperation("判断是否可以沟通")
    @PostMapping("canReplyCommu")
    public JsonResult canReplyCommu(@ApiParam @RequestParam(value = "taskId") String taskId) {
        JsonResult result=JsonResult.Success();

        BpmTask bpmTask=  bpmTaskService.get(taskId);
        if(BpmTask.STATUS_COMPLETED.equals( bpmTask.getStatus())){
            JsonResult fail= JsonResult.Fail("已经回复沟通");
            return fail;
        }
        result.setShow(false);
        return result;

    }

    @MethodDefine(title = "根据任务ID获取表单数据",path = "/getFormDataByTaskId",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "任务ID",varName = "taskId")})
    @ApiOperation("根据任务ID获取表单数据")
    @PostMapping("/getFormDataByTaskId")
    public JsonResult getFormDataByTaskId(@RequestParam(value="taskId")String taskId) {
        BpmTask bpmTask=bpmTaskService.get(taskId);
        UserTaskConfig userTaskConfig=(UserTaskConfig)bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        ProcessConfig processConfig=bpmDefService.getProcessConfig(bpmTask.getActDefId());
        /**
         * 获取表单数据。
         */
        JsonResult formData= bpmTaskService.getFormData(bpmTask.getInstId(),processConfig,userTaskConfig,null);
        return formData;

    }

}
