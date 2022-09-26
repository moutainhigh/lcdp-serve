package com.redxun.bpm.activiti.ext;

import org.activiti.engine.delegate.DelegateExecution;


/**
 * 服务任务接口
 */
public interface IServiceTask {

    ServiceType getType();

    void handle(DelegateExecution execution,String setting);

}
