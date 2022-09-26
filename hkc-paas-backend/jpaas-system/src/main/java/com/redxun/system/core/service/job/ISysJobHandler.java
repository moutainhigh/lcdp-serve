package com.redxun.system.core.service.job;

/**
 * 处理定时任务接口类。
 */
public interface ISysJobHandler {

    String getName();

    void handJob();
}
