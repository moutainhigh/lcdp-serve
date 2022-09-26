package com.redxun.bpm.activiti.user;

import com.redxun.bpm.activiti.config.UserConfig;
import com.redxun.dto.bpm.TaskExecutor;

import java.util.Collection;
import java.util.Map;

/**
 * 流程人员计算策略接口。
 * @author csx
 */
public interface ITaskExecutorCalc {
    /**
     * 人员计算策略类型
     * @return
     */
    ExecutorType getType();

    /**
     * 计算用的执行人接口
     * @param userConfig 当前节点的人员配置
     * @param vars 流程变量
     * @return
     */
    Collection<TaskExecutor> getExecutors(UserConfig userConfig, Map<String,Object> vars);

}
