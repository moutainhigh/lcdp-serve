package com.redxun.bpm.activiti.eventhandler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.EventConfig;
import com.redxun.bpm.activiti.config.UserGroupConfig;
import com.redxun.bpm.activiti.eventhandler.BaseEventMessage;
import com.redxun.bpm.activiti.eventhandler.EventHanderType;
import com.redxun.bpm.activiti.eventhandler.IEventHandler;
import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.core.entity.*;
import com.redxun.bpm.core.ext.messagehandler.MessageUtil;
import com.redxun.bpm.core.service.*;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.constant.MBoolean;
import com.redxun.common.script.GroovyEngine;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.IdGenerator;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.BpmConst;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 抄送事件。
 *
 * @author ASUS
 * @date 2022/4/5 15:59
 */
@Component
public class CopyToEventHandler implements IEventHandler {

    @Resource
    TaskExecutorService taskExecutorService;
    @Resource
    IOrgService orgService;
    @Resource
    BpmInstCpServiceImpl bpmInstCpService;
    @Resource
    BpmInstServiceImpl bpmInstService;
    @Resource
    private GroovyEngine groovyEngine;
    @Resource
    BpmDefService bpmDefService;
    @Resource
    BpmInstCcServiceImpl bpmInstCcService;
    @Resource
    BpmInstPermissionServiceImpl bpmInstPermissionService;


    @Override
    public EventHanderType getType() {
        return new EventHanderType("copyTo","抄送");
    }

    @Override
    public void handEvent(BaseEventMessage message) {

        EventConfig eventSetting= message.getEventConfig();
        JSONObject config=eventSetting.getConfig();
        if(BeanUtil.isEmpty(config)) {
            return;
        }

        JSONArray aryConf= config.getJSONArray("userCnfs");
        if(BeanUtil.isEmpty(aryConf)){
            return;
        }

        Map<String, Object> vars =message.getVars();

        if(BeanUtil.isEmpty(vars)){
            vars = new HashMap<>();
        }
        BpmInstCc instCc = buildBpmInstCc(vars,message);
        for(int i=0;i<aryConf.size();i++){
            JSONObject conf=aryConf.getJSONObject(i);
            UserGroupConfig userGroupConfig= conf.toJavaObject(UserGroupConfig.class);
            if(BeanUtil.isNotEmpty(userGroupConfig)){
                String condition=userGroupConfig.getCondition();
                //不满足条件则跳过。
                boolean condiFlag= handCondition(vars,condition);
                if(!condiFlag){
                    continue;
                }
                Set<TaskExecutor> userSets=taskExecutorService.getTaskExecutors(userGroupConfig,vars);
                //获取接收人。
                List<OsUserDto> users = getCopyUsers(userSets);
                if(BeanUtil.isEmpty(users)){
                    continue;
                }

                //添加抄送接收人
                addBpmInstReceivers(instCc, users);
                //发送消息。
                sendMessage(instCc, userGroupConfig.getInfoTypes(), users);
            }
        }
    }

    /**
     * 构建抄送对象。
     * @param vars
     * @param message
     * @return
     */
    protected BpmInstCc buildBpmInstCc(Map<String,Object> vars, BaseEventMessage message){
        // 构建抄送实例
        String defId=(String)vars.get(BpmInstVars.DEF_ID.getKey());
        String instId=(String)vars.get(BpmInstVars.INST_ID.getKey());
        String subject=(String)vars.get(BpmInstVars.PROCESS_SUBJECT.getKey());

        IUser currentUser= ContextUtil.getCurrentUser();
        BpmDef bpmDef=bpmDefService.get(defId);

        BpmInstCc instCc=new BpmInstCc();
        instCc.setId(IdGenerator.getIdStr());
        instCc.setCcType(BpmInstCc.CC_TYPE_COPY);
        instCc.setDefId(defId);
        instCc.setInstId(instId);
        instCc.setTreeId(bpmDef.getTreeId());
        instCc.setFromUser(currentUser.getFullName());
        instCc.setFromUserId(currentUser.getUserId());
        instCc.setSubject(subject);
        instCc.setNodeId(message.getNodeId());
        instCc.setNodeName(message.getNodeName());

        return  instCc;
    }

    /**
     * 流程任务消息发送
     * @param bpmInstCc
     * @param informTypes
     * @param users
     */
    protected void sendMessage(BpmInstCc bpmInstCc, String informTypes ,List<OsUserDto> users){
        IExecutionCmd cmd= ProcessHandleUtil.getProcessCmd();
        IUser user= ContextUtil.getCurrentUser();

        OsUserDto userDto=orgService.getUserById(user.getUserId());

        Map<String,Object> vars=new HashMap<>();

        if(cmd.getBoDataMap()!=null) {
            vars.putAll(cmd.getBoDataMap());
        }
        vars.put("instId",bpmInstCc.getInstId());
        BpmInst bpmInst = bpmInstService.get(bpmInstCc.getInstId());
        if(bpmInst==null){
            bpmInst=(BpmInst) cmd.getTransientVar(BpmConst.BPM_INST);
        }
        vars.put("actDefId",bpmInst.getActDefId());

        MessageUtil.sendMessage(userDto,bpmInstCc.getSubject(),informTypes,"copy",users,vars);

    }

    /**
     * 添加抄送人员。
     * @param instCc
     * @param users
     */
    protected void addBpmInstReceivers(BpmInstCc instCc, List<OsUserDto> users){
        if(BeanUtil.isEmpty(users)){
            return;
        }
        IExecutionCmd cmd=ProcessHandleUtil.getProcessCmd();
        BpmInst bpmInst= (BpmInst) cmd.getTransientVar(BpmConst.BPM_INST);

        bpmInstCcService.insert(instCc);

        for(OsUserDto user:users){
            if(BeanUtil.isEmpty(user)){
                continue;
            }
            BpmInstCp bpmInstCp=new BpmInstCp();
            bpmInstCp.setId(IdGenerator.getIdStr());
            bpmInstCp.setCcId(instCc.getId());
            bpmInstCp.setInstId(instCc.getInstId());
            bpmInstCp.setUserId(user.getUserId());
            bpmInstCp.setIsRead(MBoolean.NO.name());
            bpmInstCpService.insert(bpmInstCp);

            bpmInstPermissionService.create(bpmInst,BpmInstPermission.TYPE_HANDLER,user,"","");

        }

    }

    /**
     * 获取抄送用户
     * @param taskExecutors
     * @return
     */
    protected List<OsUserDto> getCopyUsers(Set<TaskExecutor> taskExecutors){
        List<TaskExecutor> list=new ArrayList<>();
        list.addAll(taskExecutors);
        List<OsUserDto> users= orgService.getUsersByTaskExecutor(list);
        return users;
    }


    /**
     * 处理组条件。
     * @param vars
     * @param condition
     * @return
     */
    private boolean handCondition(Map<String,Object> vars,String condition){
        if(StringUtils.isEmpty(condition)){
            return true;
        }
        Map<String,Object> model= ActivitiUtil.getConextData(vars);
        Object obj=groovyEngine.executeScripts(condition,model);
        if(obj instanceof  Boolean){
            return (Boolean)obj;
        }
        return false;
    }

    private List<UserGroupConfig> getListConfig(JSONArray aryConf){
        List<UserGroupConfig> list=new ArrayList<>();

        for(int i=0;i<aryConf.size();i++){
            JSONObject conf=aryConf.getJSONObject(i);
            UserGroupConfig userGroupConfig= conf.toJavaObject(UserGroupConfig.class);

            list.add(userGroupConfig);
        }
        return list;
    }
}
