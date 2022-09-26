package com.redxun.bpm.activiti.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
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
 *  流程任务节点人员执行策略
 *  通过用户与组关系查找
 */
@Component
public class GroupByUserGroupRelExecutorCalc implements ITaskExecutorCalc {

    @Resource
    OrgClient orgClient;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("groupByUserGroupRel","通过用户与组关系查找",11);
    }//用户组来自用户与组关系运算

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Set<TaskExecutor> idInfos=new LinkedHashSet<>();

        if(StringUtils.isEmpty(userConfig.getConfig())) {
            return idInfos;
        }

        JSONObject jsonObj=JSONObject.parseObject(userConfig.getConfig());
        String varType=jsonObj.getString("varType");
        String userIdVar=jsonObj.getString("userId");
        //从变量中取得该用户的实际值，其他来源有：上一任务审批人、发起人、变量。
        String userId=(String)vars.get(userIdVar);
        //获得关系Key及需要查找的关系方
        String relTypeKey=jsonObj.getString("relTypeKey");

        String orgDimId = jsonObj.getString("orgDimId");
        //查找一方
        List<OsRelInstDto> osRelInsts = new ArrayList<>();
        if("user".equals(varType)) {  //基于用户关系来查找
            if(StringUtils.isNotEmpty(orgDimId)){
                osRelInsts=orgClient.getByRelTypeKeyParty2AndDim1(relTypeKey, userId,orgDimId);
            }else {
                osRelInsts=orgClient.getByRelTypeKeyParty2(relTypeKey, userId);
            }
            for(OsRelInstDto inst:osRelInsts){
                OsGroupDto osGroup=orgClient.getGroupById(inst.getParty1());
                if(osGroup!=null) {
                    idInfos.add(TaskExecutor.getGroup(osGroup.getGroupId(),osGroup.getName()) );
                }
            }
        }else if("org".equals(varType)) { // 基于组关系来查找
            if(StringUtils.isNotEmpty(orgDimId)){
                osRelInsts=orgClient.getByRelTypeKeyParty1AndDim1(relTypeKey, userId,orgDimId);
            }else {
                osRelInsts=orgClient.getByRelTypeKeyParty1(relTypeKey, userId);
            }
            for(OsRelInstDto inst:osRelInsts){
                OsUserDto user = orgClient.getUserById(inst.getParty2());
                if(user!=null) {
                    idInfos.add(TaskExecutor.getUser(user.getUserId(), user.getFullName(),user.getAccount()));
                }
            }
        }

        return idInfos;
    }
}
