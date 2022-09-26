package com.redxun.bpm.activiti.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsGroupDto;
import com.redxun.dto.user.OsRelInstDto;
import com.redxun.dto.user.OsUserDto;
import com.redxun.feign.org.OrgClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 流程任务节点人员执行策略
 * 发起人所在部门往上查找符合等级的部门下关系用户
 */
@Component
public class GroupRankTypeRelExecutorCalc implements ITaskExecutorCalc {

    @Resource
    OrgClient orgClient;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("groupRankTypeRel","发起人所在部门往上查找符合等级的部门下关系用户",12);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Set<TaskExecutor> idInfos=new LinkedHashSet<>();
        if(StringUtils.isEmpty(userConfig.getConfig())) {
            return idInfos;
        }

        JSONObject jsonObj=JSONObject.parseObject(userConfig.getConfig());
        String userType=jsonObj.getString("userType");
        Integer rankLevel=jsonObj.getInteger("rankLevel");


        String userId="";
        if("start".equals(userType)){
            userId=(String)vars.get("startUserId");
        }
        else{
            userId= ContextUtil.getCurrentUserId();
        }
        String tenantId=ContextUtil.getCurrentTenantId();

        OsGroupDto mainDep= orgClient.getMainDeps(userId,tenantId);
        if(mainDep==null) {
            return idInfos;
        }

        //往上查找到符合等级的用户组
        OsGroupDto fitGroup=upGroup(mainDep,rankLevel);

        if(fitGroup==null){
            return idInfos;
        }

        //获得关系Key及需要查找的关系方
        String relTypeKey=jsonObj.getString("relTypeKey");

        //查找一方
        List<OsRelInstDto> osRelInsts=orgClient.getByRelTypeKeyParty1(relTypeKey, fitGroup.getGroupId());
        for(OsRelInstDto inst:osRelInsts){
            OsUserDto osUser=orgClient.getUserById(inst.getParty2());
            if(osUser==null){
                continue;
            }
            idInfos.add(TaskExecutor.getUser(osUser.getUserId(),osUser.getFullName(),osUser.getAccount()));
        }

        return idInfos;
    }

    /**
     * 往上查找符合条件的用户组
     * @param osGroup
     * @param level
     * @return
     */
    protected OsGroupDto upGroup(OsGroupDto osGroup,Integer level){

        if(osGroup==null){
            return null;
        }

        if(osGroup.getRankLevel()!=null && osGroup.getRankLevel().equals(level)){
            return osGroup;
        }

        if(StringUtils.isNotEmpty(osGroup.getParentId())){
            OsGroupDto parentGroup= orgClient.getGroupById(osGroup.getParentId());
            return upGroup(parentGroup,level);
        }

        return null;
    }
}
