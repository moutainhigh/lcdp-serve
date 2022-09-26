package com.redxun.bpm.listener.processend;

import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.event.ProcessCompletedEvent;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.ext.messagehandler.MessageUtil;
import com.redxun.bpm.core.service.BpmInstTrackedServiceImpl;
import com.redxun.bpm.core.service.MessageService;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.user.OsUserDto;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程完成时触发跟踪信息。
 * <pre>
 *     1.找到跟踪的实例信息。
 *     2.循环发送跟踪消息。
 *     3.删除实例的跟踪。
 * </pre>
 *
 */
@Component
public class TrackedEndListener implements ApplicationListener<ProcessCompletedEvent> {
    @Resource
    IOrgService orgService;
    @Resource
    BpmInstTrackedServiceImpl bpmInstTrackedService;


    @Override
    public void onApplicationEvent(ProcessCompletedEvent processCompletedEvent) {
        BpmInst bpmInst= processCompletedEvent.getBpmInst();
        if(bpmInst==null){
            return;
        }
        String messageType= MessageService.getTemplate("tracked");
        if(StringUtils.isEmpty(messageType)){
            return;
        }
        String instId=bpmInst.getInstId();
        List<BpmInstTracked>  instTrackeds = bpmInstTrackedService.getByInstId(instId);
        if(BeanUtil.isEmpty(instTrackeds)){
            return;
        }
        for(BpmInstTracked tracked:instTrackeds){
            sendMessage(tracked,bpmInst);
        }
        bpmInstTrackedService.removeTracked(instId);
    }

    /**
     * 发送消息。
     * @param tracked
     * @param bpmInst
     */
    private void sendMessage(BpmInstTracked tracked, BpmInst bpmInst) {
        String userId=tracked.getCreateBy();
        IUser user= ContextUtil.getCurrentUser();
        //自己不用给自己提醒。
        if(user.getUserId().equals(userId)){
            return;
        }
        String messageType= MessageService.getTemplate("tracked");

        OsUserDto userDto=orgService.getUserById(user.getUserId());
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        BpmTask bpmTask=(BpmTask) cmd.getTransientVar(BpmConst.BPM_APPROVE_TASK);
        Map<String,Object> vars=new HashMap<>();
        vars.put("instId",bpmInst.getInstId());
        vars.put("subject",bpmInst.getSubject());
        vars.put("defId",bpmInst.getDefId());
        if(bpmTask!=null) {
            vars.put("nodeId", bpmTask.getKey());
            vars.put("nodeName", bpmTask.getName());
        }
        vars.put("status","end");

        List<OsUserDto> receivers=new ArrayList<>();
        OsUserDto receiver=orgService.getUserById(userId);
        receivers.add(receiver);

        MessageUtil.sendMessage(userDto,bpmInst.getSubject(),messageType,"tracked",receivers,vars);

    }
}
