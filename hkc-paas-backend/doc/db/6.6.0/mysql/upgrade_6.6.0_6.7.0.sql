-- 2022-07-14 szw  jpaas_form  异步导出配置表新增字段
-- ALTER TABLE jpaas_form.FORM_EXCEL_GEN_TASK ADD COLUMN CREATE_BY_NAME_ varchar(128)  COMMENT '创建人名称';
-- ALTER TABLE jpaas_form.FORM_DOWNLOAD_RECORD ADD COLUMN CREATE_BY_NAME_ varchar(128)  COMMENT '创建人名称';
-- ALTER TABLE jpaas_form.FORM_CUSTOM_QUERY ADD COLUMN IS_TENANT_ VARCHAR(20) COMMENT '是否为租户使用';

-- 2022-07-19 szw  jpaas_portal  异步导出配置表新增字段
-- ALTER TABLE jpaas_portal.ins_app_collect    ADD COLUMN `APP_`  varchar(64) NULL COMMENT '内部应用';
-- ALTER TABLE jpaas_portal.ins_app_collect    ADD COLUMN `APP_TYPE_`  varchar(64) NULL COMMENT '应用类型(default、custom)';

-- 2022-07-21 szw  以上SQL已经整合到建表脚本
-- ALTER TABLE jpaas_job.xxl_job_group ADD COLUMN  update_time datetime COMMENT '更新时间';
-- ALTER TABLE jpaas_job.xxl_job_info ADD COLUMN  schedule_type varchar(64) COMMENT '调度类型';
-- ALTER TABLE jpaas_job.xxl_job_info ADD COLUMN  schedule_conf varchar(128) COMMENT '调度配置，值含义取决于调度类型';
-- ALTER TABLE jpaas_job.xxl_job_info ADD COLUMN  misfire_strategy varchar(64) COMMENT '调度过期策略';

-- 2022-07-27 yl  jpaas_datart  新增大屏脚本
-- CREATE TABLE `jpaas_datart`.`share`  (
--                           `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
--                           `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
--                           `viz_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
--                           `viz_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
--                           `authentication_mode` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
--                           `roles` text CHARACTER SET utf8 COLLATE utf8_general_ci,
--                           `row_permission_by` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
--                           `authentication_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
--                           `expiry_date` timestamp NULL DEFAULT NULL,
--                           `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
--                           `create_time` timestamp NULL DEFAULT NULL,
--                           PRIMARY KEY (`id`) USING BTREE,
--                           KEY `viz_id` (`viz_id`) USING BTREE
-- ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- CREATE TABLE `jpaas_datart`.`source_schemas`  (
--                                    `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
--                                    `source_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
--                                    `schemas` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
--                                    `update_time` datetime(0) NULL DEFAULT NULL,
--                                    PRIMARY KEY (`id`) USING BTREE
-- ) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ALTER TABLE `jpaas_datart`.`folder`
--     ADD COLUMN `sub_type` varchar(255) NULL AFTER `rel_type`,
--     ADD COLUMN `avatar` varchar(255) NULL AFTER `rel_id`;

-- ALTER TABLE `jpaas_datart`.`source`
--     ADD COLUMN `parent_id` varchar(32) NULL AFTER `org_id`,
--     ADD COLUMN `is_folder` tinyint(1) NULL AFTER `parent_id`,
--     ADD COLUMN `index` double(16, 8) NULL AFTER `is_folder`;


-- ALTER TABLE `jpaas_datart`.`schedule`
--     MODIFY COLUMN `index` double(16, 8) NULL DEFAULT NULL AFTER `is_folder`;

-- ALTER TABLE `jpaas_datart`.`variable`
--     ADD COLUMN `source_id` varchar(32) NULL AFTER `view_id`,
--     ADD COLUMN `format` varchar(255) NULL AFTER `value_type`;

-- ALTER TABLE `jpaas_datart`.`variable`
--     ADD INDEX `source_id`(`source_id`) USING BTREE;

-- ALTER TABLE `jpaas_datart`.`storyboard`
--     ADD COLUMN `parent_id` varchar(32) NULL AFTER `org_id`,
--     ADD COLUMN `is_folder` tinyint(1) NULL AFTER `parent_id`,
--     ADD COLUMN `index` double(16, 8) NULL AFTER `is_folder`;

-- ALTER TABLE `jpaas_datart`.`view`
--     ADD COLUMN `type` varchar(32) NULL AFTER `script`;
-- 2022-07-28  以上SQL已经整合到建表脚本


-- 2022-07-29 jpaas-bpm hj 流程实例表增加父act流程实例ID字段
ALTER TABLE jpaas_bpm.bpm_inst ADD PARENT_ACT_INST_ID_ VARCHAR(64) COMMENT '父act流程实例ID';

-- 2022-08-01 jpaas-bpm hj 增加流程实例权限表
CREATE TABLE jpaas_bpm.BPM_INST_PERMISSION  (
                                                ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
                                                SUBJECT_             VARCHAR(512) COMMENT '标题',
                                                INST_ID_             VARCHAR(64) COMMENT '实例ID',
                                                DEF_ID_              VARCHAR(64) COMMENT '流程定义ID',
                                                DEF_KEY_             VARCHAR(64) COMMENT '流程定义KEY',
                                                TYPE_                VARCHAR(20) COMMENT '类型',
                                                STATUS_              VARCHAR(20) COMMENT '状态',
                                                DESC_                VARCHAR(200) COMMENT '备注',
                                                AUTH_ID_             VARCHAR(64) COMMENT '授权人ID',
                                                AUTH_NAME_           VARCHAR(64) COMMENT '授权人名称',
                                                TASK_ID_             VARCHAR(20) COMMENT '任务ID',
                                                IS_DELETE_           VARCHAR(20) COMMENT '是否删除',
                                                TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
                                                CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
                                                CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
                                                CREATE_TIME_         DATETIME COMMENT '创建时间',
                                                UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
                                                UPDATE_TIME_         DATETIME COMMENT '更新时间',
                                                APP_ID_              VARCHAR(64) COMMENT '应用ID',
                                                COMPANY_ID_          VARCHAR(64) COMMENT '所属公司ID',
                                                PRIMARY KEY (ID_)
);
ALTER TABLE jpaas_bpm.BPM_INST_PERMISSION COMMENT '流程实例权限表';

-- 2022-08-02 jpaas-bpm hj 增加流程授权表
CREATE TABLE jpaas_bpm.BPM_AUTH
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TO_AUTH_USER_ID_     VARCHAR(64) COMMENT '被授权用户id',
    TO_AUTH_USER_NAME_   VARCHAR(64) COMMENT '被授权用户名称',
    STATUS_              VARCHAR(64) COMMENT '是否有效',
    AUTH_USER_ID_        VARCHAR(64) COMMENT '授权用户id',
    AUTH_USER_NAME_      VARCHAR(64) COMMENT '授权用户名称',
    DEL_USER_ID_         VARCHAR(64) COMMENT '删除用户id',
    DEL_USER_NAME_       VARCHAR(64) COMMENT '删除用户名称',
    DEL_TIME_            DATETIME    COMMENT '删除时间',
    PROCESS_KEY_         VARCHAR(64) COMMENT '流程编号',
    PROCESS_NAME_        VARCHAR(64) COMMENT '流程名称',
    ACTIVE_TIME_         VARCHAR(64) COMMENT '有效期',
    TENANT_ID_           VARCHAR(64) COMMENT '租户ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    COMPANY_ID_          VARCHAR(64) COMMENT '所属公司ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE jpaas_bpm.BPM_AUTH COMMENT '流程授权表';

-- 2022-08-02 jpaas-bpm hj 新增 流程授权功能菜单
INSERT INTO `jpaas_system`.`sys_menu`(`MENU_ID_`, `APP_ID_`, `NAME_`, `ICON_PC_`, `ICON_PIC_`, `ICON_APP_`, `PARENT_ID_`, `PATH_`, `SN_`, `SHOW_TYPE_`, `MENU_KEY_`, `MENU_TYPE_`, `COMPONENT_`, `SETTING_TYPE_`, `BO_LIST_KEY_`, `URL_`, `METHOD_`, `PARAMS_`, `TENANT_ID_`, `CREATE_DEP_ID_`, `CREATE_BY_`, `CREATE_TIME_`, `UPDATE_BY_`, `UPDATE_TIME_`, `SHARE_`, `COMPANY_ID_`, `INST_CONFIG_`) VALUES ('1554298772999675906', '1208980476072275970', '流程授权', '{\"type\":\"suggested\",\"icon\":\"plus-circle\"}', 'plus-circle', NULL, '0', '0.1554298772999675906.', 8, 'URL', 'BpmUserAccess', 'C', 'modules/bpm/core/BpmUserAccess.vue', 'custom', NULL, NULL, NULL, NULL, '1', '1', '1', '2022-08-02 10:51:20', NULL, '2022-08-02 10:51:20', NULL, '0', NULL);

-- 2022-08-03 jpaas-form hj 数据源配置表新增 是否加密字段
ALTER TABLE jpaas_form.form_datasource_def ADD ENCRYPT_ VARCHAR(20) COMMENT '是否加密';

-- 2022-08-04 jpaas-form zyg 控件字段长度调整
ALTER TABLE `form_bo_attr`
    MODIFY COLUMN `CONTROL_`  varchar(40)   COMMENT '控件类型' ;

