package com.redxun.bpm.job;

import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.RemindDefConfig;
import com.redxun.bpm.activiti.config.UserGroupConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.base.entity.JsonResult;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.DateUtils;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.*;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.form.BpmView;
import com.redxun.dto.user.OsUserDto;
import com.redxun.mq.BpmInputOutput;
import com.redxun.mq.MessageModel;
import com.redxun.msgsend.util.MesAutoUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import com.redxun.bpm.core.ext.messagehandler.MessageUtil;
import javax.annotation.Resource;
import java.util.*;


@Slf4j
@Component
public class RemindJob extends IJobHandler {

    @Resource
    private BpmRemindInstServiceImpl bpmRemindInstService;
    @Resource
    private BpmRemindHistoryServiceImpl bpmRemindHistoryServiceImpl;
    @Resource
    private BpmTaskService bpmTaskService;
    @Resource
    private BpmTaskUserServiceImpl bpmTaskUserService;
    @Resource
    private ProcessScriptEngine processScriptEngine;
    @Resource
    private BpmInstServiceImpl bpmInstService;
    @Resource
    private TaskService taskService;
    @Resource
    private IOrgService orgService;
    @Resource
    private BpmInputOutput bpmInputOutput;
    @Autowired
    BpmDefService bpmDefService;
    @Resource
    RuntimeService runtimeService;
    @Resource
    TaskExecutorService taskExecutorService;


    @Override
    @XxlJob("remindJobHandler")
    public void execute() throws Exception {
        //?????????????????????????????????
        List<BpmRemindInst> exprieList=bpmRemindInstService.getRemindInst(true);
        List<BpmRemindInst> notExpireList=bpmRemindInstService.getRemindInst(false);

        if(BeanUtil.isEmpty(exprieList) && BeanUtil.isEmpty(notExpireList)){
            return ;
        }

        //???????????????????????????????????????
        for(BpmRemindInst inst :exprieList){
            handExpireInst(inst);
        }
        //?????????????????????????????????
        for(BpmRemindInst inst :notExpireList){
            handNotExpireInst(inst);
        }
        return ;
    }

    /**
     * ???????????????????????????
     * @param inst
     */
    private void handNotExpireInst(BpmRemindInst inst){


        if("create".equals(inst.getStatus())){
            inst.setStatus("run");
            bpmRemindInstService.update(inst);
        }

        String notifyType=inst.getNotifyType();
        //?????????????????????????????????
        if(StringUtils.isEmpty(notifyType)) {
            return;
        }
        //?????????????????????
        Date baseTime=inst.getTimeToSend();

        Date tmpDate=new Date();
        //????????????????????????????????????
        if(tmpDate.before(baseTime) ||
                tmpDate.after(DateUtils.addMinutes(baseTime,  inst.getSendTimes()* inst.getSendInterval()))){
            return;
        }
        /**
         * ?????????????????????
         */
        for(int i=0;i<inst.getSendTimes();i++){
            Date start=DateUtils.addMinutes(baseTime,  i* inst.getSendInterval()) ;
            Date end=DateUtils.addMinutes(baseTime,  (i+1)* inst.getSendInterval()) ;
            Date curDate=new Date();
            //????????????????????????????????????
            if(curDate.after(start) && curDate.before(end)){
                Integer rtn= bpmRemindHistoryServiceImpl.getByStartEnd(inst.getId(), start, end);
                if(rtn>0) {
                    continue;
                }
                //??????
                handRemind(inst);
            }
        }
    }

    /**
     * ?????????????????????
     * @param inst
     * @throws Exception
     */
    private void handRemind(BpmRemindInst inst) {
        String actTaskId=inst.getTaskId();

        BpmTask bpmTask = bpmTaskService.getActTaskId(actTaskId);
        if(bpmTask==null){
            return;
        }
        Map<String,Object> variables=runtimeService.getVariables(bpmTask.getActInstId());
        //????????????
        UserTaskConfig userTaskConfig= (UserTaskConfig) bpmDefService.getNodeConfig(bpmTask.getActDefId(),bpmTask.getKey());
        List<RemindDefConfig> remindDefs = userTaskConfig.getRemindDefs();
        //????????????
        ProcessConfig processConfig = bpmDefService.getProcessConfig(bpmTask.getActDefId());

        if(BeanUtil.isEmpty(remindDefs)&&remindDefs.size()==0){
            remindDefs=processConfig.getRemindDefs();
        }

        JsonResult formData = bpmTaskService.getFormData(bpmTask.getInstId(), false);
        JSONObject formDataJson=new JSONObject();
        BpmView bpmView = (BpmView) formData.getData();
        formDataJson.put(bpmView.getAlias(),bpmView.getData());
        ProcessNextCmd cmd = new ProcessNextCmd();
        cmd.setTaskId(bpmTask.getTaskId());
        cmd.setBoDataMap(formDataJson);
        cmd.setInstId(bpmTask.getInstId());
        ProcessHandleUtil.setProcessCmd(cmd);

        List<TaskExecutor> taskExecutors=new ArrayList<>();
        for (RemindDefConfig remindDef : remindDefs) {
            //???????????????
            if("custom".equals(remindDef.getNotifyObj())){
                List<UserGroupConfig> userCnfs = remindDef.getUserCnfs();
                for (UserGroupConfig userGroupConfig : userCnfs) {
                    taskExecutors.addAll(taskExecutorService.getTaskExecutors(userGroupConfig, variables));
                }
            }
        }
        //???????????????
        Set<IUser> taskUsers=bpmTaskUserService.getTaskUsers(bpmTask.getTaskId());

        Set<IUser> userSet=new HashSet<>();
        if(taskExecutors.size()>0){
            List<OsUserDto> users = orgService.getUsersByTaskExecutor(taskExecutors);
            userSet.addAll(users);
        }

        if(BeanUtil.isEmpty(taskUsers)){
            return;
        }

        //??????????????????????????????
        String account= SysPropertiesUtil.getString("sendRemindUser");

        OsUserDto sender= orgService.getByAccount(account);

        MessageModel msgModel=new MessageModel();
        msgModel.setSubject(inst.getName() + "????????????");

        msgModel.setSender(sender);
        msgModel.setMsgType(inst.getNotifyType());

        Map<String,Object> vars=msgModel.getVars();
        vars.put("sender", sender.getFullName());
        vars.put("serverUrl", SysPropertiesUtil.getString("serverAddress")+SysPropertiesUtil.getString("ctxPath"));
        vars.put("subject", bpmTask.getDescp());
        vars.put("defId", bpmTask.getDefId());
        vars.put("instId", bpmTask.getInstId());
        vars.put("actDefId", bpmTask.getActDefId());
        vars.put("nodeId", bpmTask.getKey());
        vars.put("jumpType","AGREE");

        //????????????ID
        msgModel.setTenantId(inst.getTenantId());
        //??????????????????????????????
        sendMsg(userSet,vars,msgModel);

        //????????????????????????
        vars.put("taskId", bpmTask.getTaskId());
        sendMsg(taskUsers,vars,msgModel);

        //?????????????????????
        addHistory(inst,"remind");
    }

    /**
     * ?????????????????????
     * @param inst
     */
    private void handExpireInst(BpmRemindInst inst){
        String action=inst.getAction();
        //??????
        if("approve".equals(action) || "back".equals(action) || "backToStart".equals(action)){
            handTask(inst,action);
        }
        //??????
        else if("script".equals(action)){
            handScript(inst);
        }
        inst.setStatus("finish");

        bpmRemindInstService.update(inst);

        addHistory(inst,"expire");
    }

    /**
     * ?????????????????????
     * @param inst
     * @param type	expire??????????????????remind ???????????????
     */
    private void addHistory(BpmRemindInst inst ,String type){
        BpmRemindHistory history=new BpmRemindHistory();
        history.setReminderInstId(inst.getId());
        history.setRemindType(type);
        history.setCreateTime(new Date());

        bpmRemindHistoryServiceImpl.insert(history);
    }

    /**
     * ???????????????????????????????????????
     * @param inst
     */
    private void handTask(BpmRemindInst inst,String action){
        BpmTask task=bpmTaskService.getActTaskId(inst.getTaskId());
        if(task==null) {
            return;
        }
        String curUserId=task.getAssignee();
        if(StringUtils.isNotEmpty(curUserId)) {
            OsUserDto taskUser=orgService.getUserById(curUserId);
            ContextUtil.setCurrentUser(orgService.getByUsername(taskUser.getUserNo()));
        }

        ProcessNextCmd cmd = new ProcessNextCmd();
        cmd.setTaskId(task.getTaskId());
        String actType="";
        String opinion="";
        if("approve".equals(action)){
            actType=TaskOptionType.OVERTIME_AUTO_AGREE.name();
            opinion="??????????????????";
            cmd.addTransientVar("timeout", true);
        }
        else if("back".equals(action)){
            actType=TaskOptionType.BACK.name();
            opinion="??????????????????";
        }
        else if("backToStart".equals(action)){
            actType=TaskOptionType.BACK_TO_STARTOR.name();
            opinion="???????????????????????????";
        }
        cmd.setCheckType(actType);
        cmd.setOpinion(opinion);

        ProcessHandleUtil.clearProcessCmd();

        ProcessHandleUtil.setProcessCmd(cmd);
        try{
            bpmTaskService.completeTask(cmd);
        }
        catch(Exception ex){
           log.error(ExceptionUtil.getExceptionMessage(ex));
        }

    }

    /**
     * ?????????????????????
     * @param inst
     */
    private void handScript(BpmRemindInst inst){
        Map<String, Object> vars = getVars(inst);
        try {
            processScriptEngine.exeScript(inst.getScript(), vars);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ???????????????
     * @param inst
     * @return
     */
    private Map<String, Object>  getVars(BpmRemindInst inst){
        Map<String, Object> variables = taskService.getVariables(inst.getTaskId());
        Map<String, Object> vars = new HashMap<>(variables.size()+1);
        vars.putAll(variables);
        vars.put("inst", inst);
        return vars;
    }

    /**
     * ???????????????
     * @param userSet
     */
    private void sendMsg(Set<IUser> userSet,Map<String,Object> vars,MessageModel msgModel){
        for(IUser receiver:userSet){
            List<IUser> receiverList=new ArrayList<IUser>();
            receiverList.add(receiver);
            vars.put("receiver", receiver.getFullName());
            msgModel.setVars(vars);
            List<OsUserDto> reveiversDto =null;
            try {
                reveiversDto=(List<OsUserDto>) FileUtil.cloneObject(receiverList);
            }catch (Exception e){
                log.error("----RemindJob.handRemind.receiver is error---message="+e.getMessage());
            }
            msgModel.setReceivers(reveiversDto);

            //?????????????????????
            MessageUtil.getMessageUrl(msgModel,vars);

            String defId= (String) vars.get("defId");
            String nodeId= (String) vars.get("nodeId");

            BpmMessageTemplateServiceImpl bpmMessageTemplateService=SpringUtil.getBean(BpmMessageTemplateServiceImpl.class);
            Map<String, String> templateVars = bpmMessageTemplateService.getByBpmNode(defId, nodeId, "remind", msgModel.getMsgType());

            //????????????????????????????????????
            msgModel.addTemplateVars(templateVars);

            MesAutoUtil.sendMessage(JSONObject.toJSONString(msgModel));
        }


    }
}
