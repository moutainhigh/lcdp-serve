-- select CONCAT('ALTER TABLE jpaas_ureport.',table_name,' ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT \'逻辑删除\';') sqlval from information_schema.tables where table_schema='jpaas_ureport';

-- jpaas-system
--  逻辑删除 DELETED_   int(10)
ALTER TABLE jpaas_system.sys_app ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_app_action_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_app_auth ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_app_auth_menu ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_app_favorites ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_app_install ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_app_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_app_manager ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_app_relation ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_app_run ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_app_version ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_auth_manager ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_auth_rights ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_auth_setting ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_dic ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_error_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_excel ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_excel_batmanage ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_excel_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_external_api ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_file ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_http_task ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_http_task_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_interface_api ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_interface_class ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_interface_project ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_invoke_script ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_job ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_job_task ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_kettle_dbdef ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_kettle_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_kettle_job ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_kettle_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_menu ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_menu_release ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_office ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_office_template ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_office_ver ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_properties ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_route_type ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_routing ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_seq_id ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_signature ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_transfer_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_transfer_setting ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_tree ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_tree_cat ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_webreq_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_system.sys_word_template ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';

-- jpaas_bpm
ALTER TABLE jpaas_bpm.act_evt_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ge_bytearray ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ge_property ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_hi_actinst ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_hi_attachment ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_hi_comment ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_hi_detail ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_hi_identitylink ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_hi_procinst ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_hi_taskinst ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_hi_varinst ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_procdef_info ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_re_deployment ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_re_model ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_re_procdef ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ru_deadletter_job ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ru_event_subscr ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ru_execution ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ru_identitylink ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ru_integration ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ru_job ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ru_suspended_job ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ru_task ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ru_timer_job ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.act_ru_variable ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_agent ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_agent_flowdef ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_archive_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_cal_calendar ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_cal_grant ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_cal_setting ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_caltime_block ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_check_file ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_check_history ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_default_template ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_deliver ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_deliver_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_inst ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_inst_cc ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_inst_cp ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_inst_data ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_inst_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_inst_msg ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_inst_router ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_inst_tracked ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_message_template ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_mobile_tag ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_opinion_lib ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_quit_user_task ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_remind_history ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_remind_inst ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_ru_path ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_sign_data ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_task ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_task_transfer ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_task_user ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_temporary_opinion ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_transfer ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_bpm.bpm_transfer_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';

-- jpaas_form
ALTER TABLE jpaas_form.form_bo_attr ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_bo_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_bo_entity ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_bo_list ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_bo_list_history ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_bo_pmt ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_bo_relation ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_bus_inst_data ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_business_solution ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_calendar_view ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_change_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_chart_data_model ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_codegen_globalvar ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_codegen_setting ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_codegen_template ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_custom ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_custom_query ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_datasource_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_datasource_share ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_def_permission ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_download_record ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_ent_relation ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_entity_data_setting ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_entity_data_setting_dic ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_entity_data_type ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_excel_gen_task ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_execute_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_mobile ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_pc ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_pc_history ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_pdf_template ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_permission ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_print_lodop ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_query_strategy ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_reg_lib ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_rule ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_save_export ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_solution ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_sql_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_table_formula ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.form_template ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.grid_report_design ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_form.sys_nation_area ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';

-- jpaas_portal
ALTER TABLE jpaas_portal.inf_inbox ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.inf_inner_msg ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.inf_inner_msg_log ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.ins_app_collect ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.ins_column_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.ins_column_temp ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.ins_msg_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.ins_msgbox_box_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.ins_msgbox_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.ins_news ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.ins_portal_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.ins_portal_permission ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_portal.ins_remind_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';

-- jpaas_user
ALTER TABLE jpaas_user.os_company_auth ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_dd_agent ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_dimension ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_fs_agent ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_group ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_group_menu ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_inst ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_inst_type ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_inst_type_menu ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_inst_users ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_password_input_error ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_password_policy ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_properties_def ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_properties_group ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_properties_val ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_rank_type ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_rel_inst ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_rel_type ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_user ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_user_platform ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_user_type ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';
ALTER TABLE jpaas_user.os_wx_ent_agent ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';

-- jpaas_ureport
ALTER TABLE jpaas_ureport.ureport_file ADD COLUMN DELETED_ INT(10)  DEFAULT 0 COMMENT '逻辑删除';