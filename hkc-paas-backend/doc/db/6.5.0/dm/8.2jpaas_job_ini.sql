INSERT INTO xxl_job_group(address_list,address_type,app_name,id,order_,title)
        values(         '192.168.137.1:7676',         '0',         'bpm-job',         '1366630651513499649',         1,         'bpm');

Insert into XXL_JOB_GROUP (ID,APP_NAME,TITLE,ORDER_,ADDRESS_TYPE,ADDRESS_LIST)
        values ('1385486577859444738','user-job','用户执行器',1,0,'192.168.137.1:7677');

INSERT INTO xxl_job_info (id, job_group, job_cron, job_desc, add_time, update_time, author, alarm_email, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
        VALUES ('1366632273874481154', '1366630651513499649', '* /5 * * * ?', '催办', to_date('2021-3-2 14:11:40','yyyy-MM-dd HH24:mi:ss'), to_date('2021-3-2 16:57:46','yyyy-MM-dd HH24:mi:ss'), 'admin', '', 'FIRST', 'remindJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', to_date('2021-3-2 14:11:40','yyyy-MM-dd HH24:mi:ss'), '', 0, 0, 0);

INSERT INTO xxl_job_info (id, job_group, job_cron, job_desc, add_time, update_time, author, alarm_email, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
        VALUES ('1385489751253073922', '1385486577859444738', '* /5 * * * ?', '用户离职', to_date('2021-03-02 14:11:40','yyyy-MM-dd HH24:mi:ss'), to_date('2021-03-02 16:57:46','yyyy-MM-dd HH24:mi:ss'), 'admin', '', 'FIRST', 'quitTimeJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', to_date('2021-03-02 14:11:40','yyyy-MM-dd HH24:mi:ss'), '', 0, 0, 0);


create sequence XXL_JOB_REGISTRY_ID
minvalue 1
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 20;
commit;

