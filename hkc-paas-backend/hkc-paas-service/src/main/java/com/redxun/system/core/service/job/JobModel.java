package com.redxun.system.core.service.job;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JobModel {

    /**
     * JOB组
     */
    private String jobGroup="";

    /**
     * 定时表达时
     */
    private String jobCron="";

    /**
     * 名称
     */
    private String name="";

    /**
     * 参数ID
     */
    private String id="";

    /**
     * 具体的执行器
     */
    private String executorHandler="";

    /**
     * 调度状态：0-停止，1-运行
      */
    private String status="1";

    /**
     * 参数
     */
    private String params="";

    /**
     * jobID
     */
    private String jobId="";

}
