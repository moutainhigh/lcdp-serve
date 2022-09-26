package com.redxun.bpm.activiti.eventhandler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.EventConfig;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.SubProcessDefConfig;
import com.redxun.bpm.activiti.eventhandler.BaseEventMessage;
import com.redxun.bpm.activiti.eventhandler.EventHanderType;
import com.redxun.bpm.activiti.eventhandler.IEventHandler;
import com.redxun.bpm.activiti.eventhandler.TaskEventMessage;
import com.redxun.bpm.core.entity.BpmDef;
import com.redxun.bpm.core.entity.BpmInst;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.entity.ProcessStartCmd;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.BpmInstServiceImpl;
import com.redxun.bpm.core.service.MessageService;
import com.redxun.bpm.feign.FormClient;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.dto.form.FormBoEntityDto;
import com.redxun.feign.org.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 启动子流程。
 */
@Component
@Slf4j
public class SubProcessEventHandler implements IEventHandler {

    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    MessageService messageService;
    @Resource
    UserClient userClient;
    @Resource
    FormClient formClient;
    @Resource
    ProcessScriptEngine processScriptEngine;

    @Override
    public EventHanderType getType() {
        return new EventHanderType("subProcess","启动子流程");
    }

    @Override
    public void handEvent(BaseEventMessage message) {
        EventConfig eventSetting= message.getEventConfig();
        JSONObject config=eventSetting.getConfig();
        if(BeanUtil.isEmpty(config)) {
            return;
        }
        String script = config.getString("script");
        //子流程key
        String subProcess = config.getString("subProcess");
        if(StringUtils.isEmpty(subProcess)){
            return;
        }
        String errors="";
        BpmInst bpmInst=null;
        try{
            //子实体ID
            String userSubTable = config.getString("userSubTable");
            //使用子表启动流程。
            if(StringUtils.isNotEmpty(userSubTable)){
                JSONObject jsonData=message.getFormData();
                ProcessConfig processConfig = bpmDefService.getProcessConfig(message.getActDefId());
                String boDefs=processConfig.getBoDefs().getValue();
                FormBoEntityDto subEnt = formClient.getBoEntById(userSubTable);
                //获取子表数据
                JSONArray subArray=jsonData.getJSONObject(boDefs).getJSONArray("sub__"+subEnt.getAlias());
                for(int i=0;i<subArray.size();i++) {
                    Map<String,Object> vars=message.getVars();
                    //子表一行的数据。
                    vars.put("subJsonData", subArray.getJSONObject(i));
                    Boolean subFlag = true;
                    if(StringUtils.isNotEmpty(script)){
                        subFlag = (Boolean) processScriptEngine.exeScript(script, vars);
                    }
                    //不满足条件不启动子流程
                    if (!subFlag) {
                        continue;
                    }
                    bpmInst = doStartProcess(message, subProcess, i);
                }
            }else{
                Map<String,Object> vars=message.getVars();
                Boolean flag = true;
                //是否允许流程运行。
                if(StringUtils.isNotEmpty(script)){
                    flag = (Boolean) processScriptEngine.exeScript(script, vars);
                }
                if(!flag){
                    return;
                }
                bpmInst=doStartProcess(message,subProcess,0);
            }
        }catch (Exception e){
            errors=ExceptionUtil.getExceptionMessage(e);
            //把具体的错误放置在内部处理，以显示正确的错误信息提示，在此不作任何的错误处理
            log.error(errors);

        }finally {
            //记录出错信息
            if(StringUtils.isNotEmpty(errors)) {
                if (bpmInst != null) {
                    bpmInst.setErrors(errors);
                    bpmInstService.update(bpmInst);
                }
            }
        }
    }

    /**
     * 启动流程
     * @param message
     * @param defKey
     * @param index
     * @return
     * @throws Exception
     */
    private BpmInst doStartProcess(BaseEventMessage message,String defKey,int index) throws Exception{
        String startUserId = (String)message.getVars().get("startUserId");
        BpmDef bpmDef=bpmDefService.getMainByKey(defKey);

        ProcessStartCmd startCmd=getProcessStartCmd(message,defKey,index);

        JPaasUser jpaasUser = userClient.getUserById(startUserId);

        ContextUtil.setCurrentUser(jpaasUser);
        IExecutionCmd mainCmd=ProcessHandleUtil.getProcessCmd();
        //启动流程
        BpmInst bpmInst=bpmInstService.doStartProcess(startCmd,bpmDef,"start");
        //发送任务消息通知
        messageService.sendMsg();
        ProcessHandleUtil.clearProcessCmd();
        ProcessHandleUtil.setProcessCmd(mainCmd);
        return bpmInst;
    }

    /**
     * 获取启动流程的Cmd参数对象
     * @param message
     * @param defKey
     * @param index
     * @return
     */
    private ProcessStartCmd getProcessStartCmd(BaseEventMessage message, String defKey,int index){
        ProcessStartCmd cmd=new ProcessStartCmd();
        String actInstId = message.getActInstId();
        String mainDefId=message.getActDefId();
        String mainTaskId=null;
        if(message instanceof TaskEventMessage){
            mainTaskId=((TaskEventMessage)message).getTaskId();
        }
        String mainActivityId=message.getNodeId();
        JSONObject formData=message.getFormData();
        if(StringUtils.isNotEmpty(actInstId)) {
            cmd.setParentActInstId(actInstId);
        }
        if(StringUtils.isNotEmpty(mainDefId)) {
            cmd.getVars().put("mainDefId", mainDefId);
        }
        if(StringUtils.isNotEmpty(mainTaskId)) {
            cmd.getVars().put("mainTaskId", mainTaskId);
        }
        if(StringUtils.isNotEmpty(mainActivityId)) {
            cmd.getVars().put("mainActivityId", mainActivityId);
        }
        if(StringUtils.isNotEmpty(mainDefId)) {
            cmd.getVars().put("oldBusKey", message.getVars().get("busKey"));
        }
        JSONObject jsonData = new JSONObject();
        JSONObject vars=new JSONObject();
        BpmDef mainBpmDef=bpmDefService.getByActDefId(mainDefId);
        ProcessConfig processConfig = bpmDefService.getProcessConfig(mainBpmDef.getActDefId());
        List<SubProcessDefConfig> subConfigs=processConfig.getSubProcessDefs();
        for(SubProcessDefConfig sub:subConfigs){
            if(sub.getAlias().equals(defKey)){
                JSONArray setting=sub.getConfig().getJSONArray("data");
                jsonData.putAll(bpmInstService.parseFormData(setting,formData,index));
                JSONArray varData=sub.getConfig().getJSONArray("varData");
                vars.putAll(bpmInstService.parseVarData(varData,formData));
            }
        }
        BpmDef mainDef=bpmDefService.getMainByKey(defKey);
        cmd.setDefId(mainDef.getDefId());
        cmd.setDefKey(defKey);
        cmd.setActDefId(mainDef.getActDefId());
        cmd.setFormJson(jsonData.toJSONString());
        cmd.getVars().putAll(vars.getInnerMap());
        return cmd;
    }

}
