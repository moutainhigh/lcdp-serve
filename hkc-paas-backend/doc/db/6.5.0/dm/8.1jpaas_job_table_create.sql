-- for AT mode you must to init this sql for you business database. the seata server not need it.
CREATE TABLE undo_log
(
    id            NUMBER(19)    NOT NULL,
    branch_id     NUMBER(19)    NOT NULL,
    xid           VARCHAR2(100) NOT NULL,
    context_       VARCHAR2(128) NOT NULL,
    rollback_info BLOB          NOT NULL,
    log_status    NUMBER(10)    NOT NULL,
    log_created   TIMESTAMP(0)  NOT NULL,
    log_modified  TIMESTAMP(0)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ux_undo_log UNIQUE (xid, branch_id)
);

COMMENT ON TABLE undo_log IS 'AT transaction mode undo table';

-- Generate ID using sequence and trigger
CREATE SEQUENCE UNDO_LOG_SEQ START WITH 1 INCREMENT BY 1;

/*==============================================================*/
/* Table: XXL_JOB_GROUP                                         */
/*==============================================================*/
CREATE TABLE XXL_JOB_GROUP
(
   ID                   VARCHAR2(64)         NOT NULL,
   APP_NAME             VARCHAR2(64)         NOT NULL,
   TITLE                VARCHAR2(128)         NOT NULL,
   ORDER_               INTEGER,
   ADDRESS_TYPE         SMALLINT             NOT NULL,
   ADDRESS_LIST         VARCHAR2(512),
   CONSTRAINT PK_XXL_JOB_GROUP PRIMARY KEY (ID)
);

COMMENT ON COLUMN XXL_JOB_GROUP.APP_NAME IS
'执行器AppName';

COMMENT ON COLUMN XXL_JOB_GROUP.TITLE IS
'执行器名称';

COMMENT ON COLUMN XXL_JOB_GROUP.ADDRESS_TYPE IS
'执行器地址类型：0=自动注册、1=手动录入';

COMMENT ON COLUMN XXL_JOB_GROUP.ADDRESS_LIST IS
'执行器地址列表，多地址逗号分隔';

/*==============================================================*/
/* Table: XXL_JOB_INFO                                          */
/*==============================================================*/
CREATE TABLE XXL_JOB_INFO
(
   ID                   VARCHAR2(64)         NOT NULL,
   JOB_GROUP            VARCHAR2(64)         NOT NULL,
   JOB_CRON             VARCHAR2(128)        NOT NULL,
   JOB_DESC             VARCHAR2(255)        NOT NULL,
   ADD_TIME             DATE,
   UPDATE_TIME          DATE,
   AUTHOR               VARCHAR2(64),
   ALARM_EMAIL          VARCHAR2(255),
   EXECUTOR_ROUTE_STRATEGY VARCHAR2(50),
   EXECUTOR_HANDLER     VARCHAR2(255),
   EXECUTOR_PARAM       VARCHAR2(512),
   EXECUTOR_BLOCK_STRATEGY VARCHAR2(50),
   EXECUTOR_TIMEOUT     INTEGER              NOT NULL,
   EXECUTOR_FAIL_RETRY_COUNT INTEGER              NOT NULL,
   GLUE_TYPE            VARCHAR2(50)         NOT NULL,
   GLUE_SOURCE          CLOB,
   GLUE_REMARK          VARCHAR2(128),
   GLUE_UPDATETIME      DATE,
   CHILD_JOBID          VARCHAR2(255),
   TRIGGER_STATUS       SMALLINT             NOT NULL,
   TRIGGER_LAST_TIME    INTEGER              NOT NULL,
   TRIGGER_NEXT_TIME    INTEGER              NOT NULL,
   CONSTRAINT PK_XXL_JOB_INFO PRIMARY KEY (ID)
);

COMMENT ON COLUMN XXL_JOB_INFO.JOB_GROUP IS
'执行器主键ID';

COMMENT ON COLUMN XXL_JOB_INFO.JOB_CRON IS
'任务执行CRON';

COMMENT ON COLUMN XXL_JOB_INFO.AUTHOR IS
'作者';

COMMENT ON COLUMN XXL_JOB_INFO.ALARM_EMAIL IS
'报警邮件';

COMMENT ON COLUMN XXL_JOB_INFO.EXECUTOR_ROUTE_STRATEGY IS
'执行器路由策略';

COMMENT ON COLUMN XXL_JOB_INFO.EXECUTOR_HANDLER IS
'执行器任务handler';

COMMENT ON COLUMN XXL_JOB_INFO.EXECUTOR_PARAM IS
'执行器任务参数';

COMMENT ON COLUMN XXL_JOB_INFO.EXECUTOR_BLOCK_STRATEGY IS
'阻塞处理策略';

COMMENT ON COLUMN XXL_JOB_INFO.EXECUTOR_TIMEOUT IS
'任务执行超时时间，单位秒';

COMMENT ON COLUMN XXL_JOB_INFO.EXECUTOR_FAIL_RETRY_COUNT IS
'失败重试次数';

COMMENT ON COLUMN XXL_JOB_INFO.GLUE_TYPE IS
'GLUE类型';

COMMENT ON COLUMN XXL_JOB_INFO.GLUE_SOURCE IS
'GLUE源代码';

COMMENT ON COLUMN XXL_JOB_INFO.GLUE_REMARK IS
'GLUE备注';

COMMENT ON COLUMN XXL_JOB_INFO.GLUE_UPDATETIME IS
'GLUE更新时间';

COMMENT ON COLUMN XXL_JOB_INFO.CHILD_JOBID IS
'子任务ID，多个逗号分隔';

COMMENT ON COLUMN XXL_JOB_INFO.TRIGGER_STATUS IS
'调度状态：0-停止，1-运行';

COMMENT ON COLUMN XXL_JOB_INFO.TRIGGER_LAST_TIME IS
'上次调度时间';

COMMENT ON COLUMN XXL_JOB_INFO.TRIGGER_NEXT_TIME IS
'下次调度时间';

/*==============================================================*/
/* Table: XXL_JOB_LOCK                                          */
/*==============================================================*/
CREATE TABLE XXL_JOB_LOCK
(
   LOCK_NAME            VARCHAR2(50)         NOT NULL,
   CONSTRAINT PK_XXL_JOB_LOCK PRIMARY KEY (LOCK_NAME)
);

COMMENT ON COLUMN XXL_JOB_LOCK.LOCK_NAME IS
'锁名称';

/*==============================================================*/
/* Table: XXL_JOB_LOG                                           */
/*==============================================================*/
CREATE TABLE XXL_JOB_LOG
(
   ID                   VARCHAR2(64)         NOT NULL,
   JOB_GROUP            VARCHAR2(64)         NOT NULL,
   JOB_ID               VARCHAR2(64)         NOT NULL,
   EXECUTOR_ADDRESS     VARCHAR2(255),
   EXECUTOR_HANDLER     VARCHAR2(255),
   EXECUTOR_PARAM       VARCHAR2(512),
   EXECUTOR_SHARDING_PARAM VARCHAR2(20),
   EXECUTOR_FAIL_RETRY_COUNT INTEGER              NOT NULL,
   TRIGGER_TIME         DATE,
   TRIGGER_CODE         INTEGER              NOT NULL,
   TRIGGER_MSG          CLOB,
   HANDLE_TIME          DATE,
   HANDLE_CODE          INTEGER              NOT NULL,
   HANDLE_MSG           CLOB,
   ALARM_STATUS         SMALLINT             NOT NULL,
   CONSTRAINT PK_XXL_JOB_LOG PRIMARY KEY (ID)
);

COMMENT ON COLUMN XXL_JOB_LOG.JOB_GROUP IS
'执行器主键ID';

COMMENT ON COLUMN XXL_JOB_LOG.JOB_ID IS
'任务，主键ID';

COMMENT ON COLUMN XXL_JOB_LOG.EXECUTOR_ADDRESS IS
'执行器地址，本次执行的地址';

COMMENT ON COLUMN XXL_JOB_LOG.EXECUTOR_HANDLER IS
'执行器任务handler';

COMMENT ON COLUMN XXL_JOB_LOG.EXECUTOR_PARAM IS
'执行器任务参数';

COMMENT ON COLUMN XXL_JOB_LOG.EXECUTOR_SHARDING_PARAM IS
'执行器任务分片参数，格式如 1/2';

COMMENT ON COLUMN XXL_JOB_LOG.EXECUTOR_FAIL_RETRY_COUNT IS
'失败重试次数';

COMMENT ON COLUMN XXL_JOB_LOG.TRIGGER_TIME IS
'调度-时间';

COMMENT ON COLUMN XXL_JOB_LOG.TRIGGER_CODE IS
'调度-结果';

COMMENT ON COLUMN XXL_JOB_LOG.TRIGGER_MSG IS
'调度-日志';

COMMENT ON COLUMN XXL_JOB_LOG.HANDLE_TIME IS
'执行-时间';

COMMENT ON COLUMN XXL_JOB_LOG.HANDLE_CODE IS
'执行-状态';

COMMENT ON COLUMN XXL_JOB_LOG.HANDLE_MSG IS
'执行-日志';

COMMENT ON COLUMN XXL_JOB_LOG.ALARM_STATUS IS
'告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败';

/*==============================================================*/
/* Table: XXL_JOB_LOGGLUE                                       */
/*==============================================================*/
CREATE TABLE XXL_JOB_LOGGLUE
(
   ID                   VARCHAR2(64)         NOT NULL,
   JOB_ID               VARCHAR2(64)         NOT NULL,
   GLUE_TYPE            VARCHAR2(50),
   GLUE_SOURCE          CLOB,
   GLUE_REMARK          VARCHAR2(128)        NOT NULL,
   ADD_TIME             DATE,
   UPDATE_TIME          DATE,
   CONSTRAINT PK_XXL_JOB_LOGGLUE PRIMARY KEY (ID)
);

COMMENT ON COLUMN XXL_JOB_LOGGLUE.JOB_ID IS
'任务，主键ID';

COMMENT ON COLUMN XXL_JOB_LOGGLUE.GLUE_TYPE IS
'GLUE类型';

COMMENT ON COLUMN XXL_JOB_LOGGLUE.GLUE_SOURCE IS
'GLUE源代码';

COMMENT ON COLUMN XXL_JOB_LOGGLUE.GLUE_REMARK IS
'GLUE备注';

/*==============================================================*/
/* Table: XXL_JOB_LOG_REPORT                                    */
/*==============================================================*/
CREATE TABLE XXL_JOB_LOG_REPORT
(
   ID                   VARCHAR2(64)         NOT NULL,
   TRIGGER_DAY          DATE,
   RUNNING_COUNT        INTEGER              NOT NULL,
   SUC_COUNT            INTEGER              NOT NULL,
   FAIL_COUNT           INTEGER              NOT NULL,
   CONSTRAINT PK_XXL_JOB_LOG_REPORT PRIMARY KEY (ID)
);

COMMENT ON COLUMN XXL_JOB_LOG_REPORT.TRIGGER_DAY IS
'调度-时间';

COMMENT ON COLUMN XXL_JOB_LOG_REPORT.RUNNING_COUNT IS
'运行中-日志数量';

COMMENT ON COLUMN XXL_JOB_LOG_REPORT.SUC_COUNT IS
'执行成功-日志数量';

COMMENT ON COLUMN XXL_JOB_LOG_REPORT.FAIL_COUNT IS
'执行失败-日志数量';

/*==============================================================*/
/* Table: XXL_JOB_REGISTRY                                      */
/*==============================================================*/
CREATE TABLE XXL_JOB_REGISTRY
(
   ID                   VARCHAR2(64)         NOT NULL,
   REGISTRY_GROUP       VARCHAR2(50)         NOT NULL,
   REGISTRY_KEY         VARCHAR2(255)        NOT NULL,
   REGISTRY_VALUE       VARCHAR2(255)        NOT NULL,
   UPDATE_TIME          DATE,
   CONSTRAINT PK_XXL_JOB_REGISTRY PRIMARY KEY (ID)
);

/*==============================================================*/
/* Table: XXL_JOB_USER                                          */
/*==============================================================*/
CREATE TABLE XXL_JOB_USER
(
   ID                   VARCHAR2(64)         NOT NULL,
   USERNAME             VARCHAR2(50)         NOT NULL,
   PASSWORD             VARCHAR2(50)         NOT NULL,
   ROLE                 SMALLINT             NOT NULL,
   PERMISSION           VARCHAR2(255),
   CONSTRAINT PK_XXL_JOB_USER PRIMARY KEY (ID)
);

COMMENT ON COLUMN XXL_JOB_USER.USERNAME IS
'账号';

COMMENT ON COLUMN XXL_JOB_USER.PASSWORD IS
'密码';

COMMENT ON COLUMN XXL_JOB_USER.ROLE IS
'角色：0-普通用户、1-管理员';

COMMENT ON COLUMN XXL_JOB_USER.PERMISSION IS
'权限：执行器ID列表，多个逗号分割';