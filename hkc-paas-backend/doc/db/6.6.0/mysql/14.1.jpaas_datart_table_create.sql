/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022-07-28 14:56:21                          */
/*==============================================================*/


/*==============================================================*/
/* Table: ACCESS_LOG                                            */
/*==============================================================*/
use jpaas_datart;



/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : jpaas_datart

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2022-08-05 13:58:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for access_log
-- ----------------------------
DROP TABLE IF EXISTS `access_log`;
CREATE TABLE `access_log` (
                              `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `user` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `resource_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                              `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `access_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `access_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              `duration` int(11) DEFAULT NULL,
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of access_log
-- ----------------------------

-- ----------------------------
-- Table structure for dashboard
-- ----------------------------
DROP TABLE IF EXISTS `dashboard`;
CREATE TABLE `dashboard` (
                             `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `config` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                             `thumbnail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                             `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                             `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                             `status` tinyint(6) NOT NULL DEFAULT '1',
                             PRIMARY KEY (`id`) USING BTREE,
                             KEY `org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of dashboard
-- ----------------------------

-- ----------------------------
-- Table structure for datachart
-- ----------------------------
DROP TABLE IF EXISTS `datachart`;
CREATE TABLE `datachart` (
                             `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                             `view_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                             `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `config` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                             `thumbnail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                             `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                             `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                             `status` tinyint(6) DEFAULT '1',
                             PRIMARY KEY (`id`) USING BTREE,
                             KEY `view_id` (`view_id`) USING BTREE,
                             KEY `org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of datachart
-- ----------------------------

-- ----------------------------
-- Table structure for download
-- ----------------------------
DROP TABLE IF EXISTS `download`;
CREATE TABLE `download` (
                            `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `path` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `last_download_time` timestamp NULL DEFAULT NULL,
                            `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                            `create_by` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `status` tinyint(6) NOT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `create_by` (`create_by`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of download
-- ----------------------------

-- ----------------------------
-- Table structure for folder
-- ----------------------------
DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder` (
                          `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                          `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                          `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                          `rel_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                          `sub_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                          `rel_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                          `avatar` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
                          `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                          `index` double(16,8) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name_unique` (`name`,`org_id`,`parent_id`) USING BTREE,
  KEY `org_id` (`org_id`) USING BTREE,
  KEY `rel_id` (`rel_id`) USING BTREE,
  KEY `parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of folder
-- ----------------------------

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link` (
                        `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `rel_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                        `rel_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                        `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                        `expiration` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                        `create_time` timestamp NULL DEFAULT NULL,
                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of link
-- ----------------------------

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization` (
                                `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                `description` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                                `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `update_time` datetime DEFAULT NULL,
                                `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE KEY `orgName` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of organization
-- ----------------------------

-- ----------------------------
-- Table structure for org_settings
-- ----------------------------
DROP TABLE IF EXISTS `org_settings`;
CREATE TABLE `org_settings` (
                                `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                `type` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                `config` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
                                PRIMARY KEY (`id`) USING BTREE,
                                KEY `org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of org_settings
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
                                      `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                      `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                      `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                      `BLOB_DATA` blob,
                                      PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                      CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
                                  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                  `CALENDAR` blob NOT NULL,
                                  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
                                      `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                      `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                      `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                      `CRON_EXPRESSION` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                      `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                      PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                      CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
                                       `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                       `ENTRY_ID` varchar(95) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                       `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                       `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                       `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                       `FIRED_TIME` bigint(13) NOT NULL,
                                       `SCHED_TIME` bigint(13) NOT NULL,
                                       `PRIORITY` int(11) NOT NULL,
                                       `STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                       `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                       `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                       `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                       `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                       PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
                                    `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                    `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                    `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                    `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                    `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                    `IS_DURABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                    `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                    `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                    `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                    `JOB_DATA` blob,
                                    PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
                              `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                              `LOCK_NAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                              PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
                                            `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                            `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                            PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
                                        `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                        `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                        `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
                                        `CHECKIN_INTERVAL` bigint(13) NOT NULL,
                                        PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
                                        `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                        `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                        `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                        `REPEAT_COUNT` bigint(7) NOT NULL,
                                        `REPEAT_INTERVAL` bigint(12) NOT NULL,
                                        `TIMES_TRIGGERED` bigint(10) NOT NULL,
                                        PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                        CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
                                         `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                         `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                         `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                         `STR_PROP_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                         `STR_PROP_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                         `STR_PROP_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                         `INT_PROP_1` int(11) DEFAULT NULL,
                                         `INT_PROP_2` int(11) DEFAULT NULL,
                                         `LONG_PROP_1` bigint(20) DEFAULT NULL,
                                         `LONG_PROP_2` bigint(20) DEFAULT NULL,
                                         `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
                                         `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
                                         `BOOL_PROP_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                         `BOOL_PROP_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                         PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                         CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
                                 `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                 `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                 `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                 `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                 `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                 `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                 `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
                                 `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
                                 `PRIORITY` int(11) DEFAULT NULL,
                                 `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                 `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                 `START_TIME` bigint(13) NOT NULL,
                                 `END_TIME` bigint(13) DEFAULT NULL,
                                 `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                 `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
                                 `JOB_DATA` blob,
                                 PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
                                 KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
                                 CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for rel_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `rel_role_resource`;
CREATE TABLE `rel_role_resource` (
                                     `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                     `resource_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                     `permission` int(11) NOT NULL,
                                     `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                     `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                     `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE KEY `role_id_2` (`role_id`,`resource_id`,`resource_type`) USING BTREE,
                                     KEY `role_id` (`role_id`) USING BTREE,
                                     KEY `resource_id` (`resource_id`) USING BTREE,
                                     KEY `resource_type` (`resource_type`) USING BTREE,
                                     KEY `org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of rel_role_resource
-- ----------------------------

-- ----------------------------
-- Table structure for rel_role_user
-- ----------------------------
DROP TABLE IF EXISTS `rel_role_user`;
CREATE TABLE `rel_role_user` (
                                 `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                 `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                 `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                 `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                 `create_time` timestamp NULL DEFAULT NULL,
                                 `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                 `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`id`) USING BTREE,
                                 UNIQUE KEY `user_role` (`user_id`,`role_id`) USING BTREE,
                                 KEY `user_id` (`user_id`) USING BTREE,
                                 KEY `role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of rel_role_user
-- ----------------------------

-- ----------------------------
-- Table structure for rel_subject_columns
-- ----------------------------
DROP TABLE IF EXISTS `rel_subject_columns`;
CREATE TABLE `rel_subject_columns` (
                                       `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `view_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `subject_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `subject_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `column_permission` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                                       `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                       `create_time` timestamp NULL DEFAULT NULL,
                                       `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                       `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                       PRIMARY KEY (`id`) USING BTREE,
                                       KEY `view_id` (`view_id`) USING BTREE,
                                       KEY `subject_id` (`subject_id`) USING BTREE,
                                       KEY `subject_type` (`subject_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of rel_subject_columns
-- ----------------------------

-- ----------------------------
-- Table structure for rel_user_organization
-- ----------------------------
DROP TABLE IF EXISTS `rel_user_organization`;
CREATE TABLE `rel_user_organization` (
                                         `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                         `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                         `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                         `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                         `create_time` timestamp NULL DEFAULT NULL,
                                         `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                         `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE KEY `org_user` (`org_id`,`user_id`) USING BTREE,
                                         KEY `user_id` (`user_id`) USING BTREE,
                                         KEY `org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of rel_user_organization
-- ----------------------------

-- ----------------------------
-- Table structure for rel_variable_subject
-- ----------------------------
DROP TABLE IF EXISTS `rel_variable_subject`;
CREATE TABLE `rel_variable_subject` (
                                        `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `variable_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `subject_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `subject_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                        `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        `use_default_value` tinyint(4) NOT NULL,
                                        `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                        `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                        `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                        PRIMARY KEY (`id`) USING BTREE,
                                        UNIQUE KEY `user_var` (`variable_id`,`subject_type`,`subject_id`) USING BTREE,
                                        KEY `variable_id` (`variable_id`) USING BTREE,
                                        KEY `subject_id` (`subject_id`) USING BTREE,
                                        KEY `subject_type` (`subject_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of rel_variable_subject
-- ----------------------------

-- ----------------------------
-- Table structure for rel_widget_element
-- ----------------------------
DROP TABLE IF EXISTS `rel_widget_element`;
CREATE TABLE `rel_widget_element` (
                                      `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                      `widget_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                      `rel_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                      `rel_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                      PRIMARY KEY (`id`) USING BTREE,
                                      KEY `rel_id` (`rel_id`) USING BTREE,
                                      KEY `rel_type` (`rel_type`) USING BTREE,
                                      KEY `widget_id` (`widget_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of rel_widget_element
-- ----------------------------

-- ----------------------------
-- Table structure for rel_widget_widget
-- ----------------------------
DROP TABLE IF EXISTS `rel_widget_widget`;
CREATE TABLE `rel_widget_widget` (
                                     `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `source_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `target_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `config` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
                                     PRIMARY KEY (`id`) USING BTREE,
                                     KEY `source_id` (`source_id`) USING BTREE,
                                     KEY `target_id` (`target_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of rel_widget_widget
-- ----------------------------

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
                        `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `create_time` timestamp NULL DEFAULT NULL,
                        `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                        `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE KEY `ord_and_name` (`org_id`,`name`) USING BTREE,
                        KEY `org_id` (`org_id`) USING BTREE,
                        KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule` (
                            `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `active` tinyint(4) NOT NULL,
                            `cron_expression` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `start_date` timestamp NULL DEFAULT NULL,
                            `end_date` timestamp NULL DEFAULT NULL,
                            `config` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                            `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                            `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                            `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `is_folder` tinyint(1) DEFAULT NULL,
                            `index` double(16,8) DEFAULT NULL,
  `status` tinyint(6) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `org_id` (`org_id`) USING BTREE,
  KEY `create_by` (`create_by`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of schedule
-- ----------------------------

-- ----------------------------
-- Table structure for schedule_log
-- ----------------------------
DROP TABLE IF EXISTS `schedule_log`;
CREATE TABLE `schedule_log` (
                                `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `schedule_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                `start` timestamp NULL DEFAULT NULL,
                                `end` timestamp NULL DEFAULT NULL,
                                `status` int(11) NOT NULL,
                                `message` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                                PRIMARY KEY (`id`) USING BTREE,
                                KEY `schedule_id` (`schedule_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of schedule_log
-- ----------------------------

-- ----------------------------
-- Table structure for share
-- ----------------------------
DROP TABLE IF EXISTS `share`;
CREATE TABLE `share` (
                         `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `viz_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `viz_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `authentication_mode` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `roles` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                         `row_permission_by` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `authentication_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                         `expiry_date` timestamp NULL DEFAULT NULL,
                         `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `create_time` timestamp NULL DEFAULT NULL,
                         PRIMARY KEY (`id`) USING BTREE,
                         KEY `viz_id` (`viz_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of share
-- ----------------------------

-- ----------------------------
-- Table structure for source
-- ----------------------------
DROP TABLE IF EXISTS `source`;
CREATE TABLE `source` (
                          `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                          `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                          `config` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                          `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                          `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                          `parent_id` varchar(32) DEFAULT NULL,
                          `is_folder` tinyint(1) DEFAULT NULL,
                          `index` double(16,8) DEFAULT NULL,
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(6) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `org_name` (`name`,`org_id`) USING BTREE,
  KEY `org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of source
-- ----------------------------

-- ----------------------------
-- Table structure for source_schemas
-- ----------------------------
DROP TABLE IF EXISTS `source_schemas`;
CREATE TABLE `source_schemas` (
                                  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `source_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `schemas` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
                                  `update_time` datetime DEFAULT NULL,
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE KEY `source_id` (`source_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of source_schemas
-- ----------------------------

-- ----------------------------
-- Table structure for storyboard
-- ----------------------------
DROP TABLE IF EXISTS `storyboard`;
CREATE TABLE `storyboard` (
                              `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                              `parent_id` varchar(32) DEFAULT NULL,
                              `is_folder` tinyint(1) DEFAULT NULL,
                              `index` double(16,8) DEFAULT NULL,
  `config` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(6) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `org_id` (`org_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of storyboard
-- ----------------------------

-- ----------------------------
-- Table structure for storypage
-- ----------------------------
DROP TABLE IF EXISTS `storypage`;
CREATE TABLE `storypage` (
                             `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                             `storyboard_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                             `rel_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                             `rel_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                             `config` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
                             PRIMARY KEY (`id`) USING BTREE,
                             KEY `storyboard_id` (`storyboard_id`) USING BTREE,
                             KEY `rel_type` (`rel_type`) USING BTREE,
                             KEY `rel_id` (`rel_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of storypage
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `active` tinyint(1) DEFAULT NULL,
                        `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `create_time` timestamp NULL DEFAULT NULL,
                        `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                        `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE KEY `username` (`username`) USING BTREE,
                        UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for user_settings
-- ----------------------------
DROP TABLE IF EXISTS `user_settings`;
CREATE TABLE `user_settings` (
                                 `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                 `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                 `rel_type` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
                                 `rel_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
                                 `config` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
                                 PRIMARY KEY (`id`) USING BTREE,
                                 KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of user_settings
-- ----------------------------

-- ----------------------------
-- Table structure for variable
-- ----------------------------
DROP TABLE IF EXISTS `variable`;
CREATE TABLE `variable` (
                            `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `view_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `source_id` varchar(32) DEFAULT NULL,
                            `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `value_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `format` varchar(255) DEFAULT NULL,
                            `permission` int(11) DEFAULT NULL,
                            `encrypt` tinyint(4) DEFAULT NULL,
                            `label` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `default_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `expression` tinyint(4) DEFAULT NULL,
                            `create_time` timestamp NULL DEFAULT NULL,
                            `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                            `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `org_id` (`org_id`,`view_id`,`name`) USING BTREE,
                            KEY `org_id_2` (`org_id`) USING BTREE,
                            KEY `view_id` (`view_id`) USING BTREE,
                            KEY `source_id` (`source_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of variable
-- ----------------------------

-- ----------------------------
-- Table structure for view
-- ----------------------------
DROP TABLE IF EXISTS `view`;
CREATE TABLE `view` (
                        `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `org_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `source_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `script` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                        `type` varchar(32) DEFAULT NULL,
                        `model` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                        `config` text CHARACTER SET utf8 COLLATE utf8_general_ci,
                        `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                        `create_time` timestamp NULL DEFAULT NULL,
                        `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                        `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                        `is_folder` tinyint(1) DEFAULT NULL,
                        `index` double(16,8) DEFAULT NULL,
  `status` tinyint(6) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_name` (`name`,`org_id`,`parent_id`) USING BTREE,
  KEY `org_id` (`org_id`) USING BTREE,
  KEY `source_id` (`source_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of view
-- ----------------------------

-- ----------------------------
-- Table structure for widget
-- ----------------------------
DROP TABLE IF EXISTS `widget`;
CREATE TABLE `widget` (
                          `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                          `dashboard_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                          `config` longtext CHARACTER SET utf8 COLLATE utf8_general_ci,
                          `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                          `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                          `create_time` timestamp NULL DEFAULT NULL,
                          `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                          `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`) USING BTREE,
                          KEY `dashboard_id` (`dashboard_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of widget
-- ----------------------------
