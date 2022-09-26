package com.redxun.bpm.core.service;

import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.ext.messagehandler.MessageUtil;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.common.utils.SpringUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
@Slf4j
public class MessageService {

    @Resource
    IOrgService orgService;
    @Autowired
    BpmDefService bpmDefService;
    @Resource
    BpmMessageTemplateServiceImpl bpmMessageTemplateService;



    public void sendMsg(){

        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        List<BpmTask> tasks = cmd.getTasks();
        if(BeanUtil.isEmpty(tasks)){
            log.error("任务为空");
            return;
        }

        IUser user= ContextUtil.getCurrentUser();
        OsUserDto sender=orgService.getUserById(user.getUserId());

        String checkType=cmd.getCheckType();
        JSONObject boDataMap=cmd.getBoDataMap();

        for(BpmTask bpmTask:tasks){

            List<OsUserDto> recievers=getReceiveUser(bpmTask.getTaskExecutors());
            if(BeanUtil.isEmpty(recievers)){
                continue;
            }

            Map<String,Object> vars=new HashMap<>();
            vars.put("taskId",bpmTask.getTaskId());
            vars.put("instId",bpmTask.getInstId());
            vars.put("defId",bpmTask.getDefId());
            vars.put("nodeId",bpmTask.getKey());
            vars.putAll(cmd.getVars());

            String messageTypes=getMessageTypes(bpmTask.getActDefId(),bpmTask.getKey());

            if(StringUtils.isEmpty(messageTypes)){
                return;
            }

            MessageUtil.sendMessage(sender,bpmTask.getSubject(),messageTypes,recievers,vars,"approve",checkType,boDataMap);

        }
    }

    /**
     * 根据流程定义ID和节点ID获取消息类型。
     * @param actDefId
     * @param nodeId
     * @return
     */
    private String getMessageTypes(String actDefId,String nodeId){
        UserTaskConfig userTaskConfig=(UserTaskConfig) bpmDefService.getNodeConfig(actDefId,nodeId);
        //节点配置为空则获取全局配置
        String noticeTypes="";
        if(StringUtils.isNotEmpty(userTaskConfig.getNoticeTypes())){
            noticeTypes=userTaskConfig.getNoticeTypes();
        }else {
            ProcessConfig processConfig = bpmDefService.getProcessConfig(actDefId);
            noticeTypes=processConfig.getNoticeTypes();
        }
        return noticeTypes;
    }

    private List<OsUserDto> getReceiveUser(Set<TaskExecutor> executors){
        List<OsUserDto> list=new ArrayList<>();
        for(TaskExecutor executor:executors){
            if(TaskExecutor.TYPE_USER.equals(executor.getType())){
                OsUserDto user=orgService.getUserById(executor.getId());
                list.add(user);
            }
            else{
                if(StringUtils.isNotEmpty(executor.getId())) {
                    List<OsUserDto> dtoList = orgService.getByGroupId(executor.getId());
                    for (OsUserDto user : dtoList) {
                        list.add(user);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取模板发送消息类型。
     * @param type
     * @return
     */
    public static String getTemplate(String type){
        String template= SpringUtil.getProperty("messageType." + type);
        if(StringUtils.isEmpty(template)){
            return "mail";
        }
        return template;
    }
}
