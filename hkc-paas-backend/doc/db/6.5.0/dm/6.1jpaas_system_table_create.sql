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
/* Table: SYS_APP                                               */
/*==============================================================*/
CREATE TABLE SYS_APP  (
                          APP_ID_              VARCHAR2(64)                    NOT NULL,
                          CLIENT_CODE_         VARCHAR2(64)                    NOT NULL,
                          CLIENT_NAME_         VARCHAR2(80)                    NOT NULL,
                          CATEGORY_ID_         VARCHAR2(64),
                          ICON_                VARCHAR2(80),
                          STATUS_              VARCHAR2(20),
                          DESCP_               VARCHAR2(256),
                          HOME_TYPE_           VARCHAR2(40),
                          URL_TYPE_            VARCHAR2(20),
                          LAYOUT_              VARCHAR2(20),
                          PARENT_MODULE_       VARCHAR2(100),
                          ICON_PIC_            VARCHAR2(50),
                          HOME_URL_            VARCHAR2(120),
                          SN_                  INTEGER,
                          PARAMS_              VARCHAR2(200),
                          AUTH_SETTING_        CLOB,
                          SHARE_               VARCHAR2(20),
                          FREE_                VARCHAR2(20)                   DEFAULT 'N',
                          IS_AUTH_             VARCHAR2(20),
                          TENANT_ID_           VARCHAR2(64),
                          CREATE_DEP_ID_       VARCHAR2(64),
                          CREATE_BY_           VARCHAR2(64),
                          CREATE_TIME_         DATE,
                          UPDATE_BY_           VARCHAR2(64),
                          UPDATE_TIME_         DATE,
                          APP_TYPE_            SMALLINT                       DEFAULT 1,
                          BACK_COLOR_          VARCHAR2(20),
                          VERSION_             VARCHAR2(20),
                          COPYRIGHT_           VARCHAR2(50),
                          PC_USE_              SMALLINT                       DEFAULT 1,
                          MOBILE_USE_          SMALLINT                       DEFAULT 1,
                          MOBILE_HOME_         VARCHAR2(100),
                          PATH_                VARCHAR2(50),
                          MENU_NAV_TYPE_       SMALLINT                       DEFAULT 0 NOT NULL,
                          CONSTRAINT PK_SYS_APP PRIMARY KEY (APP_ID_),
                          CONSTRAINT AK_IDX_SYSAPP_CODE_SYS_APP UNIQUE (CLIENT_CODE_)
);

COMMENT ON TABLE SYS_APP IS
'????????????';

COMMENT ON COLUMN SYS_APP.APP_ID_ IS
'??????';

COMMENT ON COLUMN SYS_APP.CLIENT_CODE_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.CLIENT_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.CATEGORY_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP.ICON_ IS
'APP??????';

COMMENT ON COLUMN SYS_APP.STATUS_ IS
'??????';

COMMENT ON COLUMN SYS_APP.DESCP_ IS
'??????';

COMMENT ON COLUMN SYS_APP.HOME_TYPE_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.URL_TYPE_ IS
'URL??????';

COMMENT ON COLUMN SYS_APP.LAYOUT_ IS
'??????';

COMMENT ON COLUMN SYS_APP.PARENT_MODULE_ IS
'?????????';

COMMENT ON COLUMN SYS_APP.ICON_PIC_ IS
'??????';

COMMENT ON COLUMN SYS_APP.HOME_URL_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.SN_ IS
'??????';

COMMENT ON COLUMN SYS_APP.PARAMS_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.AUTH_SETTING_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.SHARE_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.FREE_ IS
'???????????????Y?????????N?????????';

COMMENT ON COLUMN SYS_APP.IS_AUTH_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_APP.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.APP_TYPE_ IS
'0:???????????????1:???????????????2:??????';

COMMENT ON COLUMN SYS_APP.BACK_COLOR_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP.VERSION_ IS
'???????????????';

COMMENT ON COLUMN SYS_APP.COPYRIGHT_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.PC_USE_ IS
'PC?????????';

COMMENT ON COLUMN SYS_APP.MOBILE_USE_ IS
'???????????????';

COMMENT ON COLUMN SYS_APP.MOBILE_HOME_ IS
'???????????????';

COMMENT ON COLUMN SYS_APP.PATH_ IS
'????????????';

COMMENT ON COLUMN SYS_APP.MENU_NAV_TYPE_ IS
'?????????????????????0:??????,1:?????????';

/*==============================================================*/
/* Table: SYS_APP_ACTION_LOG                                    */
/*==============================================================*/
CREATE TABLE SYS_APP_ACTION_LOG  (
                                     ID_                  VARCHAR2(64)                    NOT NULL,
                                     APP_ID_              VARCHAR2(64)                    NOT NULL,
                                     TYPE_                INTEGER                         NOT NULL,
                                     TITLE_               VARCHAR2(50)                   DEFAULT NULL,
                                     CONTENT_             CLOB,
                                     CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                     TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                     CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                     CREATE_TIME_         DATE                           DEFAULT NULL,
                                     UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                     UPDATE_TIME_         DATE                           DEFAULT NULL,
                                     CONSTRAINT PK_SYS_APP_ACTION_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_APP_ACTION_LOG IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.ID_ IS
'??????';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.TYPE_ IS
'1??????2??????3??????4??????5??????6??????7??????8??????????????????9??????????????????';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.TITLE_ IS
'??????';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.CONTENT_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_APP_AUTH                                          */
/*==============================================================*/
CREATE TABLE SYS_APP_AUTH  (
                               ID_                  VARCHAR2(64)                    NOT NULL,
                               APP_ID_              VARCHAR2(64),
                               METHOD_              VARCHAR2(64),
                               URL_                 VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_SYS_APP_AUTH PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_APP_AUTH IS
'???????????????';

COMMENT ON COLUMN SYS_APP_AUTH.ID_ IS
'??????';

COMMENT ON COLUMN SYS_APP_AUTH.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_AUTH.METHOD_ IS
'??????';

COMMENT ON COLUMN SYS_APP_AUTH.URL_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_AUTH.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_AUTH.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_AUTH.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_APP_AUTH.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_AUTH.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_APP_AUTH.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_APP_AUTH_MENU                                     */
/*==============================================================*/
CREATE TABLE SYS_APP_AUTH_MENU  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    APP_ID_              VARCHAR2(64)                   DEFAULT NULL,
                                    MENU_ID_             VARCHAR2(64)                   DEFAULT 'N',
                                    TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                    USER_ID_             VARCHAR2(64)                   DEFAULT NULL,
                                    ENABLE_              VARCHAR2(10)                   DEFAULT 'Y',
                                    CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                    CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                    CREATE_TIME_         DATE                           DEFAULT NULL,
                                    UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                    UPDATE_TIME_         DATE                           DEFAULT NULL,
                                    CONSTRAINT PK_SYS_APP_AUTH_MENU PRIMARY KEY (ID_),
                                    CONSTRAINT IDX_SYS_APP_AUTH_MENU_CODE UNIQUE (APP_ID_)
);

COMMENT ON TABLE SYS_APP_AUTH_MENU IS
'?????????????????????';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.ID_ IS
'??????';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.MENU_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.TENANT_ID_ IS
'??????Id';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.USER_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.ENABLE_ IS
'???????????????Y?????????N?????????';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_APP_FAVORITES                                     */
/*==============================================================*/
CREATE TABLE SYS_APP_FAVORITES  (
                                    FAV_ID_              VARCHAR2(64)                    NOT NULL,
                                    USER_ID_             VARCHAR2(64)                    NOT NULL,
                                    APP_ID_              VARCHAR2(64)                    NOT NULL,
                                    IS_FAV_              SMALLINT                       DEFAULT 1 NOT NULL,
                                    FAV_TIME_            DATE                           DEFAULT NULL,
                                    LAST_USE_TIME_       DATE                           DEFAULT NULL,
                                    TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                    CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                    CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                    CREATE_TIME_         DATE                           DEFAULT NULL,
                                    UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                    UPDATE_TIME_         DATE                           DEFAULT NULL,
                                    CONSTRAINT PK_SYS_APP_FAVORITES PRIMARY KEY (FAV_ID_),
                                    CONSTRAINT IDX_SYS_APP_FAVORITES_UNIQUE UNIQUE (USER_ID_, APP_ID_)
);

COMMENT ON TABLE SYS_APP_FAVORITES IS
'???????????????????????????';

COMMENT ON COLUMN SYS_APP_FAVORITES.FAV_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.USER_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.IS_FAV_ IS
'???????????????0???????????????';

COMMENT ON COLUMN SYS_APP_FAVORITES.FAV_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_FAVORITES.LAST_USE_TIME_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_FAVORITES.TENANT_ID_ IS
'??????Id';

COMMENT ON COLUMN SYS_APP_FAVORITES.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_FAVORITES.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_APP_INSTALL                                       */
/*==============================================================*/
CREATE TABLE SYS_APP_INSTALL  (
                                  ID_                  VARCHAR2(64)                    NOT NULL,
                                  APP_ID_              VARCHAR2(64)                   DEFAULT NULL,
                                  TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                  USER_ID_             VARCHAR2(64)                   DEFAULT NULL,
                                  FREE_                VARCHAR2(10)                   DEFAULT 'N',
                                  ENABLE_              VARCHAR2(10)                   DEFAULT 'Y',
                                  CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                  CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                  CREATE_TIME_         DATE                           DEFAULT NULL,
                                  UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                  UPDATE_TIME_         DATE                           DEFAULT NULL,
                                  CONSTRAINT PK_SYS_APP_INSTALL PRIMARY KEY (ID_),
                                  CONSTRAINT IDX_SYS_APP_INSTALL_CODE UNIQUE (APP_ID_)
);

COMMENT ON TABLE SYS_APP_INSTALL IS
'???????????????';

COMMENT ON COLUMN SYS_APP_INSTALL.ID_ IS
'??????';

COMMENT ON COLUMN SYS_APP_INSTALL.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_INSTALL.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_INSTALL.USER_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_INSTALL.FREE_ IS
'???????????????Y?????????N?????????';

COMMENT ON COLUMN SYS_APP_INSTALL.ENABLE_ IS
'???????????????Y?????????N?????????';

COMMENT ON COLUMN SYS_APP_INSTALL.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_INSTALL.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_INSTALL.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_INSTALL.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_INSTALL.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_APP_LOG                                           */
/*==============================================================*/
CREATE TABLE SYS_APP_LOG  (
                              ID_                  VARCHAR2(64)                    NOT NULL,
                              APP_NAME_            VARCHAR2(50),
                              APP_ID_              VARCHAR2(64),
                              METHOD_              VARCHAR2(40),
                              URL_                 VARCHAR2(255),
                              DURATION_            INTEGER,
                              CREATE_DEP_ID_       VARCHAR2(64),
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           VARCHAR2(64),
                              UPDATE_TIME_         DATE,
                              CONSTRAINT PK_SYS_APP_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_APP_LOG IS
'????????????';

COMMENT ON COLUMN SYS_APP_LOG.ID_ IS
'??????';

COMMENT ON COLUMN SYS_APP_LOG.APP_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_LOG.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_LOG.METHOD_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_LOG.URL_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_LOG.DURATION_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_LOG.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_LOG.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_APP_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_LOG.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_APP_LOG.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_APP_RELATION                                      */
/*==============================================================*/
CREATE TABLE SYS_APP_RELATION  (
                                   ID_                  VARCHAR2(64)                    NOT NULL,
                                   APP_ID_              VARCHAR2(64)                    NOT NULL,
                                   RELATED_APP_ID_      VARCHAR2(64)                    NOT NULL,
                                   STRONG_              INTEGER                        DEFAULT 0 NOT NULL,
                                   CONTENT_             CLOB,
                                   CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                   TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                   CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                   CREATE_TIME_         DATE                           DEFAULT NULL,
                                   UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                   UPDATE_TIME_         DATE                           DEFAULT NULL,
                                   CONSTRAINT PK_SYS_APP_RELATION PRIMARY KEY (ID_),
                                   CONSTRAINT AK_IDX_SYS_APP_RELATI_SYS_APP_ UNIQUE (APP_ID_, RELATED_APP_ID_)
);

COMMENT ON TABLE SYS_APP_RELATION IS
'????????????';

COMMENT ON COLUMN SYS_APP_RELATION.ID_ IS
'??????';

COMMENT ON COLUMN SYS_APP_RELATION.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_RELATION.RELATED_APP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_RELATION.STRONG_ IS
'???????????????';

COMMENT ON COLUMN SYS_APP_RELATION.CONTENT_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_RELATION.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_RELATION.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_APP_RELATION.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_RELATION.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_RELATION.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_RELATION.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_APP_RUN                                           */
/*==============================================================*/
CREATE TABLE SYS_APP_RUN  (
                              ID_                  VARCHAR2(64)                    NOT NULL,
                              APP_ID_              VARCHAR2(64)                    NOT NULL,
                              OS_                  VARCHAR2(10)                    NOT NULL,
                              FRONT_DEPLOY_PATH_   VARCHAR2(150)                  DEFAULT NULL,
                              BACK_DEPLOY_PATH_    VARCHAR2(150)                  DEFAULT NULL,
                              SERVICE_PORT_        INTEGER                        DEFAULT NULL,
                              RUN_FRONT_CMD_       VARCHAR2(255),
                              RUN_BACK_CMD_        VARCHAR2(255)                  DEFAULT NULL,
                              FRONT_PID_           INTEGER                        DEFAULT NULL,
                              BACK_PID_            INTEGER                        DEFAULT NULL,
                              STOP_FRONT_CMD_      VARCHAR2(150)                  DEFAULT NULL,
                              STOP_BACK_CMD_       VARCHAR2(150),
                              START_TIME_          DATE                           DEFAULT NULL,
                              STOP_TIME_           DATE                           DEFAULT NULL,
                              CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                              TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                              CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                              CREATE_TIME_         DATE                           DEFAULT NULL,
                              UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                              UPDATE_TIME_         DATE                           DEFAULT NULL,
                              CONSTRAINT PK_SYS_APP_RUN PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_APP_RUN IS
'????????????';

COMMENT ON COLUMN SYS_APP_RUN.ID_ IS
'??????';

COMMENT ON COLUMN SYS_APP_RUN.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_RUN.OS_ IS
'???????????????linux/windows';

COMMENT ON COLUMN SYS_APP_RUN.FRONT_DEPLOY_PATH_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_RUN.BACK_DEPLOY_PATH_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_RUN.SERVICE_PORT_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_RUN.RUN_FRONT_CMD_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_RUN.RUN_BACK_CMD_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_RUN.FRONT_PID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_RUN.BACK_PID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_RUN.STOP_FRONT_CMD_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_RUN.STOP_BACK_CMD_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_RUN.START_TIME_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_RUN.STOP_TIME_ IS
'??????????????????';

COMMENT ON COLUMN SYS_APP_RUN.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_RUN.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_APP_RUN.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_RUN.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_RUN.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_RUN.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_APP_VERSION                                       */
/*==============================================================*/
CREATE TABLE SYS_APP_VERSION  (
                                  ID_                  VARCHAR2(64)                    NOT NULL,
                                  APP_ID_              VARCHAR2(64)                    NOT NULL,
                                  VERSION_             VARCHAR2(20)                    NOT NULL,
                                  NOTES_               CLOB,
                                  SORT_                INTEGER                        DEFAULT 1,
                                  LAST_VERSION_        VARCHAR2(20)                   DEFAULT NULL,
                                  COMPLIANT_           INTEGER                        DEFAULT 1,
                                  UPGRADE_SCRIPT_      CLOB,
                                  UNINSTALL_SCRIPT_    CLOB,
                                  STATUS_              VARCHAR2(64)                   DEFAULT '1',
                                  CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                  TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                  CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                  CREATE_TIME_         DATE                           DEFAULT NULL,
                                  UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                  UPDATE_TIME_         DATE                           DEFAULT NULL,
                                  CONSTRAINT PK_SYS_APP_VERSION PRIMARY KEY (ID_),
                                  CONSTRAINT AK_IDX_SYS_APP_VERSIO_SYS_APP_ UNIQUE (APP_ID_, VERSION_)
);

COMMENT ON TABLE SYS_APP_VERSION IS
'????????????';

COMMENT ON COLUMN SYS_APP_VERSION.ID_ IS
'??????';

COMMENT ON COLUMN SYS_APP_VERSION.APP_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_APP_VERSION.VERSION_ IS
'?????????';

COMMENT ON COLUMN SYS_APP_VERSION.NOTES_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_VERSION.SORT_ IS
'??????';

COMMENT ON COLUMN SYS_APP_VERSION.LAST_VERSION_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_VERSION.COMPLIANT_ IS
'???????????????????????????1?????????0?????????';

COMMENT ON COLUMN SYS_APP_VERSION.UPGRADE_SCRIPT_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_VERSION.UNINSTALL_SCRIPT_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_VERSION.STATUS_ IS
'1?????????0??????';

COMMENT ON COLUMN SYS_APP_VERSION.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_APP_VERSION.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_APP_VERSION.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_VERSION.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_APP_VERSION.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_APP_VERSION.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_AUTH_MANAGER                                      */
/*==============================================================*/
CREATE TABLE SYS_AUTH_MANAGER  (
                                   ID_                  VARCHAR2(64)                    NOT NULL,
                                   NAME_                VARCHAR2(64),
                                   SECRET_              VARCHAR2(100),
                                   IS_LOG_              VARCHAR2(40),
                                   ENABLE_              VARCHAR2(40),
                                   EXPIRE_              INTEGER,
                                   CREATE_DEP_ID_       VARCHAR2(64),
                                   TENANT_ID_           VARCHAR2(64),
                                   CREATE_BY_           VARCHAR2(64),
                                   CREATE_TIME_         DATE,
                                   UPDATE_BY_           VARCHAR2(64),
                                   UPDATE_TIME_         DATE,
                                   CONSTRAINT PK_SYS_AUTH_MANAGER PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_AUTH_MANAGER IS
'?????????????????????';

COMMENT ON COLUMN SYS_AUTH_MANAGER.ID_ IS
'??????';

COMMENT ON COLUMN SYS_AUTH_MANAGER.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_AUTH_MANAGER.SECRET_ IS
'??????';

COMMENT ON COLUMN SYS_AUTH_MANAGER.IS_LOG_ IS
'??????????????????';

COMMENT ON COLUMN SYS_AUTH_MANAGER.ENABLE_ IS
'????????????';

COMMENT ON COLUMN SYS_AUTH_MANAGER.EXPIRE_ IS
'????????????';

COMMENT ON COLUMN SYS_AUTH_MANAGER.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_AUTH_MANAGER.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_AUTH_MANAGER.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_AUTH_MANAGER.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_AUTH_MANAGER.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_AUTH_MANAGER.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_AUTH_RIGHTS                                       */
/*==============================================================*/
CREATE TABLE SYS_AUTH_RIGHTS  (
                                  ID_                  VARCHAR2(64)                    NOT NULL,
                                  TREE_ID_             VARCHAR2(2000),
                                  TREE_NAME_           VARCHAR2(2000),
                                  SETTING_ID_          VARCHAR2(64),
                                  RIGHT_JSON_          CLOB,
                                  TENANT_ID_           VARCHAR2(64),
                                  CREATE_DEP_ID_       VARCHAR2(64),
                                  CREATE_BY_           VARCHAR2(64),
                                  CREATE_TIME_         DATE,
                                  UPDATE_BY_           VARCHAR2(64),
                                  UPDATE_TIME_         DATE,
                                  CONSTRAINT PK_SYS_AUTH_RIGHTS PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_AUTH_RIGHTS IS
'??????????????????';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.ID_ IS
'??????';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.TREE_ID_ IS
'??????IDS';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.TREE_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.SETTING_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.RIGHT_JSON_ IS
'????????????';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_AUTH_SETTING                                      */
/*==============================================================*/
CREATE TABLE SYS_AUTH_SETTING  (
                                   ID_                  VARCHAR2(64)                    NOT NULL,
                                   NAME_                VARCHAR2(64),
                                   ENABLE_              VARCHAR2(10),
                                   TYPE_                VARCHAR2(64),
                                   TENANT_ID_           VARCHAR2(64),
                                   CREATE_DEP_ID_       VARCHAR2(64),
                                   CREATE_BY_           VARCHAR2(64),
                                   CREATE_TIME_         DATE,
                                   UPDATE_BY_           VARCHAR2(64),
                                   UPDATE_TIME_         DATE,
                                   CONSTRAINT PK_SYS_AUTH_SETTING PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_AUTH_SETTING IS
'??????????????????';

COMMENT ON COLUMN SYS_AUTH_SETTING.ID_ IS
'??????';

COMMENT ON COLUMN SYS_AUTH_SETTING.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_AUTH_SETTING.ENABLE_ IS
'??????';

COMMENT ON COLUMN SYS_AUTH_SETTING.TYPE_ IS
'??????';

COMMENT ON COLUMN SYS_AUTH_SETTING.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_AUTH_SETTING.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_AUTH_SETTING.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_AUTH_SETTING.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_AUTH_SETTING.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_AUTH_SETTING.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_DIC                                               */
/*==============================================================*/
CREATE TABLE SYS_DIC  (
                          DIC_ID_              VARCHAR2(64)                    NOT NULL,
                          TREE_ID_             VARCHAR2(64),
                          NAME_                VARCHAR2(64)                    NOT NULL,
                          VALUE_               VARCHAR2(100)                   NOT NULL,
                          DESCP_               VARCHAR2(256),
                          SN_                  INTEGER,
                          PATH_                VARCHAR2(256),
                          PARENT_ID_           VARCHAR2(64),
                          TENANT_ID_           VARCHAR2(64),
                          CREATE_DEP_ID_       VARCHAR2(64),
                          CREATE_BY_           VARCHAR2(64),
                          CREATE_TIME_         DATE,
                          UPDATE_BY_           VARCHAR2(64),
                          UPDATE_TIME_         DATE,
                          APP_ID_              VARCHAR2(64),
                          CONSTRAINT PK_SYS_DIC PRIMARY KEY (DIC_ID_)
);

COMMENT ON TABLE SYS_DIC IS
'????????????';

COMMENT ON COLUMN SYS_DIC.DIC_ID_ IS
'??????';

COMMENT ON COLUMN SYS_DIC.TREE_ID_ IS
'??????Id';

COMMENT ON COLUMN SYS_DIC.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_DIC.VALUE_ IS
'??????';

COMMENT ON COLUMN SYS_DIC.DESCP_ IS
'??????';

COMMENT ON COLUMN SYS_DIC.SN_ IS
'??????';

COMMENT ON COLUMN SYS_DIC.PATH_ IS
'??????';

COMMENT ON COLUMN SYS_DIC.PARENT_ID_ IS
'???ID';

COMMENT ON COLUMN SYS_DIC.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_DIC.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_DIC.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_DIC.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_DIC.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_DIC.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_DIC.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: SYS_ERROR_LOG                                         */
/*==============================================================*/
CREATE TABLE SYS_ERROR_LOG  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                TRACE_ID_            VARCHAR2(64),
                                APP_NAME_            VARCHAR2(64),
                                URL_                 VARCHAR2(255),
                                CONTENT_             CLOB,
                                CREATE_DEP_ID_       VARCHAR2(64),
                                TENANT_ID_           VARCHAR2(64),
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                UPDATE_BY_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                CONSTRAINT PK_SYS_ERROR_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_ERROR_LOG IS
'????????????';

COMMENT ON COLUMN SYS_ERROR_LOG.ID_ IS
'??????';

COMMENT ON COLUMN SYS_ERROR_LOG.TRACE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_ERROR_LOG.APP_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_ERROR_LOG.URL_ IS
'????????????';

COMMENT ON COLUMN SYS_ERROR_LOG.CONTENT_ IS
'????????????';

COMMENT ON COLUMN SYS_ERROR_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_ERROR_LOG.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_ERROR_LOG.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_ERROR_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_ERROR_LOG.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_ERROR_LOG.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_EXCEL                                             */
/*==============================================================*/
CREATE TABLE SYS_EXCEL  (
                            ID_                  VARCHAR2(64)                    NOT NULL,
                            NAME_                VARCHAR2(64),
                            KEY_                 VARCHAR2(64),
                            TEMPLATE_ID_         VARCHAR2(64),
                            COMMENT_             VARCHAR2(255),
                            GRID_DATA_           CLOB,
                            FIELD_               CLOB,
                            TENANT_ID_           VARCHAR2(64),
                            CREATE_DEP_ID_       VARCHAR2(64),
                            CREATE_BY_           VARCHAR2(64),
                            CREATE_TIME_         DATE,
                            UPDATE_BY_           VARCHAR2(64),
                            UPDATE_TIME_         DATE,
                            CONSTRAINT PK_SYS_EXCEL PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_EXCEL IS
'EXCEL??????';

COMMENT ON COLUMN SYS_EXCEL.ID_ IS
'??????';

COMMENT ON COLUMN SYS_EXCEL.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_EXCEL.KEY_ IS
'??????';

COMMENT ON COLUMN SYS_EXCEL.TEMPLATE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_EXCEL.COMMENT_ IS
'??????';

COMMENT ON COLUMN SYS_EXCEL.GRID_DATA_ IS
'???????????????';

COMMENT ON COLUMN SYS_EXCEL.FIELD_ IS
'Excel?????????';

COMMENT ON COLUMN SYS_EXCEL.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_EXCEL.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_EXCEL.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_EXCEL.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_EXCEL.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_EXCEL.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_EXCEL_BATMANAGE                                   */
/*==============================================================*/
CREATE TABLE SYS_EXCEL_BATMANAGE  (
                                      ID_                  VARCHAR2(64),
                                      TABLE_               VARCHAR2(64),
                                      DS_ALIAS_            VARCHAR2(64),
                                      TEMPLATE_ID_         VARCHAR2(64),
                                      BAT_ID_              VARCHAR2(64),
                                      TENANT_ID_           VARCHAR2(64),
                                      CREATE_DEP_ID_       VARCHAR2(64),
                                      CREATE_BY_           VARCHAR2(64),
                                      CREATE_TIME_         DATE,
                                      UPDATE_BY_           VARCHAR2(64),
                                      UPDATE_TIME_         DATE
);

COMMENT ON TABLE SYS_EXCEL_BATMANAGE IS
'EXCEL??????????????????';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.ID_ IS
'??????';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.TABLE_ IS
'??????';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.DS_ALIAS_ IS
'??????';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.TEMPLATE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.BAT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_EXCEL_LOG                                         */
/*==============================================================*/
CREATE TABLE SYS_EXCEL_LOG  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                TEMPLATED_           VARCHAR2(64),
                                LOG_                 CLOB,
                                TENANT_ID_           VARCHAR2(64),
                                CREATE_DEP_ID_       VARCHAR2(64),
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                UPDATE_BY_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                CONSTRAINT PK_SYS_EXCEL_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_EXCEL_LOG IS
'??????????????????';

COMMENT ON COLUMN SYS_EXCEL_LOG.ID_ IS
'??????';

COMMENT ON COLUMN SYS_EXCEL_LOG.TEMPLATED_ IS
'??????ID';

COMMENT ON COLUMN SYS_EXCEL_LOG.LOG_ IS
'??????';

COMMENT ON COLUMN SYS_EXCEL_LOG.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_EXCEL_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_EXCEL_LOG.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_EXCEL_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_EXCEL_LOG.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_EXCEL_LOG.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_FILE                                              */
/*==============================================================*/
CREATE TABLE SYS_FILE  (
                           FILE_ID_             VARCHAR2(64)                    NOT NULL,
                           TYPE_ID_             VARCHAR2(64),
                           FILE_NAME_           VARCHAR2(200),
                           NEW_FNAME_           VARCHAR2(200),
                           PATH_                VARCHAR2(255),
                           THUMBNAIL_           VARCHAR2(255),
                           EXT_                 VARCHAR2(32),
                           MINE_TYPE_           VARCHAR2(50),
                           DESC_                VARCHAR2(255),
                           TOTAL_BYTES_         INTEGER,
                           DEL_STATUS_          VARCHAR2(20),
                           FROM_                VARCHAR2(20),
                           COVER_STATUS_        VARCHAR2(20),
                           FILE_SYSTEM_         VARCHAR2(20),
                           PDF_PATH_            VARCHAR2(255),
                           TENANT_ID_           VARCHAR2(64),
                           CREATE_DEP_ID_       VARCHAR2(64),
                           CREATE_BY_           VARCHAR2(64),
                           CREATE_TIME_         DATE,
                           UPDATE_BY_           VARCHAR2(64),
                           UPDATE_TIME_         DATE,
                           CONSTRAINT PK_SYS_FILE PRIMARY KEY (FILE_ID_)
);

COMMENT ON TABLE SYS_FILE IS
'????????????';

COMMENT ON COLUMN SYS_FILE.FILE_ID_ IS
'??????';

COMMENT ON COLUMN SYS_FILE.TYPE_ID_ IS
'??????Id';

COMMENT ON COLUMN SYS_FILE.FILE_NAME_ IS
'?????????';

COMMENT ON COLUMN SYS_FILE.NEW_FNAME_ IS
'????????????';

COMMENT ON COLUMN SYS_FILE.PATH_ IS
'??????';

COMMENT ON COLUMN SYS_FILE.THUMBNAIL_ IS
'?????????';

COMMENT ON COLUMN SYS_FILE.EXT_ IS
'?????????';

COMMENT ON COLUMN SYS_FILE.MINE_TYPE_ IS
'????????????';

COMMENT ON COLUMN SYS_FILE.DESC_ IS
'??????';

COMMENT ON COLUMN SYS_FILE.TOTAL_BYTES_ IS
'????????????';

COMMENT ON COLUMN SYS_FILE.DEL_STATUS_ IS
'????????????';

COMMENT ON COLUMN SYS_FILE.FROM_ IS
'??????';

COMMENT ON COLUMN SYS_FILE.COVER_STATUS_ IS
'???????????????PDF)';

COMMENT ON COLUMN SYS_FILE.FILE_SYSTEM_ IS
'????????????';

COMMENT ON COLUMN SYS_FILE.PDF_PATH_ IS
'PDF????????????';

COMMENT ON COLUMN SYS_FILE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_FILE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_FILE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_FILE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_FILE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_FILE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_INFORM                                            */
/*==============================================================*/
CREATE TABLE SYS_INFORM  (
                             ID_                  VARCHAR2(64)                    NOT NULL,
                             NAME_                VARCHAR2(64),
                             DESCP_               VARCHAR2(255),
                             TREE_ID_             VARCHAR2(64),
                             ACTION_              VARCHAR2(64),
                             TEXT_TEMPLATE_       VARCHAR2(2000),
                             RICH_TEXT_TEMPLATE   CLOB,
                             BO_DEF_ID_           VARCHAR2(64),
                             TENANT_ID_           VARCHAR2(64),
                             CREATE_DEP_ID_       VARCHAR2(64),
                             CREATE_BY_           VARCHAR2(64),
                             CREATE_TIME_         DATE,
                             UPDATE_BY_           VARCHAR2(64),
                             UPDATE_TIME_         DATE,
                             APP_ID_              VARCHAR2(64),
                             CONSTRAINT PK_SYS_INFORM PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_INFORM IS
'???????????????';

COMMENT ON COLUMN SYS_INFORM.ID_ IS
'??????';

COMMENT ON COLUMN SYS_INFORM.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_INFORM.DESCP_ IS
'??????';

COMMENT ON COLUMN SYS_INFORM.TREE_ID_ IS
'??????';

COMMENT ON COLUMN SYS_INFORM.ACTION_ IS
'????????????';

COMMENT ON COLUMN SYS_INFORM.TEXT_TEMPLATE_ IS
'???????????????';

COMMENT ON COLUMN SYS_INFORM.RICH_TEXT_TEMPLATE IS
'????????????';

COMMENT ON COLUMN SYS_INFORM.BO_DEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_INFORM.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_INFORM.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_INFORM.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INFORM.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INFORM.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INFORM.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INFORM.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: SYS_INFORM_PDF                                        */
/*==============================================================*/
CREATE TABLE SYS_INFORM_PDF  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 NAME_                VARCHAR2(64),
                                 KEY_                 VARCHAR2(40),
                                 TREE_ID_             VARCHAR2(64),
                                 BO_DEF_ID_           VARCHAR2(64),
                                 PDF_HTML_            CLOB,
                                 TENANT_ID_           VARCHAR2(64),
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 APP_ID_              VARCHAR2(64),
                                 CONSTRAINT PK_SYS_INFORM_PDF PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_INFORM_PDF IS
'?????????????????????';

COMMENT ON COLUMN SYS_INFORM_PDF.ID_ IS
'??????';

COMMENT ON COLUMN SYS_INFORM_PDF.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_INFORM_PDF.KEY_ IS
'??????';

COMMENT ON COLUMN SYS_INFORM_PDF.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INFORM_PDF.BO_DEF_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_INFORM_PDF.PDF_HTML_ IS
'????????????';

COMMENT ON COLUMN SYS_INFORM_PDF.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_INFORM_PDF.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_INFORM_PDF.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INFORM_PDF.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INFORM_PDF.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INFORM_PDF.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INFORM_PDF.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: SYS_INTERFACE_API                                     */
/*==============================================================*/
CREATE TABLE SYS_INTERFACE_API  (
                                    API_ID_              VARCHAR2(64)                    NOT NULL,
                                    API_NAME_            VARCHAR2(64)                   DEFAULT NULL,
                                    CLASSIFICATION_ID_   VARCHAR2(64)                   DEFAULT NULL,
                                    PROJECT_ID_          VARCHAR2(64)                   DEFAULT NULL,
                                    API_TYPE_            VARCHAR2(10)                   DEFAULT NULL,
                                    API_PATH_            VARCHAR2(120)                  DEFAULT NULL,
                                    API_METHOD_          VARCHAR2(10)                   DEFAULT NULL,
                                    STATUS_              VARCHAR2(10)                   DEFAULT NULL,
                                    API_PATH_PARAMS_     CLOB,
                                    API_HEADERS_         CLOB,
                                    API_QUERY_           CLOB,
                                    API_BODY_            CLOB,
                                    API_DATA_TYPE_       VARCHAR2(10)                   DEFAULT NULL,
                                    API_RETURN_TYPE_     VARCHAR2(10)                   DEFAULT NULL,
                                    API_RETURN_FIELDS_   CLOB,
                                    JAVA_TYPE_           VARCHAR2(10)                   DEFAULT NULL,
                                    JAVA_CODE_           CLOB,
                                    JAVA_BEAN_           VARCHAR2(64)                   DEFAULT NULL,
                                    DESCRIPTION_         VARCHAR2(255)                  DEFAULT NULL,
                                    TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                    CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                    CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                    CREATE_TIME_         DATE                           DEFAULT NULL,
                                    UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                    UPDATE_TIME_         DATE                           DEFAULT NULL,
                                    IS_LOG_              VARCHAR2(10),
                                    CONSTRAINT PK_SYS_INTERFACE_API PRIMARY KEY (API_ID_)
);

COMMENT ON TABLE SYS_INTERFACE_API IS
'??????API???';

COMMENT ON COLUMN SYS_INTERFACE_API.API_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INTERFACE_API.API_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.CLASSIFICATION_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INTERFACE_API.PROJECT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INTERFACE_API.API_TYPE_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.API_PATH_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.API_METHOD_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.STATUS_ IS
'??????';

COMMENT ON COLUMN SYS_INTERFACE_API.API_PATH_PARAMS_ IS
'??????????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.API_HEADERS_ IS
'???????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.API_QUERY_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.API_BODY_ IS
'???????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.API_DATA_TYPE_ IS
'?????????????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.API_RETURN_TYPE_ IS
'??????????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.API_RETURN_FIELDS_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.JAVA_TYPE_ IS
'??????????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.JAVA_CODE_ IS
'JAVA??????';

COMMENT ON COLUMN SYS_INTERFACE_API.JAVA_BEAN_ IS
'?????????BEAN';

COMMENT ON COLUMN SYS_INTERFACE_API.DESCRIPTION_ IS
'??????';

COMMENT ON COLUMN SYS_INTERFACE_API.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_INTERFACE_API.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_INTERFACE_API.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INTERFACE_API.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INTERFACE_API.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_API.IS_LOG_ IS
'??????????????????';

/*==============================================================*/
/* Table: SYS_INTERFACE_CALL_LOGS                               */
/*==============================================================*/
CREATE TABLE SYS_INTERFACE_CALL_LOGS  (
                                          LOG_ID_              VARCHAR2(64)                    NOT NULL,
                                          INTERFACE_ID_        VARCHAR2(64)                   DEFAULT NULL,
                                          LOG_URL_             VARCHAR2(120)                  DEFAULT NULL,
                                          LOG_HEADERS_         CLOB,
                                          LOG_QUERY_           CLOB,
                                          LOG_BODY_            CLOB,
                                          RESPONSE_STATE_      VARCHAR2(10)                   DEFAULT NULL,
                                          RESPONSE_DATA_       CLOB,
                                          TIME_CONSUMING_      VARCHAR2(64)                   DEFAULT NULL,
                                          ERROR_MESSAGE_       CLOB,
                                          TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                          CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                          CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                          CREATE_TIME_         DATE                           DEFAULT NULL,
                                          UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                          UPDATE_TIME_         DATE                           DEFAULT NULL,
                                          CONSTRAINT PK_SYS_INTERFACE_CALL_LOGS PRIMARY KEY (LOG_ID_)
);

COMMENT ON TABLE SYS_INTERFACE_CALL_LOGS IS
'?????????????????????';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.LOG_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.INTERFACE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.LOG_URL_ IS
'??????????????????';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.LOG_HEADERS_ IS
'???????????????';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.LOG_QUERY_ IS
'??????????????????';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.LOG_BODY_ IS
'???????????????';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.RESPONSE_STATE_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.RESPONSE_DATA_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.TIME_CONSUMING_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.ERROR_MESSAGE_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_INTERFACE_CLASS                                   */
/*==============================================================*/
CREATE TABLE SYS_INTERFACE_CLASS  (
                                      CLASSIFICATION_ID_   VARCHAR2(64)                    NOT NULL,
                                      CLASSIFICATION_NAME_ VARCHAR2(64)                   DEFAULT NULL,
                                      DESCRIPTION_         VARCHAR2(255)                  DEFAULT NULL,
                                      PROJECT_ID_          VARCHAR2(64)                   DEFAULT NULL,
                                      TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                      CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                      CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                      CREATE_TIME_         DATE                           DEFAULT NULL,
                                      UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                      UPDATE_TIME_         DATE                           DEFAULT NULL,
                                      CONSTRAINT PK_SYS_INTERFACE_CLASS PRIMARY KEY (CLASSIFICATION_ID_)
);

COMMENT ON TABLE SYS_INTERFACE_CLASS IS
'???????????????';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.CLASSIFICATION_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.CLASSIFICATION_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.DESCRIPTION_ IS
'??????';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.PROJECT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_INTERFACE_PROJECT                                 */
/*==============================================================*/
CREATE TABLE SYS_INTERFACE_PROJECT  (
                                        PROJECT_ID_          VARCHAR2(64)                    NOT NULL,
                                        PROJECT_ALIAS_       VARCHAR2(64)                   DEFAULT NULL,
                                        PROJECT_NAME_        VARCHAR2(64)                   DEFAULT NULL,
                                        TREE_ID_             VARCHAR2(64)                   DEFAULT NULL,
                                        DESCRIPTION_         VARCHAR2(255)                  DEFAULT NULL,
                                        DOMAIN_TCP_          VARCHAR2(10)                   DEFAULT NULL,
                                        DOMAIN_PATH_         VARCHAR2(120)                  DEFAULT NULL,
                                        BASE_PATH_           VARCHAR2(64)                   DEFAULT NULL,
                                        STATUS_              VARCHAR2(10)                   DEFAULT NULL,
                                        GLOBAL_HEADERS_      CLOB,
                                        TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                        CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                        CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                        CREATE_TIME_         DATE                           DEFAULT NULL,
                                        UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                        UPDATE_TIME_         DATE                           DEFAULT NULL,
                                        CONSTRAINT PK_SYS_INTERFACE_PROJECT PRIMARY KEY (PROJECT_ID_)
);

COMMENT ON TABLE SYS_INTERFACE_PROJECT IS
'???????????????';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.PROJECT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.PROJECT_ALIAS_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.PROJECT_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.DESCRIPTION_ IS
'??????';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.DOMAIN_TCP_ IS
'??????????????????';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.DOMAIN_PATH_ IS
'??????????????????';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.BASE_PATH_ IS
'??????????????????';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.STATUS_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.GLOBAL_HEADERS_ IS
'???????????????';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_INVOKE_SCRIPT                                     */
/*==============================================================*/
CREATE TABLE SYS_INVOKE_SCRIPT  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    TREE_ID_             VARCHAR2(64),
                                    NAME_                VARCHAR2(64),
                                    ALIAS_               VARCHAR2(20),
                                    PARAMS_              VARCHAR2(400),
                                    CONTENT_             CLOB,
                                    DESCP_               VARCHAR2(255),
                                    TENANT_ID_           VARCHAR2(64),
                                    CREATE_DEP_ID_       VARCHAR2(64),
                                    CREATE_BY_           VARCHAR2(64),
                                    CREATE_TIME_         DATE,
                                    UPDATE_BY_           VARCHAR2(64),
                                    UPDATE_TIME_         DATE,
                                    APP_ID_              VARCHAR2(64),
                                    CONSTRAINT PK_SYS_INVOKE_SCRIPT PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_INVOKE_SCRIPT IS
'??????????????????';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.ID_ IS
'??????';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.ALIAS_ IS
'??????';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.PARAMS_ IS
'????????????';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.CONTENT_ IS
'????????????';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.DESCP_ IS
'??????';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: SYS_JOB                                               */
/*==============================================================*/
CREATE TABLE SYS_JOB  (
                          ID_                  VARCHAR2(64)                    NOT NULL,
                          NAME_                VARCHAR2(64),
                          JOB_TASK_            VARCHAR2(64),
                          JOB_TASK_ID_         VARCHAR2(64),
                          STRATEGY_            VARCHAR2(255)                   NOT NULL,
                          STATUS_              INTEGER,
                          CREATE_DEP_ID_       VARCHAR2(64),
                          TENANT_ID_           VARCHAR2(64),
                          CREATE_BY_           VARCHAR2(64),
                          CREATE_TIME_         DATE,
                          UPDATE_BY_           VARCHAR2(64),
                          UPDATE_TIME_         DATE,
                          CONSTRAINT PK_SYS_JOB PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_JOB IS
'??????????????????';

COMMENT ON COLUMN SYS_JOB.ID_ IS
'??????';

COMMENT ON COLUMN SYS_JOB.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_JOB.JOB_TASK_ IS
'JOB??????ID';

COMMENT ON COLUMN SYS_JOB.JOB_TASK_ID_ IS
'JOB??????ID';

COMMENT ON COLUMN SYS_JOB.STRATEGY_ IS
'??????';

COMMENT ON COLUMN SYS_JOB.STATUS_ IS
'??????(0?????????,1?????????)';

COMMENT ON COLUMN SYS_JOB.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_JOB.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_JOB.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_JOB.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_JOB.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_JOB.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_JOB_TASK                                          */
/*==============================================================*/
CREATE TABLE SYS_JOB_TASK  (
                               ID_                  VARCHAR2(64)                    NOT NULL,
                               NAME_                VARCHAR2(64),
                               TYPE_                VARCHAR2(40),
                               CONTENT_             CLOB,
                               STATUS_              VARCHAR2(40),
                               UPDATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CONSTRAINT PK_SYS_JOB_TASK PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_JOB_TASK IS
'??????????????????';

COMMENT ON COLUMN SYS_JOB_TASK.ID_ IS
'??????';

COMMENT ON COLUMN SYS_JOB_TASK.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_JOB_TASK.TYPE_ IS
'?????? (JOB,SCRIPT,CLASS)';

COMMENT ON COLUMN SYS_JOB_TASK.CONTENT_ IS
'????????????';

COMMENT ON COLUMN SYS_JOB_TASK.STATUS_ IS
'??????';

COMMENT ON COLUMN SYS_JOB_TASK.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_JOB_TASK.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_JOB_TASK.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_JOB_TASK.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_JOB_TASK.CREATE_BY_ IS
'?????????';

/*==============================================================*/
/* Table: SYS_KETTLE_DBDEF                                      */
/*==============================================================*/
CREATE TABLE SYS_KETTLE_DBDEF  (
                                   ID_                  VARCHAR2(64)                    NOT NULL,
                                   NAME_                VARCHAR2(64),
                                   RES_USER_            VARCHAR2(64),
                                   RES_PWD_             VARCHAR2(64),
                                   DB_TYPE_             VARCHAR2(20),
                                   HOST_                VARCHAR2(64),
                                   PORT_                VARCHAR2(20),
                                   DATABASE_            VARCHAR2(20),
                                   USER_                VARCHAR2(40),
                                   PASSWORD_            VARCHAR2(64),
                                   COMMENT_             VARCHAR2(500),
                                   STATUS_              INTEGER,
                                   TENANT_ID_           VARCHAR2(64),
                                   CREATE_DEP_ID_       VARCHAR2(64),
                                   CREATE_BY_           VARCHAR2(64),
                                   CREATE_TIME_         DATE,
                                   UPDATE_BY_           VARCHAR2(64),
                                   UPDATE_TIME_         DATE,
                                   CONSTRAINT PK_SYS_KETTLE_DBDEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_KETTLE_DBDEF IS
'KETTLE???????????????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.ID_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.RES_USER_ IS
'???????????????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.RES_PWD_ IS
'???????????????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.DB_TYPE_ IS
'???????????????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.HOST_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.PORT_ IS
'?????????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.DATABASE_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.USER_ IS
'?????????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.PASSWORD_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.COMMENT_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.STATUS_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_KETTLE_DEF                                        */
/*==============================================================*/
CREATE TABLE SYS_KETTLE_DEF  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 NAME_                VARCHAR2(64),
                                 GATEGORY_            VARCHAR2(64),
                                 TYPE_                VARCHAR2(40),
                                 STORE_TYPE_          VARCHAR2(40),
                                 PARAMETERS_          VARCHAR2(500),
                                 STORE_SETTING_       VARCHAR2(400),
                                 TENANT_ID_           VARCHAR2(64),
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 CONSTRAINT PK_SYS_KETTLE_DEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_KETTLE_DEF IS
'KETTLE??????';

COMMENT ON COLUMN SYS_KETTLE_DEF.ID_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_DEF.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_DEF.GATEGORY_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_DEF.TYPE_ IS
'??????(job,trans)';

COMMENT ON COLUMN SYS_KETTLE_DEF.STORE_TYPE_ IS
'????????????(??????:file????????????:resource)';

COMMENT ON COLUMN SYS_KETTLE_DEF.PARAMETERS_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_DEF.STORE_SETTING_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_DEF.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_KETTLE_DEF.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_KETTLE_DEF.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_KETTLE_DEF.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_DEF.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_KETTLE_DEF.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_KETTLE_JOB                                        */
/*==============================================================*/
CREATE TABLE SYS_KETTLE_JOB  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 NAME_                VARCHAR2(64),
                                 KETTLE_ID_           VARCHAR2(64),
                                 STRATEGY_            VARCHAR2(255)                   NOT NULL,
                                 LOGLEVEL_            VARCHAR2(64),
                                 STATUS_              INTEGER,
                                 REMARK_              VARCHAR2(255),
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 TENANT_ID_           VARCHAR2(64),
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 CONSTRAINT PK_SYS_KETTLE_JOB PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_KETTLE_JOB IS
'KETTLE??????';

COMMENT ON COLUMN SYS_KETTLE_JOB.ID_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_JOB.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_JOB.KETTLE_ID_ IS
'KETTLE??????ID';

COMMENT ON COLUMN SYS_KETTLE_JOB.STRATEGY_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_JOB.LOGLEVEL_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_JOB.STATUS_ IS
'??????(0?????????,1?????????)';

COMMENT ON COLUMN SYS_KETTLE_JOB.REMARK_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_JOB.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_KETTLE_JOB.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_KETTLE_JOB.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_KETTLE_JOB.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_JOB.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_KETTLE_JOB.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_KETTLE_LOG                                        */
/*==============================================================*/
CREATE TABLE SYS_KETTLE_LOG  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 KETTLE_ID_           VARCHAR2(64),
                                 DRUATION_            INTEGER,
                                 STATUS_              INTEGER,
                                 LOG_                 CLOB,
                                 TENANT_ID_           VARCHAR2(64),
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 KETTLE_JOB_ID_       VARCHAR2(64),
                                 KETTLE_JOB_NAME_     VARCHAR2(64),
                                 KETTLE_TYPE_         VARCHAR2(64),
                                 START_TIME_          DATE,
                                 END_TIME_            DATE,
                                 CONSTRAINT PK_SYS_KETTLE_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_KETTLE_LOG IS
'KETTLE??????';

COMMENT ON COLUMN SYS_KETTLE_LOG.ID_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_LOG.KETTLE_ID_ IS
'KETTLE??????ID';

COMMENT ON COLUMN SYS_KETTLE_LOG.DRUATION_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_LOG.STATUS_ IS
'??????(1.??????,0??????)';

COMMENT ON COLUMN SYS_KETTLE_LOG.LOG_ IS
'??????';

COMMENT ON COLUMN SYS_KETTLE_LOG.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_KETTLE_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_KETTLE_LOG.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_KETTLE_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_LOG.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_KETTLE_LOG.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_LOG.KETTLE_JOB_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_KETTLE_LOG.KETTLE_JOB_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_KETTLE_LOG.KETTLE_TYPE_ IS
'Kettle??????';

COMMENT ON COLUMN SYS_KETTLE_LOG.START_TIME_ IS
'??????????????????';

COMMENT ON COLUMN SYS_KETTLE_LOG.END_TIME_ IS
'??????????????????';

/*==============================================================*/
/* Table: SYS_LOG                                               */
/*==============================================================*/
CREATE TABLE SYS_LOG  (
                          ID_                  VARCHAR2(64)                    NOT NULL,
                          APP_NAME_            VARCHAR2(64),
                          MODULE_              VARCHAR2(64),
                          SUB_MODULE_          VARCHAR2(64),
                          CLASS_NAME_          VARCHAR2(128),
                          METHOD_NAME_         VARCHAR2(64),
                          ACTION_              VARCHAR2(64),
                          PK_VALUE_            VARCHAR2(64),
                          IP_                  VARCHAR2(40),
                          DETAIL_              CLOB,
                          OPERATION_           VARCHAR2(64),
                          USER_NAME_           VARCHAR2(40),
                          DURATION_            INTEGER,
                          CREATE_DEP_ID_       VARCHAR2(64),
                          TENANT_ID_           VARCHAR2(64),
                          CREATE_BY_           VARCHAR2(64),
                          CREATE_TIME_         DATE,
                          UPDATE_BY_           VARCHAR2(64),
                          UPDATE_TIME_         DATE,
                          BUS_TYPE_            VARCHAR2(32),
                          IS_RESUME_           VARCHAR2(4),
                          CONSTRAINT PK_SYS_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_LOG IS
'????????????';

COMMENT ON COLUMN SYS_LOG.ID_ IS
'??????';

COMMENT ON COLUMN SYS_LOG.APP_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_LOG.MODULE_ IS
'????????????';

COMMENT ON COLUMN SYS_LOG.SUB_MODULE_ IS
'?????????';

COMMENT ON COLUMN SYS_LOG.CLASS_NAME_ IS
'??????';

COMMENT ON COLUMN SYS_LOG.METHOD_NAME_ IS
'?????????';

COMMENT ON COLUMN SYS_LOG.ACTION_ IS
'??????';

COMMENT ON COLUMN SYS_LOG.PK_VALUE_ IS
'????????????';

COMMENT ON COLUMN SYS_LOG.IP_ IS
'??????IP';

COMMENT ON COLUMN SYS_LOG.DETAIL_ IS
'????????????';

COMMENT ON COLUMN SYS_LOG.OPERATION_ IS
'??????';

COMMENT ON COLUMN SYS_LOG.USER_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_LOG.DURATION_ IS
'????????????';

COMMENT ON COLUMN SYS_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_LOG.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_LOG.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_LOG.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_LOG.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_LOG.BUS_TYPE_ IS
'????????????';

COMMENT ON COLUMN SYS_LOG.IS_RESUME_ IS
'???????????????';

/*==============================================================*/
/* Table: SYS_MENU                                              */
/*==============================================================*/
CREATE TABLE SYS_MENU  (
                           MENU_ID_             VARCHAR2(64)                    NOT NULL,
                           APP_ID_              VARCHAR2(64),
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
                           CONSTRAINT PK_SYS_MENU PRIMARY KEY (MENU_ID_)
);

COMMENT ON TABLE SYS_MENU IS
'????????????';

COMMENT ON COLUMN SYS_MENU.MENU_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_MENU.APP_ID_ IS
'???????????????';

COMMENT ON COLUMN SYS_MENU.NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU.ICON_PC_ IS
'PC????????????';

COMMENT ON COLUMN SYS_MENU.ICON_PIC_ IS
'??????';

COMMENT ON COLUMN SYS_MENU.ICON_APP_ IS
'APP????????????';

COMMENT ON COLUMN SYS_MENU.PARENT_ID_ IS
'?????????ID';

COMMENT ON COLUMN SYS_MENU.PATH_ IS
'??????';

COMMENT ON COLUMN SYS_MENU.SN_ IS
'??????';

COMMENT ON COLUMN SYS_MENU.SHOW_TYPE_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU.MENU_KEY_ IS
'??????????????????';

COMMENT ON COLUMN SYS_MENU.MENU_TYPE_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU.COMPONENT_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU.SETTING_TYPE_ IS
'????????????(custom,iframe)';

COMMENT ON COLUMN SYS_MENU.BO_LIST_KEY_ IS
'????????????KEY';

COMMENT ON COLUMN SYS_MENU.URL_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU.METHOD_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU.PARAMS_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_MENU.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_MENU.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_MENU.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_MENU.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_MENU_RELEASE                                      */
/*==============================================================*/
CREATE TABLE SYS_MENU_RELEASE  (
                                   ID_                  VARCHAR2(64)                    NOT NULL,
                                   RELEASE_ID_          VARCHAR2(64),
                                   MENU_ID_             VARCHAR2(64),
                                   MENU_NAME_           VARCHAR2(64),
                                   RELEASE_URL_         VARCHAR2(100),
                                   MENU_URL_            VARCHAR2(100),
                                   TENANT_ID_           VARCHAR2(64),
                                   CREATE_DEP_ID_       VARCHAR2(64),
                                   CREATE_BY_           VARCHAR2(64),
                                   CREATE_TIME_         DATE,
                                   UPDATE_BY_           VARCHAR2(64),
                                   UPDATE_TIME_         DATE,
                                   CONSTRAINT PK_SYS_MENU_RELEASE PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_MENU_RELEASE IS
'???????????????????????????';

COMMENT ON COLUMN SYS_MENU_RELEASE.ID_ IS
'??????';

COMMENT ON COLUMN SYS_MENU_RELEASE.RELEASE_ID_ IS
'BO??????ID';

COMMENT ON COLUMN SYS_MENU_RELEASE.MENU_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_MENU_RELEASE.MENU_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU_RELEASE.RELEASE_URL_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU_RELEASE.MENU_URL_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU_RELEASE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_MENU_RELEASE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_MENU_RELEASE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_MENU_RELEASE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_MENU_RELEASE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_MENU_RELEASE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_OFFICE                                            */
/*==============================================================*/
CREATE TABLE SYS_OFFICE  (
                             ID_                  VARCHAR2(64)                    NOT NULL,
                             NAME_                VARCHAR2(64),
                             SUPPORT_VERSION_     VARCHAR2(64),
                             VERSION_             INTEGER,
                             CREATE_DEP_ID_       VARCHAR2(64),
                             TENANT_ID_           VARCHAR2(64),
                             CREATE_BY_           VARCHAR2(64),
                             CREATE_TIME_         DATE,
                             UPDATE_BY_           VARCHAR2(64),
                             UPDATE_TIME_         DATE,
                             CONSTRAINT PK_SYS_OFFICE PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_OFFICE IS
'office???';

COMMENT ON COLUMN SYS_OFFICE.ID_ IS
'??????';

COMMENT ON COLUMN SYS_OFFICE.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_OFFICE.SUPPORT_VERSION_ IS
'??????????????????';

COMMENT ON COLUMN SYS_OFFICE.VERSION_ IS
'??????';

COMMENT ON COLUMN SYS_OFFICE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_OFFICE.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_OFFICE.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_OFFICE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_OFFICE.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_OFFICE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_OFFICE_TEMPLATE                                   */
/*==============================================================*/
CREATE TABLE SYS_OFFICE_TEMPLATE  (
                                      ID_                  VARCHAR2(64)                    NOT NULL,
                                      NAME_                VARCHAR2(200),
                                      CREATE_TIME_         DATE,
                                      DOC_NAME_            VARCHAR2(255),
                                      DESCRIPTION_         VARCHAR2(255),
                                      UPDATE_TIME_         DATE,
                                      UPDATE_BY_           VARCHAR2(64),
                                      TYPE_                VARCHAR2(20),
                                      DOC_ID_              VARCHAR2(64),
                                      CREATE_DEP_ID_       VARCHAR2(64),
                                      TENANT_ID_           VARCHAR2(64),
                                      CREATE_BY_           VARCHAR2(64),
                                      CONSTRAINT PK_SYS_OFFICE_TEMPLATE PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_OFFICE_TEMPLATE IS
'office?????????';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.ID_ IS
'??????';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.DOC_NAME_ IS
'?????????';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.DESCRIPTION_ IS
'??????';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.TYPE_ IS
'??????(normal,red)';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.DOC_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.TENANT_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.CREATE_BY_ IS
'?????????';

/*==============================================================*/
/* Table: SYS_OFFICE_VER                                        */
/*==============================================================*/
CREATE TABLE SYS_OFFICE_VER  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 OFFICE_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                 VERSION_             INTEGER                        DEFAULT NULL,
                                 FILE_ID_             VARCHAR2(64)                   DEFAULT NULL,
                                 FILE_NAME_           VARCHAR2(255)                  DEFAULT NULL,
                                 CREATE_DEP_ID_       VARCHAR2(64)                   DEFAULT NULL,
                                 CREATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                 CREATE_TIME_         DATE                           DEFAULT NULL,
                                 UPDATE_TIME_         DATE                           DEFAULT NULL,
                                 UPDATE_BY_           VARCHAR2(64)                   DEFAULT NULL,
                                 TENANT_ID_           VARCHAR2(64)                   DEFAULT NULL,
                                 CONSTRAINT PK_SYS_OFFICE_VER PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_OFFICE_VER IS
'office?????????';

COMMENT ON COLUMN SYS_OFFICE_VER.ID_ IS
'??????';

COMMENT ON COLUMN SYS_OFFICE_VER.OFFICE_ID_ IS
'OFFICE??????';

COMMENT ON COLUMN SYS_OFFICE_VER.VERSION_ IS
'??????';

COMMENT ON COLUMN SYS_OFFICE_VER.FILE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_OFFICE_VER.FILE_NAME_ IS
'?????????';

COMMENT ON COLUMN SYS_OFFICE_VER.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_OFFICE_VER.CREATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_OFFICE_VER.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_OFFICE_VER.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_OFFICE_VER.UPDATE_BY_ IS
'?????????';

COMMENT ON COLUMN SYS_OFFICE_VER.TENANT_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: SYS_PROPERTIES                                        */
/*==============================================================*/
CREATE TABLE SYS_PROPERTIES  (
                                 PRO_ID_              VARCHAR2(64)                    NOT NULL,
                                 NAME_                VARCHAR2(64)                    NOT NULL,
                                 ALIAS_               VARCHAR2(64)                    NOT NULL,
                                 GLOBAL_              VARCHAR2(64),
                                 ENCRYPT_             VARCHAR2(64),
                                 VALUE_               VARCHAR2(2000),
                                 CATEGORY_            VARCHAR2(100),
                                 DESCRIPTION_         VARCHAR2(200),
                                 TENANT_ID_           VARCHAR2(64),
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 CONSTRAINT PK_SYS_PROPERTIES PRIMARY KEY (PRO_ID_)
);

COMMENT ON TABLE SYS_PROPERTIES IS
'???????????????';

COMMENT ON COLUMN SYS_PROPERTIES.PRO_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_PROPERTIES.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_PROPERTIES.ALIAS_ IS
'??????';

COMMENT ON COLUMN SYS_PROPERTIES.GLOBAL_ IS
'????????????';

COMMENT ON COLUMN SYS_PROPERTIES.ENCRYPT_ IS
'????????????';

COMMENT ON COLUMN SYS_PROPERTIES.VALUE_ IS
'?????????';

COMMENT ON COLUMN SYS_PROPERTIES.CATEGORY_ IS
'??????';

COMMENT ON COLUMN SYS_PROPERTIES.DESCRIPTION_ IS
'??????';

COMMENT ON COLUMN SYS_PROPERTIES.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_PROPERTIES.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_PROPERTIES.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_PROPERTIES.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_PROPERTIES.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_PROPERTIES.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_ROUTE_TYPE                                        */
/*==============================================================*/
CREATE TABLE SYS_ROUTE_TYPE  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 ROUTE_TYPE_NAME_     VARCHAR2(64),
                                 DESCRIBE_            VARCHAR2(255),
                                 TENANT_ID_           VARCHAR2(64),
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 CONSTRAINT PK_SYS_ROUTE_TYPE PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_ROUTE_TYPE IS
'??????????????????';

COMMENT ON COLUMN SYS_ROUTE_TYPE.ID_ IS
'??????';

COMMENT ON COLUMN SYS_ROUTE_TYPE.ROUTE_TYPE_NAME_ IS
'??????';

COMMENT ON COLUMN SYS_ROUTE_TYPE.DESCRIBE_ IS
'??????';

COMMENT ON COLUMN SYS_ROUTE_TYPE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_ROUTE_TYPE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_ROUTE_TYPE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_ROUTE_TYPE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_ROUTE_TYPE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_ROUTE_TYPE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_ROUTING                                           */
/*==============================================================*/
CREATE TABLE SYS_ROUTING  (
                              ID_                  VARCHAR2(64)                    NOT NULL,
                              ROUTING_NAME_        VARCHAR2(128),
                              ROUTE_TYPE_          VARCHAR2(64),
                              CONDITION_           VARCHAR2(100),
                              CONDITION_PARAMETERS_ VARCHAR2(1000),
                              FILTER_              VARCHAR2(1000),
                              FILTER_PARAMETERS_   VARCHAR2(1000),
                              URI_                 VARCHAR2(255),
                              REMARK_              VARCHAR2(512),
                              STATUS_              VARCHAR2(20),
                              TENANT_ID_           VARCHAR2(64),
                              CREATE_DEP_ID_       VARCHAR2(64),
                              CREATE_BY_           VARCHAR2(64),
                              CREATE_TIME_         DATE,
                              UPDATE_BY_           VARCHAR2(64),
                              UPDATE_TIME_         DATE,
                              APP_ID_              VARCHAR2(64),
                              CONSTRAINT PK_SYS_ROUTING PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_ROUTING IS
'????????????';

COMMENT ON COLUMN SYS_ROUTING.ID_ IS
'??????';

COMMENT ON COLUMN SYS_ROUTING.ROUTING_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_ROUTING.ROUTE_TYPE_ IS
'????????????';

COMMENT ON COLUMN SYS_ROUTING.CONDITION_ IS
'??????';

COMMENT ON COLUMN SYS_ROUTING.CONDITION_PARAMETERS_ IS
'????????????';

COMMENT ON COLUMN SYS_ROUTING.FILTER_ IS
'?????????';

COMMENT ON COLUMN SYS_ROUTING.FILTER_PARAMETERS_ IS
'???????????????';

COMMENT ON COLUMN SYS_ROUTING.URI_ IS
'??????URL';

COMMENT ON COLUMN SYS_ROUTING.REMARK_ IS
'??????';

COMMENT ON COLUMN SYS_ROUTING.STATUS_ IS
'??????';

COMMENT ON COLUMN SYS_ROUTING.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_ROUTING.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_ROUTING.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_ROUTING.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_ROUTING.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_ROUTING.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_ROUTING.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: SYS_SEQ_ID                                            */
/*==============================================================*/
CREATE TABLE SYS_SEQ_ID  (
                             SEQ_ID_              VARCHAR2(64)                    NOT NULL,
                             NAME_                VARCHAR2(80)                    NOT NULL,
                             ALIAS_               VARCHAR2(50),
                             CUR_DATE_            DATE,
                             RULE_                VARCHAR2(100)                   NOT NULL,
                             RULE_CONF_           VARCHAR2(512),
                             INIT_VAL_            INTEGER,
                             GEN_TYPE_            VARCHAR2(20),
                             LEN_                 INTEGER,
                             CUR_VAL              INTEGER,
                             STEP_                INTEGER,
                             MEMO_                VARCHAR2(512),
                             IS_DEFAULT_          VARCHAR2(20),
                             TREE_ID_             VARCHAR2(64),
                             SYS_ID_              VARCHAR2(64),
                             TENANT_ID_           VARCHAR2(64),
                             CREATE_DEP_ID_       VARCHAR2(64),
                             CREATE_BY_           VARCHAR2(64),
                             CREATE_TIME_         DATE,
                             UPDATE_BY_           VARCHAR2(64),
                             UPDATE_TIME_         DATE,
                             APP_ID_              VARCHAR2(64),
                             CONSTRAINT PK_SYS_SEQ_ID PRIMARY KEY (SEQ_ID_)
);

COMMENT ON TABLE SYS_SEQ_ID IS
'???????????????';

COMMENT ON COLUMN SYS_SEQ_ID.SEQ_ID_ IS
'?????????ID';

COMMENT ON COLUMN SYS_SEQ_ID.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_SEQ_ID.ALIAS_ IS
'??????';

COMMENT ON COLUMN SYS_SEQ_ID.CUR_DATE_ IS
'????????????';

COMMENT ON COLUMN SYS_SEQ_ID.RULE_ IS
'??????';

COMMENT ON COLUMN SYS_SEQ_ID.RULE_CONF_ IS
'????????????';

COMMENT ON COLUMN SYS_SEQ_ID.INIT_VAL_ IS
'?????????';

COMMENT ON COLUMN SYS_SEQ_ID.GEN_TYPE_ IS
'????????????
DAY=??????
WEEK=??????
MONTH=??????
YEAR=??????
AUTO=????????????';

COMMENT ON COLUMN SYS_SEQ_ID.LEN_ IS
'???????????????';

COMMENT ON COLUMN SYS_SEQ_ID.CUR_VAL IS
'?????????';

COMMENT ON COLUMN SYS_SEQ_ID.STEP_ IS
'??????';

COMMENT ON COLUMN SYS_SEQ_ID.MEMO_ IS
'??????';

COMMENT ON COLUMN SYS_SEQ_ID.IS_DEFAULT_ IS
'????????????
YES
NO';

COMMENT ON COLUMN SYS_SEQ_ID.TREE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_SEQ_ID.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_SEQ_ID.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_SEQ_ID.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_SEQ_ID.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_SEQ_ID.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_SEQ_ID.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_SEQ_ID.APP_ID_ IS
'??????ID_';

/*==============================================================*/
/* Table: SYS_SIGNATURE                                         */
/*==============================================================*/
CREATE TABLE SYS_SIGNATURE  (
                                ID_                  VARCHAR2(64)                    NOT NULL,
                                FILE_ID_             VARCHAR2(64),
                                FILE_NAME_           VARCHAR2(50),
                                UPDATE_BY_           VARCHAR2(64),
                                CREATE_TIME_         DATE,
                                CREATE_BY_           VARCHAR2(64),
                                CREATE_DEP_ID_       VARCHAR2(64),
                                TENANT_ID_           VARCHAR2(64),
                                UPDATE_TIME_         DATE,
                                CONSTRAINT PK_SYS_SIGNATURE PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_SIGNATURE IS
'??????';

COMMENT ON COLUMN SYS_SIGNATURE.ID_ IS
'??????';

COMMENT ON COLUMN SYS_SIGNATURE.FILE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_SIGNATURE.FILE_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_SIGNATURE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_SIGNATURE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_SIGNATURE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_SIGNATURE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_SIGNATURE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_SIGNATURE.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_TRANSFER_LOG                                      */
/*==============================================================*/
CREATE TABLE SYS_TRANSFER_LOG  (
                                   ID_                  VARCHAR2(64)                    NOT NULL,
                                   OP_DESCP_            VARCHAR2(2000),
                                   AUTHOR_PERSON_       VARCHAR2(64),
                                   TARGET_PERSON_       VARCHAR2(64),
                                   TENANT_ID_           VARCHAR2(64),
                                   CREATE_DEP_ID_       VARCHAR2(64),
                                   CREATE_BY_           VARCHAR2(64),
                                   CREATE_TIME_         DATE,
                                   UPDATE_BY_           VARCHAR2(64),
                                   UPDATE_TIME_         DATE,
                                   CONSTRAINT PK_SYS_TRANSFER_LOG PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_TRANSFER_LOG IS
'?????????????????????';

COMMENT ON COLUMN SYS_TRANSFER_LOG.ID_ IS
'??????';

COMMENT ON COLUMN SYS_TRANSFER_LOG.OP_DESCP_ IS
'????????????';

COMMENT ON COLUMN SYS_TRANSFER_LOG.AUTHOR_PERSON_ IS
'???????????????';

COMMENT ON COLUMN SYS_TRANSFER_LOG.TARGET_PERSON_ IS
'???????????????';

COMMENT ON COLUMN SYS_TRANSFER_LOG.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_TRANSFER_LOG.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_TRANSFER_LOG.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_TRANSFER_LOG.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_TRANSFER_LOG.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_TRANSFER_LOG.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_TRANSFER_SETTING                                  */
/*==============================================================*/
CREATE TABLE SYS_TRANSFER_SETTING  (
                                       ID_                  VARCHAR2(64)                    NOT NULL,
                                       NAME_                VARCHAR2(64),
                                       STATUS_              VARCHAR2(64),
                                       DS_ALIAS_            VARCHAR2(64),
                                       ID_FIELD_            VARCHAR2(64),
                                       NAME_FIELD_          VARCHAR2(64),
                                       SELECT_SQL_          VARCHAR2(1000),
                                       UPDATE_SQL_          VARCHAR2(1000),
                                       LOG_TEMPLET_         VARCHAR2(1000),
                                       TENANT_ID_           VARCHAR2(64),
                                       CREATE_DEP_ID_       VARCHAR2(64),
                                       CREATE_BY_           VARCHAR2(64),
                                       CREATE_TIME_         DATE,
                                       UPDATE_BY_           VARCHAR2(64),
                                       UPDATE_TIME_         DATE,
                                       CONSTRAINT PK_SYS_TRANSFER_SETTING PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_TRANSFER_SETTING IS
'?????????????????????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.ID_ IS
'??????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.STATUS_ IS
'??????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.DS_ALIAS_ IS
'???????????????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.ID_FIELD_ IS
'ID??????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.NAME_FIELD_ IS
'????????????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.SELECT_SQL_ IS
'SELECTSQL??????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.UPDATE_SQL_ IS
'UPDATESQL??????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.LOG_TEMPLET_ IS
'??????????????????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_TREE                                              */
/*==============================================================*/
CREATE TABLE SYS_TREE  (
                           TREE_ID_             VARCHAR2(64)                    NOT NULL,
                           CODE_                VARCHAR2(50),
                           NAME_                VARCHAR2(128)                   NOT NULL,
                           PATH_                VARCHAR2(1024),
                           PARENT_ID_           VARCHAR2(64),
                           ALIAS_               VARCHAR2(64),
                           DESCP_               VARCHAR2(512),
                           CAT_KEY_             VARCHAR2(64)                    NOT NULL,
                           SN_                  INTEGER                         NOT NULL,
                           DATA_SHOW_TYPE_      VARCHAR2(20)                   DEFAULT 'FLAT',
                           TENANT_ID_           VARCHAR2(64),
                           CREATE_DEP_ID_       VARCHAR2(64),
                           CREATE_BY_           VARCHAR2(64),
                           CREATE_TIME_         DATE,
                           UPDATE_BY_           VARCHAR2(64),
                           UPDATE_TIME_         DATE,
                           APP_ID_              VARCHAR2(64),
                           CONSTRAINT PK_SYS_TREE PRIMARY KEY (TREE_ID_)
);

COMMENT ON TABLE SYS_TREE IS
'???????????????
????????????????????????????????????
??????????????????????????????';

COMMENT ON COLUMN SYS_TREE.TREE_ID_ IS
'??????';

COMMENT ON COLUMN SYS_TREE.CODE_ IS
'????????????';

COMMENT ON COLUMN SYS_TREE.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_TREE.PATH_ IS
'??????';

COMMENT ON COLUMN SYS_TREE.PARENT_ID_ IS
'?????????';

COMMENT ON COLUMN SYS_TREE.ALIAS_ IS
'??????';

COMMENT ON COLUMN SYS_TREE.DESCP_ IS
'??????';

COMMENT ON COLUMN SYS_TREE.CAT_KEY_ IS
'?????????key';

COMMENT ON COLUMN SYS_TREE.SN_ IS
'??????';

COMMENT ON COLUMN SYS_TREE.DATA_SHOW_TYPE_ IS
'????????????
?????????:
FLAT=????????????
TREE=?????????';

COMMENT ON COLUMN SYS_TREE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_TREE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_TREE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_TREE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_TREE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_TREE.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_TREE.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: SYS_TREE_CAT                                          */
/*==============================================================*/
CREATE TABLE SYS_TREE_CAT  (
                               CAT_ID_              VARCHAR2(64)                    NOT NULL,
                               KEY_                 VARCHAR2(64)                    NOT NULL,
                               NAME_                VARCHAR2(64)                    NOT NULL,
                               SN_                  INTEGER,
                               DESCP_               VARCHAR2(512),
                               TENANT_ID_           VARCHAR2(64),
                               CREATE_DEP_ID_       VARCHAR2(64),
                               CREATE_BY_           VARCHAR2(64),
                               CREATE_TIME_         DATE,
                               UPDATE_BY_           VARCHAR2(64),
                               UPDATE_TIME_         DATE,
                               CONSTRAINT PK_SYS_TREE_CAT PRIMARY KEY (CAT_ID_)
);

COMMENT ON TABLE SYS_TREE_CAT IS
'?????????????????????';

COMMENT ON COLUMN SYS_TREE_CAT.CAT_ID_ IS
'??????';

COMMENT ON COLUMN SYS_TREE_CAT.KEY_ IS
'??????Key';

COMMENT ON COLUMN SYS_TREE_CAT.NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_TREE_CAT.SN_ IS
'??????';

COMMENT ON COLUMN SYS_TREE_CAT.DESCP_ IS
'??????';

COMMENT ON COLUMN SYS_TREE_CAT.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_TREE_CAT.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_TREE_CAT.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_TREE_CAT.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_TREE_CAT.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_TREE_CAT.UPDATE_TIME_ IS
'????????????';

/*==============================================================*/
/* Table: SYS_WEBREQ_DEF                                        */
/*==============================================================*/
CREATE TABLE SYS_WEBREQ_DEF  (
                                 ID_                  VARCHAR2(64)                    NOT NULL,
                                 NAME_                VARCHAR2(200),
                                 ALIAS_               VARCHAR2(64),
                                 URL_                 VARCHAR2(200),
                                 MODE_                VARCHAR2(20),
                                 TYPE_                VARCHAR2(20),
                                 DATA_TYPE_           VARCHAR2(64),
                                 PARAMS_SET_          VARCHAR2(400),
                                 DATA_                CLOB,
                                 TEMP_                CLOB,
                                 STATUS_              VARCHAR2(20),
                                 TENANT_ID_           VARCHAR2(64),
                                 CREATE_DEP_ID_       VARCHAR2(64),
                                 CREATE_BY_           VARCHAR2(64),
                                 CREATE_TIME_         DATE,
                                 UPDATE_BY_           VARCHAR2(64),
                                 UPDATE_TIME_         DATE,
                                 APP_ID_              VARCHAR2(64),
                                 CONSTRAINT PK_SYS_WEBREQ_DEF PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_WEBREQ_DEF IS
'WEB??????????????????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.ID_ IS
'??????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.ALIAS_ IS
'KEY_';

COMMENT ON COLUMN SYS_WEBREQ_DEF.URL_ IS
'????????????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.MODE_ IS
'????????????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.TYPE_ IS
'????????????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.DATA_TYPE_ IS
'????????????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.PARAMS_SET_ IS
'????????????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.DATA_ IS
'????????????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.TEMP_ IS
'??????????????????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.STATUS_ IS
'??????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_WEBREQ_DEF.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_WEBREQ_DEF.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_WEBREQ_DEF.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_WEBREQ_DEF.UPDATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_WEBREQ_DEF.APP_ID_ IS
'??????ID';

/*==============================================================*/
/* Table: SYS_WORD_TEMPLATE                                     */
/*==============================================================*/
CREATE TABLE SYS_WORD_TEMPLATE  (
                                    ID_                  VARCHAR2(64)                    NOT NULL,
                                    NAME_                VARCHAR2(64),
                                    TYPE_                VARCHAR2(40),
                                    BO_DEF_ID_           VARCHAR2(64),
                                    BO_DEF_NAME_         VARCHAR2(64),
                                    SETTING_             VARCHAR2(4000),
                                    DS_ALIAS_            VARCHAR2(64),
                                    DS_NAME_             VARCHAR2(64),
                                    TEMPLATE_ID_         VARCHAR2(64),
                                    TEMPLATE_NAME_       VARCHAR2(64),
                                    DESCRIPTION_         VARCHAR2(255),
                                    TENANT_ID_           VARCHAR2(64),
                                    CREATE_DEP_ID_       VARCHAR2(64),
                                    CREATE_BY_           VARCHAR2(64),
                                    CREATE_TIME_         DATE,
                                    UPDATE_BY_           VARCHAR2(64),
                                    UPDATE_TIME_         DATE,
                                    CONSTRAINT PK_SYS_WORD_TEMPLATE PRIMARY KEY (ID_)
);

COMMENT ON TABLE SYS_WORD_TEMPLATE IS
'WORD??????';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.ID_ IS
'??????';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.NAME_ IS
'??????';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.TYPE_ IS
'???????????????(BO/SQL)';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.BO_DEF_ID_ IS
'BO??????ID';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.BO_DEF_NAME_ IS
'BO????????????';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.SETTING_ IS
'SQL??????';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.DS_ALIAS_ IS
'???????????????';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.DS_NAME_ IS
'???????????????';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.TEMPLATE_ID_ IS
'??????ID';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.TEMPLATE_NAME_ IS
'????????????';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.DESCRIPTION_ IS
'??????';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.TENANT_ID_ IS
'????????????Id';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.CREATE_DEP_ID_ IS
'????????????ID';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.CREATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.CREATE_TIME_ IS
'????????????';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.UPDATE_BY_ IS
'?????????ID';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.UPDATE_TIME_ IS
'????????????';


commit ;
