use jpaas_job;
INSERT INTO xxl_job_group (id, app_name, title, order_, address_type, address_list)
        VALUES ('1366630651513499649', 'bpm-job', 'bpm', 1, 0, '192.168.137.1:7676');

INSERT INTO xxl_job_group (id, app_name, title, order_, address_type, address_list)
        VALUES ('1385486577859444738', 'user-job', '用户执行器', 1, 0, '192.168.137.1:7677');

INSERT INTO xxl_job_info(id, job_group, job_cron, job_desc, add_time, update_time, author, alarm_email, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
        VALUES ('1366632273874481154', '1366630651513499649', '* /5 * * * ?', '催办', '2021-3-2 14:11:40', '2021-3-2 16:57:46', 'admin', '', 'FIRST', 'remindJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-3-2 14:11:40', '', 0, 0, 0);

INSERT INTO xxl_job_info(id, job_group, job_cron, job_desc, add_time, update_time, author, alarm_email, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
        VALUES ('1385489751253073922', '1385486577859444738', '* /5 * * * ?', '用户离职', '2021-3-2 14:11:40', '2021-3-2 16:57:46', 'admin', '', 'FIRST', 'quitTimeJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2021-3-2 14:11:40', '', 0, 0, 0);

commit;