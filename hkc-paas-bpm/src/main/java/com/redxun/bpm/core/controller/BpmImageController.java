package com.redxun.bpm.core.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.ActRepService;
import com.redxun.bpm.activiti.config.BpmNodeTypeEnums;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserGroupConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.core.dto.BpmImageParam;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.annotation.ClassDefine;
import com.redxun.common.annotation.MethodDefine;
import com.redxun.common.annotation.ParamDefine;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.constant.HttpMethodConstants;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.form.BpmView;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsUserDto;
import com.xxl.job.core.context.XxlJobHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/bpm/core/bpmImage")
@ClassDefine(title = "流程图",alias = "bpmImageController",path = "/bpm/core/bpmImage",packages = "core",packageName = "流程管理")
@Api(tags = "流程图")
public class BpmImageController {
    @Resource
    BpmDefService bpmDefService;
    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    BpmTaskService bpmTaskService;
    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Resource
    BpmInstRouterServiceImpl bpmInstRouterService;
    @Resource
    RuntimeService runtimeService;
    @Resource
    HistoryService historyService;
    @Resource
    IOrgService orgService;
    @Resource
    ActRepService actRepService;
    @Resource
    TaskExecutorService taskExecutorService;
    @Resource
    ProcessScriptEngine processScriptEngine;

    private static final List<String> nodeTypes = Arrays.asList(BpmNodeTypeEnums.USER_TASK.getTypeName(),BpmNodeTypeEnums.EXCLUSIVE_GATEWAY.getTypeName());

    @MethodDefine(title = "获取流程图节点的审批信息",path = "/getBpmnImageNodeInfo",method = HttpMethodConstants.POST)
    @ApiOperation("获取流程图节点的审批信息")
    @PostMapping("getBpmnImageNodeInfo")
    public JSONObject getBpmnImageNodeInfo(@RequestBody BpmImageParam bpmImageParam){
        JSONObject json=new JSONObject();
        Set<TaskExecutor> executors=new HashSet<>();
        String actDefId=null;
        String instId=null;
        boolean successEnd=false;
        if(StringUtils.isNotEmpty(bpmImageParam.getTaskId())){
            //通过流程任务进来看节点的人员
            BpmTask bpmTask=bpmTaskService.get(bpmImageParam.getTaskId());
            actDefId=bpmTask.getActDefId();
            instId=bpmTask.getInstId();
            Map<String,Object> vars=runtimeService.getVariables(bpmTask.getActInstId());
            executors=getExecutorsByBpmnImage(actDefId,bpmImageParam,vars);
        }else if(StringUtils.isNotEmpty(bpmImageParam.getInstId())){
            //通过流程实例进来看节点的人员
            List<BpmTask> bpmTasks = bpmTaskService.getByInstId(bpmImageParam.getInstId());
            //有执行的任务
            if(BeanUtil.isNotEmpty(bpmTasks) && bpmTasks.size()==1 ){
                BpmTask bpmTask=bpmTaskService.get(bpmTasks.get(0).getTaskId());
                actDefId=bpmTask.getActDefId();
                instId=bpmTask.getInstId();
                bpmImageParam.setTaskId(bpmTask.getTaskId());
                Map<String,Object> vars=runtimeService.getVariables(bpmTask.getActInstId());
                executors=getExecutorsByBpmnImage(actDefId,bpmImageParam,vars);
            }else {
                BpmInst bpmInst=bpmInstService.getById(bpmImageParam.getInstId());
                //草稿状态
                if(BpmInstStatus.DRAFTED.name().equals(bpmInst.getStatus())){
                    return json;
                }
                actDefId= bpmInst.getActDefId();
                instId=bpmInst.getInstId();
                Map<String,Object> vars = new HashMap();
                vars.put(BpmInstVars.INST_ID.getKey(),instId);
                if (!BpmInstStatus.CANCEL.name().equals(bpmInst.getStatus()) &&
                        !BpmInstStatus.ERROR_END.name().equals(bpmInst.getStatus()) &&
                        !BpmInstStatus.SUCCESS_END.name().equals(bpmInst.getStatus())) {
                    vars.putAll(runtimeService.getVariables(bpmInst.getActInstId()));
                }
                List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(bpmInst.getActInstId()).list();
                for (HistoricVariableInstance historicVariableInstance : list) {
                    vars.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue());
                }
                executors=getExecutorsByBpmnImage(actDefId,bpmImageParam,vars);
                //流程是否已结束
                if(BpmInstStatus.SUCCESS_END.name().equals(bpmInst.getStatus())){
                    successEnd=true;
                }
            }
        }else if(StringUtils.isNotEmpty(bpmImageParam.getDefId())){
            //在流程启动阶段，计算人员
            BpmDef bpmDef=bpmDefService.getById(bpmImageParam.getDefId());
            actDefId=bpmDef.getActDefId();
            executors=getExecutorsByBpmnImage(actDefId,bpmImageParam,new HashMap<>());
        }
        UserTaskConfig nodeConfig = (UserTaskConfig)bpmDefService.getNodeConfig(actDefId,bpmImageParam.getNodeId());
        TaskNodeUser taskNodeUser=new TaskNodeUser(bpmImageParam.getNodeId(),nodeConfig.getName());
        taskNodeUser.setMultiInstance(nodeConfig.getMultipleType());

        //instId 不为空时 获取审批历史
        if(StringUtils.isNotEmpty(instId)){
            List<BpmCheckHistory> histories=bpmCheckHistoryService.getByInstIdNodeId(instId, bpmImageParam.getNodeId());
            json.put("histories", histories);
            //有审批历史的任务 则获取审批历史的审批人
            if(histories.size()>0 && (StringUtils.isNotEmpty(bpmImageParam.getTaskId()) || successEnd)){
                Set<TaskExecutor> newExecutors=new HashSet<>();
                for(TaskExecutor executor:executors) {
                    if (!TaskExecutor.TYPE_USER.equals(executor.getType())) {
                        continue;
                    }
                    for (BpmCheckHistory history : histories) {
                        //流程成功结束则直接获取审批历史 或 当前执行任务 则不获取审批历史的
                        if (successEnd || !bpmImageParam.getTaskId().equals(history.getTaskId())) {
                            if (executor.getId().equals(history.getHandlerId())) {
                                OsUserDto handlerUser = orgService.getUserById(history.getHandlerId());
                                TaskExecutor taskExecutor = new TaskExecutor();
                                taskExecutor.setType("user");
                                taskExecutor.setId(handlerUser.getUserId());
                                taskExecutor.setName(handlerUser.getFullName());
                                taskExecutor.setAccount(handlerUser.getAccount());
                                newExecutors.add(taskExecutor);
                            } else {
                                newExecutors.add(executor);
                            }
                        }
                    }
                }
            }
        }
        taskNodeUser.setConfigExecutors(executors);
        json.put("taskNodeUser",taskNodeUser);
        return json;
    }

    private Set<TaskExecutor> getExecutorsByBpmnImage(String actDefId,BpmImageParam bpmImageParam,Map<String,Object> vars){
        ProcessNextCmd cmd = new ProcessNextCmd();
        cmd.setCheckType("AGREE");
        cmd.setBoDataMap(bpmImageParam.getFormData());
        cmd.setFormJson(bpmImageParam.getFormData().toJSONString());
        ProcessHandleUtil.setProcessCmd(cmd);
        return bpmTaskService.getExecutors(actDefId,bpmImageParam.getNodeId(),vars);
    }

    @MethodDefine(title = "通过定义ID/流程实例Id/任务Id 获取到流程定义Xml", path = "/getBpmnXmlFromParam", method = HttpMethodConstants.POST,
            params = {@ParamDefine(title = "流程参数", varName = "bpmParam")})
    @ApiOperation("通过定义ID/流程实例Id/任务Id 获取到流程定义Xml")
    @PostMapping("getBpmnXmlFromParam")
    public BpmImageResult getBpmnXmlFromParam(@ApiParam @RequestBody BpmParam bpmParam) {

        BpmDef bpmDef = null;
        List<BpmCheckHistory> historyList = null;
        if (StringUtils.isNotEmpty(bpmParam.getTaskId())) {
            BpmTask bpmTask = bpmTaskService.get(bpmParam.getTaskId());
            bpmDef = bpmDefService.get(bpmTask.getDefId());
            if (bpmParam.getShowHis()) {
                historyList = bpmCheckHistoryService.getByInstId(bpmTask.getInstId());
                List<BpmTask> tasks = bpmTaskService.getByInstId(bpmTask.getInstId());
                for (BpmTask task : tasks) {
                    //加上当前正在运行的
                    BpmCheckHistory hisRun = new BpmCheckHistory();
                    hisRun.setNodeId(task.getKey());
                    hisRun.setCheckStatus(task.getStatus());
                    hisRun.setCompleteTime(new Date());
                    historyList.add(hisRun);
                }
            }
        } else if (StringUtils.isNotEmpty(bpmParam.getInstId())) {
            BpmInst bpmInst;
            BpmInstRouter bpmInstRouter = bpmInstRouterService.get(bpmParam.getInstId());
            if (BeanUtil.isNotEmpty(bpmInstRouter)) {
                bpmInst = bpmInstService.getByArchiveLog(bpmParam.getInstId(), bpmInstRouter.getTableId());
            } else {
                bpmInst = bpmInstService.get(bpmParam.getInstId());
            }
            bpmDef = bpmDefService.get(bpmInst.getDefId());
            if (bpmParam.getShowHis()) {
                historyList = bpmCheckHistoryService.getByInstId(bpmInst.getInstId());
                List<BpmTask> tasks = bpmTaskService.getByInstId(bpmInst.getInstId());
                for (BpmTask task : tasks) {
                    //加上当前正在运行的
                    BpmCheckHistory hisRun = new BpmCheckHistory();
                    hisRun.setNodeId(task.getKey());
                    hisRun.setCheckStatus(task.getStatus());
                    hisRun.setCompleteTime(new Date());
                    historyList.add(hisRun);
                }
            }
        } else if (StringUtils.isNotEmpty(bpmParam.getDefId())) {
            bpmDef = bpmDefService.get(bpmParam.getDefId());
            //是否流程预演
            if(bpmParam.getPreview() && BeanUtil.isNotEmpty(bpmDef)){
                //表单数据
                JSONObject boDataMap= bpmDefService.getBoDataMap(bpmParam.getFormData());
                ProcessStartCmd cmd=new ProcessStartCmd();
                //表单数据
                cmd.setBoDataMap(boDataMap);
                IUser user= ContextUtil.getCurrentUser();
                //获取设置的发起人
                if(StringUtils.isNotEmpty(bpmParam.getStartUserId())){
                    OsUserDto osUserDto = orgService.getUserById(bpmParam.getStartUserId());
                    if(BeanUtil.isNotEmpty(osUserDto) && StringUtils.isNotEmpty(osUserDto.getUserId())){
                        user=osUserDto;
                    }
                }
                try{
                    //获取预演记录
                    List<BpmCheckHistory> bpmCheckHistories =bpmPreview(cmd, bpmDef,user);
                    if(BeanUtil.isEmpty(historyList)){
                        historyList=new ArrayList<>();
                    }
                    historyList.addAll(bpmCheckHistories);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        if(BeanUtil.isNotEmpty(historyList) && historyList.size()>0) {
            Collections.sort(historyList, new Comparator<BpmCheckHistory>() {
                @Override
                public int compare(BpmCheckHistory history1, BpmCheckHistory history2) {
                    return history1.getCompleteTime().compareTo(history2.getCompleteTime());
                }
            });
        }
        if(bpmDef==null){
            return null;
        }
        return new BpmImageResult(bpmDef.getDesignXml(),historyList);
    }

    private List<UserGroupConfig> getListConfig(JSONArray nodeUserConfig){
        List<UserGroupConfig> list=new ArrayList<>();

        for(int i=0;i<nodeUserConfig.size();i++){
            JSONObject conf=nodeUserConfig.getJSONObject(i);
            UserGroupConfig userGroupConfig= conf.toJavaObject(UserGroupConfig.class);
            list.add(userGroupConfig);
        }
        return list;
    }


    /**
     * 流程预演
     * @param cmd 流程变量
     * @param bpmDef
     * @param user
     * @return
     */
    private List<BpmCheckHistory> bpmPreview(ProcessStartCmd cmd, BpmDef bpmDef, IUser user) throws Exception {
        List<BpmCheckHistory> bpmCheckHistories=new ArrayList<>();
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                //流程参数
                ProcessHandleUtil.setProcessCmd(cmd);
                //设置当前用户
                ContextUtil.setCurrentUser(user);
                Map<String, Object> conextData = ActivitiUtil.getConextData(cmd.getVars());
                conextData.put("startUserId",user.getUserId());
                conextData.put("actDefId",bpmDef.getActDefId());
                List<BpmCheckHistory> histories=previewHistory(bpmDef,conextData);
                bpmCheckHistories.addAll(histories);
            }
        });
        thread.start();
        thread.join();
        return bpmCheckHistories;
    }


    /**
     * 预演的审批历史
     * @param bpmDef
     * @return
     */
    private List<BpmCheckHistory> previewHistory( BpmDef bpmDef,Map<String, Object> conextData){
        List<BpmCheckHistory> historyList=new ArrayList<>();
        String extConfs= bpmDef.getExtConfs();
        JSONObject extConfsJson= JSONObject.parseObject(extConfs);
        FlowNode flowNode = actRepService.getFirstUserTaskNode(bpmDef.getActDefId());
        do{
            //输出的节点
            List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
            JSONObject nodeObj = extConfsJson.getJSONObject(flowNode.getId());
            String flowNodeType = flowNode.getClass().getTypeName();
            //用户节点
            if(flowNodeType.indexOf("UserTask")!=-1){
                historyList.addAll(handUserTask(bpmDef,nodeObj,conextData));
                if(BeanUtil.isEmpty(outgoingFlows) || outgoingFlows.size()==0){
                    flowNode=null;
                }else {
                    flowNode  =(FlowNode) outgoingFlows.get(0).getTargetFlowElement();
                }
            }
            //条件网关
            else if (flowNodeType.indexOf("ExclusiveGateway")!=-1){
                flowNode=handExclusive(outgoingFlows,nodeObj,conextData);
            }
            //并行网关
            else if (flowNodeType.indexOf("ParallelGateway")!=-1){
                flowNode=handParallel(bpmDef,outgoingFlows ,extConfsJson,historyList,conextData);
            }
            //相容网关
            else if (flowNodeType.indexOf("InclusiveGateway")!=-1){
                flowNode=handInclusive(bpmDef,outgoingFlows ,extConfsJson,nodeObj,historyList,conextData);
            }
            else {
                flowNode=null;
            }
        }while (BeanUtil.isNotEmpty(flowNode));
        return historyList;
    }

    /**
     * 处理用户任务节点
     * @param bpmDef
     * @param nodeObj
     * @param conextData
     * @return
     */
    private List<BpmCheckHistory> handUserTask(BpmDef bpmDef ,JSONObject nodeObj,Map<String, Object> conextData){
        List<BpmCheckHistory> historyList=new ArrayList<>();
        List<UserGroupConfig> userConfigs = getListConfig(nodeObj.getJSONArray("userConfigs"));
        Set<TaskExecutor> executors = taskExecutorService.getTaskExecutors(userConfigs,conextData);
        if(executors.size()==0){
            BpmCheckHistory history = new BpmCheckHistory();
            history.setActDefId(bpmDef.getActDefId());
            history.setNodeId(nodeObj.getString("id"));
            history.setCheckStatus("REFUSE");
            history.setCompleteTime(new Date());
            history.setNodeName(nodeObj.getString("name"));
            history.setRemark("无执行人");
            historyList.add(history);
        }else {
            TaskExecutor executor = executors.iterator().next();
            //用户
            if(TaskExecutor.TYPE_USER.equals(executor.getType())){
                BpmCheckHistory history = new BpmCheckHistory();
                history.setActDefId(bpmDef.getActDefId());
                history.setNodeId(nodeObj.getString("id"));
                history.setCheckStatus("AGREE");
                history.setCompleteTime(new Date());
                history.setNodeName(nodeObj.getString("name"));
                history.setHandlerId(executor.getId());
                history.setHandlerUserName(executor.getName());
                history.setHandlerUserNo(executor.getAccount());
                historyList.add(history);
                OsUserDto user = orgService.getUserById(executor.getId());
                //设置为当前对象
                if(BeanUtil.isNotEmpty(user)){
                    ContextUtil.setCurrentUser(user);
                }
            }else { //用户组
                //当前用户组的所有用户 默认选第一个
                List<OsUserDto> users = orgService.getByGroupId(executor.getId());
                if(BeanUtil.isNotEmpty(users) && users.size()>0){
                    OsUserDto osUserDto = users.get(0);
                    BpmCheckHistory history = new BpmCheckHistory();
                    history.setActDefId(bpmDef.getActDefId());
                    history.setNodeId(nodeObj.getString("id"));
                    history.setCheckStatus("AGREE");
                    history.setCompleteTime(new Date());
                    history.setNodeName(nodeObj.getString("name"));
                    history.setHandlerId(osUserDto.getUserId());
                    history.setHandlerUserName(osUserDto.getFullName());
                    history.setHandlerUserNo(osUserDto.getAccount());
                    historyList.add(history);
                    OsGroupDto mainGroup = orgService.getMainDeps(osUserDto.getUserId(), osUserDto.getTenantId());
                    if(BeanUtil.isNotEmpty(mainGroup)){
                        osUserDto.setDeptId(mainGroup.getGroupId());
                    }
                    ContextUtil.setCurrentUser(osUserDto);
                }else {
                    BpmCheckHistory history = new BpmCheckHistory();
                    history.setActDefId(bpmDef.getActDefId());
                    history.setNodeId(nodeObj.getString("id"));
                    history.setCheckStatus("REFUSE");
                    history.setCompleteTime(new Date());
                    history.setNodeName(nodeObj.getString("name"));
                    history.setRemark("无执行人");
                    historyList.add(history);
                }
            }
        }
        return historyList;
    }

    //处理条件网关
    private FlowNode handExclusive(List<SequenceFlow> outgoingFlows ,JSONObject nodeObj,Map<String, Object> conextData){
        JSONArray outs = nodeObj.getJSONArray("outs");
        for (int i = 0; i < outgoingFlows.size(); i++) {
            FlowNode targetNode = (FlowNode) outgoingFlows.get(i).getTargetFlowElement();
            boolean flag=false;
            for (int j = 0; j < outs.size(); j++) {
                JSONObject node = outs.getJSONObject(j);
                if(node.getString("targetNodeId").equals(targetNode.getId())){
                    String condition = node.getString("condition");
                    Object obj=processScriptEngine.exeScript(condition,conextData);
                    if (obj instanceof Boolean) {
                        if (((Boolean) obj)) {// 符合条件
                            flag=true;
                            break;
                        }
                    }
                }
            }
            if(flag){
                return targetNode;
            }
        }
        return null;
    }

    //处理并行网关
    private FlowNode handParallel(BpmDef bpmDef ,List<SequenceFlow> outgoingFlows ,JSONObject extConfsJson,
                                   List<BpmCheckHistory> historyList,Map<String, Object> conextData){
        FlowNode flowNode=null;
        for (int i = 0; i < outgoingFlows.size(); i++) {
            FlowNode targetNode = (FlowNode) outgoingFlows.get(i).getTargetFlowElement();
            String targetNodeType = targetNode.getClass().getTypeName();
            JSONObject targetObj = extConfsJson.getJSONObject(targetNode.getId());
            if(targetNodeType.indexOf("UserTask")!=-1){
                historyList.addAll(handUserTask(bpmDef,targetObj,conextData));
            }
            //判断下一节点是否为结束
            List<SequenceFlow> outFlows = targetNode.getOutgoingFlows();
            FlowNode targetElement = (FlowNode) outFlows.get(0).getTargetFlowElement();
            String targetType = targetElement.getClass().getTypeName();
            if(targetType.indexOf("UserTask")!=-1){
                do{
                    JSONObject nodeObj = extConfsJson.getJSONObject(targetElement.getId());
                    //用户节点
                    if(targetType.indexOf("UserTask")!=-1){
                        historyList.addAll(handUserTask(bpmDef,nodeObj,conextData));
                        if(BeanUtil.isEmpty(targetElement.getOutgoingFlows()) || targetElement.getOutgoingFlows().size()==0){
                            targetElement=null;
                        }else {
                            targetElement  =(FlowNode) targetElement.getOutgoingFlows().get(0).getTargetFlowElement();
                        }
                    }
                    else if (targetType.indexOf("ExclusiveGateway")!=-1){
                        //条件网关
                        targetElement=handExclusive(outFlows,nodeObj,conextData);
                    }
                    else if (targetType.indexOf("ParallelGateway")!=-1){
                        //并行网关
                        targetElement=handParallel(bpmDef,outFlows ,extConfsJson,historyList,conextData);
                    }
                    else if (targetType.indexOf("InclusiveGateway")!=-1){
                        //相容网关
                        targetElement=handInclusive(bpmDef,outFlows ,extConfsJson,nodeObj,historyList,conextData);
                    }
                    else {
                        targetElement=null;
                    }
                    if(BeanUtil.isNotEmpty(targetElement)){
                        targetType=targetElement.getClass().getTypeName();
                        outFlows=targetElement.getOutgoingFlows();
                    }
                }while (targetType.indexOf("UserTask")!=-1);
            }
            flowNode=targetElement;
        }
        return flowNode;
    }


    //处理相容网关
    private FlowNode handInclusive(BpmDef bpmDef ,List<SequenceFlow> outgoingFlows ,JSONObject extConfsJson,JSONObject nodeObj,
                                   List<BpmCheckHistory> historyList,Map<String, Object> conextData){
        FlowNode flowNode=null;
        JSONArray outs = nodeObj.getJSONArray("outs");
        List<SequenceFlow> newOutFlows=new ArrayList<>();
        for (int i = 0; i < outgoingFlows.size(); i++) {
            FlowNode targetNode = (FlowNode) outgoingFlows.get(i).getTargetFlowElement();
            for (int j = 0; j < outs.size(); j++) {
                JSONObject node = outs.getJSONObject(j);
                if(node.getString("targetNodeId").equals(targetNode.getId())){
                    String condition = node.getString("condition");
                    Object obj=processScriptEngine.exeScript(condition,conextData);
                    if (obj instanceof Boolean) {
                        if (((Boolean) obj)) {// 符合条件
                            newOutFlows.add(outgoingFlows.get(i));
                        }
                    }
                }
            }
        }
        //处理符合条件的路线
        if(newOutFlows.size()>0){
            flowNode=handParallel(bpmDef,newOutFlows ,extConfsJson,historyList,conextData);
        }
        return flowNode;
    }
}
