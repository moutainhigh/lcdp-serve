package com.redxun.bpm.activiti.user.impl;

import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.bpm.core.entity.BpmInstVars;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.common.utils.ContextUtil;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 流程任务节点人员执行策略
 * 发起人
 */
@Component
public class StartUserExecutorCalc implements ITaskExecutorCalc {

    @Resource
    IOrgService orgService;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("starter","发起人",0);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        List<TaskExecutor> taskExecutors=new ArrayList<>();
        //从流程变量中获取启动用户
        String startUserId=(String)vars.get(BpmInstVars.START_USER_ID.getKey());
        //解决在流程发起还没有变量的情况。
        if(StringUtils.isEmpty(startUserId)){
            startUserId= ContextUtil.getCurrentUserId();
        }

        OsUserDto osUser=orgService.getUserById(startUserId);
        String userName="发起人";
        String account="";
        if(BeanUtil.isNotEmpty(osUser)){
            userName=osUser.getFullName();
            account=osUser.getAccount();
        }
        TaskExecutor userExecutor= TaskExecutor.getUser(startUserId,userName,account);

        taskExecutors.add(userExecutor);
        return taskExecutors;
    }
}
