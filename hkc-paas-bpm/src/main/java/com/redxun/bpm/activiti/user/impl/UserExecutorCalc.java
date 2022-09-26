package com.redxun.bpm.activiti.user.impl;

import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 流程任务节点人员执行策略
 * 选择用户
 */
@Component
public class UserExecutorCalc implements ITaskExecutorCalc {

    @Resource
    IOrgService orgService;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("user","选择用户",1);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Set<TaskExecutor> taskExecutors=new LinkedHashSet<>();
        String userIds=userConfig.getConfig();
        if(StringUtils.isEmpty(userIds)){
            return taskExecutors;
        }

        List<OsUserDto> userDtoList = orgService.getUsersByIds(userIds);
        for(OsUserDto osUser:userDtoList){
            TaskExecutor userExecutor= TaskExecutor.getUser(osUser.getUserId(),osUser.getFullName(),osUser.getAccount());
            taskExecutors.add(userExecutor);
        }

        return taskExecutors;
    }
}
