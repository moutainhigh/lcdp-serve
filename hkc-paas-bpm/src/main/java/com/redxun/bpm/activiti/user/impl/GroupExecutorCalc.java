package com.redxun.bpm.activiti.user.impl;

import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsGroupDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 流程任务节点人员执行策略
 * 用户组（部门、角色、职务等）
 */
@Component
public class GroupExecutorCalc implements ITaskExecutorCalc {

    @Resource
    IOrgService orgService;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("group","用户组（部门、角色、职务等）",2);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        String calcType= userConfig.getCalcType();
        Set<TaskExecutor> taskExecutors=new LinkedHashSet<>();
        String groupIds=userConfig.getConfig();
        if(StringUtils.isEmpty(groupIds)){
            return taskExecutors;
        }

        String[] gIds=groupIds.split("[,]");
        for(String gId:gIds){
            OsGroupDto osGroup=orgService.getGroupById(gId);
            TaskExecutor groupExecutor= TaskExecutor.getGroup(osGroup.getGroupId(),osGroup.getName());
            taskExecutors.add(groupExecutor);
        }

        return taskExecutors;
    }
}
