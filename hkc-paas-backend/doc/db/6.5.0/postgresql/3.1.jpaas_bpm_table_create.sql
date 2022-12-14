CREATE TABLE IF NOT EXISTS public.undo_log
(
    id            SERIAL       NOT NULL,
    branch_id     BIGINT       NOT NULL,
    xid           VARCHAR(100) NOT NULL,
    context       VARCHAR(128) NOT NULL,
    rollback_info BYTEA        NOT NULL,
    log_status    INT          NOT NULL,
    log_created   TIMESTAMP(0) NOT NULL,
    log_modified  TIMESTAMP(0) NOT NULL,
    CONSTRAINT pk_undo_log PRIMARY KEY (id),
    CONSTRAINT ux_undo_log UNIQUE (xid, branch_id)
    );
CREATE SEQUENCE IF NOT EXISTS undo_log_id_seq INCREMENT BY 1 MINVALUE 1 ;

/*==============================================================*/
/* Table: BPM_AGENT                                             */
/*==============================================================*/
CREATE TABLE BPM_AGENT (
                           ID_                  VARCHAR(64)          NOT NULL,
                           NAME_                VARCHAR(128)         NULL,
                           OWNER_ID_            VARCHAR(64)          NULL,
                           TYPE_                VARCHAR(20)          NULL,
                           TO_USER_             VARCHAR(64)          NULL,
                           TO_USER_NAME_        VARCHAR(20)          NULL,
                           STATUS_              INT4                 NULL,
                           START_TIME_          DATE                 NULL,
                           END_TIME_            DATE                 NULL,
                           DESCRIPTION_         VARCHAR(255)         NULL,
                           TENANT_ID_           VARCHAR(64)          NULL,
                           CREATE_DEP_ID_       VARCHAR(64)          NULL,
                           CREATE_BY_           VARCHAR(64)          NULL,
                           CREATE_TIME_         DATE                 NULL,
                           UPDATE_BY_           VARCHAR(64)          NULL,
                           UPDATE_TIME_         DATE                 NULL,
                           CONSTRAINT PK_BPM_AGENT PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_AGENT IS
'????????????';

COMMENT ON COLUMN BPM_AGENT.ID_ IS
'??????';

COMMENT ON COLUMN BPM_AGENT.NAME_ IS
'??????';

COMMENT ON COLUMN BPM_AGENT.OWNER_ID_ IS
'?????????';

COMMENT ON COLUMN BPM_AGENT.TYPE_ IS
'??????';

COMMENT ON COLUMN BPM_AGENT.TO_USER_ IS
'?????????';

COMMENT ON COLUMN BPM_AGENT.TO_USER_NAME_ IS
'???????????????';

COMMENT ON COLUMN BPM_AGENT.STATUS_ IS
'??????';

COMMENT ON COLUMN BPM_AGENT.START_TIME_ IS
'??????????????????';

COMMENT ON COLUMN BPM_AGENT.END_TIME_ IS
'??????????????????';

COMMENT ON COLUMN BPM_AGENT.DESCRIPTION_ IS
'??????';

COMMENT ON COLUMN BPM_AGENT.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_AGENT.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_AGENT.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_AGENT.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_AGENT.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_AGENT.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_AGENT_OWNER                                       */
/*==============================================================*/
CREATE  INDEX IDX_AGENT_OWNER ON BPM_AGENT (
                                            OWNER_ID_
    );

/*==============================================================*/
/* Table: BPM_AGENT_FLOWDEF                                     */
/*==============================================================*/
CREATE TABLE BPM_AGENT_FLOWDEF (
                                   ID_                  VARCHAR(64)          NOT NULL,
                                   AGENT_ID_            VARCHAR(64)          NULL,
                                   DEF_ID_              VARCHAR(64)          NULL,
                                   DEF_NAME_            VARCHAR(128)         NULL,
                                   TENANT_ID_           VARCHAR(64)          NULL,
                                   CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                   CREATE_BY_           VARCHAR(64)          NULL,
                                   CREATE_TIME_         DATE                 NULL,
                                   UPDATE_BY_           VARCHAR(64)          NULL,
                                   UPDATE_TIME_         DATE                 NULL,
                                   CONSTRAINT PK_BPM_AGENT_FLOWDEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_AGENT_FLOWDEF IS
'????????????????????????';

COMMENT ON COLUMN BPM_AGENT_FLOWDEF.ID_ IS
'??????';

COMMENT ON COLUMN BPM_AGENT_FLOWDEF.AGENT_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_AGENT_FLOWDEF.DEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_AGENT_FLOWDEF.DEF_NAME_ IS
'??????????????????';

COMMENT ON COLUMN BPM_AGENT_FLOWDEF.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_AGENT_FLOWDEF.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_AGENT_FLOWDEF.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_AGENT_FLOWDEF.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_AGENT_FLOWDEF.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_AGENT_FLOWDEF.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_ARCHIVE_LOG                                       */
/*==============================================================*/
CREATE TABLE BPM_ARCHIVE_LOG (
                                 ID_                  VARCHAR(64)          NOT NULL,
                                 ARCHIVE_DATE_        DATE                 NULL,
                                 MEMO_                VARCHAR(255)         NULL,
                                 TABLE_ID_            INT4                 NULL,
                                 STATUS_              VARCHAR(40)          NULL,
                                 START_TIME_          DATE                 NULL,
                                 END_TIME_            DATE                 NULL,
                                 CREATE_NAME_         VARCHAR(64)          NULL,
                                 ERR_LOG_             TEXT                 NULL,
                                 TENANT_ID_           VARCHAR(64)          NULL,
                                 CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                 CREATE_BY_           VARCHAR(64)          NULL,
                                 CREATE_TIME_         DATE                 NULL,
                                 UPDATE_BY_           VARCHAR(64)          NULL,
                                 UPDATE_TIME_         DATE                 NULL,
                                 CONSTRAINT PK_BPM_ARCHIVE_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_ARCHIVE_LOG IS
'??????????????????';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.ID_ IS
'??????';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.ARCHIVE_DATE_ IS
'???????????????';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.MEMO_ IS
'??????';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.TABLE_ID_ IS
'???ID';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.STATUS_ IS
'??????
??????(1????????? 0?????????)';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.START_TIME_ IS
'??????????????????';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.END_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.CREATE_NAME_ IS
'????????????';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.ERR_LOG_ IS
'????????????';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_ARCHIVE_LOG.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_CALTIME_BLOCK                                     */
/*==============================================================*/
CREATE TABLE BPM_CALTIME_BLOCK (
                                   SETTING_ID_          VARCHAR(64)          NOT NULL,
                                   SETTING_NAME_        VARCHAR(64)          NULL,
                                   TIME_INTERVALS_      VARCHAR(255)         NULL,
                                   TENANT_ID_           VARCHAR(64)          NULL,
                                   CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                   CREATE_BY_           VARCHAR(64)          NULL,
                                   CREATE_TIME_         DATE                 NULL,
                                   UPDATE_BY_           VARCHAR(64)          NULL,
                                   UPDATE_TIME_         DATE                 NULL,
                                   CONSTRAINT PK_BPM_CALTIME_BLOCK PRIMARY KEY (SETTING_ID_)
);

COMMENT ON TABLE BPM_CALTIME_BLOCK IS
'????????????';

COMMENT ON COLUMN BPM_CALTIME_BLOCK.SETTING_ID_ IS
'??????';

COMMENT ON COLUMN BPM_CALTIME_BLOCK.SETTING_NAME_ IS
'????????????';

COMMENT ON COLUMN BPM_CALTIME_BLOCK.TIME_INTERVALS_ IS
'????????????';

COMMENT ON COLUMN BPM_CALTIME_BLOCK.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_CALTIME_BLOCK.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_CALTIME_BLOCK.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CALTIME_BLOCK.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_CALTIME_BLOCK.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CALTIME_BLOCK.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_CAL_CALENDAR                                      */
/*==============================================================*/
CREATE TABLE BPM_CAL_CALENDAR (
                                  CALENDER_ID_         VARCHAR(64)          NOT NULL,
                                  SETTING_ID_          VARCHAR(64)          NULL,
                                  START_TIME_          DATE                 NULL,
                                  END_TIME_            DATE                 NULL,
                                  CONNECT_ID_          VARCHAR(64)          NULL,
                                  START_DAY_           DATE                 NULL,
                                  END_DAY_             DATE                 NULL,
                                  INFO_                VARCHAR(4000)        NULL,
                                  TIME_INTERVALS_      TEXT                 NULL,
                                  TENANT_ID_           VARCHAR(64)          NULL,
                                  CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                  CREATE_BY_           VARCHAR(64)          NULL,
                                  CREATE_TIME_         DATE                 NULL,
                                  UPDATE_BY_           VARCHAR(64)          NULL,
                                  UPDATE_TIME_         DATE                 NULL,
                                  CONSTRAINT PK_BPM_CAL_CALENDAR PRIMARY KEY (CALENDER_ID_)
);

COMMENT ON TABLE BPM_CAL_CALENDAR IS
'????????????';

COMMENT ON COLUMN BPM_CAL_CALENDAR.CALENDER_ID_ IS
'??????';

COMMENT ON COLUMN BPM_CAL_CALENDAR.SETTING_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_CAL_CALENDAR.START_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_CAL_CALENDAR.END_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_CAL_CALENDAR.CONNECT_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CAL_CALENDAR.START_DAY_ IS
'????????????';

COMMENT ON COLUMN BPM_CAL_CALENDAR.END_DAY_ IS
'????????????';

COMMENT ON COLUMN BPM_CAL_CALENDAR.INFO_ IS
'???????????????';

COMMENT ON COLUMN BPM_CAL_CALENDAR.TIME_INTERVALS_ IS
'???????????????json';

COMMENT ON COLUMN BPM_CAL_CALENDAR.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_CAL_CALENDAR.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_CAL_CALENDAR.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CAL_CALENDAR.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_CAL_CALENDAR.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CAL_CALENDAR.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_CAL_GRANT                                         */
/*==============================================================*/
CREATE TABLE BPM_CAL_GRANT (
                               GRANT_ID_            VARCHAR(64)          NOT NULL,
                               SETTING_ID_          VARCHAR(64)          NULL,
                               GRANT_TYPE_          VARCHAR(40)          NULL,
                               BELONG_WHO_          VARCHAR(64)          NULL,
                               BELONG_WHO_ID_       VARCHAR(64)          NULL,
                               TENANT_ID_           VARCHAR(64)          NULL,
                               CREATE_DEP_ID_       VARCHAR(64)          NULL,
                               CREATE_BY_           VARCHAR(64)          NULL,
                               CREATE_TIME_         DATE                 NULL,
                               UPDATE_BY_           VARCHAR(64)          NULL,
                               UPDATE_TIME_         DATE                 NULL,
                               CONSTRAINT PK_BPM_CAL_GRANT PRIMARY KEY (GRANT_ID_)
);

COMMENT ON TABLE BPM_CAL_GRANT IS
'????????????';

COMMENT ON COLUMN BPM_CAL_GRANT.GRANT_ID_ IS
'??????';

COMMENT ON COLUMN BPM_CAL_GRANT.SETTING_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_CAL_GRANT.GRANT_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_CAL_GRANT.BELONG_WHO_ IS
'?????????';

COMMENT ON COLUMN BPM_CAL_GRANT.BELONG_WHO_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CAL_GRANT.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_CAL_GRANT.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_CAL_GRANT.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CAL_GRANT.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_CAL_GRANT.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CAL_GRANT.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_CAL_SETTING                                       */
/*==============================================================*/
CREATE TABLE BPM_CAL_SETTING (
                                 SETTING_ID_          VARCHAR(64)          NOT NULL,
                                 CAL_NAME_            VARCHAR(64)          NULL,
                                 IS_COMMON_           VARCHAR(40)          NULL,
                                 TENANT_ID_           VARCHAR(64)          NULL,
                                 CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                 CREATE_BY_           VARCHAR(64)          NULL,
                                 CREATE_TIME_         DATE                 NULL,
                                 UPDATE_BY_           VARCHAR(64)          NULL,
                                 UPDATE_TIME_         DATE                 NULL,
                                 CONSTRAINT PK_BPM_CAL_SETTING PRIMARY KEY (SETTING_ID_)
);

COMMENT ON TABLE BPM_CAL_SETTING IS
'????????????';

COMMENT ON COLUMN BPM_CAL_SETTING.SETTING_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_CAL_SETTING.CAL_NAME_ IS
'????????????';

COMMENT ON COLUMN BPM_CAL_SETTING.IS_COMMON_ IS
'??????';

COMMENT ON COLUMN BPM_CAL_SETTING.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_CAL_SETTING.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_CAL_SETTING.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CAL_SETTING.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_CAL_SETTING.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CAL_SETTING.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_CHECK_FILE                                        */
/*==============================================================*/
CREATE TABLE BPM_CHECK_FILE (
                                ID_                  VARCHAR(64)          NOT NULL,
                                FILE_ID_             VARCHAR(64)          NOT NULL,
                                FILE_NAME_           VARCHAR(255)         NULL,
                                INST_ID_             VARCHAR(64)          NULL,
                                JUMP_ID_             VARCHAR(64)          NOT NULL,
                                UPDATE_TIME_         DATE                 NULL,
                                UPDATE_BY_           VARCHAR(64)          NULL,
                                CREATE_TIME_         DATE                 NULL,
                                CREATE_BY_           VARCHAR(64)          NULL,
                                TENANT_ID_           VARCHAR(64)          NULL,
                                CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                CONSTRAINT PK_BPM_CHECK_FILE PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_CHECK_FILE IS
'??????????????????';

COMMENT ON COLUMN BPM_CHECK_FILE.ID_ IS
'??????';

COMMENT ON COLUMN BPM_CHECK_FILE.FILE_ID_ IS
'??????Id';

COMMENT ON COLUMN BPM_CHECK_FILE.FILE_NAME_ IS
'?????????';

COMMENT ON COLUMN BPM_CHECK_FILE.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_CHECK_FILE.JUMP_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_CHECK_FILE.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_CHECK_FILE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CHECK_FILE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_CHECK_FILE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CHECK_FILE.TENANT_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_CHECK_FILE.CREATE_DEP_ID_ IS
'????????????ID';

/*==============================================================*/
/* Index: IDX_CHKFILE_JUMP                                      */
/*==============================================================*/
CREATE  INDEX IDX_CHKFILE_JUMP ON BPM_CHECK_FILE (
                                                  JUMP_ID_
    );

/*==============================================================*/
/* Table: BPM_CHECK_HISTORY                                     */
/*==============================================================*/
CREATE TABLE BPM_CHECK_HISTORY (
                                   HIS_ID_              VARCHAR(64)          NOT NULL,
                                   ACT_DEF_ID_          VARCHAR(64)          NULL,
                                   INST_ID_             VARCHAR(64)          NULL,
                                   TREE_ID_             VARCHAR(64)          NULL,
                                   SUBJECT_             VARCHAR(64)          NULL,
                                   NODE_NAME_           VARCHAR(255)         NULL,
                                   NODE_ID_             VARCHAR(255)         NULL,
                                   TASK_ID_             VARCHAR(64)          NULL,
                                   CM_ST_TASK_ID_       VARCHAR(64)          NULL,
                                   COMPLETE_TIME_       DATE                 NULL,
                                   DURATION_            INT4                 NULL,
                                   DURATION_VAL_        INT4                 NULL,
                                   OWNER_ID_            VARCHAR(64)          NULL,
                                   HANDLER_ID_          VARCHAR(64)          NULL,
                                   AGENT_USER_ID_       VARCHAR(64)          NULL,
                                   CHECK_STATUS_        VARCHAR(50)          NULL,
                                   JUMP_TYPE_           VARCHAR(50)          NULL,
                                   REMARK_              VARCHAR(512)         NULL,
                                   OPINION_NAME_        VARCHAR(50)          NULL,
                                   HANDLE_DEP_ID_       VARCHAR(64)          NULL,
                                   HANDLE_DEP_FULL_     VARCHAR(300)         NULL,
                                   LINK_UP_USER_IDS     VARCHAR(400)         NULL,
                                   ENABLE_MOBILE_       INT2                 NULL,
                                   REL_INSTS_           VARCHAR(300)         NULL,
                                   TENANT_ID_           VARCHAR(64)          NULL,
                                   CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                   CREATE_BY_           VARCHAR(64)          NULL,
                                   CREATE_TIME_         DATE                 NULL,
                                   UPDATE_BY_           VARCHAR(64)          NULL,
                                   UPDATE_TIME_         DATE                 NULL,
                                   CONSTRAINT PK_BPM_CHECK_HISTORY PRIMARY KEY (HIS_ID_)
);

COMMENT ON TABLE BPM_CHECK_HISTORY IS
'????????????????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.HIS_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.ACT_DEF_ID_ IS
'ACT????????????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.SUBJECT_ IS
'??????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.NODE_NAME_ IS
'????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.NODE_ID_ IS
'??????Key';

COMMENT ON COLUMN BPM_CHECK_HISTORY.TASK_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.CM_ST_TASK_ID_ IS
'?????????????????????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.COMPLETE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.DURATION_ IS
'????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.DURATION_VAL_ IS
'??????????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.OWNER_ID_ IS
'???????????????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.HANDLER_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.AGENT_USER_ID_ IS
'????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.CHECK_STATUS_ IS
'????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.JUMP_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.REMARK_ IS
'????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.OPINION_NAME_ IS
'??????????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.HANDLE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.HANDLE_DEP_FULL_ IS
'??????????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.LINK_UP_USER_IDS IS
'???????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.ENABLE_MOBILE_ IS
'??????????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.REL_INSTS_ IS
'??????????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_CHECK_HISTORY.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_CHECK_HISTORY.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_CHECK_HISTORY.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_HISTORY_INSTID                                    */
/*==============================================================*/
CREATE  INDEX IDX_HISTORY_INSTID ON BPM_CHECK_HISTORY (
                                                       INST_ID_
    );

/*==============================================================*/
/* Index: IDX_HISTORY_TASKID                                    */
/*==============================================================*/
CREATE  INDEX IDX_HISTORY_TASKID ON BPM_CHECK_HISTORY (
                                                       TASK_ID_
    );

/*==============================================================*/
/* Table: BPM_DEF                                               */
/*==============================================================*/
CREATE TABLE BPM_DEF (
                         DEF_ID_              VARCHAR(64)          NOT NULL,
                         TREE_ID_             VARCHAR(64)          NULL,
                         NAME_                VARCHAR(255)         NOT NULL,
                         KEY_                 VARCHAR(255)         NOT NULL,
                         DESCP_               VARCHAR(1024)        NULL,
                         ACT_DEF_ID_          VARCHAR(255)         NULL,
                         ACT_DEP_ID_          VARCHAR(255)         NULL,
                         STATUS_              VARCHAR(20)          NOT NULL,
                         VERSION_             INT4                 NOT NULL,
                         IS_MAIN_             VARCHAR(20)          NULL,
                         DESIGN_XML_          TEXT                 NULL,
                         EXT_CONFS            TEXT                 NULL,
                         MAIN_DEF_ID_         VARCHAR(64)          NULL,
                         FORMAL_              VARCHAR(10)          NULL,
                         BO_DEF_IDS_          VARCHAR(64)          NULL,
                         ICON_                VARCHAR(64)          NULL,
                         COLOR_               VARCHAR(64)          NULL,
                         TENANT_ID_           VARCHAR(64)          NULL,
                         CREATE_DEP_ID_       VARCHAR(64)          NULL,
                         CREATE_BY_           VARCHAR(64)          NULL,
                         CREATE_TIME_         DATE                 NULL,
                         UPDATE_BY_           VARCHAR(64)          NULL,
                         UPDATE_TIME_         DATE                 NULL,
                         DESIGN_XML_TEMP_     TEXT                 NULL,
                         EXT_CONFS_TEMP       TEXT                 NULL,
                         APP_ID_              VARCHAR(64)          NULL,
                         CONSTRAINT PK_BPM_DEF PRIMARY KEY (DEF_ID_)
);

COMMENT ON TABLE BPM_DEF IS
'????????????';

COMMENT ON COLUMN BPM_DEF.DEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_DEF.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_DEF.NAME_ IS
'??????';

COMMENT ON COLUMN BPM_DEF.KEY_ IS
'??????Key';

COMMENT ON COLUMN BPM_DEF.DESCP_ IS
'??????';

COMMENT ON COLUMN BPM_DEF.ACT_DEF_ID_ IS
'ACT??????ID';

COMMENT ON COLUMN BPM_DEF.ACT_DEP_ID_ IS
'ACT??????ID';

COMMENT ON COLUMN BPM_DEF.STATUS_ IS
'??????
DRAFT=??????
DEPLOY=??????
INVALID=??????';

COMMENT ON COLUMN BPM_DEF.VERSION_ IS
'?????????';

COMMENT ON COLUMN BPM_DEF.IS_MAIN_ IS
'?????????';

COMMENT ON COLUMN BPM_DEF.DESIGN_XML_ IS
'??????XML';

COMMENT ON COLUMN BPM_DEF.EXT_CONFS IS
'????????????';

COMMENT ON COLUMN BPM_DEF.MAIN_DEF_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_DEF.FORMAL_ IS
'????????????';

COMMENT ON COLUMN BPM_DEF.BO_DEF_IDS_ IS
'BO??????ID';

COMMENT ON COLUMN BPM_DEF.ICON_ IS
'??????????????????';

COMMENT ON COLUMN BPM_DEF.COLOR_ IS
'????????????????????????';

COMMENT ON COLUMN BPM_DEF.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_DEF.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_DEF.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_DEF.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_DEF.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_DEF.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_DEF.DESIGN_XML_TEMP_ IS
'??????XML(??????)';

COMMENT ON COLUMN BPM_DEF.EXT_CONFS_TEMP IS
'????????????(??????)';

COMMENT ON COLUMN BPM_DEF.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: BPM_INST                                              */
/*==============================================================*/
CREATE TABLE BPM_INST (
                          INST_ID_             VARCHAR(64)          NOT NULL,
                          TREE_ID_             VARCHAR(64)          NULL,
                          DEF_ID_              VARCHAR(64)          NOT NULL,
                          ACT_INST_ID_         VARCHAR(64)          NULL,
                          ACT_DEF_ID_          VARCHAR(64)          NOT NULL,
                          DEF_CODE_            VARCHAR(64)          NULL,
                          INST_NO_             VARCHAR(50)          NULL,
                          BILL_TYPE_           VARCHAR(40)          NULL,
                          BILL_NO_             VARCHAR(255)         NULL,
                          SUBJECT_             VARCHAR(255)         NULL,
                          STATUS_              VARCHAR(20)          NULL,
                          VERSION_             INT4                 NULL,
                          BUS_KEY_             VARCHAR(64)          NULL,
                          CHECK_FILE_ID_       VARCHAR(64)          NULL,
                          IS_TEST_             VARCHAR(20)          NULL,
                          ERRORS_              TEXT                 NULL,
                          END_TIME_            DATE                 NULL,
                          DATA_SAVE_MODE_      VARCHAR(10)          NULL,
                          SUPPORT_MOBILE_      INT4                 NULL,
                          START_DEP_ID_        VARCHAR(64)          NULL,
                          START_DEP_FULL_      VARCHAR(300)         NULL,
                          LOCKED_BY_           VARCHAR(64)          NULL,
                          LIVE_INST_ID_        VARCHAR(64)          NULL,
                          FIELD_JSON_          VARCHAR(600)         NULL,
                          IS_LIVE_             VARCHAR(64)          NULL,
                          TENANT_ID_           VARCHAR(64)          NULL,
                          CREATE_DEP_ID_       VARCHAR(64)          NULL,
                          CREATE_BY_           VARCHAR(64)          NULL,
                          CREATE_TIME_         DATE                 NULL,
                          UPDATE_BY_           VARCHAR(64)          NULL,
                          UPDATE_TIME_         DATE                 NULL,
                          FORM_SOLUTION_ALIAS  VARCHAR(64)          NULL,
                          CONSTRAINT PK_BPM_INST PRIMARY KEY (INST_ID_)
);

COMMENT ON TABLE BPM_INST IS
'????????????';

COMMENT ON COLUMN BPM_INST.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_INST.DEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST.ACT_INST_ID_ IS
'ACT??????ID';

COMMENT ON COLUMN BPM_INST.ACT_DEF_ID_ IS
'ACT??????ID';

COMMENT ON COLUMN BPM_INST.DEF_CODE_ IS
'????????????ID_';

COMMENT ON COLUMN BPM_INST.INST_NO_ IS
'?????????????????????';

COMMENT ON COLUMN BPM_INST.BILL_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_INST.BILL_NO_ IS
'????????????';

COMMENT ON COLUMN BPM_INST.SUBJECT_ IS
'??????';

COMMENT ON COLUMN BPM_INST.STATUS_ IS
'????????????';

COMMENT ON COLUMN BPM_INST.VERSION_ IS
'??????';

COMMENT ON COLUMN BPM_INST.BUS_KEY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST.CHECK_FILE_ID_ IS
'??????????????????ID';

COMMENT ON COLUMN BPM_INST.IS_TEST_ IS
'???????????????';

COMMENT ON COLUMN BPM_INST.ERRORS_ IS
'????????????';

COMMENT ON COLUMN BPM_INST.END_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_INST.DATA_SAVE_MODE_ IS
'??????????????????(all,json,db)';

COMMENT ON COLUMN BPM_INST.SUPPORT_MOBILE_ IS
'???????????????';

COMMENT ON COLUMN BPM_INST.START_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST.START_DEP_FULL_ IS
'??????????????????';

COMMENT ON COLUMN BPM_INST.LOCKED_BY_ IS
'?????????';

COMMENT ON COLUMN BPM_INST.LIVE_INST_ID_ IS
'?????????????????????';

COMMENT ON COLUMN BPM_INST.FIELD_JSON_ IS
'??????????????????';

COMMENT ON COLUMN BPM_INST.IS_LIVE_ IS
'????????????';

COMMENT ON COLUMN BPM_INST.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_INST.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_INST.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_INST.FORM_SOLUTION_ALIAS IS
'??????????????????';

/*==============================================================*/
/* Index: IDX_BPMINST_ACTINSTID                                 */
/*==============================================================*/
CREATE  INDEX IDX_BPMINST_ACTINSTID ON BPM_INST (
                                                 ACT_INST_ID_
    );

/*==============================================================*/
/* Table: BPM_INST_CC                                           */
/*==============================================================*/
CREATE TABLE BPM_INST_CC (
                             ID_                  VARCHAR(64)          NOT NULL,
                             SUBJECT_             VARCHAR(64)          NULL,
                             NODE_ID_             VARCHAR(64)          NULL,
                             NODE_NAME_           VARCHAR(64)          NULL,
                             FROM_USER_           VARCHAR(64)          NULL,
                             FROM_USER_ID_        VARCHAR(64)          NULL,
                             INST_ID_             VARCHAR(64)          NULL,
                             DEF_ID_              VARCHAR(64)          NULL,
                             CC_TYPE_             VARCHAR(20)          NULL,
                             TENANT_ID_           VARCHAR(64)          NULL,
                             CREATE_DEP_ID_       VARCHAR(64)          NULL,
                             CREATE_BY_           VARCHAR(64)          NULL,
                             CREATE_TIME_         DATE                 NULL,
                             UPDATE_BY_           VARCHAR(64)          NULL,
                             UPDATE_TIME_         DATE                 NULL,
                             TREE_ID_             VARCHAR(64)          NULL,
                             CONSTRAINT PK_BPM_INST_CC PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_INST_CC IS
'????????????';

COMMENT ON COLUMN BPM_INST_CC.ID_ IS
'??????';

COMMENT ON COLUMN BPM_INST_CC.SUBJECT_ IS
'??????';

COMMENT ON COLUMN BPM_INST_CC.NODE_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_INST_CC.NODE_NAME_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_CC.FROM_USER_ IS
'?????????';

COMMENT ON COLUMN BPM_INST_CC.FROM_USER_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_CC.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST_CC.DEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST_CC.CC_TYPE_ IS
'??????';

COMMENT ON COLUMN BPM_INST_CC.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_INST_CC.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST_CC.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_CC.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_CC.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_CC.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_CC.TREE_ID_ IS
'??????ID';

/*==============================================================*/
/* Index: IDX_CC_FROMUSER                                       */
/*==============================================================*/
CREATE  INDEX IDX_CC_FROMUSER ON BPM_INST_CC (
                                              FROM_USER_ID_
    );

/*==============================================================*/
/* Index: IDX_CC_INSTID                                         */
/*==============================================================*/
CREATE  INDEX IDX_CC_INSTID ON BPM_INST_CC (
                                            INST_ID_
    );

/*==============================================================*/
/* Table: BPM_INST_CP                                           */
/*==============================================================*/
CREATE TABLE BPM_INST_CP (
                             ID_                  VARCHAR(64)          NOT NULL,
                             CC_ID_               VARCHAR(64)          NULL,
                             INST_ID_             VARCHAR(64)          NULL,
                             USER_ID_             VARCHAR(64)          NULL,
                             IS_READ_             VARCHAR(64)          NULL,
                             TENANT_ID_           VARCHAR(64)          NULL,
                             CREATE_DEP_ID_       VARCHAR(64)          NULL,
                             CREATE_BY_           VARCHAR(64)          NULL,
                             CREATE_TIME_         DATE                 NULL,
                             UPDATE_BY_           VARCHAR(64)          NULL,
                             UPDATE_TIME_         DATE                 NULL,
                             CONSTRAINT PK_BPM_INST_CP PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_INST_CP IS
'???????????????';

COMMENT ON COLUMN BPM_INST_CP.ID_ IS
'??????';

COMMENT ON COLUMN BPM_INST_CP.CC_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_INST_CP.INST_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_INST_CP.USER_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST_CP.IS_READ_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_CP.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_INST_CP.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST_CP.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_CP.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_CP.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_CP.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_INSTCP_CCID                                       */
/*==============================================================*/
CREATE  INDEX IDX_INSTCP_CCID ON BPM_INST_CP (
                                              CC_ID_
    );

/*==============================================================*/
/* Table: BPM_INST_DATA                                         */
/*==============================================================*/
CREATE TABLE BPM_INST_DATA (
                               ID_                  VARCHAR(64)          NOT NULL,
                               INST_ID_             VARCHAR(64)          NULL,
                               PK_                  VARCHAR(64)          NULL,
                               BODEF_ALIAS_         VARCHAR(64)          NULL,
                               TENANT_ID_           VARCHAR(64)          NULL,
                               CREATE_DEP_ID_       VARCHAR(64)          NULL,
                               CREATE_BY_           VARCHAR(64)          NULL,
                               CREATE_TIME_         DATE                 NULL,
                               UPDATE_BY_           VARCHAR(64)          NULL,
                               UPDATE_TIME_         DATE                 NULL,
                               CONSTRAINT PK_BPM_INST_DATA PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_INST_DATA IS
'??????????????????';

COMMENT ON COLUMN BPM_INST_DATA.ID_ IS
'??????';

COMMENT ON COLUMN BPM_INST_DATA.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST_DATA.PK_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_DATA.BODEF_ALIAS_ IS
'??????????????????';

COMMENT ON COLUMN BPM_INST_DATA.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_INST_DATA.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST_DATA.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_DATA.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_DATA.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_DATA.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_INSTDATA_INST                                     */
/*==============================================================*/
CREATE  INDEX IDX_INSTDATA_INST ON BPM_INST_DATA (
                                                  INST_ID_
    );

/*==============================================================*/
/* Table: BPM_INST_LOG                                          */
/*==============================================================*/
CREATE TABLE BPM_INST_LOG (
                              ID_                  VARCHAR(64)          NOT NULL,
                              INST_ID_             VARCHAR(64)          NOT NULL,
                              USER_ID_             VARCHAR(64)          NULL,
                              USER_NAME_           VARCHAR(64)          NULL,
                              TASK_ID_             VARCHAR(64)          NULL,
                              TASK_NAME_           VARCHAR(64)          NULL,
                              TASK_KEY_            VARCHAR(40)          NULL,
                              OP_DESCP_            TEXT                 NULL,
                              TENANT_ID_           VARCHAR(64)          NULL,
                              CREATE_DEP_ID_       VARCHAR(64)          NULL,
                              CREATE_BY_           VARCHAR(64)          NULL,
                              CREATE_TIME_         DATE                 NULL,
                              UPDATE_BY_           VARCHAR(64)          NULL,
                              UPDATE_TIME_         DATE                 NULL,
                              CONSTRAINT PK_BPM_INST_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_INST_LOG IS
'??????????????????';

COMMENT ON COLUMN BPM_INST_LOG.ID_ IS
'??????';

COMMENT ON COLUMN BPM_INST_LOG.INST_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_INST_LOG.USER_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_INST_LOG.USER_NAME_ IS
'?????????';

COMMENT ON COLUMN BPM_INST_LOG.TASK_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_INST_LOG.TASK_NAME_ IS
'?????????';

COMMENT ON COLUMN BPM_INST_LOG.TASK_KEY_ IS
'??????ID';

COMMENT ON COLUMN BPM_INST_LOG.OP_DESCP_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_LOG.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_INST_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST_LOG.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_LOG.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_LOG.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_INSTLOG_INST                                      */
/*==============================================================*/
CREATE  INDEX IDX_INSTLOG_INST ON BPM_INST_LOG (
                                                INST_ID_
    );

/*==============================================================*/
/* Table: BPM_INST_MSG                                          */
/*==============================================================*/
CREATE TABLE BPM_INST_MSG (
                              ID_                  VARCHAR(64)          NOT NULL,
                              INST_ID_             VARCHAR(64)          NULL,
                              AUTHOR_              VARCHAR(64)          NULL,
                              AUTHOR_ID_           VARCHAR(64)          NULL,
                              CONTENT_             VARCHAR(400)         NULL,
                              FILE_ID_             VARCHAR(64)          NULL,
                              TENANT_ID_           VARCHAR(64)          NULL,
                              CREATE_DEP_ID_       VARCHAR(64)          NULL,
                              CREATE_BY_           VARCHAR(64)          NULL,
                              CREATE_TIME_         DATE                 NULL,
                              UPDATE_BY_           VARCHAR(64)          NULL,
                              UPDATE_TIME_         DATE                 NULL,
                              CONSTRAINT PK_BPM_INST_MSG PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_INST_MSG IS
'????????????';

COMMENT ON COLUMN BPM_INST_MSG.ID_ IS
'??????';

COMMENT ON COLUMN BPM_INST_MSG.INST_ID_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_MSG.AUTHOR_ IS
'?????????';

COMMENT ON COLUMN BPM_INST_MSG.AUTHOR_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_MSG.CONTENT_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_MSG.FILE_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_INST_MSG.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_INST_MSG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST_MSG.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_MSG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_INST_MSG.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_INST_MSG.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_INSTMSG_INST                                      */
/*==============================================================*/
CREATE  INDEX IDX_INSTMSG_INST ON BPM_INST_MSG (
                                                INST_ID_
    );

/*==============================================================*/
/* Table: BPM_INST_ROUTER                                       */
/*==============================================================*/
CREATE TABLE BPM_INST_ROUTER (
                                 INST_ID_             VARCHAR(64)          NOT NULL,
                                 TABLE_ID_            INT4                 NULL,
                                 CONSTRAINT PK_BPM_INST_ROUTER PRIMARY KEY (INST_ID_)
);

COMMENT ON TABLE BPM_INST_ROUTER IS
'??????????????????';

COMMENT ON COLUMN BPM_INST_ROUTER.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_INST_ROUTER.TABLE_ID_ IS
'???ID';

/*==============================================================*/
/* Table: BPM_MOBILE_TAG                                        */
/*==============================================================*/
CREATE TABLE BPM_MOBILE_TAG (
                                TAGID_               VARCHAR(64)          NOT NULL,
                                CID_                 VARCHAR(64)          NULL,
                                MOBILE_TYPE_         VARCHAR(20)          NULL,
                                ISBAN_               VARCHAR(20)          NULL,
                                USER_ID_             VARCHAR(40)          NULL,
                                TAG_                 VARCHAR(64)          NULL,
                                TENANT_ID_           VARCHAR(64)          NULL,
                                CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                CREATE_BY_           VARCHAR(64)          NULL,
                                CREATE_TIME_         DATE                 NULL,
                                UPDATE_BY_           VARCHAR(64)          NULL,
                                UPDATE_TIME_         DATE                 NULL,
                                CONSTRAINT PK_BPM_MOBILE_TAG PRIMARY KEY (TAGID_)
);

COMMENT ON TABLE BPM_MOBILE_TAG IS
'????????????????????????';

COMMENT ON COLUMN BPM_MOBILE_TAG.TAGID_ IS
'??????';

COMMENT ON COLUMN BPM_MOBILE_TAG.CID_ IS
'??????????????????APP?????????';

COMMENT ON COLUMN BPM_MOBILE_TAG.MOBILE_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_MOBILE_TAG.ISBAN_ IS
'??????????????????';

COMMENT ON COLUMN BPM_MOBILE_TAG.USER_ID_ IS
'????????????';

COMMENT ON COLUMN BPM_MOBILE_TAG.TAG_ IS
'CID????????????';

COMMENT ON COLUMN BPM_MOBILE_TAG.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_MOBILE_TAG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_MOBILE_TAG.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_MOBILE_TAG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_MOBILE_TAG.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_MOBILE_TAG.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_OPINION_LIB                                       */
/*==============================================================*/
CREATE TABLE BPM_OPINION_LIB (
                                 OP_ID_               VARCHAR(64)          NOT NULL,
                                 USER_ID_             VARCHAR(64)          NULL,
                                 OP_TEXT_             VARCHAR(255)         NULL,
                                 TENANT_ID_           VARCHAR(64)          NULL,
                                 CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                 CREATE_BY_           VARCHAR(64)          NULL,
                                 CREATE_TIME_         DATE                 NULL,
                                 UPDATE_BY_           VARCHAR(64)          NULL,
                                 UPDATE_TIME_         DATE                 NULL,
                                 CONSTRAINT PK_BPM_OPINION_LIB PRIMARY KEY (OP_ID_)
);

COMMENT ON TABLE BPM_OPINION_LIB IS
'???????????????';

COMMENT ON COLUMN BPM_OPINION_LIB.OP_ID_ IS
'??????';

COMMENT ON COLUMN BPM_OPINION_LIB.USER_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_OPINION_LIB.OP_TEXT_ IS
'????????????';

COMMENT ON COLUMN BPM_OPINION_LIB.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_OPINION_LIB.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_OPINION_LIB.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_OPINION_LIB.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_OPINION_LIB.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_OPINION_LIB.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_REMIND_HISTORY                                    */
/*==============================================================*/
CREATE TABLE BPM_REMIND_HISTORY (
                                    ID_                  VARCHAR(64)          NOT NULL,
                                    REMINDER_INST_ID_    VARCHAR(64)          NULL,
                                    REMIND_TYPE_         VARCHAR(40)          NULL,
                                    INST_ID_             VARCHAR(64)          NULL,
                                    TENANT_ID_           VARCHAR(64)          NULL,
                                    CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                    CREATE_BY_           VARCHAR(64)          NULL,
                                    CREATE_TIME_         DATE                 NULL,
                                    UPDATE_BY_           VARCHAR(64)          NULL,
                                    UPDATE_TIME_         DATE                 NULL,
                                    CONSTRAINT PK_BPM_REMIND_HISTORY PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_REMIND_HISTORY IS
'????????????';

COMMENT ON COLUMN BPM_REMIND_HISTORY.ID_ IS
'??????';

COMMENT ON COLUMN BPM_REMIND_HISTORY.REMINDER_INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_REMIND_HISTORY.REMIND_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_REMIND_HISTORY.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_REMIND_HISTORY.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_REMIND_HISTORY.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_REMIND_HISTORY.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_REMIND_HISTORY.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_REMIND_HISTORY.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_REMIND_HISTORY.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_REMIND_INST                                       */
/*==============================================================*/
CREATE TABLE BPM_REMIND_INST (
                                 ID_                  VARCHAR(64)          NOT NULL,
                                 INST_ID_             VARCHAR(64)          NULL,
                                 TASK_ID_             VARCHAR(64)          NULL,
                                 NAME_                VARCHAR(64)          NULL,
                                 ACTION_              VARCHAR(20)          NULL,
                                 EXPIRE_DATE_         DATE                 NULL,
                                 SCRIPT_              VARCHAR(1000)        NULL,
                                 NOTIFY_TYPE_         VARCHAR(40)          NULL,
                                 TIME_TO_SEND_        DATE                 NULL,
                                 SEND_TIMES_          INT4                 NULL,
                                 SEND_INTERVAL_       INT4                 NULL,
                                 STATUS_              VARCHAR(40)          NULL,
                                 TENANT_ID_           VARCHAR(64)          NULL,
                                 CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                 CREATE_BY_           VARCHAR(64)          NULL,
                                 CREATE_TIME_         DATE                 NULL,
                                 UPDATE_BY_           VARCHAR(64)          NULL,
                                 UPDATE_TIME_         DATE                 NULL,
                                 CONSTRAINT PK_BPM_REMIND_INST PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_REMIND_INST IS
'????????????';

COMMENT ON COLUMN BPM_REMIND_INST.ID_ IS
'??????';

COMMENT ON COLUMN BPM_REMIND_INST.INST_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_REMIND_INST.TASK_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_REMIND_INST.NAME_ IS
'??????';

COMMENT ON COLUMN BPM_REMIND_INST.ACTION_ IS
'????????????';

COMMENT ON COLUMN BPM_REMIND_INST.EXPIRE_DATE_ IS
'??????';

COMMENT ON COLUMN BPM_REMIND_INST.SCRIPT_ IS
'??????????????????';

COMMENT ON COLUMN BPM_REMIND_INST.NOTIFY_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_REMIND_INST.TIME_TO_SEND_ IS
'???????????????????????????';

COMMENT ON COLUMN BPM_REMIND_INST.SEND_TIMES_ IS
'????????????';

COMMENT ON COLUMN BPM_REMIND_INST.SEND_INTERVAL_ IS
'??????????????????';

COMMENT ON COLUMN BPM_REMIND_INST.STATUS_ IS
'??????';

COMMENT ON COLUMN BPM_REMIND_INST.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_REMIND_INST.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_REMIND_INST.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_REMIND_INST.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_REMIND_INST.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_REMIND_INST.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_REMIND_TASK                                       */
/*==============================================================*/
CREATE  INDEX IDX_REMIND_TASK ON BPM_REMIND_INST (
                                                  TASK_ID_
    );

/*==============================================================*/
/* Index: IDX_REMIND_INST                                       */
/*==============================================================*/
CREATE  INDEX IDX_REMIND_INST ON BPM_REMIND_INST (
                                                  INST_ID_
    );

/*==============================================================*/
/* Table: BPM_RU_PATH                                           */
/*==============================================================*/
CREATE TABLE BPM_RU_PATH (
                             PATH_ID_             VARCHAR(64)          NOT NULL,
                             INST_ID_             VARCHAR(64)          NOT NULL,
                             DEF_ID_              VARCHAR(64)          NULL,
                             ACT_DEF_ID_          VARCHAR(64)          NOT NULL,
                             ACT_INST_ID_         VARCHAR(64)          NOT NULL,
                             NODE_ID_             VARCHAR(255)         NOT NULL,
                             NODE_NAME_           VARCHAR(255)         NULL,
                             NODE_TYPE_           VARCHAR(50)          NULL,
                             START_TIME_          DATE                 NOT NULL,
                             END_TIME_            DATE                 NULL,
                             ASSIGNEE_            VARCHAR(64)          NULL,
                             TO_USER_ID_          VARCHAR(64)          NULL,
                             USER_IDS_            VARCHAR(300)         NULL,
                             MULTIPLE_TYPE_       VARCHAR(20)          NULL,
                             EXECUTION_ID_        VARCHAR(64)          NULL,
                             PARENT_ID_           VARCHAR(64)          NULL,
                             LEVEL_               INT4                 NULL,
                             OUT_TRAN_ID_         VARCHAR(255)         NULL,
                             TOKEN_               VARCHAR(255)         NULL,
                             JUMP_TYPE_           VARCHAR(50)          NULL,
                             NEXT_JUMP_TYPE_      VARCHAR(50)          NULL,
                             REF_PATH_ID_         VARCHAR(64)          NULL,
                             TENANT_ID_           VARCHAR(64)          NULL,
                             CREATE_DEP_ID_       VARCHAR(64)          NULL,
                             CREATE_BY_           VARCHAR(64)          NULL,
                             CREATE_TIME_         DATE                 NULL,
                             UPDATE_BY_           VARCHAR(64)          NULL,
                             UPDATE_TIME_         DATE                 NULL,
                             CONSTRAINT PK_BPM_RU_PATH PRIMARY KEY (PATH_ID_)
);

COMMENT ON TABLE BPM_RU_PATH IS
'????????????????????????';

COMMENT ON COLUMN BPM_RU_PATH.PATH_ID_ IS
'??????';

COMMENT ON COLUMN BPM_RU_PATH.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_RU_PATH.DEF_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_RU_PATH.ACT_DEF_ID_ IS
'Act??????ID';

COMMENT ON COLUMN BPM_RU_PATH.ACT_INST_ID_ IS
'Act??????ID';

COMMENT ON COLUMN BPM_RU_PATH.NODE_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_RU_PATH.NODE_NAME_ IS
'????????????';

COMMENT ON COLUMN BPM_RU_PATH.NODE_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_RU_PATH.START_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_RU_PATH.END_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_RU_PATH.ASSIGNEE_ IS
'?????????ID';

COMMENT ON COLUMN BPM_RU_PATH.TO_USER_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_RU_PATH.USER_IDS_ IS
'????????????IDS';

COMMENT ON COLUMN BPM_RU_PATH.MULTIPLE_TYPE_ IS
'??????????????????';

COMMENT ON COLUMN BPM_RU_PATH.EXECUTION_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_RU_PATH.PARENT_ID_ IS
'???ID';

COMMENT ON COLUMN BPM_RU_PATH.LEVEL_ IS
'??????';

COMMENT ON COLUMN BPM_RU_PATH.OUT_TRAN_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_RU_PATH.TOKEN_ IS
'????????????';

COMMENT ON COLUMN BPM_RU_PATH.JUMP_TYPE_ IS
'????????????????????????
????????????
????????????
????????????';

COMMENT ON COLUMN BPM_RU_PATH.NEXT_JUMP_TYPE_ IS
'?????????????????????';

COMMENT ON COLUMN BPM_RU_PATH.REF_PATH_ID_ IS
'????????????ID
??????????????????????????????????????????????????????????????????????????????????????????????????????????????????';

COMMENT ON COLUMN BPM_RU_PATH.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_RU_PATH.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_RU_PATH.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_RU_PATH.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_RU_PATH.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_RU_PATH.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_RUPATH_ACTINSTID                                  */
/*==============================================================*/
CREATE  INDEX IDX_RUPATH_ACTINSTID ON BPM_RU_PATH (
                                                   ACT_INST_ID_
    );

/*==============================================================*/
/* Index: IDX_RUPATH_INSTID                                     */
/*==============================================================*/
CREATE  INDEX IDX_RUPATH_INSTID ON BPM_RU_PATH (
                                                INST_ID_
    );

/*==============================================================*/
/* Table: BPM_SIGN_DATA                                         */
/*==============================================================*/
CREATE TABLE BPM_SIGN_DATA (
                               DATA_ID_             VARCHAR(64)          NOT NULL,
                               ACT_DEF_ID_          VARCHAR(64)          NULL,
                               ACT_INST_ID_         VARCHAR(64)          NULL,
                               NODE_ID_             VARCHAR(64)          NULL,
                               USER_ID_             VARCHAR(64)          NULL,
                               VOTE_STATUS_         VARCHAR(40)          NULL,
                               TENANT_ID_           VARCHAR(64)          NULL,
                               CREATE_DEP_ID_       VARCHAR(64)          NULL,
                               CREATE_BY_           VARCHAR(64)          NULL,
                               CREATE_TIME_         DATE                 NULL,
                               UPDATE_BY_           VARCHAR(64)          NULL,
                               UPDATE_TIME_         DATE                 NULL,
                               CONSTRAINT PK_BPM_SIGN_DATA PRIMARY KEY (DATA_ID_)
);

COMMENT ON TABLE BPM_SIGN_DATA IS
'????????????';

COMMENT ON COLUMN BPM_SIGN_DATA.DATA_ID_ IS
'??????';

COMMENT ON COLUMN BPM_SIGN_DATA.ACT_DEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_SIGN_DATA.ACT_INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_SIGN_DATA.NODE_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_SIGN_DATA.USER_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_SIGN_DATA.VOTE_STATUS_ IS
'????????????';

COMMENT ON COLUMN BPM_SIGN_DATA.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_SIGN_DATA.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_SIGN_DATA.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_SIGN_DATA.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_SIGN_DATA.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_SIGN_DATA.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_SIGNDATA_ACTINST                                  */
/*==============================================================*/
CREATE  INDEX IDX_SIGNDATA_ACTINST ON BPM_SIGN_DATA (
                                                     ACT_INST_ID_
    );

/*==============================================================*/
/* Table: BPM_TASK                                              */
/*==============================================================*/
CREATE TABLE BPM_TASK (
                          TASK_ID_             VARCHAR(64)          NOT NULL,
                          ACT_TASK_ID_         VARCHAR(64)          NULL,
                          TREE_ID_             VARCHAR(64)          NULL,
                          NAME_                VARCHAR(100)         NULL,
                          KEY_                 VARCHAR(64)          NULL,
                          BILL_TYPE_           VARCHAR(64)          NULL,
                          BILL_NO_             VARCHAR(64)          NULL,
                          BUS_KEY_             VARCHAR(64)          NULL,
                          DESCP_               VARCHAR(255)         NULL,
                          SUBJECT_             VARCHAR(512)         NULL,
                          OWNER_               VARCHAR(64)          NULL,
                          ASSIGNEE_            VARCHAR(64)          NULL,
                          ACT_INST_ID_         VARCHAR(64)          NULL,
                          ACT_DEF_ID_          VARCHAR(64)          NULL,
                          DEF_ID_              VARCHAR(64)          NULL,
                          INST_ID_             VARCHAR(64)          NULL,
                          STATUS_              VARCHAR(64)          NULL,
                          PRIORITY_            VARCHAR(64)          NULL,
                          EXPIRED_TIME_        DATE                 NULL,
                          TASK_TYPE_           VARCHAR(64)          NULL,
                          EXECUTION_ID_        VARCHAR(64)          NULL,
                          PARENT_ID_           VARCHAR(64)          NULL,
                          EXECUTOR_            VARCHAR(20)          NULL,
                          PRE_TASK_ID_         VARCHAR(64)          NULL,
                          TENANT_ID_           VARCHAR(64)          NULL,
                          CREATE_DEP_ID_       VARCHAR(64)          NULL,
                          CREATE_BY_           VARCHAR(64)          NULL,
                          CREATE_TIME_         DATE                 NULL,
                          UPDATE_BY_           VARCHAR(64)          NULL,
                          UPDATE_TIME_         DATE                 NULL,
                          CONSTRAINT PK_BPM_TASK PRIMARY KEY (TASK_ID_)
);

COMMENT ON TABLE BPM_TASK IS
'????????????';

COMMENT ON COLUMN BPM_TASK.TASK_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_TASK.ACT_TASK_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TASK.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_TASK.NAME_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK.KEY_ IS
'??????Key';

COMMENT ON COLUMN BPM_TASK.BILL_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK.BILL_NO_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK.BUS_KEY_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK.DESCP_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK.SUBJECT_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK.OWNER_ IS
'???????????????';

COMMENT ON COLUMN BPM_TASK.ASSIGNEE_ IS
'???????????????';

COMMENT ON COLUMN BPM_TASK.ACT_INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TASK.ACT_DEF_ID_ IS
'ACT????????????ID';

COMMENT ON COLUMN BPM_TASK.DEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TASK.INST_ID_ IS
'??????????????????ID';

COMMENT ON COLUMN BPM_TASK.STATUS_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK.PRIORITY_ IS
'???????????????';

COMMENT ON COLUMN BPM_TASK.EXPIRED_TIME_ IS
'??????????????????';

COMMENT ON COLUMN BPM_TASK.TASK_TYPE_ IS
'????????????
FLOW_TASK=????????????
MAN_TASK=????????????(??????)';

COMMENT ON COLUMN BPM_TASK.EXECUTION_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_TASK.PARENT_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TASK.EXECUTOR_ IS
'?????????????????????';

COMMENT ON COLUMN BPM_TASK.PRE_TASK_ID_ IS
'????????????KEY';

COMMENT ON COLUMN BPM_TASK.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_TASK.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TASK.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TASK.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TASK.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_TASK_ACTTASK                                      */
/*==============================================================*/
CREATE  INDEX IDX_TASK_ACTTASK ON BPM_TASK (
                                            ACT_TASK_ID_
    );

/*==============================================================*/
/* Table: BPM_TASK_TRANSFER                                     */
/*==============================================================*/
CREATE TABLE BPM_TASK_TRANSFER (
                                   ID_                  VARCHAR(64)          NOT NULL,
                                   TREE_ID_             VARCHAR(64)          NULL,
                                   INST_ID_             VARCHAR(64)          NULL,
                                   OWNER_ID_            VARCHAR(64)          NULL,
                                   SUBJECT_             VARCHAR(128)         NULL,
                                   TASK_ID_             VARCHAR(64)          NULL,
                                   TO_USER_ID_          VARCHAR(64)          NULL,
                                   TYPE_                VARCHAR(20)          NULL,
                                   TENANT_ID_           VARCHAR(64)          NULL,
                                   CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                   CREATE_BY_           VARCHAR(64)          NULL,
                                   CREATE_TIME_         DATE                 NULL,
                                   UPDATE_BY_           VARCHAR(64)          NULL,
                                   UPDATE_TIME_         DATE                 NULL,
                                   CONSTRAINT PK_BPM_TASK_TRANSFER PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_TASK_TRANSFER IS
'????????????????????????';

COMMENT ON COLUMN BPM_TASK_TRANSFER.ID_ IS
'??????';

COMMENT ON COLUMN BPM_TASK_TRANSFER.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_TASK_TRANSFER.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TASK_TRANSFER.OWNER_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TASK_TRANSFER.SUBJECT_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK_TRANSFER.TASK_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_TASK_TRANSFER.TO_USER_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TASK_TRANSFER.TYPE_ IS
'??????(trans,agent)';

COMMENT ON COLUMN BPM_TASK_TRANSFER.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_TASK_TRANSFER.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TASK_TRANSFER.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TASK_TRANSFER.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK_TRANSFER.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TASK_TRANSFER.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_TRANS_TOUSER                                      */
/*==============================================================*/
CREATE  INDEX IDX_TRANS_TOUSER ON BPM_TASK_TRANSFER (
                                                     TO_USER_ID_
    );

/*==============================================================*/
/* Index: IDX_TRANS_OWNER                                       */
/*==============================================================*/
CREATE  INDEX IDX_TRANS_OWNER ON BPM_TASK_TRANSFER (
                                                    OWNER_ID_
    );

/*==============================================================*/
/* Table: BPM_TASK_USER                                         */
/*==============================================================*/
CREATE TABLE BPM_TASK_USER (
                               ID_                  VARCHAR(64)          NOT NULL,
                               TASK_ID_             VARCHAR(64)          NOT NULL,
                               USER_ID_             VARCHAR(64)          NULL,
                               INST_ID_             VARCHAR(64)          NULL,
                               GROUP_ID_            VARCHAR(64)          NULL,
                               USER_TYPE_           VARCHAR(64)          NULL,
                               PART_TYPE_           VARCHAR(64)          NULL,
                               IS_READ_             VARCHAR(20)          NULL,
                               TENANT_ID_           VARCHAR(64)          NULL,
                               CREATE_DEP_ID_       VARCHAR(64)          NULL,
                               CREATE_BY_           VARCHAR(64)          NULL,
                               CREATE_TIME_         DATE                 NULL,
                               UPDATE_BY_           VARCHAR(64)          NULL,
                               UPDATE_TIME_         DATE                 NULL,
                               CONSTRAINT PK_BPM_TASK_USER PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_TASK_USER IS
'?????????????????????';

COMMENT ON COLUMN BPM_TASK_USER.ID_ IS
'??????';

COMMENT ON COLUMN BPM_TASK_USER.TASK_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_TASK_USER.USER_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_TASK_USER.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TASK_USER.GROUP_ID_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TASK_USER.USER_TYPE_ IS
'????????????
USER=??????
GROUP=?????????
';

COMMENT ON COLUMN BPM_TASK_USER.PART_TYPE_ IS
'????????????
?????????=ASSIGNEE
?????????=COPY
?????????=CANDIDATE';

COMMENT ON COLUMN BPM_TASK_USER.IS_READ_ IS
'????????????
YES,NO';

COMMENT ON COLUMN BPM_TASK_USER.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_TASK_USER.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TASK_USER.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TASK_USER.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_TASK_USER.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TASK_USER.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_TASKUSER_TASK                                     */
/*==============================================================*/
CREATE  INDEX IDX_TASKUSER_TASK ON BPM_TASK_USER (
                                                  TASK_ID_
    );

/*==============================================================*/
/* Table: BPM_TEMPORARY_OPINION                                 */
/*==============================================================*/
CREATE TABLE BPM_TEMPORARY_OPINION (
                                       ID_                  VARCHAR(64)          NOT NULL,
                                       TASK_ID_             VARCHAR(64)          NULL,
                                       OPINION_             VARCHAR(1000)        NULL,
                                       TENANT_ID_           VARCHAR(64)          NULL,
                                       CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                       CREATE_BY_           VARCHAR(64)          NULL,
                                       CREATE_TIME_         DATE                 NULL,
                                       UPDATE_BY_           VARCHAR(64)          NULL,
                                       UPDATE_TIME_         DATE                 NULL,
                                       CONSTRAINT PK_BPM_TEMPORARY_OPINION PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_TEMPORARY_OPINION IS
'?????????????????????';

COMMENT ON COLUMN BPM_TEMPORARY_OPINION.ID_ IS
'??????';

COMMENT ON COLUMN BPM_TEMPORARY_OPINION.TASK_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_TEMPORARY_OPINION.OPINION_ IS
'??????';

COMMENT ON COLUMN BPM_TEMPORARY_OPINION.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_TEMPORARY_OPINION.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TEMPORARY_OPINION.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TEMPORARY_OPINION.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_TEMPORARY_OPINION.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TEMPORARY_OPINION.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_TRANSFER                                          */
/*==============================================================*/
CREATE TABLE BPM_TRANSFER (
                              ID_                  VARCHAR(64)          NOT NULL,
                              TASK_ID_             VARCHAR(64)          NULL,
                              APPROVE_TYPE_        VARCHAR(40)          NULL,
                              TRANSFER_TYPE_       VARCHAR(40)          NULL,
                              COMPLETE_TYPE_       INT4                 NULL,
                              COUNT_               INT4                 NULL,
                              COMPLETE_COUNT_      INT4                 NULL,
                              COMPLETE_SETTING_    INT4                 NULL,
                              NOTICE_TYPE_         VARCHAR(40)          NULL,
                              COMPLETE_JUDGE_TYPE_ VARCHAR(40)          NULL,
                              TASK_USER_ID_        VARCHAR(2000)        NULL,
                              TASK_USER_IDX_       INT4                 NULL,
                              INST_ID_             VARCHAR(64)          NULL,
                              TENANT_ID_           VARCHAR(64)          NULL,
                              CREATE_DEP_ID_       VARCHAR(64)          NULL,
                              CREATE_BY_           VARCHAR(64)          NULL,
                              CREATE_TIME_         DATE                 NULL,
                              UPDATE_BY_           VARCHAR(64)          NULL,
                              UPDATE_TIME_         DATE                 NULL,
                              CONSTRAINT PK_BPM_TRANSFER PRIMARY KEY (ID_)
);

COMMENT ON COLUMN BPM_TRANSFER.ID_ IS
'??????';

COMMENT ON COLUMN BPM_TRANSFER.TASK_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_TRANSFER.APPROVE_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_TRANSFER.TRANSFER_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_TRANSFER.COMPLETE_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_TRANSFER.COUNT_ IS
'??????';

COMMENT ON COLUMN BPM_TRANSFER.COMPLETE_COUNT_ IS
'????????????';

COMMENT ON COLUMN BPM_TRANSFER.COMPLETE_SETTING_ IS
'??????????????????';

COMMENT ON COLUMN BPM_TRANSFER.NOTICE_TYPE_ IS
'????????????';

COMMENT ON COLUMN BPM_TRANSFER.COMPLETE_JUDGE_TYPE_ IS
'??????????????????';

COMMENT ON COLUMN BPM_TRANSFER.TASK_USER_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TRANSFER.TASK_USER_IDX_ IS
'??????????????????';

COMMENT ON COLUMN BPM_TRANSFER.INST_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TRANSFER.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_TRANSFER.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TRANSFER.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TRANSFER.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_TRANSFER.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TRANSFER.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: BPM_TRANSFER_LOG                                      */
/*==============================================================*/
CREATE TABLE BPM_TRANSFER_LOG (
                                  ID_                  VARCHAR(64)          NOT NULL,
                                  ASSIGNEE_            VARCHAR(64)          NULL,
                                  REMARK_              VARCHAR(255)         NULL,
                                  STATUS_              VARCHAR(40)          NULL,
                                  TASK_ID_             VARCHAR(40)          NULL,
                                  TRANS_TASK_ID_       VARCHAR(64)          NULL,
                                  TENANT_ID_           VARCHAR(64)          NULL,
                                  CREATE_DEP_ID_       VARCHAR(64)          NULL,
                                  CREATE_BY_           VARCHAR(64)          NULL,
                                  CREATE_TIME_         DATE                 NULL,
                                  UPDATE_BY_           VARCHAR(64)          NULL,
                                  UPDATE_TIME_         DATE                 NULL,
                                  CONSTRAINT PK_BPM_TRANSFER_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE BPM_TRANSFER_LOG IS
'?????????????????????';

COMMENT ON COLUMN BPM_TRANSFER_LOG.ID_ IS
'??????';

COMMENT ON COLUMN BPM_TRANSFER_LOG.ASSIGNEE_ IS
'?????????';

COMMENT ON COLUMN BPM_TRANSFER_LOG.REMARK_ IS
'????????????';

COMMENT ON COLUMN BPM_TRANSFER_LOG.STATUS_ IS
'????????????';

COMMENT ON COLUMN BPM_TRANSFER_LOG.TASK_ID_ IS
'??????ID';

COMMENT ON COLUMN BPM_TRANSFER_LOG.TRANS_TASK_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TRANSFER_LOG.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN BPM_TRANSFER_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN BPM_TRANSFER_LOG.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TRANSFER_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN BPM_TRANSFER_LOG.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN BPM_TRANSFER_LOG.UPDATE_TIME_ IS
'????????????';

commit ;

