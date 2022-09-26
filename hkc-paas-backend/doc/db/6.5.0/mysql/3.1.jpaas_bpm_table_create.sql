use jpaas_bpm;
-- for AT mode you must to init this sql for you business database. the seata server not need it.
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
/* Table: BPM_AGENT                                             */
/*==============================================================*/
CREATE TABLE BPM_AGENT
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    NAME_                VARCHAR(128) COMMENT '名称',
    OWNER_ID_            VARCHAR(64) COMMENT '所有人',
    TYPE_                VARCHAR(20) COMMENT '类型',
    TO_USER_             VARCHAR(64) COMMENT '代理人',
    TO_USER_NAME_        VARCHAR(20) COMMENT '代理人名称',
    STATUS_              INT COMMENT '状态',
    START_TIME_          DATETIME COMMENT '有效开始时间',
    END_TIME_            DATETIME COMMENT '有效结束时间',
    DESCRIPTION_         VARCHAR(255) COMMENT '描述',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_AGENT COMMENT '流程代理';

/*==============================================================*/
/* Index: IDX_AGENT_OWNER                                       */
/*==============================================================*/
CREATE INDEX IDX_AGENT_OWNER ON BPM_AGENT
    (
     OWNER_ID_
        );

/*==============================================================*/
/* Table: BPM_AGENT_FLOWDEF                                     */
/*==============================================================*/
CREATE TABLE BPM_AGENT_FLOWDEF
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    AGENT_ID_            VARCHAR(64) COMMENT '代理定义ID',
    DEF_ID_              VARCHAR(64) COMMENT '流程定义ID',
    DEF_NAME_            VARCHAR(128) COMMENT '流程定义名称',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_AGENT_FLOWDEF COMMENT '流程代理流程定义';

/*==============================================================*/
/* Table: BPM_ARCHIVE_LOG                                       */
/*==============================================================*/
CREATE TABLE BPM_ARCHIVE_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    ARCHIVE_DATE_        DATETIME COMMENT '归档时间点',
    MEMO_                VARCHAR(255) COMMENT '备注',
    TABLE_ID_            INT COMMENT '表ID',
    STATUS_              VARCHAR(40) COMMENT '状态
            状态(1：成功 0：失败)',
    START_TIME_          DATETIME COMMENT '开始执行时间',
    END_TIME_            DATETIME COMMENT '结束时间',
    CREATE_NAME_         VARCHAR(64) COMMENT '创建人名',
    ERR_LOG_             TEXT COMMENT '错误日志',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_ARCHIVE_LOG COMMENT '流程归档日志';

/*==============================================================*/
/* Table: BPM_CALTIME_BLOCK                                     */
/*==============================================================*/
CREATE TABLE BPM_CALTIME_BLOCK
(
    SETTING_ID_          VARCHAR(64) NOT NULL COMMENT '主键',
    SETTING_NAME_        VARCHAR(64) COMMENT '班次名称',
    TIME_INTERVALS_      VARCHAR(255) COMMENT '班次配置',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (SETTING_ID_)
);

ALTER TABLE BPM_CALTIME_BLOCK COMMENT '班次设定';

/*==============================================================*/
/* Table: BPM_CAL_CALENDAR                                      */
/*==============================================================*/
CREATE TABLE BPM_CAL_CALENDAR
(
    CALENDER_ID_         VARCHAR(64) NOT NULL COMMENT '主键',
    SETTING_ID_          VARCHAR(64) COMMENT '日历ID',
    START_TIME_          DATETIME COMMENT '开始时间',
    END_TIME_            DATETIME COMMENT '结束时间',
    CONNECT_ID_          VARCHAR(64) COMMENT '时间段ID',
    START_DAY_           DATETIME COMMENT '开始时间',
    END_DAY_             DATETIME COMMENT '结束时间',
    INFO_                VARCHAR(4000) COMMENT '条状的信息',
    TIME_INTERVALS_      TEXT COMMENT '选择的时间json',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (CALENDER_ID_)
);

ALTER TABLE BPM_CAL_CALENDAR COMMENT '日历定义';

/*==============================================================*/
/* Table: BPM_CAL_GRANT                                         */
/*==============================================================*/
CREATE TABLE BPM_CAL_GRANT
(
    GRANT_ID_            VARCHAR(64) NOT NULL COMMENT '主键',
    SETTING_ID_          VARCHAR(64) COMMENT '日历ID',
    GRANT_TYPE_          VARCHAR(40) COMMENT '分配类型',
    BELONG_WHO_          VARCHAR(64) COMMENT '所有人',
    BELONG_WHO_ID_       VARCHAR(64) COMMENT '所有人ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (GRANT_ID_)
);

ALTER TABLE BPM_CAL_GRANT COMMENT '日历授权';

/*==============================================================*/
/* Table: BPM_CAL_SETTING                                       */
/*==============================================================*/
CREATE TABLE BPM_CAL_SETTING
(
    SETTING_ID_          VARCHAR(64) NOT NULL COMMENT '日历ID',
    CAL_NAME_            VARCHAR(64) COMMENT '日历名称',
    IS_COMMON_           VARCHAR(40) COMMENT '默认',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (SETTING_ID_)
);

ALTER TABLE BPM_CAL_SETTING COMMENT '日历设定';

/*==============================================================*/
/* Table: BPM_CHECK_FILE                                        */
/*==============================================================*/
CREATE TABLE BPM_CHECK_FILE
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    FILE_ID_             VARCHAR(64) NOT NULL COMMENT '文件Id',
    FILE_NAME_           VARCHAR(255) COMMENT '文件名',
    INST_ID_             VARCHAR(64) COMMENT '流程实例ID',
    JUMP_ID_             VARCHAR(64) NOT NULL COMMENT '跳转ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租用机构ID',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_CHECK_FILE COMMENT '审批意见附件';

/*==============================================================*/
/* Index: IDX_CHKFILE_JUMP                                      */
/*==============================================================*/
CREATE INDEX IDX_CHKFILE_JUMP ON BPM_CHECK_FILE
    (
     JUMP_ID_
        );

/*==============================================================*/
/* Table: BPM_CHECK_HISTORY                                     */
/*==============================================================*/
CREATE TABLE BPM_CHECK_HISTORY
(
    HIS_ID_              VARCHAR(64) NOT NULL COMMENT '审批历史ID',
    ACT_DEF_ID_          VARCHAR(64) COMMENT 'ACT流程定义ID',
    INST_ID_             VARCHAR(64) COMMENT '流程实例ID',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    SUBJECT_             VARCHAR(64) COMMENT '主题',
    NODE_NAME_           VARCHAR(255) COMMENT '节点名称',
    NODE_ID_             VARCHAR(255) COMMENT '节点Key',
    TASK_ID_             VARCHAR(64) COMMENT '任务ID',
    CM_ST_TASK_ID_       VARCHAR(64) COMMENT '回复的沟通任务ID',
    COMPLETE_TIME_       DATETIME COMMENT '完成时间',
    DURATION_            INT COMMENT '持续时长',
    DURATION_VAL_        INT COMMENT '有效审批时长',
    OWNER_ID_            VARCHAR(64) COMMENT '任务所属人ID',
    HANDLER_ID_          VARCHAR(64) COMMENT '处理人ID',
    AGENT_USER_ID_       VARCHAR(64) COMMENT '被代理人',
    CHECK_STATUS_        VARCHAR(50) COMMENT '审批状态',
    JUMP_TYPE_           VARCHAR(50) COMMENT '跳转类型',
    REMARK_              VARCHAR(512) COMMENT '意见备注',
    OPINION_NAME_        VARCHAR(50) COMMENT '字段意见名称',
    HANDLE_DEP_ID_       VARCHAR(64) COMMENT '处理部门ID',
    HANDLE_DEP_FULL_     VARCHAR(300) COMMENT '处理部门全名',
    LINK_UP_USER_IDS     VARCHAR(400) COMMENT '沟通人信息',
    ENABLE_MOBILE_       SMALLINT COMMENT '是否支持手机',
    REL_INSTS_           VARCHAR(300) COMMENT '关联流程实例',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (HIS_ID_)
);

ALTER TABLE BPM_CHECK_HISTORY COMMENT '流程审批流转记录';

/*==============================================================*/
/* Index: IDX_HISTORY_INSTID                                    */
/*==============================================================*/
CREATE INDEX IDX_HISTORY_INSTID ON BPM_CHECK_HISTORY
    (
     INST_ID_
        );

/*==============================================================*/
/* Index: IDX_HISTORY_TASKID                                    */
/*==============================================================*/
CREATE INDEX IDX_HISTORY_TASKID ON BPM_CHECK_HISTORY
    (
     TASK_ID_
        );

/*==============================================================*/
/* Table: BPM_DEF                                               */
/*==============================================================*/
CREATE TABLE BPM_DEF
(
    DEF_ID_              VARCHAR(64) NOT NULL COMMENT '流程定义ID',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    NAME_                VARCHAR(255) NOT NULL COMMENT '标题',
    KEY_                 VARCHAR(255) NOT NULL COMMENT '标识Key',
    DESCP_               VARCHAR(1024) COMMENT '描述',
    ACT_DEF_ID_          VARCHAR(255) COMMENT 'ACT定义ID',
    ACT_DEP_ID_          VARCHAR(255) COMMENT 'ACT发布ID',
    STATUS_              VARCHAR(20) NOT NULL COMMENT '状态
            DRAFT=草稿
            DEPLOY=发布
            INVALID=作废',
    VERSION_             INT NOT NULL COMMENT '版本号',
    IS_MAIN_             VARCHAR(20) COMMENT '主版本',
    DESIGN_XML_          TEXT COMMENT '设计XML',
    EXT_CONFS            TEXT COMMENT '扩展配置',
    MAIN_DEF_ID_         VARCHAR(64) COMMENT '主定义ID',
    FORMAL_              VARCHAR(10) COMMENT '是否正式',
    BO_DEF_IDS_          VARCHAR(64) COMMENT 'BO定义ID',
    ICON_                VARCHAR(64) COMMENT '流程方案图标',
    COLOR_               VARCHAR(64) COMMENT '流程方案图标颜色',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    DESIGN_XML_TEMP_     TEXT COMMENT '设计XML(临时)',
    EXT_CONFS_TEMP       TEXT COMMENT '扩展配置(临时)',
    APP_ID_              VARCHAR(64) COMMENT '应用ID',
    PRIMARY KEY (DEF_ID_)
);

ALTER TABLE BPM_DEF COMMENT '流程定义';

/*==============================================================*/
/* Table: BPM_INST                                              */
/*==============================================================*/
CREATE TABLE BPM_INST
(
    INST_ID_             VARCHAR(64) NOT NULL,
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    DEF_ID_              VARCHAR(64) NOT NULL COMMENT '流程定义ID',
    ACT_INST_ID_         VARCHAR(64) COMMENT 'ACT实例ID',
    ACT_DEF_ID_          VARCHAR(64) NOT NULL COMMENT 'ACT定义ID',
    DEF_CODE_            VARCHAR(64) COMMENT '解决方案ID_',
    INST_NO_             VARCHAR(50) COMMENT '流程实例工单号',
    BILL_TYPE_           VARCHAR(40) COMMENT '单据类型',
    BILL_NO_             VARCHAR(255) COMMENT '业务单号',
    SUBJECT_             VARCHAR(255) COMMENT '标题',
    STATUS_              VARCHAR(20) COMMENT '运行状态',
    VERSION_             INT COMMENT '版本',
    BUS_KEY_             VARCHAR(64) COMMENT '业务键ID',
    CHECK_FILE_ID_       VARCHAR(64) COMMENT '审批正文依据ID',
    IS_TEST_             VARCHAR(20) COMMENT '是否为测试',
    ERRORS_              TEXT COMMENT '出错信息',
    END_TIME_            DATETIME COMMENT '结束时间',
    DATA_SAVE_MODE_      VARCHAR(10) COMMENT '数据保存模式(all,json,db)',
    SUPPORT_MOBILE_      INT COMMENT '支持手机端',
    START_DEP_ID_        VARCHAR(64) COMMENT '发起部门ID',
    START_DEP_FULL_      VARCHAR(300) COMMENT '发起部门全名',
    LOCKED_BY_           VARCHAR(64) COMMENT '锁定人',
    LIVE_INST_ID_        VARCHAR(64) COMMENT '复活的流程实例',
    FIELD_JSON_          VARCHAR(600) COMMENT '表单关联字段',
    IS_LIVE_             VARCHAR(64) COMMENT '是否复活',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    FORM_SOLUTION_ALIAS  VARCHAR(64) COMMENT '表单方案别名',
    PRIMARY KEY (INST_ID_)
);

ALTER TABLE BPM_INST COMMENT '流程实例';

/*==============================================================*/
/* Index: IDX_BPMINST_ACTINSTID                                 */
/*==============================================================*/
CREATE INDEX IDX_BPMINST_ACTINSTID ON BPM_INST
    (
     ACT_INST_ID_
        );

/*==============================================================*/
/* Table: BPM_INST_CC                                           */
/*==============================================================*/
CREATE TABLE BPM_INST_CC
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    SUBJECT_             VARCHAR(64) COMMENT '主题',
    NODE_ID_             VARCHAR(64) COMMENT '节点ID',
    NODE_NAME_           VARCHAR(64) COMMENT '节点名称',
    FROM_USER_           VARCHAR(64) COMMENT '发送人',
    FROM_USER_ID_        VARCHAR(64) COMMENT '发送人ID',
    INST_ID_             VARCHAR(64) COMMENT '流程实例ID',
    DEF_ID_              VARCHAR(64) COMMENT '流程定义ID',
    CC_TYPE_             VARCHAR(20) COMMENT '类型',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_INST_CC COMMENT '流程抄送';

/*==============================================================*/
/* Index: IDX_CC_FROMUSER                                       */
/*==============================================================*/
CREATE INDEX IDX_CC_FROMUSER ON BPM_INST_CC
    (
     FROM_USER_ID_
        );

/*==============================================================*/
/* Index: IDX_CC_INSTID                                         */
/*==============================================================*/
CREATE INDEX IDX_CC_INSTID ON BPM_INST_CC
    (
     INST_ID_
        );

/*==============================================================*/
/* Table: BPM_INST_CP                                           */
/*==============================================================*/
CREATE TABLE BPM_INST_CP
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    CC_ID_               VARCHAR(64) COMMENT '抄送ID',
    INST_ID_             VARCHAR(64) COMMENT '实例ID',
    USER_ID_             VARCHAR(64) COMMENT '接收用户ID',
    IS_READ_             VARCHAR(64) COMMENT '是否阅读',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_INST_CP COMMENT '抄送接收人';

/*==============================================================*/
/* Index: IDX_INSTCP_CCID                                       */
/*==============================================================*/
CREATE INDEX IDX_INSTCP_CCID ON BPM_INST_CP
    (
     CC_ID_
        );

/*==============================================================*/
/* Table: BPM_INST_DATA                                         */
/*==============================================================*/
CREATE TABLE BPM_INST_DATA
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    INST_ID_             VARCHAR(64) COMMENT '流程实例ID',
    PK_                  VARCHAR(64) COMMENT '数据主键',
    BODEF_ALIAS_         VARCHAR(64) COMMENT '业务模型别名',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_INST_DATA COMMENT '流程实例数据';

/*==============================================================*/
/* Index: IDX_INSTDATA_INST                                     */
/*==============================================================*/
CREATE INDEX IDX_INSTDATA_INST ON BPM_INST_DATA
    (
     INST_ID_
        );

/*==============================================================*/
/* Table: BPM_INST_LOG                                          */
/*==============================================================*/
CREATE TABLE BPM_INST_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    INST_ID_             VARCHAR(64) NOT NULL COMMENT '实例ID',
    USER_ID_             VARCHAR(64) COMMENT '用户ID',
    USER_NAME_           VARCHAR(64) COMMENT '用户名',
    TASK_ID_             VARCHAR(64) COMMENT '任务ID',
    TASK_NAME_           VARCHAR(64) COMMENT '任务名',
    TASK_KEY_            VARCHAR(40) COMMENT '节点ID',
    OP_DESCP_            TEXT COMMENT '操作描述',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_INST_LOG COMMENT '流程实例日志';

/*==============================================================*/
/* Index: IDX_INSTLOG_INST                                      */
/*==============================================================*/
CREATE INDEX IDX_INSTLOG_INST ON BPM_INST_LOG
    (
     INST_ID_
        );

/*==============================================================*/
/* Table: BPM_INST_MSG                                          */
/*==============================================================*/
CREATE TABLE BPM_INST_MSG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    INST_ID_             VARCHAR(64) COMMENT '流程实例',
    AUTHOR_              VARCHAR(64) COMMENT '留言人',
    AUTHOR_ID_           VARCHAR(64) COMMENT '留言人ID',
    CONTENT_             VARCHAR(400) COMMENT '留言内容',
    FILE_ID_             VARCHAR(64) COMMENT '附件ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_INST_MSG COMMENT '流程留言';

/*==============================================================*/
/* Index: IDX_INSTMSG_INST                                      */
/*==============================================================*/
CREATE INDEX IDX_INSTMSG_INST ON BPM_INST_MSG
    (
     INST_ID_
        );

/*==============================================================*/
/* Table: BPM_INST_ROUTER                                       */
/*==============================================================*/
CREATE TABLE BPM_INST_ROUTER
(
    INST_ID_             VARCHAR(64) NOT NULL COMMENT '流程实例ID',
    TABLE_ID_            INT COMMENT '表ID',
    PRIMARY KEY (INST_ID_)
);

ALTER TABLE BPM_INST_ROUTER COMMENT '流程实例路由';

/*==============================================================*/
/* Table: BPM_MOBILE_TAG                                        */
/*==============================================================*/
CREATE TABLE BPM_MOBILE_TAG
(
    TAGID_               VARCHAR(64) NOT NULL COMMENT '主键',
    CID_                 VARCHAR(64) COMMENT '每台机器每个APP标识码',
    MOBILE_TYPE_         VARCHAR(20) COMMENT '手机类型',
    ISBAN_               VARCHAR(20) COMMENT '是屏蔽则不发',
    USER_ID_             VARCHAR(40) COMMENT '系统用户',
    TAG_                 VARCHAR(64) COMMENT 'CID归类使用',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (TAGID_)
);

ALTER TABLE BPM_MOBILE_TAG COMMENT '手机和用户关联表';

/*==============================================================*/
/* Table: BPM_OPINION_LIB                                       */
/*==============================================================*/
CREATE TABLE BPM_OPINION_LIB
(
    OP_ID_               VARCHAR(64) NOT NULL COMMENT '主键',
    USER_ID_             VARCHAR(64) COMMENT '用户ID',
    OP_TEXT_             VARCHAR(255) COMMENT '意见内容',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (OP_ID_)
);

ALTER TABLE BPM_OPINION_LIB COMMENT '意见收藏表';

/*==============================================================*/
/* Table: BPM_REMIND_HISTORY                                    */
/*==============================================================*/
CREATE TABLE BPM_REMIND_HISTORY
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    REMINDER_INST_ID_    VARCHAR(64) COMMENT '催办实例ID',
    REMIND_TYPE_         VARCHAR(40) COMMENT '催办类型',
    INST_ID_             VARCHAR(64) COMMENT '流程实例ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_REMIND_HISTORY COMMENT '催办历史';

/*==============================================================*/
/* Table: BPM_REMIND_INST                                       */
/*==============================================================*/
CREATE TABLE BPM_REMIND_INST
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    INST_ID_             VARCHAR(64) COMMENT '实例ID',
    TASK_ID_             VARCHAR(64) COMMENT '任务ID',
    NAME_                VARCHAR(64) COMMENT '名称',
    ACTION_              VARCHAR(20) COMMENT '到期动作',
    EXPIRE_DATE_         DATETIME COMMENT '期限',
    SCRIPT_              VARCHAR(1000) COMMENT '到期执行脚本',
    NOTIFY_TYPE_         VARCHAR(40) COMMENT '通知类型',
    TIME_TO_SEND_        DATETIME COMMENT '开始发送消息时间点',
    SEND_TIMES_          INT COMMENT '发送次数',
    SEND_INTERVAL_       INT COMMENT '发送时间间隔',
    STATUS_              VARCHAR(40) COMMENT '状态',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_REMIND_INST COMMENT '催办实例';

/*==============================================================*/
/* Index: IDX_REMIND_TASK                                       */
/*==============================================================*/
CREATE INDEX IDX_REMIND_TASK ON BPM_REMIND_INST
    (
     TASK_ID_
        );

/*==============================================================*/
/* Index: IDX_REMIND_INST                                       */
/*==============================================================*/
CREATE INDEX IDX_REMIND_INST ON BPM_REMIND_INST
    (
     INST_ID_
        );

/*==============================================================*/
/* Table: BPM_RU_PATH                                           */
/*==============================================================*/
CREATE TABLE BPM_RU_PATH
(
    PATH_ID_             VARCHAR(64) NOT NULL COMMENT '主键',
    INST_ID_             VARCHAR(64) NOT NULL COMMENT '流程实例ID',
    DEF_ID_              VARCHAR(64) COMMENT '流程定义Id',
    ACT_DEF_ID_          VARCHAR(64) NOT NULL COMMENT 'Act定义ID',
    ACT_INST_ID_         VARCHAR(64) NOT NULL COMMENT 'Act实例ID',
    NODE_ID_             VARCHAR(255) NOT NULL COMMENT '节点ID',
    NODE_NAME_           VARCHAR(255) COMMENT '节点名称',
    NODE_TYPE_           VARCHAR(50) COMMENT '节点类型',
    START_TIME_          DATETIME NOT NULL COMMENT '开始时间',
    END_TIME_            DATETIME COMMENT '结束时间',
    ASSIGNEE_            VARCHAR(64) COMMENT '处理人ID',
    TO_USER_ID_          VARCHAR(64) COMMENT '代理人ID',
    USER_IDS_            VARCHAR(300) COMMENT '原执行人IDS',
    MULTIPLE_TYPE_       VARCHAR(20) COMMENT '是否为多实例',
    EXECUTION_ID_        VARCHAR(64) COMMENT '活动执行ID',
    PARENT_ID_           VARCHAR(64) COMMENT '父ID',
    LEVEL_               INT COMMENT '层次',
    OUT_TRAN_ID_         VARCHAR(255) COMMENT '跳出路线ID',
    TOKEN_               VARCHAR(255) COMMENT '路线令牌',
    JUMP_TYPE_           VARCHAR(50) COMMENT '跳到该节点的方式
            正常跳转
            自由跳转
            回退跳转',
    NEXT_JUMP_TYPE_      VARCHAR(50) COMMENT '下一步跳转方式',
    REF_PATH_ID_         VARCHAR(64) COMMENT '引用路径ID
            当回退时，重新生成的结点，需要记录引用的回退节点，方便新生成的路径再次回退。',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (PATH_ID_)
);

ALTER TABLE BPM_RU_PATH COMMENT '流程实例运行路线';

/*==============================================================*/
/* Index: IDX_RUPATH_ACTINSTID                                  */
/*==============================================================*/
CREATE INDEX IDX_RUPATH_ACTINSTID ON BPM_RU_PATH
    (
     ACT_INST_ID_
        );

/*==============================================================*/
/* Index: IDX_RUPATH_INSTID                                     */
/*==============================================================*/
CREATE INDEX IDX_RUPATH_INSTID ON BPM_RU_PATH
    (
     INST_ID_
        );

/*==============================================================*/
/* Table: BPM_SIGN_DATA                                         */
/*==============================================================*/
CREATE TABLE BPM_SIGN_DATA
(
    DATA_ID_             VARCHAR(64) NOT NULL COMMENT '主键',
    ACT_DEF_ID_          VARCHAR(64) COMMENT '流程定义ID',
    ACT_INST_ID_         VARCHAR(64) COMMENT '流程实例ID',
    NODE_ID_             VARCHAR(64) COMMENT '节点ID',
    USER_ID_             VARCHAR(64) COMMENT '用户ID',
    VOTE_STATUS_         VARCHAR(40) COMMENT '投票意见',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (DATA_ID_)
);

ALTER TABLE BPM_SIGN_DATA COMMENT '会签数据';

/*==============================================================*/
/* Index: IDX_SIGNDATA_ACTINST                                  */
/*==============================================================*/
CREATE INDEX IDX_SIGNDATA_ACTINST ON BPM_SIGN_DATA
    (
     ACT_INST_ID_
        );

/*==============================================================*/
/* Table: BPM_TASK                                              */
/*==============================================================*/
CREATE TABLE BPM_TASK
(
    TASK_ID_             VARCHAR(64) NOT NULL COMMENT '任务ID',
    ACT_TASK_ID_         VARCHAR(64) COMMENT '流程任务ID',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    NAME_                VARCHAR(100) COMMENT '任务名称',
    KEY_                 VARCHAR(64) COMMENT '任务Key',
    BILL_TYPE_           VARCHAR(64) COMMENT '流程类型',
    BILL_NO_             VARCHAR(64) COMMENT '流程单号',
    BUS_KEY_             VARCHAR(64) COMMENT '业务主键',
    DESCP_               VARCHAR(255) COMMENT '任务描述',
    SUBJECT_             VARCHAR(512) COMMENT '事项标题',
    OWNER_               VARCHAR(64) COMMENT '任务所属人',
    ASSIGNEE_            VARCHAR(64) COMMENT '任务执行人',
    ACT_INST_ID_         VARCHAR(64) COMMENT '流程实例ID',
    ACT_DEF_ID_          VARCHAR(64) COMMENT 'ACT流程定义ID',
    DEF_ID_              VARCHAR(64) COMMENT '流程定义ID',
    INST_ID_             VARCHAR(64) COMMENT '流程扩展实例ID',
    STATUS_              VARCHAR(64) COMMENT '任务状态',
    PRIORITY_            VARCHAR(64) COMMENT '任务优先级',
    EXPIRED_TIME_        DATETIME COMMENT '任务过期时间',
    TASK_TYPE_           VARCHAR(64) COMMENT '任务类型
            FLOW_TASK=流程任务
            MAN_TASK=人工任务(创建)',
    EXECUTION_ID_        VARCHAR(64) COMMENT '执行ID',
    PARENT_ID_           VARCHAR(64) COMMENT '父任务ID',
    EXECUTOR_            VARCHAR(20) COMMENT '是否设置执行人',
    PRE_TASK_ID_         VARCHAR(64) COMMENT '上一任务KEY',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (TASK_ID_)
);

ALTER TABLE BPM_TASK COMMENT '流程任务';

/*==============================================================*/
/* Index: IDX_TASK_ACTTASK                                      */
/*==============================================================*/
CREATE INDEX IDX_TASK_ACTTASK ON BPM_TASK
    (
     ACT_TASK_ID_
        );

/*==============================================================*/
/* Table: BPM_TASK_TRANSFER                                     */
/*==============================================================*/
CREATE TABLE BPM_TASK_TRANSFER
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TREE_ID_             VARCHAR(64) COMMENT '分类ID',
    INST_ID_             VARCHAR(64) COMMENT '流程实例ID',
    OWNER_ID_            VARCHAR(64) COMMENT '所有人ID',
    SUBJECT_             VARCHAR(128) COMMENT '任务标题',
    TASK_ID_             VARCHAR(64) COMMENT '任务ID',
    TO_USER_ID_          VARCHAR(64) COMMENT '转办人ID',
    TYPE_                VARCHAR(20) COMMENT '类型(trans,agent)',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_TASK_TRANSFER COMMENT '流程任务转移记录';

/*==============================================================*/
/* Index: IDX_TRANS_TOUSER                                      */
/*==============================================================*/
CREATE INDEX IDX_TRANS_TOUSER ON BPM_TASK_TRANSFER
    (
     TO_USER_ID_
        );

/*==============================================================*/
/* Index: IDX_TRANS_OWNER                                       */
/*==============================================================*/
CREATE INDEX IDX_TRANS_OWNER ON BPM_TASK_TRANSFER
    (
     OWNER_ID_
        );

/*==============================================================*/
/* Table: BPM_TASK_USER                                         */
/*==============================================================*/
CREATE TABLE BPM_TASK_USER
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TASK_ID_             VARCHAR(64) NOT NULL COMMENT '任务ID',
    USER_ID_             VARCHAR(64) COMMENT '用户ID',
    INST_ID_             VARCHAR(64) COMMENT '流程实例ID',
    GROUP_ID_            VARCHAR(64) COMMENT '用户组ID',
    USER_TYPE_           VARCHAR(64) COMMENT '用户类型
            USER=用户
            GROUP=用户组
            ',
    PART_TYPE_           VARCHAR(64) COMMENT '参与类型
            执行人=ASSIGNEE
            抄送人=COPY
            候选人=CANDIDATE',
    IS_READ_             VARCHAR(20) COMMENT '是否已读
            YES,NO',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_TASK_USER COMMENT '任务处理相关人';

/*==============================================================*/
/* Index: IDX_TASKUSER_TASK                                     */
/*==============================================================*/
CREATE INDEX IDX_TASKUSER_TASK ON BPM_TASK_USER
    (
     TASK_ID_
        );

/*==============================================================*/
/* Table: BPM_TEMPORARY_OPINION                                 */
/*==============================================================*/
CREATE TABLE BPM_TEMPORARY_OPINION
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TASK_ID_             VARCHAR(64) COMMENT '任务ID',
    OPINION_             VARCHAR(1000) COMMENT '意见',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_TEMPORARY_OPINION COMMENT '流程意见暂存表';

/*==============================================================*/
/* Table: BPM_TRANSFER                                          */
/*==============================================================*/
CREATE TABLE BPM_TRANSFER
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    TASK_ID_             VARCHAR(64) COMMENT '任务ID',
    APPROVE_TYPE_        VARCHAR(40) COMMENT '签批类型',
    TRANSFER_TYPE_       VARCHAR(40) COMMENT '流转类型',
    COMPLETE_TYPE_       INT COMMENT '完成类型',
    COUNT_               INT COMMENT '数量',
    COMPLETE_COUNT_      INT COMMENT '完成次数',
    COMPLETE_SETTING_    INT COMMENT '完成次数设定',
    NOTICE_TYPE_         VARCHAR(40) COMMENT '通知类型',
    COMPLETE_JUDGE_TYPE_ VARCHAR(40) COMMENT '完成判断类型',
    TASK_USER_ID_        VARCHAR(2000) COMMENT '流转人员ID',
    TASK_USER_IDX_       INT COMMENT '当前流转下标',
    INST_ID_             VARCHAR(64) COMMENT '流程实例ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

/*==============================================================*/
/* Table: BPM_TRANSFER_LOG                                      */
/*==============================================================*/
CREATE TABLE BPM_TRANSFER_LOG
(
    ID_                  VARCHAR(64) NOT NULL COMMENT '主键',
    ASSIGNEE_            VARCHAR(64) COMMENT '流转人',
    REMARK_              VARCHAR(255) COMMENT '流转意见',
    STATUS_              VARCHAR(40) COMMENT '流转状态',
    TASK_ID_             VARCHAR(40) COMMENT '任务ID',
    TRANS_TASK_ID_       VARCHAR(64) COMMENT '流转任务ID',
    TENANT_ID_           VARCHAR(64) COMMENT '租用用户Id',
    CREATE_DEP_ID_       VARCHAR(64) COMMENT '创建部门ID',
    CREATE_BY_           VARCHAR(64) COMMENT '创建人ID',
    CREATE_TIME_         DATETIME COMMENT '创建时间',
    UPDATE_BY_           VARCHAR(64) COMMENT '更新人ID',
    UPDATE_TIME_         DATETIME COMMENT '更新时间',
    PRIMARY KEY (ID_)
);

ALTER TABLE BPM_TRANSFER_LOG COMMENT '流转任务日志表';

COMMIT ;
