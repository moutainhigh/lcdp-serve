package com.redxun.bpm.activiti.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.bpm.core.entity.BpmCheckHistory;
import com.redxun.bpm.core.entity.BpmInstVars;
import com.redxun.bpm.core.entity.IExecutionCmd;
import com.redxun.bpm.core.entity.ProcessNextCmd;
import com.redxun.bpm.core.service.BpmCheckHistoryServiceImpl;
import com.redxun.bpm.core.service.BpmTaskUserServiceImpl;
import com.redxun.bpm.util.ProcessHandleUtil;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsRelInstDto;
import com.redxun.dto.user.OsRelTypeDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.OrgClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 流程任务节点人员执行策略
 * 通过用户与用户关系查找用户
 */
@Component
public class UserRelExecutorCalc implements ITaskExecutorCalc {

    @Resource
    OrgClient orgClient;
    @Resource
    private BpmTaskUserServiceImpl bpmTaskUserService;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("userRel","通过用户与用户关系查找用户",8);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Set<TaskExecutor> idInfos=new LinkedHashSet<>();
        IExecutionCmd cmd=  ProcessHandleUtil.getProcessCmd();

        if(StringUtils.isEmpty(userConfig.getConfig())){
            return idInfos;
        }

        JSONObject jsonObj=JSONObject.parseObject(userConfig.getConfig());

        String userVar=jsonObj.getString("user");

        Set<String> userIds=new HashSet<>();
        //发起人
        if("startUser".equals(userVar)){
            userIds.add((String)vars.get("startUserId"));
        }
        //来自表单数据
        else if("formJson".equals(userVar)){
            userIds=getFormUsers(cmd,jsonObj);
        }
        //当前审批人
        else if("curApprover".equals(userVar)){
            if(cmd!=null) {
                ProcessNextCmd nextCmd=(ProcessNextCmd)cmd;
                String taskId = nextCmd.getTaskId();
                Set<IUser> taskUsers=bpmTaskUserService.getTaskUsers(taskId);
                for (IUser taskUser : taskUsers) {
                    userIds.add(taskUser.getUserId());
                }
            }
        }
        //上一节点审批人
        else if("preApprover".equals(userVar)){
            IUser curUser= ContextUtil.getCurrentUser();
            userIds.add(curUser.getUserId());
        }

        //获得关系Key及需要查找的关系方
        String relTypeKey=jsonObj.getString("relTypeKey");
        //查找的关系方，为party1或party2里的值
        String party=jsonObj.getString("party");
        //查找到该关系类型
        OsRelTypeDto osRelType=orgClient.getRelTypeByKey(relTypeKey);

        if(osRelType==null||userIds.size()==0){
            return idInfos;
        }
        //查找一方
        if(osRelType.getParty1().equals(party)){
            for (String id : userIds) {
                List<OsRelInstDto> osRelInsts=orgClient.getByRelTypeKeyParty2(relTypeKey, id);
                for(OsRelInstDto inst:osRelInsts){
                    if(inst==null || StringUtils.isEmpty(inst.getParty1())){
                        continue;
                    }
                    OsUserDto osUser=orgClient.getUserById(inst.getParty1());
                    if(osUser!=null){
                        idInfos.add(TaskExecutor.getUser(osUser.getUserId(),osUser.getFullName(),osUser.getAccount()));
                    }
                }
            }

        }else{//查找另一方
            for (String id : userIds) {
                List<OsRelInstDto> osRelInsts=orgClient.getByRelTypeKeyParty1(relTypeKey, id);
                for(OsRelInstDto inst:osRelInsts){
                    OsUserDto osUser=orgClient.getUserById(inst.getParty2());
                    if(osUser!=null){
                        idInfos.add(TaskExecutor.getUser(osUser.getUserId(),osUser.getFullName(),osUser.getAccount()));
                    }
                }
            }
        }

        return idInfos;
    }


    private Set<String> getFormUsers(IExecutionCmd cmd,JSONObject jsonObj){
        Set<String> userIds=new LinkedHashSet<>();
        String userIdStr="";
        String varKey=jsonObj.getString("varKey");
        String boAlias=jsonObj.getString("boAlias");
        Integer isSingle=jsonObj.getInteger("isSingle");
        if(cmd!=null) {
            JSONObject formData = cmd.getBoDataMap();
            if (!formData.containsKey(boAlias)) {
                return userIds;
            }
            formData = formData.getJSONObject(boAlias);
            userIdStr=formData.getString(varKey);
            if(StringUtils.isEmpty(userIdStr)) {
                return userIds;
            }
            if(isSingle!=null && isSingle!=1){
                //双值
                JSONObject jsonObject = JSONObject.parseObject(userIdStr);
                userIdStr=jsonObject.getString("value");
            }
            String[] ids = userIdStr.split(",");
            for (String id : ids) {
                userIds.add(id);
            }
        }
        return userIds;
    }

}
