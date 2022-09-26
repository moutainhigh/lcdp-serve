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
'应用系统';

COMMENT ON COLUMN SYS_APP.APP_ID_ IS
'主键';

COMMENT ON COLUMN SYS_APP.CLIENT_CODE_ IS
'应用编码';

COMMENT ON COLUMN SYS_APP.CLIENT_NAME_ IS
'应用名称';

COMMENT ON COLUMN SYS_APP.CATEGORY_ID_ IS
'分类ID';

COMMENT ON COLUMN SYS_APP.ICON_ IS
'APP图标';

COMMENT ON COLUMN SYS_APP.STATUS_ IS
'状态';

COMMENT ON COLUMN SYS_APP.DESCP_ IS
'描述';

COMMENT ON COLUMN SYS_APP.HOME_TYPE_ IS
'首页类型';

COMMENT ON COLUMN SYS_APP.URL_TYPE_ IS
'URL类型';

COMMENT ON COLUMN SYS_APP.LAYOUT_ IS
'布局';

COMMENT ON COLUMN SYS_APP.PARENT_MODULE_ IS
'父组件';

COMMENT ON COLUMN SYS_APP.ICON_PIC_ IS
'图标';

COMMENT ON COLUMN SYS_APP.HOME_URL_ IS
'主页地址';

COMMENT ON COLUMN SYS_APP.SN_ IS
'序号';

COMMENT ON COLUMN SYS_APP.PARAMS_ IS
'菜单参数';

COMMENT ON COLUMN SYS_APP.AUTH_SETTING_ IS
'授权数据';

COMMENT ON COLUMN SYS_APP.SHARE_ IS
'是否共享';

COMMENT ON COLUMN SYS_APP.FREE_ IS
'是否付费（Y：是，N：否）';

COMMENT ON COLUMN SYS_APP.IS_AUTH_ IS
'是否授权';

COMMENT ON COLUMN SYS_APP.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_APP.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_APP.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_APP.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_APP.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_APP.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_APP.APP_TYPE_ IS
'0:系统内置，1:用户开发，2:外部';

COMMENT ON COLUMN SYS_APP.BACK_COLOR_ IS
'图标背景颜色';

COMMENT ON COLUMN SYS_APP.VERSION_ IS
'当前版本号';

COMMENT ON COLUMN SYS_APP.COPYRIGHT_ IS
'版权所有';

COMMENT ON COLUMN SYS_APP.PC_USE_ IS
'PC端可用';

COMMENT ON COLUMN SYS_APP.MOBILE_USE_ IS
'手机端可用';

COMMENT ON COLUMN SYS_APP.MOBILE_HOME_ IS
'手机端首页';

COMMENT ON COLUMN SYS_APP.PATH_ IS
'访问路径';

COMMENT ON COLUMN SYS_APP.MENU_NAV_TYPE_ IS
'菜单导航方式：0:内置,1:微前端';

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
'应用操作日志';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.ID_ IS
'主键';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.TYPE_ IS
'1启动2停止3升级4卸载5导入6导出7发布8生成前端工程9生成后端工程';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.TITLE_ IS
'标题';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.CONTENT_ IS
'详细内容';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_APP_ACTION_LOG.UPDATE_TIME_ IS
'更新时间';

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
'应用授权表';

COMMENT ON COLUMN SYS_APP_AUTH.ID_ IS
'主键';

COMMENT ON COLUMN SYS_APP_AUTH.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN SYS_APP_AUTH.METHOD_ IS
'方法';

COMMENT ON COLUMN SYS_APP_AUTH.URL_ IS
'接口路径';

COMMENT ON COLUMN SYS_APP_AUTH.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_APP_AUTH.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_APP_AUTH.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN SYS_APP_AUTH.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_APP_AUTH.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_APP_AUTH.UPDATE_TIME_ IS
'更新时间';

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
'应用授权菜单表';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.ID_ IS
'主键';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.MENU_ID_ IS
'菜单ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.TENANT_ID_ IS
'租户Id';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.USER_ID_ IS
'用户ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.ENABLE_ IS
'是否启用（Y：是，N：否）';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_APP_AUTH_MENU.UPDATE_TIME_ IS
'更新时间';

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
'平台开发应用收藏夹';

COMMENT ON COLUMN SYS_APP_FAVORITES.FAV_ID_ IS
'记录ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.USER_ID_ IS
'用户ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.IS_FAV_ IS
'是否收藏（0取消收藏）';

COMMENT ON COLUMN SYS_APP_FAVORITES.FAV_TIME_ IS
'收藏时间';

COMMENT ON COLUMN SYS_APP_FAVORITES.LAST_USE_TIME_ IS
'最近使用时间';

COMMENT ON COLUMN SYS_APP_FAVORITES.TENANT_ID_ IS
'租户Id';

COMMENT ON COLUMN SYS_APP_FAVORITES.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_APP_FAVORITES.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_APP_FAVORITES.UPDATE_TIME_ IS
'更新时间';

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
'应用安装表';

COMMENT ON COLUMN SYS_APP_INSTALL.ID_ IS
'主键';

COMMENT ON COLUMN SYS_APP_INSTALL.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN SYS_APP_INSTALL.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_APP_INSTALL.USER_ID_ IS
'用户ID';

COMMENT ON COLUMN SYS_APP_INSTALL.FREE_ IS
'是否付费（Y：是，N：否）';

COMMENT ON COLUMN SYS_APP_INSTALL.ENABLE_ IS
'是否启用（Y：是，N：否）';

COMMENT ON COLUMN SYS_APP_INSTALL.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_APP_INSTALL.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_APP_INSTALL.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_APP_INSTALL.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_APP_INSTALL.UPDATE_TIME_ IS
'更新时间';

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
'应用日志';

COMMENT ON COLUMN SYS_APP_LOG.ID_ IS
'主键';

COMMENT ON COLUMN SYS_APP_LOG.APP_NAME_ IS
'应用名称';

COMMENT ON COLUMN SYS_APP_LOG.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN SYS_APP_LOG.METHOD_ IS
'方法类型';

COMMENT ON COLUMN SYS_APP_LOG.URL_ IS
'接口路径';

COMMENT ON COLUMN SYS_APP_LOG.DURATION_ IS
'持续时间';

COMMENT ON COLUMN SYS_APP_LOG.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_APP_LOG.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_APP_LOG.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN SYS_APP_LOG.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_APP_LOG.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_APP_LOG.UPDATE_TIME_ IS
'更新时间';

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
'应用关系';

COMMENT ON COLUMN SYS_APP_RELATION.ID_ IS
'主键';

COMMENT ON COLUMN SYS_APP_RELATION.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN SYS_APP_RELATION.RELATED_APP_ID_ IS
'关联应用ID';

COMMENT ON COLUMN SYS_APP_RELATION.STRONG_ IS
'是否强依赖';

COMMENT ON COLUMN SYS_APP_RELATION.CONTENT_ IS
'详细内容';

COMMENT ON COLUMN SYS_APP_RELATION.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_APP_RELATION.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_APP_RELATION.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_APP_RELATION.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_APP_RELATION.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_APP_RELATION.UPDATE_TIME_ IS
'更新时间';

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
'应用运行';

COMMENT ON COLUMN SYS_APP_RUN.ID_ IS
'主键';

COMMENT ON COLUMN SYS_APP_RUN.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN SYS_APP_RUN.OS_ IS
'操作系统：linux/windows';

COMMENT ON COLUMN SYS_APP_RUN.FRONT_DEPLOY_PATH_ IS
'前端部署路径';

COMMENT ON COLUMN SYS_APP_RUN.BACK_DEPLOY_PATH_ IS
'后端部署路径';

COMMENT ON COLUMN SYS_APP_RUN.SERVICE_PORT_ IS
'后端服务端口';

COMMENT ON COLUMN SYS_APP_RUN.RUN_FRONT_CMD_ IS
'启动前端命令';

COMMENT ON COLUMN SYS_APP_RUN.RUN_BACK_CMD_ IS
'启动后端命令';

COMMENT ON COLUMN SYS_APP_RUN.FRONT_PID_ IS
'前端进程ID';

COMMENT ON COLUMN SYS_APP_RUN.BACK_PID_ IS
'后端进程ID';

COMMENT ON COLUMN SYS_APP_RUN.STOP_FRONT_CMD_ IS
'停止前端命令';

COMMENT ON COLUMN SYS_APP_RUN.STOP_BACK_CMD_ IS
'停止后端命令';

COMMENT ON COLUMN SYS_APP_RUN.START_TIME_ IS
'最后启动时间';

COMMENT ON COLUMN SYS_APP_RUN.STOP_TIME_ IS
'最后停止时间';

COMMENT ON COLUMN SYS_APP_RUN.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_APP_RUN.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_APP_RUN.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_APP_RUN.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_APP_RUN.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_APP_RUN.UPDATE_TIME_ IS
'更新时间';

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
'应用版本';

COMMENT ON COLUMN SYS_APP_VERSION.ID_ IS
'主键';

COMMENT ON COLUMN SYS_APP_VERSION.APP_ID_ IS
'应用ID';

COMMENT ON COLUMN SYS_APP_VERSION.VERSION_ IS
'版本号';

COMMENT ON COLUMN SYS_APP_VERSION.NOTES_ IS
'发布说明';

COMMENT ON COLUMN SYS_APP_VERSION.SORT_ IS
'序号';

COMMENT ON COLUMN SYS_APP_VERSION.LAST_VERSION_ IS
'上一版本';

COMMENT ON COLUMN SYS_APP_VERSION.COMPLIANT_ IS
'是否兼容上一版本：1兼容，0不兼容';

COMMENT ON COLUMN SYS_APP_VERSION.UPGRADE_SCRIPT_ IS
'升级脚本';

COMMENT ON COLUMN SYS_APP_VERSION.UNINSTALL_SCRIPT_ IS
'卸载脚本';

COMMENT ON COLUMN SYS_APP_VERSION.STATUS_ IS
'1有效，0无效';

COMMENT ON COLUMN SYS_APP_VERSION.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_APP_VERSION.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_APP_VERSION.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_APP_VERSION.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_APP_VERSION.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_APP_VERSION.UPDATE_TIME_ IS
'更新时间';

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
'客户端访问授权';

COMMENT ON COLUMN SYS_AUTH_MANAGER.ID_ IS
'主键';

COMMENT ON COLUMN SYS_AUTH_MANAGER.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_AUTH_MANAGER.SECRET_ IS
'密钥';

COMMENT ON COLUMN SYS_AUTH_MANAGER.IS_LOG_ IS
'是否记录日志';

COMMENT ON COLUMN SYS_AUTH_MANAGER.ENABLE_ IS
'是否启用';

COMMENT ON COLUMN SYS_AUTH_MANAGER.EXPIRE_ IS
'过期时间';

COMMENT ON COLUMN SYS_AUTH_MANAGER.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_AUTH_MANAGER.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_AUTH_MANAGER.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN SYS_AUTH_MANAGER.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_AUTH_MANAGER.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_AUTH_MANAGER.UPDATE_TIME_ IS
'更新时间';

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
'系统授权权限';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.ID_ IS
'主键';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.TREE_ID_ IS
'分类IDS';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.TREE_NAME_ IS
'分类名称';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.SETTING_ID_ IS
'权限ID';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.RIGHT_JSON_ IS
'权限设置';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_AUTH_RIGHTS.UPDATE_TIME_ IS
'更新时间';

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
'系统授权设置';

COMMENT ON COLUMN SYS_AUTH_SETTING.ID_ IS
'主键';

COMMENT ON COLUMN SYS_AUTH_SETTING.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_AUTH_SETTING.ENABLE_ IS
'启用';

COMMENT ON COLUMN SYS_AUTH_SETTING.TYPE_ IS
'类型';

COMMENT ON COLUMN SYS_AUTH_SETTING.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_AUTH_SETTING.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_AUTH_SETTING.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_AUTH_SETTING.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_AUTH_SETTING.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_AUTH_SETTING.UPDATE_TIME_ IS
'更新时间';

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
'数据字典';

COMMENT ON COLUMN SYS_DIC.DIC_ID_ IS
'主键';

COMMENT ON COLUMN SYS_DIC.TREE_ID_ IS
'分类Id';

COMMENT ON COLUMN SYS_DIC.NAME_ IS
'项名';

COMMENT ON COLUMN SYS_DIC.VALUE_ IS
'项值';

COMMENT ON COLUMN SYS_DIC.DESCP_ IS
'描述';

COMMENT ON COLUMN SYS_DIC.SN_ IS
'序号';

COMMENT ON COLUMN SYS_DIC.PATH_ IS
'路径';

COMMENT ON COLUMN SYS_DIC.PARENT_ID_ IS
'父ID';

COMMENT ON COLUMN SYS_DIC.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_DIC.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_DIC.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_DIC.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_DIC.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_DIC.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_DIC.APP_ID_ IS
'应用ID';

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
'错误日志';

COMMENT ON COLUMN SYS_ERROR_LOG.ID_ IS
'主键';

COMMENT ON COLUMN SYS_ERROR_LOG.TRACE_ID_ IS
'跟踪ID';

COMMENT ON COLUMN SYS_ERROR_LOG.APP_NAME_ IS
'应用名称';

COMMENT ON COLUMN SYS_ERROR_LOG.URL_ IS
'访问地址';

COMMENT ON COLUMN SYS_ERROR_LOG.CONTENT_ IS
'错误内容';

COMMENT ON COLUMN SYS_ERROR_LOG.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_ERROR_LOG.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_ERROR_LOG.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN SYS_ERROR_LOG.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_ERROR_LOG.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_ERROR_LOG.UPDATE_TIME_ IS
'更新时间';

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
'EXCEL导入';

COMMENT ON COLUMN SYS_EXCEL.ID_ IS
'主键';

COMMENT ON COLUMN SYS_EXCEL.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_EXCEL.KEY_ IS
'别名';

COMMENT ON COLUMN SYS_EXCEL.TEMPLATE_ID_ IS
'模板ID';

COMMENT ON COLUMN SYS_EXCEL.COMMENT_ IS
'备注';

COMMENT ON COLUMN SYS_EXCEL.GRID_DATA_ IS
'映射表内容';

COMMENT ON COLUMN SYS_EXCEL.FIELD_ IS
'Excel表内容';

COMMENT ON COLUMN SYS_EXCEL.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_EXCEL.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_EXCEL.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_EXCEL.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_EXCEL.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_EXCEL.UPDATE_TIME_ IS
'更新时间';

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
'EXCEL导入批次管理';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.ID_ IS
'主键';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.TABLE_ IS
'表名';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.DS_ALIAS_ IS
'列名';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.TEMPLATE_ID_ IS
'模板ID';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.BAT_ID_ IS
'批次ID';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_EXCEL_BATMANAGE.UPDATE_TIME_ IS
'更新时间';

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
'导出错误日志';

COMMENT ON COLUMN SYS_EXCEL_LOG.ID_ IS
'主键';

COMMENT ON COLUMN SYS_EXCEL_LOG.TEMPLATED_ IS
'模板ID';

COMMENT ON COLUMN SYS_EXCEL_LOG.LOG_ IS
'日志';

COMMENT ON COLUMN SYS_EXCEL_LOG.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_EXCEL_LOG.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_EXCEL_LOG.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_EXCEL_LOG.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_EXCEL_LOG.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_EXCEL_LOG.UPDATE_TIME_ IS
'更新时间';

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
'系统文件';

COMMENT ON COLUMN SYS_FILE.FILE_ID_ IS
'主键';

COMMENT ON COLUMN SYS_FILE.TYPE_ID_ IS
'分类Id';

COMMENT ON COLUMN SYS_FILE.FILE_NAME_ IS
'文件名';

COMMENT ON COLUMN SYS_FILE.NEW_FNAME_ IS
'新文件名';

COMMENT ON COLUMN SYS_FILE.PATH_ IS
'路径';

COMMENT ON COLUMN SYS_FILE.THUMBNAIL_ IS
'缩略图';

COMMENT ON COLUMN SYS_FILE.EXT_ IS
'扩展名';

COMMENT ON COLUMN SYS_FILE.MINE_TYPE_ IS
'附件类型';

COMMENT ON COLUMN SYS_FILE.DESC_ IS
'说明';

COMMENT ON COLUMN SYS_FILE.TOTAL_BYTES_ IS
'总字节数';

COMMENT ON COLUMN SYS_FILE.DEL_STATUS_ IS
'删除状态';

COMMENT ON COLUMN SYS_FILE.FROM_ IS
'来源';

COMMENT ON COLUMN SYS_FILE.COVER_STATUS_ IS
'转换状态（PDF)';

COMMENT ON COLUMN SYS_FILE.FILE_SYSTEM_ IS
'文件系统';

COMMENT ON COLUMN SYS_FILE.PDF_PATH_ IS
'PDF文件路径';

COMMENT ON COLUMN SYS_FILE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_FILE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_FILE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_FILE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_FILE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_FILE.UPDATE_TIME_ IS
'更新时间';

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
'通知模板表';

COMMENT ON COLUMN SYS_INFORM.ID_ IS
'主键';

COMMENT ON COLUMN SYS_INFORM.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_INFORM.DESCP_ IS
'描述';

COMMENT ON COLUMN SYS_INFORM.TREE_ID_ IS
'分类';

COMMENT ON COLUMN SYS_INFORM.ACTION_ IS
'审批动作';

COMMENT ON COLUMN SYS_INFORM.TEXT_TEMPLATE_ IS
'纯文本模板';

COMMENT ON COLUMN SYS_INFORM.RICH_TEXT_TEMPLATE IS
'富文本框';

COMMENT ON COLUMN SYS_INFORM.BO_DEF_ID_ IS
'业务模型ID';

COMMENT ON COLUMN SYS_INFORM.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_INFORM.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_INFORM.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_INFORM.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_INFORM.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_INFORM.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_INFORM.APP_ID_ IS
'应用ID';

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
'通知表单模板表';

COMMENT ON COLUMN SYS_INFORM_PDF.ID_ IS
'主键';

COMMENT ON COLUMN SYS_INFORM_PDF.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_INFORM_PDF.KEY_ IS
'标识';

COMMENT ON COLUMN SYS_INFORM_PDF.TREE_ID_ IS
'分类ID';

COMMENT ON COLUMN SYS_INFORM_PDF.BO_DEF_ID_ IS
'业务模型ID';

COMMENT ON COLUMN SYS_INFORM_PDF.PDF_HTML_ IS
'表单模板';

COMMENT ON COLUMN SYS_INFORM_PDF.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_INFORM_PDF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_INFORM_PDF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_INFORM_PDF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_INFORM_PDF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_INFORM_PDF.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_INFORM_PDF.APP_ID_ IS
'应用ID';

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
'接口API表';

COMMENT ON COLUMN SYS_INTERFACE_API.API_ID_ IS
'接口ID';

COMMENT ON COLUMN SYS_INTERFACE_API.API_NAME_ IS
'接口名称';

COMMENT ON COLUMN SYS_INTERFACE_API.CLASSIFICATION_ID_ IS
'分类ID';

COMMENT ON COLUMN SYS_INTERFACE_API.PROJECT_ID_ IS
'项目ID';

COMMENT ON COLUMN SYS_INTERFACE_API.API_TYPE_ IS
'接口类型';

COMMENT ON COLUMN SYS_INTERFACE_API.API_PATH_ IS
'接口路径';

COMMENT ON COLUMN SYS_INTERFACE_API.API_METHOD_ IS
'请求类型';

COMMENT ON COLUMN SYS_INTERFACE_API.STATUS_ IS
'状态';

COMMENT ON COLUMN SYS_INTERFACE_API.API_PATH_PARAMS_ IS
'请求路径参数';

COMMENT ON COLUMN SYS_INTERFACE_API.API_HEADERS_ IS
'请求头参数';

COMMENT ON COLUMN SYS_INTERFACE_API.API_QUERY_ IS
'请求参数';

COMMENT ON COLUMN SYS_INTERFACE_API.API_BODY_ IS
'请求体参数';

COMMENT ON COLUMN SYS_INTERFACE_API.API_DATA_TYPE_ IS
'请求体数据类型';

COMMENT ON COLUMN SYS_INTERFACE_API.API_RETURN_TYPE_ IS
'返回数据类型';

COMMENT ON COLUMN SYS_INTERFACE_API.API_RETURN_FIELDS_ IS
'返回字段';

COMMENT ON COLUMN SYS_INTERFACE_API.JAVA_TYPE_ IS
'数据处理类型';

COMMENT ON COLUMN SYS_INTERFACE_API.JAVA_CODE_ IS
'JAVA脚本';

COMMENT ON COLUMN SYS_INTERFACE_API.JAVA_BEAN_ IS
'处理器BEAN';

COMMENT ON COLUMN SYS_INTERFACE_API.DESCRIPTION_ IS
'备注';

COMMENT ON COLUMN SYS_INTERFACE_API.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_INTERFACE_API.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_INTERFACE_API.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_INTERFACE_API.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_INTERFACE_API.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_INTERFACE_API.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_INTERFACE_API.IS_LOG_ IS
'是否记录日志';

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
'接口调用日志表';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.LOG_ID_ IS
'日志ID';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.INTERFACE_ID_ IS
'接口ID';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.LOG_URL_ IS
'接口完整路径';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.LOG_HEADERS_ IS
'请求头数据';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.LOG_QUERY_ IS
'请求路径数据';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.LOG_BODY_ IS
'请求体数据';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.RESPONSE_STATE_ IS
'返回状态';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.RESPONSE_DATA_ IS
'返回数据';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.TIME_CONSUMING_ IS
'接口耗时';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.ERROR_MESSAGE_ IS
'错误信息';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_INTERFACE_CALL_LOGS.UPDATE_TIME_ IS
'更新时间';

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
'接口分类表';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.CLASSIFICATION_ID_ IS
'分类ID';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.CLASSIFICATION_NAME_ IS
'分类名称';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.DESCRIPTION_ IS
'描述';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.PROJECT_ID_ IS
'项目ID';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_INTERFACE_CLASS.UPDATE_TIME_ IS
'更新时间';

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
'接口项目表';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.PROJECT_ID_ IS
'项目ID';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.PROJECT_ALIAS_ IS
'项目别名';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.PROJECT_NAME_ IS
'项目名称';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.TREE_ID_ IS
'分类ID';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.DESCRIPTION_ IS
'描述';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.DOMAIN_TCP_ IS
'接口通讯协议';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.DOMAIN_PATH_ IS
'接口域名路径';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.BASE_PATH_ IS
'接口基本路径';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.STATUS_ IS
'项目状态';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.GLOBAL_HEADERS_ IS
'全局请求头';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_INTERFACE_PROJECT.UPDATE_TIME_ IS
'更新时间';

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
'表单调用脚本';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.ID_ IS
'主键';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.TREE_ID_ IS
'分类ID';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.ALIAS_ IS
'别名';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.PARAMS_ IS
'参数定义';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.CONTENT_ IS
'脚本定义';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.DESCP_ IS
'描述';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_INVOKE_SCRIPT.APP_ID_ IS
'应用ID';

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
'系统定时任务';

COMMENT ON COLUMN SYS_JOB.ID_ IS
'主键';

COMMENT ON COLUMN SYS_JOB.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_JOB.JOB_TASK_ IS
'JOB任务ID';

COMMENT ON COLUMN SYS_JOB.JOB_TASK_ID_ IS
'JOB任务ID';

COMMENT ON COLUMN SYS_JOB.STRATEGY_ IS
'策略';

COMMENT ON COLUMN SYS_JOB.STATUS_ IS
'状态(0为禁用,1为启用)';

COMMENT ON COLUMN SYS_JOB.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_JOB.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_JOB.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN SYS_JOB.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_JOB.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_JOB.UPDATE_TIME_ IS
'更新时间';

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
'定时任务定义';

COMMENT ON COLUMN SYS_JOB_TASK.ID_ IS
'主键';

COMMENT ON COLUMN SYS_JOB_TASK.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_JOB_TASK.TYPE_ IS
'类型 (JOB,SCRIPT,CLASS)';

COMMENT ON COLUMN SYS_JOB_TASK.CONTENT_ IS
'配置内容';

COMMENT ON COLUMN SYS_JOB_TASK.STATUS_ IS
'状态';

COMMENT ON COLUMN SYS_JOB_TASK.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_JOB_TASK.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_JOB_TASK.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_JOB_TASK.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_JOB_TASK.CREATE_BY_ IS
'创建人';

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
'KETTLE资源库定义';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.ID_ IS
'主键';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.RES_USER_ IS
'资源库用户';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.RES_PWD_ IS
'资源库密码';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.DB_TYPE_ IS
'数据库类型';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.HOST_ IS
'主机地址';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.PORT_ IS
'端口号';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.DATABASE_ IS
'数据库名';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.USER_ IS
'用户名';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.PASSWORD_ IS
'密码';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.COMMENT_ IS
'备注';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.STATUS_ IS
'状态';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_KETTLE_DBDEF.UPDATE_TIME_ IS
'更新时间';

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
'KETTLE定义';

COMMENT ON COLUMN SYS_KETTLE_DEF.ID_ IS
'主键';

COMMENT ON COLUMN SYS_KETTLE_DEF.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_KETTLE_DEF.GATEGORY_ IS
'分类';

COMMENT ON COLUMN SYS_KETTLE_DEF.TYPE_ IS
'类型(job,trans)';

COMMENT ON COLUMN SYS_KETTLE_DEF.STORE_TYPE_ IS
'存储类型(文件:file，资源库:resource)';

COMMENT ON COLUMN SYS_KETTLE_DEF.PARAMETERS_ IS
'变量配置';

COMMENT ON COLUMN SYS_KETTLE_DEF.STORE_SETTING_ IS
'存储设定';

COMMENT ON COLUMN SYS_KETTLE_DEF.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_KETTLE_DEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_KETTLE_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_KETTLE_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_KETTLE_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_KETTLE_DEF.UPDATE_TIME_ IS
'更新时间';

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
'KETTLE任务';

COMMENT ON COLUMN SYS_KETTLE_JOB.ID_ IS
'主键';

COMMENT ON COLUMN SYS_KETTLE_JOB.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_KETTLE_JOB.KETTLE_ID_ IS
'KETTLE定义ID';

COMMENT ON COLUMN SYS_KETTLE_JOB.STRATEGY_ IS
'策略';

COMMENT ON COLUMN SYS_KETTLE_JOB.LOGLEVEL_ IS
'日志级别';

COMMENT ON COLUMN SYS_KETTLE_JOB.STATUS_ IS
'状态(0为禁用,1为启用)';

COMMENT ON COLUMN SYS_KETTLE_JOB.REMARK_ IS
'备注';

COMMENT ON COLUMN SYS_KETTLE_JOB.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_KETTLE_JOB.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_KETTLE_JOB.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN SYS_KETTLE_JOB.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_KETTLE_JOB.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_KETTLE_JOB.UPDATE_TIME_ IS
'更新时间';

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
'KETTLE日志';

COMMENT ON COLUMN SYS_KETTLE_LOG.ID_ IS
'主键';

COMMENT ON COLUMN SYS_KETTLE_LOG.KETTLE_ID_ IS
'KETTLE定义ID';

COMMENT ON COLUMN SYS_KETTLE_LOG.DRUATION_ IS
'运行时长';

COMMENT ON COLUMN SYS_KETTLE_LOG.STATUS_ IS
'状态(1.成功,0失败)';

COMMENT ON COLUMN SYS_KETTLE_LOG.LOG_ IS
'日志';

COMMENT ON COLUMN SYS_KETTLE_LOG.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_KETTLE_LOG.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_KETTLE_LOG.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_KETTLE_LOG.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_KETTLE_LOG.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_KETTLE_LOG.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_KETTLE_LOG.KETTLE_JOB_ID_ IS
'任务ID';

COMMENT ON COLUMN SYS_KETTLE_LOG.KETTLE_JOB_NAME_ IS
'任务名称';

COMMENT ON COLUMN SYS_KETTLE_LOG.KETTLE_TYPE_ IS
'Kettle类型';

COMMENT ON COLUMN SYS_KETTLE_LOG.START_TIME_ IS
'任务开始时间';

COMMENT ON COLUMN SYS_KETTLE_LOG.END_TIME_ IS
'任务结束时间';

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
'系统日志';

COMMENT ON COLUMN SYS_LOG.ID_ IS
'主键';

COMMENT ON COLUMN SYS_LOG.APP_NAME_ IS
'应用名称';

COMMENT ON COLUMN SYS_LOG.MODULE_ IS
'一级模块';

COMMENT ON COLUMN SYS_LOG.SUB_MODULE_ IS
'子模块';

COMMENT ON COLUMN SYS_LOG.CLASS_NAME_ IS
'类名';

COMMENT ON COLUMN SYS_LOG.METHOD_NAME_ IS
'方法名';

COMMENT ON COLUMN SYS_LOG.ACTION_ IS
'动作';

COMMENT ON COLUMN SYS_LOG.PK_VALUE_ IS
'业务主键';

COMMENT ON COLUMN SYS_LOG.IP_ IS
'访问IP';

COMMENT ON COLUMN SYS_LOG.DETAIL_ IS
'日志明细';

COMMENT ON COLUMN SYS_LOG.OPERATION_ IS
'操作';

COMMENT ON COLUMN SYS_LOG.USER_NAME_ IS
'操作用户';

COMMENT ON COLUMN SYS_LOG.DURATION_ IS
'操作时长';

COMMENT ON COLUMN SYS_LOG.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_LOG.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_LOG.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN SYS_LOG.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_LOG.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_LOG.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_LOG.BUS_TYPE_ IS
'业务类型';

COMMENT ON COLUMN SYS_LOG.IS_RESUME_ IS
'是否已恢复';

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
'系统菜单';

COMMENT ON COLUMN SYS_MENU.MENU_ID_ IS
'菜单ID';

COMMENT ON COLUMN SYS_MENU.APP_ID_ IS
'所属子系统';

COMMENT ON COLUMN SYS_MENU.NAME_ IS
'菜单名称';

COMMENT ON COLUMN SYS_MENU.ICON_PC_ IS
'PC图标样式';

COMMENT ON COLUMN SYS_MENU.ICON_PIC_ IS
'图标';

COMMENT ON COLUMN SYS_MENU.ICON_APP_ IS
'APP图标样式';

COMMENT ON COLUMN SYS_MENU.PARENT_ID_ IS
'上级父ID';

COMMENT ON COLUMN SYS_MENU.PATH_ IS
'路径';

COMMENT ON COLUMN SYS_MENU.SN_ IS
'序号';

COMMENT ON COLUMN SYS_MENU.SHOW_TYPE_ IS
'访问方式';

COMMENT ON COLUMN SYS_MENU.MENU_KEY_ IS
'菜单唯一标识';

COMMENT ON COLUMN SYS_MENU.MENU_TYPE_ IS
'菜单类型';

COMMENT ON COLUMN SYS_MENU.COMPONENT_ IS
'展示组件';

COMMENT ON COLUMN SYS_MENU.SETTING_TYPE_ IS
'配置类型(custom,iframe)';

COMMENT ON COLUMN SYS_MENU.BO_LIST_KEY_ IS
'单据列表KEY';

COMMENT ON COLUMN SYS_MENU.URL_ IS
'接口地址';

COMMENT ON COLUMN SYS_MENU.METHOD_ IS
'接口方法';

COMMENT ON COLUMN SYS_MENU.PARAMS_ IS
'菜单参数';

COMMENT ON COLUMN SYS_MENU.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_MENU.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_MENU.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_MENU.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_MENU.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_MENU.UPDATE_TIME_ IS
'更新时间';

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
'发布菜单路径记录表';

COMMENT ON COLUMN SYS_MENU_RELEASE.ID_ IS
'主键';

COMMENT ON COLUMN SYS_MENU_RELEASE.RELEASE_ID_ IS
'BO列表ID';

COMMENT ON COLUMN SYS_MENU_RELEASE.MENU_ID_ IS
'菜单ID';

COMMENT ON COLUMN SYS_MENU_RELEASE.MENU_NAME_ IS
'菜单名称';

COMMENT ON COLUMN SYS_MENU_RELEASE.RELEASE_URL_ IS
'发布路径';

COMMENT ON COLUMN SYS_MENU_RELEASE.MENU_URL_ IS
'当前路径';

COMMENT ON COLUMN SYS_MENU_RELEASE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_MENU_RELEASE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_MENU_RELEASE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_MENU_RELEASE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_MENU_RELEASE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_MENU_RELEASE.UPDATE_TIME_ IS
'更新时间';

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
'office表';

COMMENT ON COLUMN SYS_OFFICE.ID_ IS
'主键';

COMMENT ON COLUMN SYS_OFFICE.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_OFFICE.SUPPORT_VERSION_ IS
'是否支持版本';

COMMENT ON COLUMN SYS_OFFICE.VERSION_ IS
'版本';

COMMENT ON COLUMN SYS_OFFICE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_OFFICE.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_OFFICE.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN SYS_OFFICE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_OFFICE.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_OFFICE.UPDATE_TIME_ IS
'更新时间';

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
'office模板表';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.ID_ IS
'主键';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.DOC_NAME_ IS
'文件名';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.DESCRIPTION_ IS
'描述';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.TYPE_ IS
'类型(normal,red)';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.DOC_ID_ IS
'文档ID';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.TENANT_ID_ IS
'租户ID';

COMMENT ON COLUMN SYS_OFFICE_TEMPLATE.CREATE_BY_ IS
'创建人';

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
'office版本表';

COMMENT ON COLUMN SYS_OFFICE_VER.ID_ IS
'主键';

COMMENT ON COLUMN SYS_OFFICE_VER.OFFICE_ID_ IS
'OFFICE主键';

COMMENT ON COLUMN SYS_OFFICE_VER.VERSION_ IS
'版本';

COMMENT ON COLUMN SYS_OFFICE_VER.FILE_ID_ IS
'附件ID';

COMMENT ON COLUMN SYS_OFFICE_VER.FILE_NAME_ IS
'文件名';

COMMENT ON COLUMN SYS_OFFICE_VER.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_OFFICE_VER.CREATE_BY_ IS
'创建人';

COMMENT ON COLUMN SYS_OFFICE_VER.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_OFFICE_VER.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_OFFICE_VER.UPDATE_BY_ IS
'更新人';

COMMENT ON COLUMN SYS_OFFICE_VER.TENANT_ID_ IS
'租户ID';

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
'系统属性表';

COMMENT ON COLUMN SYS_PROPERTIES.PRO_ID_ IS
'属性ID';

COMMENT ON COLUMN SYS_PROPERTIES.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_PROPERTIES.ALIAS_ IS
'别名';

COMMENT ON COLUMN SYS_PROPERTIES.GLOBAL_ IS
'是否全局';

COMMENT ON COLUMN SYS_PROPERTIES.ENCRYPT_ IS
'是否加密';

COMMENT ON COLUMN SYS_PROPERTIES.VALUE_ IS
'属性值';

COMMENT ON COLUMN SYS_PROPERTIES.CATEGORY_ IS
'分类';

COMMENT ON COLUMN SYS_PROPERTIES.DESCRIPTION_ IS
'描述';

COMMENT ON COLUMN SYS_PROPERTIES.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_PROPERTIES.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_PROPERTIES.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_PROPERTIES.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_PROPERTIES.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_PROPERTIES.UPDATE_TIME_ IS
'更新时间';

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
'动态路由配置';

COMMENT ON COLUMN SYS_ROUTE_TYPE.ID_ IS
'主键';

COMMENT ON COLUMN SYS_ROUTE_TYPE.ROUTE_TYPE_NAME_ IS
'类型';

COMMENT ON COLUMN SYS_ROUTE_TYPE.DESCRIBE_ IS
'描述';

COMMENT ON COLUMN SYS_ROUTE_TYPE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_ROUTE_TYPE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_ROUTE_TYPE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_ROUTE_TYPE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_ROUTE_TYPE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_ROUTE_TYPE.UPDATE_TIME_ IS
'更新时间';

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
'网关路由';

COMMENT ON COLUMN SYS_ROUTING.ID_ IS
'主键';

COMMENT ON COLUMN SYS_ROUTING.ROUTING_NAME_ IS
'路由名称';

COMMENT ON COLUMN SYS_ROUTING.ROUTE_TYPE_ IS
'路由类型';

COMMENT ON COLUMN SYS_ROUTING.CONDITION_ IS
'条件';

COMMENT ON COLUMN SYS_ROUTING.CONDITION_PARAMETERS_ IS
'条件参数';

COMMENT ON COLUMN SYS_ROUTING.FILTER_ IS
'过滤器';

COMMENT ON COLUMN SYS_ROUTING.FILTER_PARAMETERS_ IS
'过滤器参数';

COMMENT ON COLUMN SYS_ROUTING.URI_ IS
'目标URL';

COMMENT ON COLUMN SYS_ROUTING.REMARK_ IS
'备注';

COMMENT ON COLUMN SYS_ROUTING.STATUS_ IS
'状态';

COMMENT ON COLUMN SYS_ROUTING.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_ROUTING.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_ROUTING.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_ROUTING.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_ROUTING.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_ROUTING.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_ROUTING.APP_ID_ IS
'应用ID';

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
'系统流水号';

COMMENT ON COLUMN SYS_SEQ_ID.SEQ_ID_ IS
'流水号ID';

COMMENT ON COLUMN SYS_SEQ_ID.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_SEQ_ID.ALIAS_ IS
'别名';

COMMENT ON COLUMN SYS_SEQ_ID.CUR_DATE_ IS
'当前日期';

COMMENT ON COLUMN SYS_SEQ_ID.RULE_ IS
'规则';

COMMENT ON COLUMN SYS_SEQ_ID.RULE_CONF_ IS
'规则配置';

COMMENT ON COLUMN SYS_SEQ_ID.INIT_VAL_ IS
'初始值';

COMMENT ON COLUMN SYS_SEQ_ID.GEN_TYPE_ IS
'生成方式
DAY=每天
WEEK=每周
MONTH=每月
YEAR=每年
AUTO=一直增长';

COMMENT ON COLUMN SYS_SEQ_ID.LEN_ IS
'流水号长度';

COMMENT ON COLUMN SYS_SEQ_ID.CUR_VAL IS
'当前值';

COMMENT ON COLUMN SYS_SEQ_ID.STEP_ IS
'步长';

COMMENT ON COLUMN SYS_SEQ_ID.MEMO_ IS
'备注';

COMMENT ON COLUMN SYS_SEQ_ID.IS_DEFAULT_ IS
'系统缺省
YES
NO';

COMMENT ON COLUMN SYS_SEQ_ID.TREE_ID_ IS
'分类ID';

COMMENT ON COLUMN SYS_SEQ_ID.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_SEQ_ID.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_SEQ_ID.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_SEQ_ID.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_SEQ_ID.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_SEQ_ID.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_SEQ_ID.APP_ID_ IS
'应用ID_';

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
'签名';

COMMENT ON COLUMN SYS_SIGNATURE.ID_ IS
'主键';

COMMENT ON COLUMN SYS_SIGNATURE.FILE_ID_ IS
'文件ID';

COMMENT ON COLUMN SYS_SIGNATURE.FILE_NAME_ IS
'图片名称';

COMMENT ON COLUMN SYS_SIGNATURE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_SIGNATURE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_SIGNATURE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_SIGNATURE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_SIGNATURE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_SIGNATURE.UPDATE_TIME_ IS
'更新时间';

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
'权限转移日志表';

COMMENT ON COLUMN SYS_TRANSFER_LOG.ID_ IS
'主键';

COMMENT ON COLUMN SYS_TRANSFER_LOG.OP_DESCP_ IS
'操作描述';

COMMENT ON COLUMN SYS_TRANSFER_LOG.AUTHOR_PERSON_ IS
'权限转移人';

COMMENT ON COLUMN SYS_TRANSFER_LOG.TARGET_PERSON_ IS
'目标转移人';

COMMENT ON COLUMN SYS_TRANSFER_LOG.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_TRANSFER_LOG.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_TRANSFER_LOG.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_TRANSFER_LOG.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_TRANSFER_LOG.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_TRANSFER_LOG.UPDATE_TIME_ IS
'更新时间';

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
'权限转移设置表';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.ID_ IS
'主键';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.STATUS_ IS
'状态';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.DS_ALIAS_ IS
'数据源别名';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.ID_FIELD_ IS
'ID字段';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.NAME_FIELD_ IS
'名称字段';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.SELECT_SQL_ IS
'SELECTSQL语句';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.UPDATE_SQL_ IS
'UPDATESQL语句';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.LOG_TEMPLET_ IS
'日志内容模板';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_TRANSFER_SETTING.UPDATE_TIME_ IS
'更新时间';

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
'系统分类树
用于显示树层次结构的分类
可以允许任何层次结构';

COMMENT ON COLUMN SYS_TREE.TREE_ID_ IS
'主键';

COMMENT ON COLUMN SYS_TREE.CODE_ IS
'同级编码';

COMMENT ON COLUMN SYS_TREE.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_TREE.PATH_ IS
'路径';

COMMENT ON COLUMN SYS_TREE.PARENT_ID_ IS
'父节点';

COMMENT ON COLUMN SYS_TREE.ALIAS_ IS
'别名';

COMMENT ON COLUMN SYS_TREE.DESCP_ IS
'描述';

COMMENT ON COLUMN SYS_TREE.CAT_KEY_ IS
'树分类key';

COMMENT ON COLUMN SYS_TREE.SN_ IS
'序号';

COMMENT ON COLUMN SYS_TREE.DATA_SHOW_TYPE_ IS
'展示类型
默认为:
FLAT=平铺类型
TREE=树类型';

COMMENT ON COLUMN SYS_TREE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_TREE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_TREE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_TREE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_TREE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_TREE.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_TREE.APP_ID_ IS
'应用ID';

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
'系统树分类类型';

COMMENT ON COLUMN SYS_TREE_CAT.CAT_ID_ IS
'主键';

COMMENT ON COLUMN SYS_TREE_CAT.KEY_ IS
'分类Key';

COMMENT ON COLUMN SYS_TREE_CAT.NAME_ IS
'分类名称';

COMMENT ON COLUMN SYS_TREE_CAT.SN_ IS
'序号';

COMMENT ON COLUMN SYS_TREE_CAT.DESCP_ IS
'描述';

COMMENT ON COLUMN SYS_TREE_CAT.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_TREE_CAT.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_TREE_CAT.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_TREE_CAT.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_TREE_CAT.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_TREE_CAT.UPDATE_TIME_ IS
'更新时间';

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
'WEB请求调用定义';

COMMENT ON COLUMN SYS_WEBREQ_DEF.ID_ IS
'主键';

COMMENT ON COLUMN SYS_WEBREQ_DEF.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_WEBREQ_DEF.ALIAS_ IS
'KEY_';

COMMENT ON COLUMN SYS_WEBREQ_DEF.URL_ IS
'请求地址';

COMMENT ON COLUMN SYS_WEBREQ_DEF.MODE_ IS
'请求方式';

COMMENT ON COLUMN SYS_WEBREQ_DEF.TYPE_ IS
'请求类型';

COMMENT ON COLUMN SYS_WEBREQ_DEF.DATA_TYPE_ IS
'数据类型';

COMMENT ON COLUMN SYS_WEBREQ_DEF.PARAMS_SET_ IS
'参数配置';

COMMENT ON COLUMN SYS_WEBREQ_DEF.DATA_ IS
'传递数据';

COMMENT ON COLUMN SYS_WEBREQ_DEF.TEMP_ IS
'请求报文模板';

COMMENT ON COLUMN SYS_WEBREQ_DEF.STATUS_ IS
'状态';

COMMENT ON COLUMN SYS_WEBREQ_DEF.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_WEBREQ_DEF.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_WEBREQ_DEF.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_WEBREQ_DEF.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_WEBREQ_DEF.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_WEBREQ_DEF.UPDATE_TIME_ IS
'更新时间';

COMMENT ON COLUMN SYS_WEBREQ_DEF.APP_ID_ IS
'应用ID';

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
'WORD模板';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.ID_ IS
'主键';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.NAME_ IS
'名称';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.TYPE_ IS
'数据来源类(BO/SQL)';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.BO_DEF_ID_ IS
'BO定义ID';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.BO_DEF_NAME_ IS
'BO定义名称';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.SETTING_ IS
'SQL设置';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.DS_ALIAS_ IS
'数据源别名';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.DS_NAME_ IS
'数据源名称';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.TEMPLATE_ID_ IS
'模板ID';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.TEMPLATE_NAME_ IS
'模板名称';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.DESCRIPTION_ IS
'描述';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.TENANT_ID_ IS
'租用用户Id';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.CREATE_DEP_ID_ IS
'创建部门ID';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.CREATE_BY_ IS
'创建人ID';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.CREATE_TIME_ IS
'创建时间';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.UPDATE_BY_ IS
'更新人ID';

COMMENT ON COLUMN SYS_WORD_TEMPLATE.UPDATE_TIME_ IS
'更新时间';


commit ;
