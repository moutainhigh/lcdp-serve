package com.redxun.bpm.listener.taskcomplete;

import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.event.TaskCompleteApplicationEvent;
import com.redxun.bpm.core.entity.BpmInstTracked;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.IExecutionCmd;
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
 * 节点审批完成后发送消息。
 */
@Component
public class TrackedCompleteListener implements ApplicationListener<TaskCompleteApplicationEvent> {

    @Resource
    BpmInstTrackedServiceImpl bpmInstTrackedService;
    @Resource
    IOrgService orgService;


    @Override
    public void onApplicationEvent(TaskCompleteApplicationEvent taskCompleteApplicationEvent) {
        String messageType= MessageService.getTemplate("tracked");

        if(StringUtils.isEmpty(messageType)){
            return;
        }
        BpmTask bpmTask;
        Object obj=taskCompleteApplicationEvent.getSource();
        if(obj instanceof BpmTask){
            bpmTask=(BpmTask)obj;
        }else{
            IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
            bpmTask= (BpmTask) cmd.getTransientVar(BpmConst.BPM_APPROVE_TASK);
        }
        String instId=bpmTask.getInstId();
        List<BpmInstTracked>  instTrackeds = bpmInstTrackedService.getByInstId(instId);
        if(BeanUtil.isEmpty(instTrackeds)){
            return;
        }

        for(BpmInstTracked tracked:instTrackeds){
            sendMessage(tracked,bpmTask);
        }
    }

    private void sendMessage(BpmInstTracked tracked,BpmTask bpmTask) {
        String messageType= MessageService.getTemplate("tracked");

        String userId=tracked.getCreateBy();

        IUser user= ContextUtil.getCurrentUser();
        //自己不用给自己提醒。
        if(user.getUserId().equals(userId)){
            return;
        }

        OsUserDto userDto=orgService.getUserById(user.getUserId());

        Map<String,Object> vars=new HashMap<>();
        vars.put("instId",bpmTask.getInstId());
        vars.put("subject",bpmTask.getSubject());
        vars.put("defId",bpmTask.getDefId());
        vars.put("nodeId",bpmTask.getKey());
        vars.put("nodeName",bpmTask.getName());
        vars.put("status","approve");

        List<OsUserDto> receivers=new ArrayList<>();
        OsUserDto receiver=orgService.getUserById(userId);
        receivers.add(receiver);

        MessageUtil.sendMessage(userDto,bpmTask.getSubject(),messageType,"tracked",receivers,vars);

    }


}
