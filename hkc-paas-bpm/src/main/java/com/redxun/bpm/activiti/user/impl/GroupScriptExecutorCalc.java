package com.redxun.bpm.activiti.user.impl;

import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.bpm.activiti.utils.ActivitiUtil;
import com.redxun.bpm.script.ProcessScriptEngine;
import com.redxun.common.base.entity.IUser;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsGroupDto;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * 流程任务节点人员执行策略
 * 通过人员或组（部门、角色、组）脚本查找
 */
@Component
public class GroupScriptExecutorCalc implements ITaskExecutorCalc {

    @Resource
    ProcessScriptEngine processScriptEngine;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("groupScript","通过人员或组（部门、角色、组）脚本查找",7);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Set<TaskExecutor> idList=new LinkedHashSet<>();
        if(StringUtils.isEmpty(userConfig.getConfig())){
            return idList;
        }

        Map<String,Object> contextData= ActivitiUtil.getConextData(vars);
        Object jsonResult= processScriptEngine.exeScript(userConfig.getConfig(),contextData);
        //若返回为集合
        if(jsonResult instanceof Collection){ //返回用户或组集合
            Collection  idInfos=(Collection)jsonResult;
            idList.addAll(idInfos);
        }else if(jsonResult instanceof OsGroupDto){ // 返回用户组的脚本
            //返回值为用户组
            OsGroupDto group=(OsGroupDto)jsonResult;
            idList.add(TaskExecutor.getGroup(group.getGroupId(),group.getName()));
        }else if(jsonResult instanceof IUser){ //返回用户的脚本
            //返回值为用户
            IUser user=(IUser)jsonResult;
            idList.add(TaskExecutor.getUser(user.getUserId(),user.getFullName(),user.getAccount()));
        }


        return idList;
    }
}
