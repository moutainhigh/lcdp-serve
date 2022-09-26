package com.redxun.bpm.activiti.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsRelInstDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.OrgClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 流程任务节点人员执行策略
 * 通过用户组（部门、角色、组）与用户关系查找用户
 */
@Component
public class UserByUserGroupRelExecutorCalc implements ITaskExecutorCalc {

    @Resource
    OrgClient orgClient;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("userByUserGroupRel","通过用户组（部门、角色、组）与用户关系查找用户",10);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Set<TaskExecutor> idInfos=new LinkedHashSet<>();
        if(StringUtils.isEmpty(userConfig.getConfig())){
            return idInfos;
        }

        String groupId= getGroupId( userConfig,vars);
        JSONObject jsonObj=JSONObject.parseObject(userConfig.getConfig());

        boolean useRelation=jsonObj.getBooleanValue("useRelation");
        if(useRelation){
            idInfos= getByRelations(jsonObj,groupId);
        }
        else{
            List<OsUserDto> users=orgClient.getByGroupId(groupId);
            for(OsUserDto user:users){
                idInfos.add(TaskExecutor.getUser(user.getUserId(), user.getFullName(),user.getAccount()));
            }
        }

        return idInfos;
    }

    private Set<TaskExecutor> getByRelations(JSONObject jsonObj,String groupId){
        Set<TaskExecutor> idInfos=new LinkedHashSet<>();
        //获得关系Key及需要查找的关系方
        String relTypeKey=jsonObj.getString("relTypeKey");

        if(StringUtils.isEmpty(groupId)) {
            return idInfos;
        }

        String[] aryGroup=groupId.split("[,]");
        for(String id :aryGroup){
            //查找一方
            List<OsRelInstDto> osRelInsts=orgClient.getByRelTypeKeyParty1(relTypeKey, id);
            for(OsRelInstDto inst:osRelInsts){
                if(StringUtils.isEmpty(inst.getParty2())){
                    continue;
                }
                OsUserDto osUser=orgClient.getUserById(inst.getParty2());
                if(osUser==null) {
                    continue;
                }
                idInfos.add(TaskExecutor.getUser(osUser.getUserId(), osUser.getFullName(),osUser.getAccount()));
            }
        }
        return idInfos;
    }

    /**
     * 根据配置获取用户组。
     * @param userConfig
     * @return
     */
    private String getGroupId(UserConfig userConfig,Map<String, Object> vars) {
        String tenantId=ContextUtil.getCurrentTenantId();
        JSONObject jsonObj = JSONObject.parseObject(userConfig.getConfig());
        String fromVar = jsonObj.getString("fromVar");
        String groupId = null;
        if ("start-org".equals(fromVar)) {
            //发起人所在部门
            String startUserId = (String) vars.get("startUserId");
            if (StringUtils.isEmpty(startUserId)) {
                startUserId = ContextUtil.getCurrentUserId();
            }
            OsGroupDto mainGroup = orgClient.getMainDeps(startUserId, tenantId);
            if (mainGroup != null) {
                groupId = mainGroup.getGroupId();
            }
        }
        //上一步执行人所在部门
        else if ("cur-org".equals(fromVar)) {
            String userId = ContextUtil.getCurrentUserId();
            OsGroupDto mainGroup = orgClient.getMainDeps(userId, tenantId);
            if (mainGroup != null) {
                groupId = mainGroup.getGroupId();
            }
        } else if ("var".equals(fromVar)) {
            //变量
            String varName = jsonObj.getString("groupVar");
            groupId = (String) vars.get(varName);
        } else {//来自用户组
            groupId = jsonObj.getString("groupId");
        }
        //查找上级部门。
        boolean upperDep = false;
        if(jsonObj.containsKey("upperDep")){
            upperDep=jsonObj.getBoolean("upperDep");
        }
        if (upperDep) {
            OsGroupDto group = orgClient.getGroupById(groupId);
            if (BeanUtil.isNotEmpty(group)) {
                groupId = group.getParentId();
            }
        }

        return groupId;
    }
}
