package com.redxun.bpm.activiti.event;

import com.redxun.bpm.core.entity.BpmTask;
import com.redxun.bpm.core.entity.BpmTaskUser;
import com.redxun.dto.bpm.TaskExecutor;
import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 流程任务节点人员分配事件
 */
public class TaskAssignApplicationEvent extends ApplicationEvent {
    public TaskAssignApplicationEvent(final BpmTask bpmTask) {
        super(bpmTask);
    }

    private BpmTaskUser bpmTaskUser=new BpmTaskUser();

    public void setBpmTaskUser(BpmTaskUser bpmTaskUser){
        this.bpmTaskUser=new BpmTaskUser();
    }

    public BpmTaskUser getBpmTaskUser(){
        return this.bpmTaskUser;
    }
}
