package com.redxun.bpm.activiti.event;

import com.redxun.bpm.activiti.config.ProcessConfig;
import com.redxun.bpm.activiti.config.UserTaskConfig;
import com.redxun.bpm.core.entity.BpmTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.context.ApplicationEvent;

/**
 * <pre>
 * 描述：任务完成监听器
 * 作者：cjx
 * 邮箱:chshxuan@163.com
 * 日期:2016年5月7日-下午2:17:18
 * @Copyright (c) 2014-2016 广州红迅软件有限公司（http://www.redxun.cn）
 * 本源代码受软件著作法保护，请在授权允许范围内使用。
 * </pre>
 */
public class TaskCompleteApplicationEvent extends ApplicationEvent {

    private UserTaskConfig userConfig;
    private ProcessConfig processConfig;

    public TaskCompleteApplicationEvent(final TaskEntity taskEntity) {
        super(taskEntity);
    }

    public TaskCompleteApplicationEvent(final TaskEntity taskEntity, UserTaskConfig userTaskConfig) {
        super(taskEntity);
        this.userConfig = userTaskConfig;
    }


    public TaskCompleteApplicationEvent(final TaskEntity taskEntity, UserTaskConfig userTaskConfig, ProcessConfig processConfig) {
        super(taskEntity);
        this.userConfig = userTaskConfig;
        this.processConfig=processConfig;
    }

    public TaskCompleteApplicationEvent(final BpmTask taskEntity, UserTaskConfig userTaskConfig) {
        super(taskEntity);
        this.userConfig = userTaskConfig;
    }

    public TaskCompleteApplicationEvent(final BpmTask taskEntity, UserTaskConfig userTaskConfig, ProcessConfig processConfig) {
        super(taskEntity);
        this.userConfig = userTaskConfig;
        this.processConfig=processConfig;
    }

    public UserTaskConfig getConfig() {
        return this.userConfig;
    }

    public ProcessConfig getProcessConfig() {
        return this.processConfig;
    }


}
