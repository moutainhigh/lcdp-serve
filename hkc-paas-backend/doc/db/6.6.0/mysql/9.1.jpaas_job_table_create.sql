use jpaas_job;

CREATE TABLE `undo_log` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'increment id',
                            `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
                            `xid` varchar(100) NOT NULL COMMENT 'global transaction id',
                            `context_` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
                            `rollback_info` longblob NOT NULL COMMENT 'rollback info',
                            `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
                            `log_created` datetime NOT NULL COMMENT 'create datetime',
                            `log_modified` datetime NOT NULL COMMENT 'modify datetime',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';



/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022-7-22 19:04:30                           */
/*==============================================================*/


/*==============================================================*/
/* Table: XXL_JOB_GROUP                                         */
/*==============================================================*/
CREATE TABLE XXL_JOB_GROUP
(
   ID                   VARCHAR(64) NOT NULL,
   APP_NAME             VARCHAR(64) NOT NULL COMMENT '执行器AppName',
   TITLE                VARCHAR(128) NOT NULL COMMENT '执行器名称',
   ORDER_               INT,
   ADDRESS_TYPE         SMALLINT NOT NULL COMMENT '执行器地址类型：0=自动注册、1=手动录入',
   ADDRESS_LIST         VARCHAR(512) COMMENT '执行器地址列表，多地址逗号分隔',
   UPDATE_TIME          DATETIME COMMENT '更新时间',
   PRIMARY KEY (ID)
);

/*==============================================================*/
/* Table: XXL_JOB_INFO                                          */
/*==============================================================*/
CREATE TABLE XXL_JOB_INFO
(
   ID                   VARCHAR(64) NOT NULL,
   JOB_GROUP            VARCHAR(64) NOT NULL COMMENT '执行器主键ID',
   JOB_DESC             VARCHAR(255) NOT NULL,
   ADD_TIME             DATETIME,
   UPDATE_TIME          DATETIME,
   AUTHOR               VARCHAR(64) COMMENT '作者',
   ALARM_EMAIL          VARCHAR(255) COMMENT '报警邮件',
   EXECUTOR_ROUTE_STRATEGY VARCHAR(50) COMMENT '执行器路由策略',
   EXECUTOR_HANDLER     VARCHAR(255) COMMENT '执行器任务handler',
   EXECUTOR_PARAM       VARCHAR(512) COMMENT '执行器任务参数',
   EXECUTOR_BLOCK_STRATEGY VARCHAR(50) COMMENT '阻塞处理策略',
   EXECUTOR_TIMEOUT     INT NOT NULL COMMENT '任务执行超时时间，单位秒',
   EXECUTOR_FAIL_RETRY_COUNT INT NOT NULL COMMENT '失败重试次数',
   GLUE_TYPE            VARCHAR(50) NOT NULL COMMENT 'GLUE类型',
   GLUE_SOURCE          TEXT COMMENT 'GLUE源代码',
   GLUE_REMARK          VARCHAR(128) COMMENT 'GLUE备注',
   GLUE_UPDATETIME      DATETIME COMMENT 'GLUE更新时间',
   CHILD_JOBID          VARCHAR(255) COMMENT '子任务ID，多个逗号分隔',
   TRIGGER_STATUS       SMALLINT NOT NULL COMMENT '调度状态：0-停止，1-运行',
   TRIGGER_LAST_TIME    INT NOT NULL COMMENT '上次调度时间',
   TRIGGER_NEXT_TIME    INT NOT NULL COMMENT '下次调度时间',
   SCHEDULE_TYPE        VARCHAR(64) COMMENT '调度类型',
   SCHEDULE_CONF        VARCHAR(128) COMMENT '调度配置，值含义取决于调度类型',
   MISFIRE_STRATEGY     VARCHAR(64) COMMENT '调度过期策略',
   PRIMARY KEY (ID)
);

/*==============================================================*/
/* Table: XXL_JOB_LOCK                                          */
/*==============================================================*/
CREATE TABLE XXL_JOB_LOCK
(
   LOCK_NAME            VARCHAR(50) NOT NULL COMMENT '锁名称',
   PRIMARY KEY (LOCK_NAME)
);

/*==============================================================*/
/* Table: XXL_JOB_LOG                                           */
/*==============================================================*/
CREATE TABLE XXL_JOB_LOG
(
   ID                   VARCHAR(64) NOT NULL,
   JOB_GROUP            VARCHAR(64) NOT NULL COMMENT '执行器主键ID',
   JOB_ID               VARCHAR(64) NOT NULL COMMENT '任务，主键ID',
   EXECUTOR_ADDRESS     VARCHAR(255) COMMENT '执行器地址，本次执行的地址',
   EXECUTOR_HANDLER     VARCHAR(255) COMMENT '执行器任务handler',
   EXECUTOR_PARAM       VARCHAR(512) COMMENT '执行器任务参数',
   EXECUTOR_SHARDING_PARAM VARCHAR(20) COMMENT '执行器任务分片参数，格式如 1/2',
   EXECUTOR_FAIL_RETRY_COUNT INT COMMENT '失败重试次数',
   TRIGGER_TIME         DATETIME COMMENT '调度-时间',
   TRIGGER_CODE         INT COMMENT '调度-结果',
   TRIGGER_MSG          TEXT COMMENT '调度-日志',
   HANDLE_TIME          DATETIME COMMENT '执行-时间',
   HANDLE_CODE          INT COMMENT '执行-状态',
   HANDLE_MSG           TEXT COMMENT '执行-日志',
   ALARM_STATUS         SMALLINT COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
   PRIMARY KEY (ID)
);

/*==============================================================*/
/* Table: XXL_JOB_LOGGLUE                                       */
/*==============================================================*/
CREATE TABLE XXL_JOB_LOGGLUE
(
   ID                   VARCHAR(64) NOT NULL,
   JOB_ID               VARCHAR(64) NOT NULL COMMENT '任务，主键ID',
   GLUE_TYPE            VARCHAR(50) COMMENT 'GLUE类型',
   GLUE_SOURCE          TEXT COMMENT 'GLUE源代码',
   GLUE_REMARK          VARCHAR(128) NOT NULL COMMENT 'GLUE备注',
   ADD_TIME             DATETIME,
   UPDATE_TIME          DATETIME,
   PRIMARY KEY (ID)
);

/*==============================================================*/
/* Table: XXL_JOB_LOG_REPORT                                    */
/*==============================================================*/
CREATE TABLE XXL_JOB_LOG_REPORT
(
   ID                   VARCHAR(64) NOT NULL,
   TRIGGER_DAY          DATETIME COMMENT '调度-时间',
   RUNNING_COUNT        INT NOT NULL COMMENT '运行中-日志数量',
   SUC_COUNT            INT NOT NULL COMMENT '执行成功-日志数量',
   FAIL_COUNT           INT NOT NULL COMMENT '执行失败-日志数量',
   PRIMARY KEY (ID)
);

/*==============================================================*/
/* Table: XXL_JOB_REGISTRY                                      */
/*==============================================================*/
CREATE TABLE XXL_JOB_REGISTRY
(
   ID                   VARCHAR(64) NOT NULL,
   REGISTRY_GROUP       VARCHAR(50) NOT NULL,
   REGISTRY_KEY         VARCHAR(255) NOT NULL,
   REGISTRY_VALUE       VARCHAR(255) NOT NULL,
   UPDATE_TIME          DATETIME,
   PRIMARY KEY (ID)
);

/*==============================================================*/
/* Table: XXL_JOB_USER                                          */
/*==============================================================*/
CREATE TABLE XXL_JOB_USER
(
   ID                   VARCHAR(64) NOT NULL,
   USERNAME             VARCHAR(50) NOT NULL COMMENT '账号',
   PASSWORD             VARCHAR(50) NOT NULL COMMENT '密码',
   ROLE                 SMALLINT NOT NULL COMMENT '角色：0-普通用户、1-管理员',
   PERMISSION           VARCHAR(255) COMMENT '权限：执行器ID列表，多个逗号分割',
   PRIMARY KEY (ID)
);





