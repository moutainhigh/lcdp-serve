
ALTER TABLE jpaas_bpm.bpm_agent ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_agent_flowdef ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_archive_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_cal_calendar ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_cal_grant ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_cal_setting ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_caltime_block ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_check_file ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_check_history ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_default_template ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_deliver ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_deliver_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_inst ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_inst_cc ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_inst_cp ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_inst_data ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_inst_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_inst_msg ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_inst_router ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_message_template ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_mobile_tag ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_opinion_lib ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_quit_user_task ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_remind_history ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_remind_inst ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_ru_path ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_sign_data ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_task ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_task_transfer ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_task_user ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_temporary_opinion ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_transfer ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.bpm_transfer_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_bpm.demo ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';


ALTER TABLE jpaas_portal.inf_inbox ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.inf_inner_msg ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.inf_inner_msg_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_app_collect ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_column_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_column_temp ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_msg_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_msgbox_box_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_msgbox_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_news ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_news_company_grant ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_portal_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_portal_permission ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_portal.ins_remind_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';

ALTER TABLE jpaas_user.os_dd_agent ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_dimension ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_grade_admin ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_grade_role ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_group ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_group_api ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_group_app ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_group_menu ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_inst ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_inst_type ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_inst_type_menu ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_inst_users ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_password_input_error ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_password_policy_configure ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_properties_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_properties_group ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_properties_val ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_rank_type ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_rel_inst ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_rel_type ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_user ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_user_type ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_user.os_wx_ent_agent ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';

ALTER TABLE jpaas_form.demo ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_attachment ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_bo_attr ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_bo_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_bo_entity ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_bo_list ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_bo_list_history ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_bo_pmt ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_bo_relation ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_bus_api ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_bus_inst_data ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_business_solution ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_chart_data_model ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_codegen_globalvar ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_codegen_template ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_custom ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_custom_query ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_datasource_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_datasource_share ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_def_permission ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_entity_data_setting ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_entity_data_setting_dic ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_entity_data_type ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_file_type ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_mobile ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_pc ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_pc_history ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_pdf_template ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_permission ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_print_lodop ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_query_strategy ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_reg_lib ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_rule ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_save_export ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_solution ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_sql_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_table_formula ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_template ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.grid_report_design ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_form.form_calendar_view ADD COMPANY_ID_ VARCHAR(24)  DEFAULT '0' COMMENT '公司ID';


ALTER TABLE jpaas_system.sys_app ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_app_action_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_app_auth ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_app_auth_menu ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_app_favorites ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_app_install ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_app_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_app_manager ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_app_relation ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_app_run ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_app_version ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_auth_manager ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_auth_rights ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_auth_setting ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_dic ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_error_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_excel ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_excel_batmanage ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_excel_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_file ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_inform ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_inform_pdf ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_interface_api ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_interface_call_logs ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_interface_class ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_interface_project ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_invoke_script ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_job ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_job_task ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_kettle_dbdef ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_kettle_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_kettle_job ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_kettle_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_menu ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_menu_release ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_office ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_office_template ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_office_ver ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_properties ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_route_type ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_routing ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_seq_id ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_signature ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_transfer_log ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_transfer_setting ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_tree ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_tree_cat ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_webreq_def ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';
ALTER TABLE jpaas_system.sys_word_template ADD COMPANY_ID_ varchar(24)  DEFAULT '0' COMMENT '公司ID';

