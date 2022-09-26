package com.redxun.bpm.activiti.user.impl;

import com.redxun.api.org.IOrgService;
import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.bpm.activiti.user.ExecutorType;
import com.redxun.bpm.activiti.user.ITaskExecutorCalc;
import com.redxun.bpm.core.entity.BpmCheckHistory;
import com.redxun.bpm.core.entity.BpmInstVars;
import com.redxun.bpm.core.service.BpmCheckHistoryServiceImpl;
import com.redxun.common.tool.BeanUtil;
import com.redxun.common.tool.StringUtils;
import com.redxun.dto.bpm.TaskExecutor;
import com.redxun.dto.user.OsUserDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 流程任务节点人员执行策略
 * 用户来自其他节点的审批人
 */
@Component
public class PreNodeUserExecutorCalc implements ITaskExecutorCalc {

    @Resource
    BpmCheckHistoryServiceImpl bpmCheckHistoryService;
    @Resource
    IOrgService orgService;

    @Override
    public ExecutorType getType() {
        return new ExecutorType("preNodeUser","用户来自其他节点的审批人",3);
    }

    @Override
    public Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String, Object> vars) {
        Collection<TaskExecutor> users=new ArrayList<>();
        String nodeId = userConfig.getConfig();
        String instId = (String)vars.get(BpmInstVars.INST_ID.getKey());
        if(StringUtils.isEmpty(instId) || StringUtils.isEmpty(nodeId)){
            return users;
        }
        BpmCheckHistory bpmCheckHistory=bpmCheckHistoryService.getLatestHistoriesByNodeId(instId,nodeId);
        if(BeanUtil.isEmpty(bpmCheckHistory) || StringUtils.isEmpty(bpmCheckHistory.getHandlerId())){
            return users;
        }
        OsUserDto osUser=orgService.getUserById(bpmCheckHistory.getHandlerId());
        if(BeanUtil.isNotEmpty(osUser)) {
            TaskExecutor userExecutor = TaskExecutor.getUser(osUser.getUserId(), osUser.getFullName(),osUser.getAccount());
            users.add(userExecutor);
        }
        return users;
    }
}
