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
/* Table: OS_DD_AGENT                                           */
/*==============================================================*/
CREATE TABLE OS_DD_AGENT  (
                              ID_                  VARCHAR2(64)                    NOT NULL,
                              NAME_                VARCHAR2(64)                    NOT NULL,
                              TYPE_                VARCHAR2(40),
                              APP_KEY_             VARCHAR2(64),
                              AGENT_ID_            VARCHAR2(64),
                              CORP_ID_             VARCHAR2(64),
                              PC_HOMEPAGE_         VARCHAR2(255),
                              ADMIN_PAGE_          VARCHAR2(255),
                              H5_HOMEPAGE_         VARCHAR2(255),
                              IS_DEFAULT_          INTEGER,
                              SECRET_              VARCHAR2(64),
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_DEP_ID_       VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           INTEGER,
                              UPDATE_TIME_         DATE,
                              CONSTRAINT PK_OS_DD_AGENT PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_DD_AGENT IS
'???????????????';

COMMENT ON COLUMN OS_DD_AGENT.ID_ IS
'??????';

COMMENT ON COLUMN OS_DD_AGENT.NAME_ IS
'????????????';

COMMENT ON COLUMN OS_DD_AGENT.TYPE_ IS
'????????????';

COMMENT ON COLUMN OS_DD_AGENT.APP_KEY_ IS
'??????KEY';

COMMENT ON COLUMN OS_DD_AGENT.AGENT_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_DD_AGENT.CORP_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_DD_AGENT.PC_HOMEPAGE_ IS
'PC??????';

COMMENT ON COLUMN OS_DD_AGENT.ADMIN_PAGE_ IS
'????????????';

COMMENT ON COLUMN OS_DD_AGENT.H5_HOMEPAGE_ IS
'H5??????';

COMMENT ON COLUMN OS_DD_AGENT.IS_DEFAULT_ IS
'????????????';

COMMENT ON COLUMN OS_DD_AGENT.SECRET_ IS
'??????';

COMMENT ON COLUMN OS_DD_AGENT.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_DD_AGENT.CREATE_DEP_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_DD_AGENT.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN OS_DD_AGENT.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_DD_AGENT.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN OS_DD_AGENT.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_DIMENSION                                          */
/*==============================================================*/
CREATE TABLE OS_DIMENSION  (
                               DIM_ID_              VARCHAR2(64)                    NOT NULL,
                               NAME_                VARCHAR2(40)                    NOT NULL,
                               CODE_                VARCHAR2(64)                    NOT NULL,
                               IS_SYSTEM_           VARCHAR2(10)                    NOT NULL,
                               STATUS_              VARCHAR2(40)                    NOT NULL,
                               SN_                  INTEGER                         NOT NULL,
                               SHOW_TYPE_           VARCHAR2(40)                    NOT NULL,
                               IS_GRANT_            VARCHAR2(10),
                               DESC_                VARCHAR2(255),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_OS_DIMENSION PRIMARY KEY (DIM_ID_)
);

COMMENT ON TABLE OS_DIMENSION IS
'???????????????';

COMMENT ON COLUMN OS_DIMENSION.DIM_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_DIMENSION.NAME_ IS
'????????????';

COMMENT ON COLUMN OS_DIMENSION.CODE_ IS
'??????????????????';

COMMENT ON COLUMN OS_DIMENSION.IS_SYSTEM_ IS
'????????????????????????';

COMMENT ON COLUMN OS_DIMENSION.STATUS_ IS
'??????
actived ????????????locked ?????????????????????deleted ?????????';

COMMENT ON COLUMN OS_DIMENSION.SN_ IS
'?????????';

COMMENT ON COLUMN OS_DIMENSION.SHOW_TYPE_ IS
'??????????????????
tree=???????????????flat=????????????';

COMMENT ON COLUMN OS_DIMENSION.IS_GRANT_ IS
'??????????????????';

COMMENT ON COLUMN OS_DIMENSION.DESC_ IS
'??????';

COMMENT ON COLUMN OS_DIMENSION.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_DIMENSION.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_DIMENSION.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_DIMENSION.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_DIMENSION.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_DIMENSION.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_GRADE_ADMIN                                        */
/*==============================================================*/
CREATE TABLE OS_GRADE_ADMIN  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 GROUP_ID_            VARCHAR2(64),
                                 USER_ID_             VARCHAR2(64),
                                 FULLNAME_            VARCHAR2(64),
                                 PARENT_ID_           VARCHAR2(64),
                                 DEPTH_               INTEGER,
                                 PATH_                VARCHAR2(255),
                                 SN_                  INTEGER,
                                 CHILDS_              INTEGER,
                                 TENANT_ID_           VARCHAR2(64),
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 CONSTRAINT PK_OS_GRADE_ADMIN PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_GRADE_ADMIN IS
'??????????????????';

COMMENT ON COLUMN OS_GRADE_ADMIN.ID_ IS
'??????';

COMMENT ON COLUMN OS_GRADE_ADMIN.GROUP_ID_ IS
'????????????';

COMMENT ON COLUMN OS_GRADE_ADMIN.USER_ID_ IS
'?????????ID';

COMMENT ON COLUMN OS_GRADE_ADMIN.FULLNAME_ IS
'???????????????';

COMMENT ON COLUMN OS_GRADE_ADMIN.PARENT_ID_ IS
'???ID';

COMMENT ON COLUMN OS_GRADE_ADMIN.DEPTH_ IS
'??????';

COMMENT ON COLUMN OS_GRADE_ADMIN.PATH_ IS
'??????';

COMMENT ON COLUMN OS_GRADE_ADMIN.SN_ IS
'??????';

COMMENT ON COLUMN OS_GRADE_ADMIN.CHILDS_ IS
'???????????????';

COMMENT ON COLUMN OS_GRADE_ADMIN.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_GRADE_ADMIN.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_GRADE_ADMIN.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_GRADE_ADMIN.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_GRADE_ADMIN.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_GRADE_ADMIN.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_GRADE_ROLE                                         */
/*==============================================================*/
CREATE TABLE OS_GRADE_ROLE  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                ADMIN_ID_            VARCHAR2(64),
                                GROUP_ID_            VARCHAR2(64),
                                NAME_                VARCHAR2(20),
                                TENANT_ID_           VARCHAR2(64),
                                CREATE_DEP_ID_       VARCHAR2(64),
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                UPDATE_BY_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                CONSTRAINT PK_OS_GRADE_ROLE PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_GRADE_ROLE IS
'?????????????????????';

COMMENT ON COLUMN OS_GRADE_ROLE.ID_ IS
'??????';

COMMENT ON COLUMN OS_GRADE_ROLE.ADMIN_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_GRADE_ROLE.GROUP_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_GRADE_ROLE.NAME_ IS
'?????????';

COMMENT ON COLUMN OS_GRADE_ROLE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_GRADE_ROLE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_GRADE_ROLE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_GRADE_ROLE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_GRADE_ROLE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_GRADE_ROLE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_GROUP                                              */
/*==============================================================*/
CREATE TABLE OS_GROUP  (
                           GROUP_ID_            VARCHAR2(64)                    NOT NULL,
                           DIM_ID_              VARCHAR2(64)                    NOT NULL,
                           NAME_                VARCHAR2(64)                    NOT NULL,
                           KEY_                 VARCHAR2(64)                    NOT NULL,
                           RANK_LEVEL_          INTEGER,
                           STATUS_              VARCHAR2(40)                    NOT NULL,
                           DESCP_               VARCHAR2(255),
                           SN_                  INTEGER                         NOT NULL,
                           PARENT_ID_           VARCHAR2(64),
                           PATH_                VARCHAR2(200),
                           AREA_CODE_           VARCHAR2(50),
                           FORM_                VARCHAR2(20),
                           SYNC_WX_             INTEGER,
                           WX_PARENT_PID_       INTEGER,
                           WX_PID_              INTEGER,
                           IS_DEFAULT_          VARCHAR2(40),
                           DD_PARENT_ID_        VARCHAR2(64),
                           DD_ID_               VARCHAR2(64),
                           IS_LEAF_             CHAR(1),
                           TENANT_ID_           VARCHAR2(64),
                           CREATE_DEP_ID_       VARCHAR2(64),
                           CREATE_BY_           VARCHAR2(64),
                           CREATE_TIME_         DATE,
                           UPDATE_BY_           VARCHAR2(64),
                           UPDATE_TIME_         DATE,
                           CONSTRAINT PK_OS_GROUP PRIMARY KEY (GROUP_ID_)
);

COMMENT ON TABLE OS_GROUP IS
'???????????????';

COMMENT ON COLUMN OS_GROUP.GROUP_ID_ IS
'?????????ID';

COMMENT ON COLUMN OS_GROUP.DIM_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_GROUP.NAME_ IS
'???????????????';

COMMENT ON COLUMN OS_GROUP.KEY_ IS
'?????????????????????';

COMMENT ON COLUMN OS_GROUP.RANK_LEVEL_ IS
'????????????????????????';

COMMENT ON COLUMN OS_GROUP.STATUS_ IS
'??????
inactive ????????????actived ????????????locked ?????????deleted ?????????';

COMMENT ON COLUMN OS_GROUP.DESCP_ IS
'??????';

COMMENT ON COLUMN OS_GROUP.SN_ IS
'?????????';

COMMENT ON COLUMN OS_GROUP.PARENT_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_GROUP.PATH_ IS
'??????';

COMMENT ON COLUMN OS_GROUP.AREA_CODE_ IS
'????????????';

COMMENT ON COLUMN OS_GROUP.FORM_ IS
'??????
sysem,????????????,import??????,grade,???????????????';

COMMENT ON COLUMN OS_GROUP.SYNC_WX_ IS
'???????????????';

COMMENT ON COLUMN OS_GROUP.WX_PARENT_PID_ IS
'???????????????????????????ID';

COMMENT ON COLUMN OS_GROUP.WX_PID_ IS
'????????????????????????ID';

COMMENT ON COLUMN OS_GROUP.IS_DEFAULT_ IS
'???????????????????????????????????????????????????';

COMMENT ON COLUMN OS_GROUP.DD_PARENT_ID_ IS
'?????????ID';

COMMENT ON COLUMN OS_GROUP.DD_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_GROUP.IS_LEAF_ IS
'??????????????? Y/N';

COMMENT ON COLUMN OS_GROUP.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_GROUP.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_GROUP.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_GROUP.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_GROUP.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_GROUP.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_GROUP_API                                          */
/*==============================================================*/
CREATE TABLE OS_GROUP_API  (
                               ID_                  VARCHAR2(64)                    NOT NULL,
                               GROUP_ID_            VARCHAR2(64),
                               API_PATH_            VARCHAR2(100),
                               API_METHOD_          VARCHAR2(64),
                               SERVICE_             VARCHAR2(64),
                               CONSTRAINT PK_OS_GROUP_API PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_GROUP_API IS
'?????????API';

COMMENT ON COLUMN OS_GROUP_API.ID_ IS
'??????';

COMMENT ON COLUMN OS_GROUP_API.GROUP_ID_ IS
'?????????Id';

COMMENT ON COLUMN OS_GROUP_API.API_PATH_ IS
'API ??????';

COMMENT ON COLUMN OS_GROUP_API.API_METHOD_ IS
'HTTP??????';

COMMENT ON COLUMN OS_GROUP_API.SERVICE_ IS
'???????????????';

/*==============================================================*/
/* Table: OS_GROUP_MENU                                         */
/*==============================================================*/
CREATE TABLE OS_GROUP_MENU  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                MENU_ID_             VARCHAR2(64),
                                GROUP_ID_            VARCHAR2(64),
                                APP_ID_              VARCHAR2(64),
                                NAME_                VARCHAR2(60),
                                ICON_PC_             VARCHAR2(100),
                                ICON_PIC_            VARCHAR2(100),
                                ICON_APP_            VARCHAR2(50),
                                PARENT_ID_           VARCHAR2(64)                    NOT NULL,
                                PATH_                VARCHAR2(256),
                                SN_                  INTEGER,
                                SHOW_TYPE_           VARCHAR2(20),
                                MENU_KEY_            VARCHAR2(50),
                                MENU_TYPE_           VARCHAR2(20),
                                COMPONENT_           VARCHAR2(255),
                                SETTING_TYPE_        VARCHAR2(20),
                                BO_LIST_KEY_         VARCHAR2(64),
                                URL_                 VARCHAR2(128),
                                METHOD_              VARCHAR2(20),
                                PARAMS_              VARCHAR2(1000),
                                TENANT_ID_           VARCHAR2(64),
                                CREATE_DEP_ID_       VARCHAR2(64),
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                UPDATE_BY_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                CONSTRAINT PK_OS_GROUP_MENU PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_GROUP_MENU IS
'???????????????';

COMMENT ON COLUMN OS_GROUP_MENU.ID_ IS
'??????';

COMMENT ON COLUMN OS_GROUP_MENU.MENU_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_GROUP_MENU.GROUP_ID_ IS
'?????????Id';

COMMENT ON COLUMN OS_GROUP_MENU.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_GROUP_MENU.NAME_ IS
'????????????';

COMMENT ON COLUMN OS_GROUP_MENU.ICON_PC_ IS
'PC????????????';

COMMENT ON COLUMN OS_GROUP_MENU.ICON_PIC_ IS
'??????';

COMMENT ON COLUMN OS_GROUP_MENU.ICON_APP_ IS
'APP????????????';

COMMENT ON COLUMN OS_GROUP_MENU.PARENT_ID_ IS
'?????????ID';

COMMENT ON COLUMN OS_GROUP_MENU.PATH_ IS
'??????';

COMMENT ON COLUMN OS_GROUP_MENU.SN_ IS
'??????';

COMMENT ON COLUMN OS_GROUP_MENU.SHOW_TYPE_ IS
'????????????';

COMMENT ON COLUMN OS_GROUP_MENU.MENU_KEY_ IS
'??????????????????';

COMMENT ON COLUMN OS_GROUP_MENU.MENU_TYPE_ IS
'????????????';

COMMENT ON COLUMN OS_GROUP_MENU.COMPONENT_ IS
'????????????';

COMMENT ON COLUMN OS_GROUP_MENU.SETTING_TYPE_ IS
'????????????(custom,iframe)';

COMMENT ON COLUMN OS_GROUP_MENU.BO_LIST_KEY_ IS
'????????????KEY';

COMMENT ON COLUMN OS_GROUP_MENU.URL_ IS
'????????????';

COMMENT ON COLUMN OS_GROUP_MENU.METHOD_ IS
'????????????';

COMMENT ON COLUMN OS_GROUP_MENU.PARAMS_ IS
'????????????';

COMMENT ON COLUMN OS_GROUP_MENU.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_GROUP_MENU.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_GROUP_MENU.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_GROUP_MENU.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_GROUP_MENU.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_GROUP_MENU.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_INST                                               */
/*==============================================================*/
CREATE TABLE OS_INST  (
                          INST_ID_             VARCHAR2(64)                    NOT NULL,
                          NAME_CN_             VARCHAR2(256)                   NOT NULL,
                          NAME_EN_             VARCHAR2(256),
                          INST_NO_             VARCHAR2(50),
                          BUS_LICE_NO_         VARCHAR2(50),
                          DOMAIN_              VARCHAR2(100),
                          NAME_CN_S_           VARCHAR2(80),
                          NAME_EN_S_           VARCHAR2(80),
                          LEGAL_MAN_           VARCHAR2(64),
                          DESCP_               VARCHAR2(1000),
                          ADDRESS_             VARCHAR2(128),
                          PHONE_               VARCHAR2(30),
                          EMAIL_               VARCHAR2(255),
                          FAX_                 VARCHAR2(30),
                          CONTRACTOR_          VARCHAR2(30),
                          HOME_URL_            VARCHAR2(120),
                          INST_TYPE_           VARCHAR2(64),
                          STATUS_              VARCHAR2(30),
                          PARENT_ID_           VARCHAR2(64),
                          PATH_                VARCHAR2(1024),
                          CREATE_DEP_ID_       VARCHAR2(64),
                          CREATE_BY_           VARCHAR2(64),
                          CREATE_TIME_         DATE,
                          UPDATE_BY_           VARCHAR2(64),
                          UPDATE_TIME_         DATE,
                          CONSTRAINT PK_OS_INST PRIMARY KEY (INST_ID_)
);

COMMENT ON TABLE OS_INST IS
'????????????';

COMMENT ON COLUMN OS_INST.INST_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_INST.NAME_CN_ IS
'???????????????';

COMMENT ON COLUMN OS_INST.NAME_EN_ IS
'???????????????';

COMMENT ON COLUMN OS_INST.INST_NO_ IS
'????????????';

COMMENT ON COLUMN OS_INST.BUS_LICE_NO_ IS
'??????????????????';

COMMENT ON COLUMN OS_INST.DOMAIN_ IS
'????????????';

COMMENT ON COLUMN OS_INST.NAME_CN_S_ IS
'????????????(??????)';

COMMENT ON COLUMN OS_INST.NAME_EN_S_ IS
'????????????(??????)';

COMMENT ON COLUMN OS_INST.LEGAL_MAN_ IS
'????????????';

COMMENT ON COLUMN OS_INST.DESCP_ IS
'????????????';

COMMENT ON COLUMN OS_INST.ADDRESS_ IS
'??????';

COMMENT ON COLUMN OS_INST.PHONE_ IS
'????????????';

COMMENT ON COLUMN OS_INST.EMAIL_ IS
'??????';

COMMENT ON COLUMN OS_INST.FAX_ IS
'??????';

COMMENT ON COLUMN OS_INST.CONTRACTOR_ IS
'?????????';

COMMENT ON COLUMN OS_INST.HOME_URL_ IS
'??????URL';

COMMENT ON COLUMN OS_INST.INST_TYPE_ IS
'????????????';

COMMENT ON COLUMN OS_INST.STATUS_ IS
'??????';

COMMENT ON COLUMN OS_INST.PARENT_ID_ IS
'???ID';

COMMENT ON COLUMN OS_INST.PATH_ IS
'??????';

COMMENT ON COLUMN OS_INST.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_INST.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_INST.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_INST.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_INST.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_INST_TYPE                                          */
/*==============================================================*/
CREATE TABLE OS_INST_TYPE  (
                               TYPE_ID_             VARCHAR2(64)                    NOT NULL,
                               TYPE_CODE_           VARCHAR2(50),
                               TYPE_NAME_           VARCHAR2(100),
                               ENABLED_             VARCHAR2(20),
                               IS_DEFAULT_          VARCHAR2(20),
                               HOME_URL_            VARCHAR2(200),
                               DESCP_               VARCHAR2(500),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_OS_INST_TYPE PRIMARY KEY (TYPE_ID_)
);

COMMENT ON TABLE OS_INST_TYPE IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE.TYPE_ID_ IS
'??????';

COMMENT ON COLUMN OS_INST_TYPE.TYPE_CODE_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE.TYPE_NAME_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE.ENABLED_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE.IS_DEFAULT_ IS
'??????????????????';

COMMENT ON COLUMN OS_INST_TYPE.HOME_URL_ IS
'??????URL';

COMMENT ON COLUMN OS_INST_TYPE.DESCP_ IS
'??????';

COMMENT ON COLUMN OS_INST_TYPE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_INST_TYPE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_INST_TYPE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_INST_TYPE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_INST_TYPE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_INST_TYPE_MENU                                     */
/*==============================================================*/
CREATE TABLE OS_INST_TYPE_MENU  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    INST_TYPE_ID_        VARCHAR2(64),
                                    APP_ID_              VARCHAR2(64),
                                    MENU_ID_             VARCHAR2(64),
                                    NAME_                VARCHAR2(60)                    NOT NULL,
                                    ICON_PC_             VARCHAR2(100),
                                    ICON_PIC_            VARCHAR2(100),
                                    ICON_APP_            VARCHAR2(50),
                                    PARENT_ID_           VARCHAR2(64)                    NOT NULL,
                                    PATH_                VARCHAR2(256),
                                    SN_                  INTEGER,
                                    SHOW_TYPE_           VARCHAR2(20),
                                    MENU_KEY_            VARCHAR2(50),
                                    MENU_TYPE_           VARCHAR2(20),
                                    COMPONENT_           VARCHAR2(255),
                                    SETTING_TYPE_        VARCHAR2(20),
                                    BO_LIST_KEY_         VARCHAR2(64),
                                    URL_                 VARCHAR2(128),
                                    METHOD_              VARCHAR2(20),
                                    PARAMS_              VARCHAR2(1000),
                                    TENANT_ID_           VARCHAR2(64),
                                    CREATE_DEP_ID_       VARCHAR2(64),
                                    CREATE_BY_           VARCHAR2(64),
                                    CREATE_TIME_         DATE,
                                    UPDATE_BY_           VARCHAR2(64),
                                    UPDATE_TIME_         DATE,
                                    CONSTRAINT PK_OS_INST_TYPE_MENU PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_INST_TYPE_MENU IS
'????????????????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.ID_ IS
'??????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.INST_TYPE_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.MENU_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.NAME_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.ICON_PC_ IS
'PC????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.ICON_PIC_ IS
'??????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.ICON_APP_ IS
'APP????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.PARENT_ID_ IS
'?????????ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.PATH_ IS
'??????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.SN_ IS
'??????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.SHOW_TYPE_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.MENU_KEY_ IS
'??????????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.MENU_TYPE_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.COMPONENT_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.SETTING_TYPE_ IS
'????????????(custom,iframe)';

COMMENT ON COLUMN OS_INST_TYPE_MENU.BO_LIST_KEY_ IS
'????????????KEY';

COMMENT ON COLUMN OS_INST_TYPE_MENU.URL_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.METHOD_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.PARAMS_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_INST_TYPE_MENU.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_INST_TYPE_MENU.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_INST_TYPE_MENU.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_INST_USERS                                         */
/*==============================================================*/
CREATE TABLE OS_INST_USERS  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                USER_ID_             VARCHAR2(64),
                                IS_ADMIN_            INTEGER,
                                USER_TYPE_           VARCHAR2(64),
                                APPLY_NOTE_          VARCHAR2(255),
                                APPLY_STATUS_        VARCHAR2(40),
                                CREATE_TYPE_         VARCHAR2(40),
                                APPROVE_USER_        VARCHAR2(64),
                                STATUS_              VARCHAR2(40),
                                TENANT_ID_           VARCHAR2(64),
                                CREATE_DEP_ID_       VARCHAR2(64),
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                UPDATE_BY_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                CONSTRAINT PK_OS_INST_USERS PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_INST_USERS IS
'?????????????????????';

COMMENT ON COLUMN OS_INST_USERS.ID_ IS
'??????';

COMMENT ON COLUMN OS_INST_USERS.USER_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_INST_USERS.IS_ADMIN_ IS
'?????????';

COMMENT ON COLUMN OS_INST_USERS.USER_TYPE_ IS
'????????????';

COMMENT ON COLUMN OS_INST_USERS.APPLY_NOTE_ IS
'????????????';

COMMENT ON COLUMN OS_INST_USERS.APPLY_STATUS_ IS
'????????????';

COMMENT ON COLUMN OS_INST_USERS.CREATE_TYPE_ IS
'????????????';

COMMENT ON COLUMN OS_INST_USERS.APPROVE_USER_ IS
'????????????';

COMMENT ON COLUMN OS_INST_USERS.STATUS_ IS
'??????';

COMMENT ON COLUMN OS_INST_USERS.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_INST_USERS.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_INST_USERS.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_INST_USERS.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_INST_USERS.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_INST_USERS.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_OS_INST_USERS_USERID                              */
/*==============================================================*/
CREATE INDEX IDX_OS_INST_USERS_USERID ON OS_INST_USERS (
                                                        USER_ID_ ASC
    );

/*==============================================================*/
/* Table: OS_PROPERTIES_DEF                                     */
/*==============================================================*/
CREATE TABLE OS_PROPERTIES_DEF  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    GROUP_ID_            VARCHAR2(64),
                                    DIM_ID_              VARCHAR2(64),
                                    NAME_                VARCHAR2(64),
                                    DATA_TYPE_           VARCHAR2(40),
                                    CTLTYPE_             VARCHAR2(20),
                                    EXT_JSON_            VARCHAR2(2000),
                                    TENANT_ID_           VARCHAR2(64),
                                    CREATE_DEP_ID_       VARCHAR2(64),
                                    CREATE_BY_           VARCHAR2(64),
                                    CREATE_TIME_         DATE,
                                    UPDATE_BY_           VARCHAR2(64),
                                    UPDATE_TIME_         DATE,
                                    CONSTRAINT PK_OS_PROPERTIES_DEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_PROPERTIES_DEF IS
'?????????????????????';

COMMENT ON COLUMN OS_PROPERTIES_DEF.ID_ IS
'??????';

COMMENT ON COLUMN OS_PROPERTIES_DEF.GROUP_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_PROPERTIES_DEF.DIM_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_PROPERTIES_DEF.NAME_ IS
'????????????';

COMMENT ON COLUMN OS_PROPERTIES_DEF.DATA_TYPE_ IS
'????????????
number,
varchar
date';

COMMENT ON COLUMN OS_PROPERTIES_DEF.CTLTYPE_ IS
'????????????';

COMMENT ON COLUMN OS_PROPERTIES_DEF.EXT_JSON_ IS
'????????????';

COMMENT ON COLUMN OS_PROPERTIES_DEF.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_PROPERTIES_DEF.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_PROPERTIES_DEF.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_PROPERTIES_DEF.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_PROPERTIES_DEF.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_PROPERTIES_DEF.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_PROPERTIES_GROUP                                   */
/*==============================================================*/
CREATE TABLE OS_PROPERTIES_GROUP  (
                                      ID_                  VARCHAR2(64)                    NOT NULL,
                                      NAME_                VARCHAR2(64),
                                      ENABLED_             VARCHAR2(40),
                                      DIM_ID_              VARCHAR2(64),
                                      TENANT_ID_           VARCHAR2(64),
                                      CREATE_DEP_ID_       VARCHAR2(64),
                                      CREATE_BY_           VARCHAR2(64),
                                      CREATE_TIME_         DATE,
                                      UPDATE_BY_           VARCHAR2(64),
                                      UPDATE_TIME_         DATE,
                                      CONSTRAINT PK_OS_PROPERTIES_GROUP PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_PROPERTIES_GROUP IS
'?????????????????????';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.ID_ IS
'??????';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.NAME_ IS
'????????????';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.ENABLED_ IS
'????????????';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.DIM_ID_ IS
'??????ID
??????????????????-1';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_PROPERTIES_GROUP.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_PROPERTIES_VAL                                     */
/*==============================================================*/
CREATE TABLE OS_PROPERTIES_VAL  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    DIM_ID_              VARCHAR2(64),
                                    GROUP_ID_            VARCHAR2(64),
                                    OWNER_ID_            VARCHAR2(64),
                                    PROPERY_ID_          VARCHAR2(64),
                                    NUM_VAL_             NUMBER(14,2),
                                    TXT_VAL              VARCHAR2(200),
                                    DATE_VAL             DATE,
                                    TENANT_ID_           VARCHAR2(64),
                                    CREATE_DEP_ID_       VARCHAR2(64),
                                    CREATE_BY_           VARCHAR2(64),
                                    CREATE_TIME_         DATE,
                                    UPDATE_BY_           VARCHAR2(64),
                                    UPDATE_TIME_         DATE,
                                    CONSTRAINT PK_OS_PROPERTIES_VAL PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_PROPERTIES_VAL IS
'???????????????';

COMMENT ON COLUMN OS_PROPERTIES_VAL.ID_ IS
'??????';

COMMENT ON COLUMN OS_PROPERTIES_VAL.DIM_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.GROUP_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.OWNER_ID_ IS
'?????????';

COMMENT ON COLUMN OS_PROPERTIES_VAL.PROPERY_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.NUM_VAL_ IS
'?????????';

COMMENT ON COLUMN OS_PROPERTIES_VAL.TXT_VAL IS
'?????????';

COMMENT ON COLUMN OS_PROPERTIES_VAL.DATE_VAL IS
'?????????';

COMMENT ON COLUMN OS_PROPERTIES_VAL.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_PROPERTIES_VAL.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_PROPERTIES_VAL.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_RANK_TYPE                                          */
/*==============================================================*/
CREATE TABLE OS_RANK_TYPE  (
                               RK_ID_               VARCHAR2(64)                    NOT NULL,
                               DIM_ID_              VARCHAR2(64),
                               NAME_                VARCHAR2(255)                   NOT NULL,
                               KEY_                 VARCHAR2(64)                    NOT NULL,
                               LEVEL_               INTEGER                         NOT NULL,
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_OS_RANK_TYPE PRIMARY KEY (RK_ID_)
);

COMMENT ON TABLE OS_RANK_TYPE IS
'?????????????????????????????????????????????????????????
???????????????????????????????????????';

COMMENT ON COLUMN OS_RANK_TYPE.RK_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_RANK_TYPE.DIM_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_RANK_TYPE.NAME_ IS
'??????';

COMMENT ON COLUMN OS_RANK_TYPE.KEY_ IS
'???????????????';

COMMENT ON COLUMN OS_RANK_TYPE.LEVEL_ IS
'????????????';

COMMENT ON COLUMN OS_RANK_TYPE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_RANK_TYPE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_RANK_TYPE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_RANK_TYPE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_RANK_TYPE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_RANK_TYPE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_REL_INST                                           */
/*==============================================================*/
CREATE TABLE OS_REL_INST  (
                              INST_ID_             VARCHAR2(64)                    NOT NULL,
                              REL_TYPE_ID_         VARCHAR2(64),
                              REL_TYPE_KEY_        VARCHAR2(64),
                              PARTY1_              VARCHAR2(64)                    NOT NULL,
                              PARTY2_              VARCHAR2(64)                    NOT NULL,
                              DIM1_                VARCHAR2(64),
                              DIM2_                VARCHAR2(64),
                              IS_MAIN_             VARCHAR2(20)                   DEFAULT 'NO' NOT NULL,
                              STATUS_              VARCHAR2(40)                    NOT NULL,
                              ALIAS_               VARCHAR2(80),
                              REL_TYPE_            VARCHAR2(64),
                              PATH_                VARCHAR2(200),
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_DEP_ID_       VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           VARCHAR2(64),
                              UPDATE_TIME_         DATE,
                              CONSTRAINT PK_OS_REL_INST PRIMARY KEY (INST_ID_)
);

COMMENT ON TABLE OS_REL_INST IS
'????????????';

COMMENT ON COLUMN OS_REL_INST.INST_ID_ IS
'???????????????ID';

COMMENT ON COLUMN OS_REL_INST.REL_TYPE_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_REL_INST.REL_TYPE_KEY_ IS
'????????????KEY_
';

COMMENT ON COLUMN OS_REL_INST.PARTY1_ IS
'?????????ID';

COMMENT ON COLUMN OS_REL_INST.PARTY2_ IS
'?????????ID';

COMMENT ON COLUMN OS_REL_INST.DIM1_ IS
'???????????????';

COMMENT ON COLUMN OS_REL_INST.DIM2_ IS
'???????????????ID';

COMMENT ON COLUMN OS_REL_INST.IS_MAIN_ IS
'??????????????????';

COMMENT ON COLUMN OS_REL_INST.STATUS_ IS
'??????
ENABLED
DISABLED';

COMMENT ON COLUMN OS_REL_INST.ALIAS_ IS
'??????';

COMMENT ON COLUMN OS_REL_INST.REL_TYPE_ IS
'????????????';

COMMENT ON COLUMN OS_REL_INST.PATH_ IS
'??????';

COMMENT ON COLUMN OS_REL_INST.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_REL_INST.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_REL_INST.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_REL_INST.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_REL_INST.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_REL_INST.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Index: IDX_OS_REL_INST_PARTY2                                */
/*==============================================================*/
CREATE INDEX IDX_OS_REL_INST_PARTY2 ON OS_REL_INST (
                                                    PARTY2_ ASC
    );

/*==============================================================*/
/* Index: IDX_OS_REL_INST_PARTY1                                */
/*==============================================================*/
CREATE INDEX IDX_OS_REL_INST_PARTY1 ON OS_REL_INST (
                                                    PARTY1_ ASC
    );

/*==============================================================*/
/* Table: OS_REL_TYPE                                           */
/*==============================================================*/
CREATE TABLE OS_REL_TYPE  (
                              ID_                  VARCHAR2(64)                    NOT NULL,
                              NAME_                VARCHAR2(64)                    NOT NULL,
                              KEY_                 VARCHAR2(64)                    NOT NULL,
                              REL_TYPE_            VARCHAR2(40)                    NOT NULL,
                              CONST_TYPE_          VARCHAR2(40),
                              PARTY1_              VARCHAR2(128)                   NOT NULL,
                              PARTY2_              VARCHAR2(128)                   NOT NULL,
                              DIM_ID1_             VARCHAR2(64),
                              DIM_ID2_             VARCHAR2(64),
                              LEVEL_               INTEGER,
                              STATUS_              VARCHAR2(40)                    NOT NULL,
                              IS_SYSTEM_           VARCHAR2(10)                    NOT NULL,
                              IS_DEFAULT_          VARCHAR2(10)                    NOT NULL,
                              IS_TWO_WAY_          VARCHAR2(10),
                              MEMO_                VARCHAR2(255),
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_DEP_ID_       VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           VARCHAR2(64),
                              UPDATE_TIME_         DATE,
                              CONSTRAINT PK_OS_REL_TYPE PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_REL_TYPE IS
'??????????????????';

COMMENT ON COLUMN OS_REL_TYPE.ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_REL_TYPE.NAME_ IS
'?????????';

COMMENT ON COLUMN OS_REL_TYPE.KEY_ IS
'??????????????????';

COMMENT ON COLUMN OS_REL_TYPE.REL_TYPE_ IS
'???????????????????????????=USER-USER??????????????????=GROUP-GROUP?????????????????????=USER-GROUP?????????????????????=GROUP-USER';

COMMENT ON COLUMN OS_REL_TYPE.CONST_TYPE_ IS
'?????????????????????1???1=one2one???1??????=one2many?????????1=many2one????????????=many2many';

COMMENT ON COLUMN OS_REL_TYPE.PARTY1_ IS
'?????????????????????';

COMMENT ON COLUMN OS_REL_TYPE.PARTY2_ IS
'?????????????????????';

COMMENT ON COLUMN OS_REL_TYPE.DIM_ID1_ IS
'???????????????ID???????????????????????????';

COMMENT ON COLUMN OS_REL_TYPE.DIM_ID2_ IS
'???????????????ID??????????????????????????????';

COMMENT ON COLUMN OS_REL_TYPE.STATUS_ IS
'?????????actived ????????????locked ?????????deleted ?????????';

COMMENT ON COLUMN OS_REL_TYPE.IS_SYSTEM_ IS
'??????????????????';

COMMENT ON COLUMN OS_REL_TYPE.IS_DEFAULT_ IS
'????????????';

COMMENT ON COLUMN OS_REL_TYPE.IS_TWO_WAY_ IS
'??????????????????';

COMMENT ON COLUMN OS_REL_TYPE.MEMO_ IS
'????????????';

COMMENT ON COLUMN OS_REL_TYPE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_REL_TYPE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_REL_TYPE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_REL_TYPE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_REL_TYPE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_REL_TYPE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_USER                                               */
/*==============================================================*/
CREATE TABLE OS_USER  (
                          USER_ID_             VARCHAR2(64)                    NOT NULL,
                          FULLNAME_            VARCHAR2(64)                    NOT NULL,
                          USER_NO_             VARCHAR2(64)                    NOT NULL,
                          PWD_                 VARCHAR2(64),
                          ENTRY_TIME_          DATE,
                          QUIT_TIME_           DATE,
                          USER_TYPE_           VARCHAR2(20),
                          FROM_                VARCHAR2(20),
                          BIRTHDAY_            DATE,
                          SEX_                 VARCHAR2(10),
                          MOBILE_              VARCHAR2(32),
                          EMAIL_               VARCHAR2(100),
                          ADDRESS_             VARCHAR2(255),
                          URGENT_              VARCHAR2(64),
                          SYNC_WX_             INTEGER,
                          URGENT_MOBILE_       VARCHAR2(20),
                          QQ_                  VARCHAR2(20),
                          PHOTO_               VARCHAR2(255),
                          ENABLED_             VARCHAR2(20),
                          DD_ID_               VARCHAR2(64),
                          OPEN_ID_             VARCHAR2(64),
                          WX_OPEN_ID_          VARCHAR2(64),
                          STATUS_              VARCHAR2(50),
                          TENANT_ID_           VARCHAR2(64),
                          CREATE_DEP_ID_       VARCHAR2(64),
                          CREATE_BY_           VARCHAR2(64),
                          CREATE_TIME_         DATE,
                          UPDATE_BY_           VARCHAR2(64),
                          UPDATE_TIME_         DATE,
                          CONSTRAINT PK_OS_USER PRIMARY KEY (USER_ID_)
);

COMMENT ON TABLE OS_USER IS
'???????????????';

COMMENT ON COLUMN OS_USER.USER_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_USER.FULLNAME_ IS
'??????';

COMMENT ON COLUMN OS_USER.USER_NO_ IS
'????????????';

COMMENT ON COLUMN OS_USER.PWD_ IS
'??????';

COMMENT ON COLUMN OS_USER.ENTRY_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_USER.QUIT_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_USER.USER_TYPE_ IS
'????????????';

COMMENT ON COLUMN OS_USER.FROM_ IS
'??????
system,????????????,import,??????,grade,???????????????';

COMMENT ON COLUMN OS_USER.BIRTHDAY_ IS
'????????????';

COMMENT ON COLUMN OS_USER.SEX_ IS
'??????';

COMMENT ON COLUMN OS_USER.MOBILE_ IS
'??????';

COMMENT ON COLUMN OS_USER.EMAIL_ IS
'??????';

COMMENT ON COLUMN OS_USER.ADDRESS_ IS
'??????';

COMMENT ON COLUMN OS_USER.URGENT_ IS
'???????????????';

COMMENT ON COLUMN OS_USER.SYNC_WX_ IS
'?????????????????????';

COMMENT ON COLUMN OS_USER.URGENT_MOBILE_ IS
'?????????????????????';

COMMENT ON COLUMN OS_USER.QQ_ IS
'QQ???';

COMMENT ON COLUMN OS_USER.PHOTO_ IS
'??????';

COMMENT ON COLUMN OS_USER.ENABLED_ IS
'????????????';

COMMENT ON COLUMN OS_USER.DD_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_USER.WX_OPEN_ID_ IS
'????????????OPENID';

COMMENT ON COLUMN OS_USER.STATUS_ IS
'??????????????????????????????
IN_JOB=??????
OUT_JOB=??????';

COMMENT ON COLUMN OS_USER.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_USER.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_USER.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_USER.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_USER.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_USER.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_USER_TYPE                                          */
/*==============================================================*/
CREATE TABLE OS_USER_TYPE  (
                               ID_                  VARCHAR2(64)                    NOT NULL,
                               CODE_                VARCHAR2(50)                    NOT NULL,
                               NAME_                VARCHAR2(50)                    NOT NULL,
                               DESCP_               VARCHAR2(500),
                               GROUP_ID_            VARCHAR2(64),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_OS_USER_TYPE PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_USER_TYPE IS
'????????????';

COMMENT ON COLUMN OS_USER_TYPE.ID_ IS
'??????';

COMMENT ON COLUMN OS_USER_TYPE.CODE_ IS
'??????';

COMMENT ON COLUMN OS_USER_TYPE.NAME_ IS
'??????';

COMMENT ON COLUMN OS_USER_TYPE.DESCP_ IS
'??????';

COMMENT ON COLUMN OS_USER_TYPE.GROUP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_USER_TYPE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN OS_USER_TYPE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_USER_TYPE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_USER_TYPE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_USER_TYPE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN OS_USER_TYPE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: OS_WX_ENT_AGENT                                       */
/*==============================================================*/
CREATE TABLE OS_WX_ENT_AGENT  (
                                  ID_                  VARCHAR2(64)                    NOT NULL,
                                  NAME_                VARCHAR2(100),
                                  DESCRIPTION_         VARCHAR2(200),
                                  DOMAIN_              VARCHAR2(64),
                                  HOME_URL_            VARCHAR2(400),
                                  CORP_ID_             VARCHAR2(64),
                                  AGENT_ID_            VARCHAR2(64),
                                  SECRET_              VARCHAR2(64),
                                  DEFAULT_AGENT_       INTEGER,
                                  UPDATE_TIME_         DATE,
                                  UPDATE_BY_           VARCHAR2(64),
                                  CREATE_DEP_ID_       VARCHAR2(64),
                                  CREATE_TIME_         DATE,
                                  TENANT_ID_           VARCHAR2(64),
                                  CREATE_BY_           VARCHAR2(64),
                                  TOKEN_               VARCHAR2(64)                   DEFAULT NULL,
                                  AESKEY_              VARCHAR2(64)                   DEFAULT NULL,
                                  CONSTRAINT PK_OS_WX_ENT_AGENT PRIMARY KEY (ID_)
);

COMMENT ON TABLE OS_WX_ENT_AGENT IS
'?????????????????????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.ID_ IS
'??????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.NAME_ IS
'??????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.DESCRIPTION_ IS
'??????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.DOMAIN_ IS
'????????????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.HOME_URL_ IS
'????????????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.CORP_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_WX_ENT_AGENT.AGENT_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_WX_ENT_AGENT.SECRET_ IS
'??????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.DEFAULT_AGENT_ IS
'????????????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN OS_WX_ENT_AGENT.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN OS_WX_ENT_AGENT.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN OS_WX_ENT_AGENT.TOKEN_ IS
'????????????TOKEN';

COMMENT ON COLUMN OS_WX_ENT_AGENT.AESKEY_ IS
'????????????AESKEY';

COMMIT ;