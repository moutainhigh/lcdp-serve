package com.redxun.bpm.restApi;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.redxun.annotation.CurrentUser;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.dto.NodeUsersDto;
import com.redxun.bpm.core.dto.TaskLinkup;
import com.redxun.bpm.core.dto.TaskLinkupReply;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonPageResult;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.base.entity.QueryData;
import com.redxun.common.base.search.QueryFilter;
import com.redxun.common.base.search.QueryFilterBuilder;
import com.redxun.common.base.search.SortParam;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.common.utils.FileUtil;
import com.redxun.common.utils.MessageUtil;
import com.redxun.controller.RestApiController;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.form.BpmView;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.UserClient;
import com.redxun.feign.sys.SystemClient;
import com.redxun.idempotence.IdempotenceRequired;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * ??????????????????API
 */
@Slf4j
@RestController
@RequestMapping("/restApi/bpm")
@ClassDefine(title = "??????????????????API", alias = "bpm", path = "/restApi/bpm")
public class BpmApiController implements RestApiController {



    @Autowired
    BpmInstServiceImpl bpmInstService;
    @Autowired
    BpmDefService bpmDefService;
    @Autowired
    BpmTaskService bpmTaskService;
    @Autowired
    BpmTaskUserServiceImpl bpmTaskUserService;
    @Autowired
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Autowired
    BpmTaskTransferServiceImpl bpmTaskTransferService;
    @Autowired
    BpmRuPathServiceImpl bpmRuPathService;
    @Autowired
    SystemClient systemClient;
    @Autowired
    UserClient userClient;

    @Autowired
    RuntimeService runtimeService;
    @Resource
    RepositoryService repositoryService;
    @Resource
    ProcessDiagramGenerator diagramGenerator;
    @Resource
    BpmTransferServiceImpl bpmTransferService;




    @MethodDefine(title = "????????????",path = "/startProcess",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????????????????",varName = "startCmd")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/startProcess")
    public JsonResult<BpmInst> startProcess(@RequestBody ProcessStartCmd startCmd){


        JsonResult jsonResult=handReq(startCmd);
        if(!jsonResult.isSuccess()){
            return  jsonResult;
        }
        BpmDef bpmDef= (BpmDef) jsonResult.getData();
        boolean hasRight=systemClient.findAuthRight("BPM","def.start",bpmDef.getTreeId());
        if(!hasRight){
            return JsonResult.Fail("????????????????????????");
        }
        try {
            //????????????
            BpmInst bpmInst=bpmInstService.doStartProcess(startCmd,bpmDef,"start");
            return jsonResult.setData(bpmInst)
                    .setSuccess(true).setMessage("?????????????????????");
        }
        catch (Exception ex){
            bpmInstService.handStartException(ex);
            return null;
        }
    }

    /**
     * ???????????????
     * @param cmd
     * @return
     */
    private JsonResult handReq(ProcessStartCmd cmd){
        JsonResult jsonResult=new JsonResult();
        //????????????
        if(StringUtils.isEmpty(cmd.getDefId())
                && StringUtils.isEmpty(cmd.getDefKey())){
            return jsonResult.setSuccess(false).setMessage("????????????defId???defKey");
        }
        BpmDef bpmDef=null;
        if(StringUtils.isNotEmpty(cmd.getDefId())) {
            bpmDef=bpmDefService.get(cmd.getDefId());
        }else if(StringUtils.isNotEmpty(cmd.getDefKey())){
            bpmDef=bpmDefService.getMainByKey(cmd.getDefKey());
        }
        //???????????????????????????????????????????????????
        if(bpmDef==null && StringUtils.isEmpty( cmd.getInstId())){
            return jsonResult.setSuccess(false).setMessage("?????????????????????!");
        }
        cmd.setDefId(bpmDef.getDefId());
        cmd.setActDefId(bpmDef.getActDefId());
        cmd.setDefKey(bpmDef.getKey());

        ProcessHandleUtil.clearProcessCmd();
        ProcessHandleUtil.setProcessCmd(cmd);

        return jsonResult.setData(bpmDef);
    }

    @MethodDefine(title="????????????/??????",path = "/completeTask",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????????????????",varName = "nextCmd")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/completeTask")
    public JsonResult completeTask(@RequestBody ProcessNextCmd nextCmd){
        try{

            ProcessHandleUtil.clearProcessCmd();
            ProcessHandleUtil.setProcessCmd(nextCmd);
            return bpmTaskService.completeTask(nextCmd);
        } catch (Exception ex){
            bpmTaskService.handException(ex);
            return null;
        }
    }

    @MethodDefine(title="????????????????????????????????????",path = "/getTasksByUserAccount",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "queryData")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getTasksByUserAccount")
    public JsonPageResult getTasksByUserAccount(@RequestBody QueryData queryData){


        JsonPageResult result=JsonPageResult.getSuccess("");
        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        filter.addSortParam(new SortParam("create_time_","desc"));
        String userId=ContextUtil.getCurrentUserId();
        List<String> groupIds= userClient.getUserById(userId).getRoles();

        IPage<BpmTask> page= bpmTaskService.getByUserId(userId,groupIds,ContextUtil.getCurrentTenantId(),filter);
        result.setPageData(page);
        List<BpmTask> list=page.getRecords();
        for(BpmTask task:list){
            Set<TaskExecutor> executors=bpmTaskUserService.getTaskExecutors(task);
            task.setTaskExecutors(executors);
        }
        return result;

    }

    @MethodDefine(title="????????????????????????????????????",path = "/getTaskCountsByUserAccount",method = HttpMethodConstants.POST)
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getTaskCountsByUserAccount")
    public JsonResult getTaskCountsByUserAccount(){

        JsonResult rtn = new JsonResult(true);
        IUser curUser=ContextUtil.getCurrentUser();
        String curUserId=curUser.getUserId();
        List<String> groupIds=curUser.getRoles();
        Integer counts= bpmTaskService.getCountsByUserId(curUserId,groupIds,ContextUtil.getCurrentTenantId());
        rtn.setData(counts);
        return rtn;
    }

    @MethodDefine(title = "????????????????????????????????????",path = "/getMySolutions",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "queryData")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getMySolutions")
    public JsonPageResult getMySolutions(@RequestBody QueryData queryData){
        JsonPageResult jsonResult=JsonPageResult.getSuccess("??????????????????!");


        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        filter.addQueryParam("Q_status__S_EQ",BpmDef.STATUS_DEPLOYED);
        filter.addQueryParam("Q_IS_MAIN__S_EQ", BpmDef.IS_MAIN);
        IPage page= bpmDefService.query(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }

    @MethodDefine(title = "????????????ID??????????????????",path = "/getTaskOutNodes",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID",varName = "taskId"),@ParamDefine(title = "????????????",varName = "checkType")})
    @IdempotenceRequired
    @PostMapping("/getTaskOutNodes")
    public JsonResult getTaskOutNodes(@RequestParam(value = "taskId",required = false)String taskId,@RequestParam(value = "checkType",required = false)String checkType){
        if(StringUtils.isEmpty(taskId)){
            return JsonResult.Fail("??????ID?????????");
        }
        BpmTask bpmTask=bpmTaskService.get(taskId);
        if(BeanUtil.isEmpty(bpmTask)){
            return JsonResult.Fail("??????????????????");
        }
        Map<String,Object> vars= runtimeService.getVariables(bpmTask.getActInstId());
        UserTaskConfig userTaskConfig = (UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(), bpmTask.getKey());
        ProcessConfig processConfig = (ProcessConfig) bpmDefService.getProcessConfig(bpmTask.getActDefId());
        JsonResult formData = bpmTaskService.getFormData(bpmTask.getInstId(), processConfig, userTaskConfig, null);
        JSONObject formDataJson = new JSONObject();
        for (BpmView bpmView : (List<BpmView>) formData.getData()) {
            formDataJson.put(bpmView.getBoAlias(), bpmView.getData());
        }
        //?????????????????????????????????
        ProcessNextCmd cmd= new ProcessNextCmd();
        if(StringUtils.isEmpty(checkType)){
            checkType="AGREE";
        }
        cmd.setCheckType(checkType);
        cmd.setBoDataMap(formDataJson);
        cmd.setFormJson(formDataJson.toJSONString());
        ProcessHandleUtil.setProcessCmd(cmd);
        //?????????????????????????????????
        List<NodeUsersDto>  nodeUsersDtos= bpmTaskService.getTaskFlowNodesExecutors(bpmTask,vars);
        return JsonResult.getSuccessResult(nodeUsersDtos);
    }

    @MethodDefine(title = "??????????????????ID????????????????????????",path = "/getTasksByInstId",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID",varName = "instId")})
    @IdempotenceRequired
    @PostMapping("/getTasksByInstId")
    public JsonResult getTasksByInstId(@RequestParam(value = "instId",required = false)String instId){
        if(StringUtils.isEmpty(instId)){
            return JsonResult.Fail("??????ID?????????");
        }
        List<BpmTask> list=bpmTaskService.getByInstId(instId);
        return JsonResult.getSuccessResult(list);
    }

    @MethodDefine(title = "??????????????????ID??????????????????",path = "/getCheckHistorys",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID",varName = "instId")})
    @IdempotenceRequired
    @PostMapping("/getCheckHistorys")
    public JsonResult getCheckHistorys(@RequestParam(value = "instId",required = false)String instId) {
        if (StringUtils.isEmpty(instId)) {
            return JsonResult.Fail("??????ID?????????");
        }
        List<BpmCheckHistory> list = bpmCheckHistoryService.getByInstId(instId);
        return JsonResult.getSuccessResult(list);
    }

    @MethodDefine(title = "????????????",path = "/linkup",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "taskLinkup")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/linkup")
    public JsonResult linkup(@RequestBody TaskLinkup taskLinkup) {
        try{

            bpmTaskService.taskLinkups(taskLinkup);
            JsonResult jsonResult=new JsonResult(true,"???????????????");
            return jsonResult;
        } catch (Exception ex){
            String message=ExceptionUtil.getExceptionMessage(ex);
            MessageUtil.triggerException("??????????????????!",message);
            return null;
        }
    }

    @MethodDefine(title = "??????????????????",path = "/replyLinkupTask",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????????????????",varName = "reply")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/replyLinkupTask")
    public JsonResult replyLinkupTask(@RequestBody TaskLinkupReply reply) {


        JsonResult result= bpmTaskService.doCompleteLinkupTask(reply);
        return result;

    }

    @MethodDefine(title = "??????????????????",path = "/revokeCmTask",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID",varName = "taskId"),@ParamDefine(title = "????????????????????????",varName = "delOpinion")})
    @IdempotenceRequired
    @PostMapping("/revokeCmTask")
    public JsonResult revokeCmTask(@RequestParam(value="taskId")String taskId,@RequestParam(value="delOpinion")boolean delOpinion) {
        try{
            bpmTaskService.doRevokeLinkupTask(taskId,delOpinion);
            return new JsonResult(true,"???????????????");
        }
        catch (Exception ex){
            MessageUtil.triggerException("????????????????????????!",ExceptionUtil.getExceptionMessage(ex));
            return null;
        }
    }

    @MethodDefine(title = "????????????ID??????????????????",path = "/getFormDataByTaskId",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID",varName = "taskId")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getFormDataByTaskId")
    public JsonResult getFormDataByTaskId(@RequestParam(value="taskId")String taskId) {


            BpmTask bpmTask=bpmTaskService.get(taskId);
            UserTaskConfig userTaskConfig=(UserTaskConfig)bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
            ProcessConfig processConfig=bpmDefService.getProcessConfig(bpmTask.getActDefId());
            /**
             * ?????????????????????
             */
            JsonResult formData= bpmTaskService.getFormData(bpmTask.getInstId(),processConfig,userTaskConfig,null);
            return formData;

    }

    @MethodDefine(title = "????????????ID??????????????????",path = "/getFormDataByInstId",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID",varName = "instId")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getFormDataByInstId")
    public JsonResult getFormDataByInstId(@RequestParam(value="instId")String instId) {


            BpmInst bpmInst=bpmInstService.get(instId);
            ProcessConfig processConfig=bpmDefService.getProcessConfig(bpmInst.getActDefId());
            /**
             * ?????????????????????
             */
            JsonResult formData= bpmTaskService.getFormData(bpmInst.getInstId(),processConfig,null,null);
            return formData;

    }

    @MethodDefine(title = "??????????????????????????????",path = "/getMyStartInsts",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "queryData")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getMyStartInsts")
    public JsonPageResult getMyStartInsts(@RequestBody QueryData queryData){
        JsonPageResult jsonResult=JsonPageResult.getSuccess("??????????????????!");


        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String curUserId= ContextUtil.getCurrentUserId();
        filter.addParam("userId",curUserId);
        IPage page= bpmInstService.getByUserId(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }

    @MethodDefine(title = "??????????????????",path = "/takeBackTask",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID",varName = "instId"),@ParamDefine(title = "??????ID",varName = "nodeId")})
    @IdempotenceRequired
    @PostMapping("/takeBackTask")
    public JsonResult takeBackTask(@RequestParam(value="instId")String instId,
                                   @RequestParam(value="nodeId")String nodeId) {
        //???????????????????????????????????????
        List<BpmTask> bpmTasks = bpmTaskService.getByInstId(instId);
        if (bpmTasks == null || bpmTasks.size() == 0) {
            return new JsonResult(true, "??????????????????????????????????????????????????????");
        }

        //???????????????actTaskId?????????
        String taskId = null;
        for (BpmTask task : bpmTasks) {
            if (StringUtils.isNotEmpty(task.getActTaskId())) {
                taskId = task.getTaskId();
            }
        }
        if (taskId == null) {
            return new JsonResult(true, "??????????????????????????????????????????????????????");
        }

        ProcessNextCmd cmd = new ProcessNextCmd();
        cmd.setTaskId(taskId);
        cmd.setCheckType(TaskOptionType.RECOVER.name());
        cmd.setDestNodeId(nodeId);
        cmd.setOpinion("??????");
        return completeTask( cmd);
    }

    @MethodDefine(title = "???????????????????????????",path = "/getMyReceiveTask",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "queryData")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getMyReceiveTask")
    public JsonPageResult getMyReceiveTask(@RequestBody QueryData queryData){
        JsonPageResult jsonResult=JsonPageResult.getSuccess("??????????????????!");


        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String currentUserId= ContextUtil.getCurrentUserId();
        filter.addParam("TO_USER_ID_",currentUserId);
        IPage page= bpmTaskTransferService.getMyReceiveTasks(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }

    @MethodDefine(title = "??????????????????",path = "/getInstDetail",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID",varName = "instId")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getInstDetail")
    public JsonResult getInstDetail(
                                    @RequestParam(value = "instId")String instId) {
        if (StringUtils.isEmpty(instId)) {
            return JsonResult.Fail("??????ID?????????");
        }

        BpmInstDetail detail = bpmInstService.getInstDetail(instId);
        return JsonResult.getSuccessResult(detail,"???????????????");
    }

    @MethodDefine(title = "???????????????????????????????????????",path = "/getMyApproved",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "queryData")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getMyApproved")
    public JsonPageResult getMyApproved(@RequestBody QueryData queryData){



        JsonPageResult jsonResult=JsonPageResult.getSuccess("??????????????????!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String currentUserId= ContextUtil.getCurrentUserId();
        filter.addParam("userId",currentUserId);
        IPage page= bpmInstService.getMyApproved(filter);
        jsonResult.setPageData(page);

        return jsonResult;

    }

    @MethodDefine(title = "????????????????????????????????????",path = "/getMyApprovedCounts",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "queryData")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getMyApprovedCounts")
    public JsonResult getMyApprovedCounts(){


        JsonResult jsonResult=JsonPageResult.getSuccess("??????????????????!");

        String currentUserId= ContextUtil.getCurrentUserId();
        Integer counts= bpmInstService.getMyApprovedCount(currentUserId,ContextUtil.getCurrentTenantId());
        jsonResult.setData(counts);

        return jsonResult;

    }

    @MethodDefine(title = "??????????????????",path = "/delInstById",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????ID",varName = "instId")})
    @IdempotenceRequired
    @PostMapping("/delInstById")
    public JsonResult delInstById(@RequestParam(value="instId")String instId){
        if(StringUtils.isEmpty(instId)){
            return JsonResult.Fail("??????ID?????????");
        }
        String[] idArr=instId.split("[,]");
        List<String> idList= Arrays.asList(idArr);
        bpmInstService.delete((List)idList);
        return new JsonResult(true,"?????????????????????????????????");
    }

    @MethodDefine(title = "??????????????????",path = "/saveDraft",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "??????????????????",varName = "startCmd")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/saveDraft")
    public JsonResult<BpmInst> saveDraft(@RequestBody ProcessStartCmd startCmd){


            JsonResult jsonResult = handReq(startCmd);

            if (!jsonResult.isSuccess()) {
                return jsonResult;
            }
            BpmDef bpmDef = (BpmDef) jsonResult.getData();
            //????????????
            BpmInst bpmInst = bpmInstService.doSaveDraft(startCmd, bpmDef);

            return jsonResult.setData(bpmInst)
                    .setSuccess(true).setMessage("?????????????????????");

    }

    @MethodDefine(title = "????????????????????????",path = "/getMyDraft",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "queryData")})
    @IdempotenceRequired
    @CurrentUser
    @PostMapping("/getMyDraft")
    public JsonPageResult getMyDraft(@RequestBody QueryData queryData){
        JsonPageResult jsonResult=JsonPageResult.getSuccess("??????????????????!");


        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        String currentUserId= ContextUtil.getCurrentUserId();
        filter.addParam("userId",currentUserId);
        filter.addQueryParam("Q_i.STATUS__S_EQ",BpmDef.STATUS_DRAFT);
        IPage page= bpmInstService.getByUserId(filter);
        jsonResult.setPageData(page);

        return jsonResult;
    }

    @MethodDefine(title = "????????????ID??????????????????????????????",path = "/getTaskInfo",method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????",varName = "checkType"),@ParamDefine(title = "??????ID",varName = "taskId")})
    @IdempotenceRequired
    @GetMapping("/getTaskInfo")
    public BpmTaskDetail getTaskInfo(@RequestParam(required = false)String checkType,@RequestParam(value="taskId")String taskId) {


            BpmTaskDetail detail = new BpmTaskDetail();
            BpmTask bpmTask = bpmTaskService.get(taskId);

            detail.setBpmTask(bpmTask);
            //???????????????????????????
            if (bpmTask == null) {
                return detail;
            }
            if(BpmTask.TYPE_TRANSFER_TASK.equals(bpmTask.getTaskType())){
                detail.setIsTransferRoam(true);
            }
            BpmTransfer bpmTransfer=bpmTransferService.getByTaskId(taskId);
            if(bpmTransfer!=null){
                detail.setIsCanRoamTransfer(true);
            }
            IUser curUser = ContextUtil.getCurrentUser();

            detail.setCurUser(curUser);
            //???????????????????????????
            if (curUser.getUserId().equals(bpmTask.getAssignee())) {
                detail.setCanTransfer(true);
            }


            UserTaskConfig userTaskConfig = (UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(), bpmTask.getKey());
            ProcessConfig processConfig = (ProcessConfig) bpmDefService.getProcessConfig(bpmTask.getActDefId());

            if (StringUtils.isNotEmpty(bpmTask.getDefId())) {
                detail.setTaskConfig(userTaskConfig);
            }
            if (StringUtils.isNotEmpty(bpmTask.getInstId())) {
                BpmInst bpmInst = bpmInstService.get(bpmTask.getInstId());
                detail.setBpmInst(bpmInst);
            }
            if (StringUtils.isNotEmpty(bpmTask.getDefId())) {
                detail.setProcessConfig(processConfig);
            }

            //????????????????????????????????????????????????
            Map<String, Object> vars = null;
            try {
                vars = runtimeService.getVariables(bpmTask.getActInstId());
            } catch (Exception ex) {
                vars = new HashMap<>();
                log.error(ex.getMessage());
            }
            //??????????????????
            boolean canReject = bpmRuPathService.canReject(bpmTask.getActDefId(), bpmTask.getKey());
            if (canReject) {
                detail.setIsCanBack(true);
            }

            //?????????????????????//TODO????????????????????????
            if (BpmTask.TYPE_SEQUENTIAL_TASK.equals(bpmTask.getTaskType())
                    || BpmTask.TYPE_PARALLEL_TASK.equals(bpmTask.getTaskType())) {
                detail.setCanAddSign(true);
            }

            //??????????????????
            if (BeanUtil.isNotEmpty(detail.getBpmInst())) {
                boolean isDiscard = bpmTaskService.canDiscard(detail.getBpmInst());
                detail.setIsShowDiscardBtn(isDiscard);
            }

            //??????????????????????????????????????????????????????????????????????????????????????????
            List<BpmTask> cmBpmTasks = bpmTaskService.getCommuByTaskId(bpmTask.getTaskId(), false);
            if (cmBpmTasks.size() > 0) {
                detail.setCommunicated(true);
                detail.setCanReply(false);
            }

            /**
             * ?????????????????????
             */
            JsonResult formData = bpmTaskService.getFormData(bpmTask.getInstId(), processConfig, userTaskConfig, null);
            //?????????????????????
            bpmTaskService.handOpinion(processConfig, userTaskConfig, formData);

            detail.setFormData(formData);

            JSONObject formDataJson = new JSONObject();
            for (BpmView bpmView : (List<BpmView>) formData.getData()) {
                formDataJson.put(bpmView.getBoAlias(), bpmView.getData());
            }
            //????????????
            List<BpmCheckHistory> opinionHistoryList = bpmCheckHistoryService.getOpinionNameNotEmpty(bpmTask.getInstId());
            detail.setBpmCheckHistories(opinionHistoryList);

            /**
             * ??????????????????
             */
            JsonResult allowApproveResult = bpmInstService.getAllowApprove(userTaskConfig, formDataJson, vars);
            detail.setAllowApprove(allowApproveResult);
            //?????????????????????????????????
            ProcessNextCmd cmd= new ProcessNextCmd();
            if(StringUtils.isEmpty(checkType)){
                checkType="AGREE";
            }
            cmd.setCheckType(checkType);
            cmd.setBoDataMap(formDataJson);
            cmd.setFormJson(formDataJson.toJSONString());
            ProcessHandleUtil.setProcessCmd(cmd);
            List<NodeUsersDto>  nodeUsersDtos= bpmTaskService.getTaskFlowNodesExecutors(bpmTask,vars);
            detail.setNodeExecutors(nodeUsersDtos);
            //????????????????????????
            if (BpmTask.TYPE_LINKUP_TASK.equals(bpmTask.getTaskType())) {
                detail.setCanCheck(false);
            }
            //???????????????????????????
            List<BpmTaskUser> taskUsers = bpmTaskUserService.getByTaskId(taskId);
            if (taskUsers.size() > 0) {
                bpmTask.setHasCandicate(true);
            }

            //???????????????????????????????????? ???????????????????????????
            if (BpmTask.STATUS_UNHANDLE.equals(bpmTask.getStatus())) {
                boolean hasHandlerRight = bpmTaskService.hasHandlerRight(bpmTask, taskUsers);
                if (hasHandlerRight) {
                    bpmTask.setStatus(BpmTask.STATUS_HANDLE);
                    bpmTaskService.update(bpmTask);
                }
            }

            return detail;

    }

    @MethodDefine(title = "??????????????????ID???????????????",path = "/getImageByDefId",method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????ID",varName = "defId")})
    @IdempotenceRequired
    @GetMapping("/getImageByDefId")
    public void getImageByDefId(@RequestParam(value="defId")String defId) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        response.setContentType("image/svg+xml");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        BpmDef bpmDef=bpmDefService.getById(defId);
        String actAefId=bpmDef.getActDefId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(actAefId);

        List<String> list=new ArrayList<>();
        List<String> flowlist=new ArrayList<>();
        InputStream is=diagramGenerator.generateDiagram( bpmnModel,
                list,
                flowlist);

        FileUtil.writeInput(is,response.getOutputStream());
    }

    @MethodDefine(title = "??????????????????ID???????????????",path = "/getImageByInstId",method = HttpMethodConstants.GET,
            params = {@ParamDefine(title = "????????????ID",varName = "instId")})
    @IdempotenceRequired
    @GetMapping("/getImageByInstId")
    public void getImageByInstId(@RequestParam(value="instId")String instId) throws IOException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response=attributes.getResponse();
        response.setContentType("image/svg+xml");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        BpmInst bpmInst=bpmInstService.getById(instId);
        String actAefId=bpmInst.getActDefId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(actAefId);

        List<String> highLightedActivities=new ArrayList<>();//runtimeService.getActiveActivityIds(bpmInst.getActInstId());
        List<String> flowlist=new ArrayList<>();
        InputStream is=diagramGenerator.generateDiagram( bpmnModel,
                highLightedActivities,
                flowlist);

        FileUtil.writeInput(is,response.getOutputStream());
    }

    @MethodDefine(title="???????????????????????????",path = "/getAllTasks",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "queryData")})
    @IdempotenceRequired
    @PostMapping("/getAllTasks")
    public JsonPageResult getAllTasks(@RequestBody QueryData queryData){
        JsonPageResult result=JsonPageResult.getSuccess("??????????????????!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        filter.addSortParam(new SortParam("create_time_","desc"));
        IPage<BpmTask> page= bpmTaskService.query(filter);
        result.setPageData(page);
        List<BpmTask> list=page.getRecords();
        for(BpmTask task:list){
            Set<TaskExecutor> executors=bpmTaskUserService.getExecutors(task);
            task.setTaskExecutors(executors);
        }

        return result;

    }

    /**
     * ????????????????????????????????????
     * @param queryData
     * @return
     */
    @MethodDefine(title = "????????????????????????????????????",path = "/getAllApproved",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "queryData")})
    @IdempotenceRequired
    @PostMapping("/getAllApproved")
    public JsonPageResult getAllApproved(@RequestBody QueryData queryData){
        JsonPageResult jsonResult=JsonPageResult.getSuccess("??????????????????!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        IPage page= bpmCheckHistoryService.query(filter);
        jsonResult.setPageData(page);
        List<BpmCheckHistory> list=page.getRecords();
        for(BpmCheckHistory bpmCheckHistory:list){
            BpmInst bpmInst = bpmInstService.get(bpmCheckHistory.getInstId());
            if(BeanUtil.isNotEmpty(bpmInst)){
                //?????????
                OsUserDto applicant = userClient.findByUserId(bpmInst.getCreateBy());
                bpmCheckHistory.setApplicantName(applicant.getFullName());
                bpmCheckHistory.setApplicantNo(applicant.getAccount());
            }
            //?????????
            OsUserDto userDto = userClient.findByUserId(bpmCheckHistory.getHandlerId());
            if(BeanUtil.isNotEmpty(userDto)){
                bpmCheckHistory.setHandlerUserName(userDto.getFullName());
                bpmCheckHistory.setHandlerUserNo(userDto.getAccount());
            }
        }

        return jsonResult;
    }

    @MethodDefine(title = "????????????????????????",path = "/getAllInsts",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "queryData")})
    @IdempotenceRequired
    @PostMapping("/getAllInsts")
    public JsonPageResult getAllInsts(@RequestBody QueryData queryData){
        JsonPageResult jsonResult=JsonPageResult.getSuccess("??????????????????!");

        QueryFilter filter= QueryFilterBuilder.createQueryFilter(queryData);
        IPage page= bpmInstService.query(filter);
        jsonResult.setPageData(page);
        List<BpmInst> list=page.getRecords();
        for(BpmInst inst:list){
            OsUserDto userDto = userClient.findByUserId(inst.getCreateBy());
            if(BeanUtil.isNotEmpty(userDto)){
                inst.setApplicantName(userDto.getFullName());
                inst.setApplicantNo(userDto.getAccount());
            }
        }

        return jsonResult;
    }

    @MethodDefine(title = "??????????????????ID????????????",path = "/cancelProcess",method = HttpMethodConstants.POST,
            params = { @ParamDefine(title = "????????????ID",varName = "instId"),
                    @ParamDefine(title = "??????",varName = "reason")})
    @IdempotenceRequired
    @PostMapping("/cancelProcess")
    public JsonResult cancelProcess(
                                     @RequestParam(value="instId")String instId,
                                     @RequestParam(value="reason")String reason,
                                     @RequestParam(value="opFiles",required = false)String opFiles){

        try{
            bpmInstService.cancelProcess(instId,reason,opFiles);
            return JsonResult.Success();
        }
        catch (Exception ex){
            MessageUtil.triggerException("??????????????????!",ExceptionUtil.getExceptionMessage(ex));
            return null;
        }

    }


    @MethodDefine(title = "??????????????????ID????????????",path = "/endProcess",method = HttpMethodConstants.POST,
            params = {
                    @ParamDefine(title = "????????????ID",varName = "instId"),
                    @ParamDefine(title = "??????",varName = "reason"),
                    @ParamDefine(title = "??????",varName = "opFiles")})
    @IdempotenceRequired
    @PostMapping("/endProcess")
    public JsonResult endProcess(@RequestParam(value="instId")String instId,
                                 @RequestParam(value="reason")String reason,
                                 @RequestParam(value="opFiles",required = false)String opFiles){

        try{
            bpmInstService.doEndProcess(instId,reason,opFiles);
            return JsonResult.Success();
        }
        catch (Exception ex){
            MessageUtil.triggerException("??????????????????!",ExceptionUtil.getExceptionMessage(ex));
            return null;
        }
    }

    @MethodDefine(title = "??????????????????",path = "/setVariables",method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "????????????",varName = "data")})
    @IdempotenceRequired
    @PostMapping("/setVariables")
    public JsonResult setVariables(@RequestBody String data){
        if(StringUtils.isEmpty(data)){
            return JsonResult.getFailResult("???????????????????????????");
        }
        String actInstId=null;
        Map<String,Object> varsMap=new HashMap<>();
        JSONObject bpmData=JSONObject.parseObject(data);
        if(bpmData.containsKey("instId")){
            String instId=bpmData.getString("instId");
            BpmInst bpmInst=bpmInstService.get(instId);
            if(bpmInst!=null){
                actInstId=bpmInst.getActInstId();
            }
        }
        if(bpmData.containsKey("vars")){
            varsMap= bpmData.getJSONObject("vars").getInnerMap();
        }
        if(StringUtils.isNotEmpty(actInstId)) {
            runtimeService.setVariables(actInstId, varsMap);
        }
        return JsonResult.getSuccessResult("???????????????????????????");
    }

}
