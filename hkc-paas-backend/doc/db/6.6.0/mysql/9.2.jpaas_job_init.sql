use jpaas_job;

INSERT INTO xxl_job_group(ID,APP_NAME,TITLE,ORDER_,ADDRESS_TYPE,ADDRESS_LIST,UPDATE_TIME)
        values(          '1366630651513499649',          'bpm-job',          'bpm',          1,          '0',          '192.168.137.1:7676',          null);
        
INSERT INTO xxl_job_group(ID,APP_NAME,TITLE,ORDER_,ADDRESS_TYPE,ADDRESS_LIST,UPDATE_TIME)
        values(          '1385486577859444738',          'user-job',          '用户执行器',          1,          '0',          '192.168.137.1:7677',          null);
        
INSERT INTO xxl_job_info(ID,JOB_GROUP,JOB_DESC,ADD_TIME,UPDATE_TIME,AUTHOR,ALARM_EMAIL,EXECUTOR_ROUTE_STRATEGY,EXECUTOR_HANDLER,EXECUTOR_PARAM,EXECUTOR_BLOCK_STRATEGY,EXECUTOR_TIMEOUT,EXECUTOR_FAIL_RETRY_COUNT,GLUE_TYPE,GLUE_SOURCE,GLUE_REMARK,GLUE_UPDATETIME,CHILD_JOBID,TRIGGER_STATUS,TRIGGER_LAST_TIME,TRIGGER_NEXT_TIME,SCHEDULE_TYPE,SCHEDULE_CONF,MISFIRE_STRATEGY)
        values(          '1366632273874481154',          '1366630651513499649',          '催办',          '2021-3-2 14:11:40',          '2021-3-2 16:57:46',          'admin',          '',          'FIRST',          'remindJobHandler',          '',          'SERIAL_EXECUTION',          0,          0,          'BEAN',          '',          'GLUE代码初始化',          '2021-3-2 14:11:40',          '',          '0',          0,          0,          null,          null,          null);
        
INSERT INTO xxl_job_info(ID,JOB_GROUP,JOB_DESC,ADD_TIME,UPDATE_TIME,AUTHOR,ALARM_EMAIL,EXECUTOR_ROUTE_STRATEGY,EXECUTOR_HANDLER,EXECUTOR_PARAM,EXECUTOR_BLOCK_STRATEGY,EXECUTOR_TIMEOUT,EXECUTOR_FAIL_RETRY_COUNT,GLUE_TYPE,GLUE_SOURCE,GLUE_REMARK,GLUE_UPDATETIME,CHILD_JOBID,TRIGGER_STATUS,TRIGGER_LAST_TIME,TRIGGER_NEXT_TIME,SCHEDULE_TYPE,SCHEDULE_CONF,MISFIRE_STRATEGY)
        values(          '1385489751253073922',          '1385486577859444738',          '用户离职',          '2021-3-2 14:11:40',          '2021-3-2 16:57:46',          'admin',          '',          'FIRST',          'quitTimeJobHandler',          '',          'SERIAL_EXECUTION',          0,          0,          'BEAN',          '',          'GLUE代码初始化',          '2021-3-2 14:11:40',          '',          '0',          0,          0,          null,          null,          null);
        

commit;