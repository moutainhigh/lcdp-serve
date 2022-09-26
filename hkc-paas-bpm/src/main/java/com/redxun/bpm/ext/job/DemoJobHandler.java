package com.redxun.bpm.ext.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * Xxl任务调用示例
 */
//@Component
public class DemoJobHandler extends IJobHandler {

    @Override
    @XxlJob("demoJobHandler")
    public void execute() throws Exception {
        System.err.println("demo");
        return ;
    }
}
