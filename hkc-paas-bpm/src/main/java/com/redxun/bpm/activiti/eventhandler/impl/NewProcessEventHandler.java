package com.redxun.bpm.activiti.eventhandler.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.EventConfig;
import com.redxun.bpm.activiti.eventhandler.BaseEventMessage;
import com.redxun.bpm.activiti.eventhandler.EventHanderType;
import com.redxun.bpm.activiti.eventhandler.IEventHandler;
import com.redxun.bpm.activiti.eventhandler.TaskEventMessage;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.service.BpmDefService;
import com.redxun.bpm.core.service.BpmInstServiceImpl;
import com.redxun.bpm.core.service.MessageService;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.model.JPaasUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.ExceptionUtil;
import com.redxun.feign.org.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 新建流程处理器。
 *
 * 业务场景:
 *  在流程运行时，我们希望新建一个流程，这个流程的业务主键和当前的主键是一致的，即表单配置是一致的。
 *
 */
@Component
@Slf4j
public class NewProcessEventHandler implements IEventHandler {

    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    MessageService messageService;
    @Resource
    UserClient userClient;

    @Override
    public EventHanderType getType() {
        return new EventHanderType("newProcess", "新流程");
    }

    @Override
    public void handEvent(BaseEventMessage message) {
        EventConfig eventSetting = message.getEventConfig();
        JSONObject config = eventSetting.getConfig();
        if (BeanUtil.isEmpty(config)) {
            return;
        }
        String processKey = config.getString("processKey");
        if (StringUtils.isEmpty(processKey)) {
            return;
        }
        String error="";
        BpmInst bpmInst=null;
        try{
            bpmInst=doStartProcess(message,processKey);
        }catch (Exception e){
            //把具体的错误放置在内部处理，以显示正确的错误信息提示，在此不作任何的错误处理
            error=ExceptionUtil.getExceptionMessage(e);
            log.error(error);
        }finally {
            //记录出错信息
            if(StringUtils.isNotEmpty(error)) {
                if (bpmInst != null) {
                    bpmInst.setErrors(error);
                    bpmInstService.update(bpmInst);
                }
            }
        }
    }

    private BpmInst doStartProcess(BaseEventMessage message, String process) throws Exception{
        String startUserId = (String) message.getVars().get("startUserId");
        BpmDef bpmDef = bpmDefService.getMainByKey(process);

        ProcessStartCmd startCmd = getProcessStartCmd(message, process);

        JPaasUser jpaasUser = userClient.getUserById(startUserId);
        ContextUtil.setCurrentUser(jpaasUser);
        IExecutionCmd mainCmd= ProcessHandleUtil.getProcessCmd();
        //启动流程
        BpmInst bpmInst=bpmInstService.doStartProcess(startCmd,bpmDef,"start");
        //发送任务消息通知
        messageService.sendMsg();
        ProcessHandleUtil.clearProcessCmd();
        ProcessHandleUtil.setProcessCmd(mainCmd);

        return bpmInst;
    }

    private ProcessStartCmd getProcessStartCmd(BaseEventMessage message, String defKey) {
        ProcessStartCmd cmd = new ProcessStartCmd();
        String actInstId = message.getActInstId();
        String mainDefId=message.getActDefId();
        String mainTaskId=null;
        if(message instanceof TaskEventMessage){
            mainTaskId=((TaskEventMessage)message).getTaskId();
        }
        String mainActivityId=message.getNodeId();
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
        BpmDef mainDef=bpmDefService.getMainByKey(defKey);
        cmd.setDefId(mainDef.getDefId());
        cmd.setDefKey(defKey);
        cmd.setActDefId(mainDef.getActDefId());
        cmd.setBusKey((String)message.getVars().get(BpmInstVars.BUS_KEY.getKey()));
        return cmd;
    }
}
